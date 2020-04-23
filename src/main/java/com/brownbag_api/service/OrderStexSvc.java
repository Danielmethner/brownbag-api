package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.ExecStex;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.OrderStex;
import com.brownbag_api.model.OrderTrans;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.PosMacc;
import com.brownbag_api.model.PosStex;
import com.brownbag_api.model.User;
import com.brownbag_api.model.enums.EOrderAction;
import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.ExecStexRepo;
import com.brownbag_api.repo.OrderRepo;
//import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.OrderTransRepo;
import com.brownbag_api.repo.PosRepo;

@Service
public class OrderStexSvc extends OrderSvc {

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private OrderTransRepo orderTransRepo;

	@Autowired
	private ExecStexRepo execStexRepo;

	@Autowired
	private AssetRepo assetRepo;

	@Autowired
	private OrderSvc orderSvc;

	@Autowired
	private OrderPaySvc orderPaySvc;

	@Autowired
	private PartySvc partySvc;

	@Autowired
	private PosSvc posSvc;

	@Autowired
	private LogSvc logSvc;

	public OrderStex placeOrder(OrderStex orderStex) {
		if (orderStex.getId() == null) {
			logSvc.write("OrderStexSvc.placeOrder: Order must be persisted before placed.");
			orderSvc.execAction(orderStex, EOrderAction.DISCARD);
			return null;
		}

		return (OrderStex) orderSvc.execAction(orderStex, EOrderAction.PLACE);
	}

	public OrderStex placeNewOrder(EOrderDir orderDir, EOrderType orderType, Asset asset, int qty, double price,
			User user, Party party) {

		OrderStex orderStex = new OrderStex(orderDir, qty, asset, orderType, EOrderStatus.NEW, user, price, party, 0);
		orderStex = orderRepo.save(orderStex);

		if (orderStex.getQty() <= 0 || orderStex.getPrice() <= 0) {
			logSvc.write("OrderStexSvc.placeNewOrder: Order with ID: " + orderStex.getId()
					+ " must have a qty and price greater 0.");
			orderSvc.execAction(orderStex, EOrderAction.DISCARD);
			return null;
		}

		if (orderDir == EOrderDir.BUY) {
			// check if capital is sufficient
			PosMacc posMacc = partySvc.getMacc(party);
			double qtyAvbl = posSvc.getQtyAvbl(posMacc);
			double orderVol = qty * price;
			if (qtyAvbl < orderVol) {
				logSvc.write("OrderStexSvc.placeNewOrder: Insufficient Funds! Party: " + party.getName() + " Asset: "
						+ asset.getName() + " Order Volume: " + orderVol + " Available Funds: " + qtyAvbl);
				orderSvc.execAction(orderStex, EOrderAction.DISCARD);
				return null;
			}
			posMacc.setQtyBlocked(posMacc.getQtyBlocked() + orderVol);
			posSvc.save(posMacc);
		}
		if (orderDir == EOrderDir.SELL) {
			
			// STANDARD CASE: Selling Stocks from portfolio
			if (orderStex.getOrderType() == EOrderType.STEX) {
				// check if asset amount is sufficient
				PosStex posStex = posSvc.getByAssetAndParty(asset, party);
				double qtyAvbl = posSvc.getQtyAvbl(posStex);
				if (qtyAvbl < qty) {
					logSvc.write("OrderStexSvc.placeNewOrder: Not enough Shares! Party: " + party.getName() + " Asset: "
							+ asset.getName() + " Order Quantity: " + qty + " Available Shares: " + qtyAvbl);
					orderSvc.execAction(orderStex, EOrderAction.DISCARD);
					return null;
				}
				posStex.setQtyBlocked(posStex.getQtyBlocked() + qty);
				posSvc.save(posStex);
			}
			
		}

		return placeOrder(orderStex);

	}

	public void matchOrders(OrderStex orderBuy, OrderStex orderSell) {
		if (orderBuy.getOrderDir() == EOrderDir.SELL || orderSell.getOrderDir() == EOrderDir.BUY) {
			logSvc.write(
					"OrderStexSvc.matchOrders: Cannot match orders: Buy Order has Direction 'SELL' or Sell Order has Direciton 'BUY'.");
			return;
		}
		Party partySeller = orderSell.getParty();
		Party partyBuyer = orderBuy.getParty();
		PosStex posSend = posSvc.getByAssetAndParty(orderSell.getAsset(), partySeller); // Instanciates if not exists
		PosStex posRcv = posSvc.getByAssetAndParty(orderBuy.getAsset(), partyBuyer); // Instanciates if not exists

		int qty = (int) (orderBuy.getQty() < orderSell.getQty() ? orderBuy.getQty() : orderSell.getQty());

		String book_text = "Buyer: " + partyBuyer.getName() + " Seller: " + partySeller.getName() + " Qty: " + qty;

		// ENSURE PRICE LIMITS ARE COMPATIBLE
		if (orderBuy.getPriceLimit() < orderSell.getPriceLimit()) {
			logSvc.write("OrderStexSvc.matchOrders: Cannot match orders: Buy order Price: '" + orderBuy.getPriceLimit()
					+ " is lower than Sell order Price: '" + orderSell.getPriceLimit() + "'");
			return;
		}

		// CALCULATE EXECUTION PRICE
		double execPrice = (orderBuy.getPrice() / orderSell.getPrice()) / 2;

		// ADJUST AVG_PRICE - BUYORDER
		double newAvgBuyPrice = (posRcv.getQty() * posRcv.getPriceAvg() + orderBuy.getQty() * orderBuy.getPrice())
				/ (posRcv.getQty() + orderBuy.getQty());
		posRcv.setPriceAvg(newAvgBuyPrice);
		posRcv = (PosStex) posSvc.save(posRcv);

		// CREATE EXECUTION
		ExecStex execStex = new ExecStex(posSend, posRcv, orderSell, orderBuy, book_text, qty, execPrice);
		execStexRepo.save(execStex);

		// BOOK POSITIONS
		posSvc.creditPos(orderBuy, execStex);
		posSvc.debitPos(orderSell, execStex);

		// ADJUST EXEC QTY - ORDER
		orderSell.setQtyExec(orderSell.getQtyExec() + execStex.getQtyExec());
		orderBuy.setQtyExec(orderBuy.getQtyExec() + execStex.getQtyExec());

		// TRANSFER MONEY
		PosMacc maccSend = partySvc.getMacc(partyBuyer);
		PosMacc maccRcv = partySvc.getMacc(partySeller);
		String bookText = "Payment for execution of Stex Buy Order ID: " + orderBuy.getId()
				+ " against STEX sell order ID: " + orderSell.getId();

		OrderPay payment = orderPaySvc.createPay(qty, orderBuy.getUser(), null, bookText, maccSend, maccRcv);
		orderPaySvc.execPay(payment);

		// TODO move order to next WFC status
		if (orderBuy.getQty() == orderBuy.getQtyExec()) {
			orderSvc.execAction(orderBuy, EOrderAction.EXECUTE_FULL);
		} else {
			orderSvc.execAction(orderBuy, EOrderAction.EXECUTE_PART);
		}

		if (orderSell.getQty() == orderSell.getQtyExec()) {
			orderSvc.execAction(orderSell, EOrderAction.EXECUTE_FULL);
		} else {
			orderSvc.execAction(orderSell, EOrderAction.EXECUTE_PART);
		}
	}

}
