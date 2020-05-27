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
import com.brownbag_api.model.jpa.OrderStex;
import com.brownbag_api.repo.ExecStexRepo;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.OrderStexRepo;

@Service
public class OrderStexSvc extends OrderSvc {

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private ExecStexRepo execStexRepo;

	@Autowired
	private OrderStexRepo orderStexRepo;

	@Autowired
	private AssetSvc assetSvc;

	@Autowired
	private OrderSvc orderSvc;

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
				ObjPosStex posStex = (ObjPosStex) posSvc.getByAssetAndParty(asset, party);
				if (posStex == null) {
					logSvc.write(
							"No Position with Asset: '" + asset.getName() + " exists for Party: '" + party.getName());
					orderSvc.execAction(orderStex, EOrderAction.DISCARD);
					return null;
				}
				double qtyAvbl = posSvc.getQtyAvbl(posStex);
				if (qtyAvbl < qty) {
					logSvc.write(
							"OrderStexSvc.placeNewOrder: Not enough Shares! Party: '" + party.getName() + "' Asset: '"
									+ asset.getName() + "' Order Quantity: " + qty + " Available Shares: " + qtyAvbl);
					orderSvc.execAction(orderStex, EOrderAction.DISCARD);
					return null;
				}
				posSvc.save(posStex.raiseQtyBlocked(qty));
			}

		}

		return placeOrder(orderStex);

	}

	public void matchOrders(OrderStex orderBuy, OrderStex orderSell) {
		
		// ENSURE BUY ORDER IS NOT A SELL ORDER
		if (orderBuy.getOrderDir() == EOrderDir.SELL) {
			logSvc.write("OrderStexSvc.matchOrders: Cannot match orders: BUY Order has Direction 'SELL'.");
			return;
		}

		// ENSURE SELL ORDER IS NOT A BUY ORDER
		if (orderSell.getOrderDir() == EOrderDir.BUY) {
			logSvc.write("OrderStexSvc.matchOrders: Cannot match orders: SELL Order has Direction 'BUY'.");
			return;
		}
		
		// ENSURE BUY ORDER IS NOT FULLY EXECUTED
		if (orderBuy.getOrderStatus() == EOrderStatus.EXEC_FULL) {
			logSvc.write(
					"OrderStexSvc.matchOrders: Cannot match orders: BUY Order is already in Status 'Fully Executed'.");
			return;
		}
		
		// ENSURE SELL ORDER IS NOT FULLY EXECUTED
		if (orderSell.getOrderStatus() == EOrderStatus.EXEC_FULL) {
			logSvc.write(
					"OrderStexSvc.matchOrders: Cannot match orders: SELL Order is already in Status 'Fully Executed'.");
			return;
		}
		
		// ENSURE PRICE LIMITS ARE COMPATIBLE
		if (orderBuy.getPriceLimit() < orderSell.getPriceLimit()) {
			logSvc.write("OrderStexSvc.matchOrders: Cannot match orders: Buy order Price: '" + orderBuy.getPriceLimit()
					+ " is lower than Sell order Price: '" + orderSell.getPriceLimit() + "'");
			return;
		}
		
		// GET TRADING PARTIES
		ObjParty partySeller = orderSell.getParty();
		ObjParty partyBuyer = orderBuy.getParty();
		
		// GET STEX POSITIONS
		ObjPosStex posSend = (ObjPosStex) posSvc.getByAssetAndParty(orderSell.getAsset(), partySeller); // Instanciates if not exists
		if (posSend == null)
			posSend = posSvc.createPosStex(orderSell.getAsset(), partySeller);

		ObjPosStex posRcv = (ObjPosStex) posSvc.getByAssetAndParty(orderBuy.getAsset(), partyBuyer); // Instanciates if not exists
		if (posRcv == null)
			posRcv = posSvc.createPosStex(orderBuy.getAsset(), partyBuyer);

		// DETERMINE EXECUTION QTY
		int qtyExec = (int) (orderBuy.getQtyOpn() < orderSell.getQtyOpn() ? orderBuy.getQtyOpn()
				: orderSell.getQtyOpn());

		// BOOK TEXT
		String book_text = "Buyer: " + partyBuyer.getName() + " Seller: " + partySeller.getName() + " Qty: " + qtyExec;



		// CALCULATE EXECUTION PRICE
		double execPrice = (orderBuy.getPriceLimit() + orderSell.getPriceLimit()) / 2;

		// ADJUST AVG_PRICE OF POS OF BUY ORDER
		double newAvgBuyPrice = (posRcv.getQty() * posRcv.getPriceAvg() + qtyExec * execPrice)
				/ (posRcv.getQty() + qtyExec);
		posRcv.setPriceAvg(newAvgBuyPrice);
		posRcv = (ObjPosStex) posSvc.save(posRcv);

		// CREATE EXECUTION
		ExecStex execStex = new ExecStex(posSend, posRcv, orderSell, orderBuy, book_text, qtyExec, execPrice);
		execStexRepo.save(execStex);

		// BOOK POSITIONS - STEX BUY
		ObjPosStex creditPos = posSvc.creditPosStex(orderBuy, execStex);

		// IF NOT IPO
		if (orderSell.getOrderType() != EOrderType.STEX_IPO) {

			// BOOK POSITIONS - STEX SELL
			posSvc.debitPosStex(orderSell, execStex);

			// RELASE POS BLOCK - SHARE
			posSend.lowerQtyBlocked(qtyExec);
			posSvc.save(posSend);
		} else {
			ObjAsset asset = orderSell.getAsset();
			asset.raiseTotalShares(qtyExec);
			assetSvc.save(asset);
		}

		// ADJUST EXEC QTY - ORDER
		orderSell.raiseQtyExec(execStex.getQtyExec());
		orderSell = orderStexRepo.save(orderSell);
		orderBuy.raiseQtyExec(execStex.getQtyExec());
		orderBuy = orderStexRepo.save(orderBuy);

		posSvc.creditPosMacc(orderSell, execStex);
		ObjPosMacc maccBuyer = posSvc.debitPosMacc(orderBuy, execStex);

		// RELASE POS BLOCK - MACC
		maccBuyer.lowerQtyBlocked(execStex.getQtyExec() * orderBuy.getPriceLimit());
		posSvc.save(maccBuyer);

		if (creditPos != null) {

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

		// TODO: Cleanup if order fails
	}

	public List<OrderStex> getByAsset(ObjAsset asset) {
		return orderStexRepo.findByAsset(asset);

	}

	public List<OrderStex> getByAssetAndDir(ObjAsset asset, EOrderDir orderDir) {
		return orderStexRepo.findByAssetAndOrderDir(asset, orderDir);

	}

	public List<OrderStex> getByParty(ObjParty party) {
		return orderStexRepo.findByParty(party);
	}
	
	public List<OrderStex> getByAssetAndStatusList(ObjAsset asset, List<EOrderStatus> orderStatusList) {
		return orderStexRepo.findByAssetAndOrderStatusIn(asset, orderStatusList);
	}
	
	public OrderStex getById(Long orderId) {
		return orderStexRepo.getOne(orderId);
	}

	public OrderStex discardOrder(OrderStex orderStex) {
		if(!orderStex.getOrderStatus().discardeable) {
			logSvc.write("OrderStexSvc.discardOrder(): OrderStatus of Order with ID: " + orderStex.getId() + " does not allow it to be discarded.");
			return null;
		}
		
		if(orderStex.getOrderDir() == EOrderDir.BUY) {
			ObjPos objPos = partySvc.getMacc(orderStex.getParty());
			objPos.lowerQtyBlocked(orderStex.getQtyOpn() * orderStex.getPriceLimit());
			posSvc.save(objPos);
		} else {
			ObjPos objPos = posSvc.getByAssetAndParty(orderStex.getAsset(), orderStex.getParty());
			objPos.lowerQtyBlocked(orderStex.getQtyOpn());
			posSvc.save(objPos);
		}
		orderStex = (OrderStex) execAction(orderStex, EOrderAction.DISCARD);
		return orderStex;
	}
}
