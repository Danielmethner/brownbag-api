package com.brownbag_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EOrderAction;
import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.jpa.ExecStex;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjPosMacc;
import com.brownbag_api.model.jpa.ObjPosStex;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.Order;
import com.brownbag_api.model.jpa.OrderPay;
import com.brownbag_api.model.jpa.OrderStex;
import com.brownbag_api.model.jpa.OrderTrans;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.ExecStexRepo;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.OrderStexRepo;
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
	private OrderStexRepo orderStexRepo;

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

	public OrderStex placeNewOrder(EOrderDir orderDir, EOrderType orderType, ObjAsset asset, int qty, double price,
			ObjUser user, ObjParty party) {

		OrderStex orderStex = new OrderStex(orderDir, qty, asset, orderType, EOrderStatus.NEW, user, price, party, 0);
		orderStex = orderRepo.save(orderStex);

		if (orderStex.getQty() <= 0 || orderStex.getPriceLimit() <= 0) {
			logSvc.write("OrderStexSvc.placeNewOrder: Order with ID: " + orderStex.getId()
					+ " must have a qty and price greater 0.");
			orderSvc.execAction(orderStex, EOrderAction.DISCARD);
			return null;
		}

		if (orderDir == EOrderDir.BUY) {
			// check if capital is sufficient
			ObjPosMacc posMacc = partySvc.getMacc(party);
			double qtyAvbl = posSvc.getQtyAvbl(posMacc);
			double orderVol = qty * price;
			if (qtyAvbl < orderVol) {
				logSvc.write("OrderStexSvc.placeNewOrder: Insufficient Funds! Party: " + party.getName() + " Asset: "
						+ asset.getName() + " Order Volume: " + orderVol + " Available Funds: " + qtyAvbl);
				orderSvc.execAction(orderStex, EOrderAction.DISCARD);
				return null;
			}
			posMacc.raiseQtyBlocked(orderVol);
			posSvc.save(posMacc);
		}
		if (orderDir == EOrderDir.SELL) {

			// STANDARD CASE: Selling Stocks from portfolio
			if (orderStex.getOrderType() == EOrderType.STEX) {
				// check if asset amount is sufficient
				ObjPosStex posStex = posSvc.getByAssetAndParty(asset, party);
				double qtyAvbl = posSvc.getQtyAvbl(posStex);
				if (qtyAvbl < qty) {
					logSvc.write("OrderStexSvc.placeNewOrder: Not enough Shares! Party: " + party.getName() + " Asset: "
							+ asset.getName() + " Order Quantity: " + qty + " Available Shares: " + qtyAvbl);
					orderSvc.execAction(orderStex, EOrderAction.DISCARD);
					return null;
				}
				posSvc.save(posStex.raiseQtyBlocked(qty));
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
		ObjParty partySeller = orderSell.getParty();
		ObjParty partyBuyer = orderBuy.getParty();
		ObjPosStex posSend = posSvc.getByAssetAndParty(orderSell.getAsset(), partySeller); // Instanciates if not exists
		ObjPosStex posRcv = posSvc.getByAssetAndParty(orderBuy.getAsset(), partyBuyer); // Instanciates if not exists

		int qtyExec = (int) (orderBuy.getQty() < orderSell.getQty() ? orderBuy.getQty() : orderSell.getQty());

		String book_text = "Buyer: " + partyBuyer.getName() + " Seller: " + partySeller.getName() + " Qty: " + qtyExec;

		// ENSURE PRICE LIMITS ARE COMPATIBLE
		if (orderBuy.getPriceLimit() < orderSell.getPriceLimit()) {
			logSvc.write("OrderStexSvc.matchOrders: Cannot match orders: Buy order Price: '" + orderBuy.getPriceLimit()
					+ " is lower than Sell order Price: '" + orderSell.getPriceLimit() + "'");
			return;
		}

		// CALCULATE EXECUTION PRICE
		double execPrice = (orderBuy.getPriceLimit() + orderSell.getPriceLimit()) / 2;
		
		// ADJUST AVG_PRICE - BUYORDER
		double newAvgBuyPrice = (posRcv.getQty() * posRcv.getPriceAvg() + orderBuy.getQty() * orderBuy.getPriceLimit())
				/ (posRcv.getQty() + orderBuy.getQty());
		posRcv.setPriceAvg(newAvgBuyPrice);
		posRcv = (ObjPosStex) posSvc.save(posRcv);

		// CREATE EXECUTION
		ExecStex execStex = new ExecStex(posSend, posRcv, orderSell, orderBuy, book_text, qtyExec, execPrice);
		execStexRepo.save(execStex);

		// BOOK POSITIONS
		posSvc.creditPos(orderBuy, execStex);
		posSvc.debitPos(orderSell, execStex);

		// ADJUST EXEC QTY - ORDER
		orderSell.raiseQtyExec(execStex.getQtyExec());
		orderBuy.raiseQtyExec(execStex.getQtyExec());

		// TRANSFER MONEY
		ObjPosMacc maccSend = partySvc.getMacc(partyBuyer);
		ObjPosMacc maccRcv = partySvc.getMacc(partySeller);
		String bookText = "Payment for execution of Stex Buy Order ID: " + orderBuy.getId()
				+ " against STEX sell order ID: " + orderSell.getId();

		OrderPay payment = orderPaySvc.createPay(execStex.getAmtExec(), orderBuy.getUser(), null, bookText, maccSend,
				maccRcv);
		orderPaySvc.execPay(payment);
		
		//TODO: manually transfer money

		// RELASE POS BLOCK - SHARE
		posSend.lowerQtyBlocked(qtyExec);
		posSvc.save(posSend);
		
		

		// RELASE POS BLOCK - MACC
		maccRcv.lowerQtyBlocked(qtyExec);
		posSvc.save(maccRcv);

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

	public List<OrderStex> getByAsset(ObjAsset asset) {
		return orderStexRepo.findByAsset(asset);

	}

}
