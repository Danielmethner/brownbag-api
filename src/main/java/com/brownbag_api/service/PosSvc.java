package com.brownbag_api.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EAsset;
import com.brownbag_api.model.enums.EBookingDir;
import com.brownbag_api.model.enums.EFinStmtItemType;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.jpa.ExecStex;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjPosMacc;
import com.brownbag_api.model.jpa.ObjPosStex;
import com.brownbag_api.model.jpa.OrderCreateMon;
import com.brownbag_api.model.jpa.OrderPay;
import com.brownbag_api.model.jpa.OrderStex;
import com.brownbag_api.model.trans.FinStmtTrxTrans;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.PosMaccRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.repo.PosStexRepo;

@Service
public class PosSvc {

	@Autowired
	private AssetRepo assetRepo;
	@Autowired
	private PosRepo posRepo;
	@Autowired
	private PosMaccRepo posMaccRepo;
	@Autowired
	private PosStexRepo posStexRepo;
	@Autowired
	private BookingSvc bookingSvc;
	@Autowired
	private PartySvc partySvc;

	public ObjPos save(ObjPos pos) {
		return posRepo.save(pos);
	}

	public ObjPosMacc createPosMacc(double qty, ObjAsset assetCurry, ObjParty party) {
		ObjPosMacc posMacc = new ObjPosMacc(qty, assetCurry, party, 1);
		return posRepo.save(posMacc);
	}

	public ObjPosStex createPosStex(ObjAsset asset, ObjParty party) {
		ObjPosStex posStex = new ObjPosStex(0, 0, asset, party, 0);
		return posRepo.save(posStex);
	}

	public ObjPosStex createPosStex(ObjAsset asset, ObjParty partyOwner, int qty) {
		if (partyOwner == null) {
			System.err.println("Party Owner is null");
		}
		ObjPosStex posStex = new ObjPosStex(qty, 0, asset, partyOwner, 0);
		return posRepo.save(posStex);
	}

	public ObjPosMacc createMacc(@NotNull ObjParty owner, double odLimit, ObjAsset currency) {

		// SET EUR AS DEFAULT CURRENCY
		if (currency == null) {
			currency = assetRepo.findByName(EAsset.EUR.getName());
		}

		ObjPosMacc newMacc = createPosMacc(0, currency, owner);

		return newMacc;
	}

	public ObjPosMacc findByParty(ObjParty party) {
		List<ObjPosMacc> maccList = posMaccRepo.findByParty(party);
		return maccList.isEmpty() ? null : maccList.get(0);
	}

	public List<ObjPosStex> getByAsset(ObjAsset asset) {
		return posStexRepo.findByAsset(asset);
	}

	public ObjPos getByAssetAndParty(ObjAsset asset, ObjParty party) {
		ObjPos posStex = posRepo.findByAssetAndParty(asset, party);

		return posStex;
	}

	public double getQtyAvbl(ObjPos pos) {
		return pos.getQty() - pos.getQtyBlocked();
	}

	// -----------------------------------------------------------------
	// PAYMENT - DEBIT
	// -----------------------------------------------------------------
	public ObjPos debitPos(OrderPay orderPay) {
		ObjParty partyPayer = orderPay.getPosSend().getParty();
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// DEBIT ASSETS
		balTrxList.add(
				new FinStmtTrxTrans(EFinStmtItemType.BAL_A_CASH, orderPay.getQty(), EBookingDir.DEBIT, partyPayer));
		// DEBIT EQUITY
		balTrxList.add(
				new FinStmtTrxTrans(EFinStmtItemType.BAL_E_RET_EARN, orderPay.getQty(), EBookingDir.DEBIT, partyPayer));

		// BOOKING
		return bookingSvc.createBooking(orderPay, orderPay.getPosSend(), EBookingDir.DEBIT, balTrxList,
				orderPay.getQty(), orderPay.getAdvText());

	}

	// -----------------------------------------------------------------
	// PAYMENT - CREDIT
	// -----------------------------------------------------------------
	public ObjPos creditPos(OrderPay orderPay) {
		ObjParty partyRecipient = orderPay.getPosRcv().getParty();
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// CREDIT ASSETS
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_A_CASH, orderPay.getQty(), EBookingDir.CREDIT,
				partyRecipient));
		// CREDIT EQUITY
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_E_RET_EARN, orderPay.getQty(), EBookingDir.CREDIT,
				partyRecipient));

		// BOOKING
		return bookingSvc.createBooking(orderPay, orderPay.getPosRcv(), EBookingDir.CREDIT, balTrxList,
				orderPay.getQty(), orderPay.getAdvText());

	}

	// -----------------------------------------------------------------
	// MONEY CREATION - CREDIT
	// -----------------------------------------------------------------
	public ObjPos creditPos(OrderCreateMon orderCreateMon) {
		ObjParty party = orderCreateMon.getPosRcv().getParty();
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// CREDIT ASSETS
		balTrxList.add(
				new FinStmtTrxTrans(EFinStmtItemType.BAL_A_CASH, orderCreateMon.getQty(), EBookingDir.CREDIT, party));
		// CREDIT EQUITY
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_E_RET_EARN, orderCreateMon.getQty(), EBookingDir.CREDIT,
				party));

		// BOOKING
		return bookingSvc.createBooking(orderCreateMon, orderCreateMon.getPosRcv(), EBookingDir.CREDIT, balTrxList,
				orderCreateMon.getQty(), orderCreateMon.getAdvText());
	}

	// -----------------------------------------------------------------
	// STEX - CREDIT POS STEX (BUY Order)
	// -----------------------------------------------------------------
	public ObjPosStex creditPosStex(OrderStex orderBuy, ExecStex execStex) {

		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// CREDIT STOCKS - BUYER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_A_STOCKS, execStex.getAmtExec(), EBookingDir.CREDIT,
				orderBuy.getParty()));

		// CREDIT EQUITY - BUYER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_E_RET_EARN, execStex.getAmtExec(), EBookingDir.CREDIT,
				orderBuy.getParty()));

		// BOOKING
		return (ObjPosStex) bookingSvc.createBooking(orderBuy, execStex.getPosRcv(), EBookingDir.CREDIT, balTrxList,
				execStex.getQtyExec(), execStex.getBookText());

	}

	// -----------------------------------------------------------------
	// STEX - DEBIT POS STEX (SELL Order)
	// -----------------------------------------------------------------
	public ObjPosStex debitPosStex(OrderStex orderSell, ExecStex execStex) {
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		double assetDbAmt = execStex.getQtyExec() * execStex.getPosSend().getPriceAvg();

		// DEBIT P/L FOR EQUITY AMOUNT IF *NOT* IPO
		if (orderSell.getOrderType() != EOrderType.STEX_IPO) {
			// DEBIT STOCKS - BUYER
			balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_A_STOCKS, assetDbAmt, EBookingDir.DEBIT,
					orderSell.getParty()));

			// DEBIT PROFIT
			balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.PL_INVEST, assetDbAmt, EBookingDir.DEBIT,
					orderSell.getParty()));
			// DEBIT EQUITY
			balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_E_RET_EARN, assetDbAmt, EBookingDir.DEBIT,
					orderSell.getParty()));
		}

		// BOOKING
		return (ObjPosStex) bookingSvc.createBooking(orderSell, execStex.getPosSend(), EBookingDir.DEBIT, balTrxList,
				execStex.getQtyExec(), execStex.getBookText());

	}

	// -----------------------------------------------------------------
	// STEX - CREDIT POS MACC (SELL Order)
	// -----------------------------------------------------------------
	public ObjPosMacc creditPosMacc(OrderStex orderSell, ExecStex execStex) {
		ObjPosMacc maccSeller = partySvc.getMacc(orderSell.getParty());
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();

		// CREDIT CASH- SELLER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_A_CASH, execStex.getAmtExec(), EBookingDir.CREDIT,
				orderSell.getParty()));

		// CREDIT P/L FOR EQUITY AMOUNT IF *NOT* IPO
		if (orderSell.getOrderType() == EOrderType.STEX_IPO) {
			// CREDIT PAID IN CAPITAL
			balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_E_CAPITAL_STOCK, execStex.getAmtExec(),
					EBookingDir.CREDIT, orderSell.getParty()));
		} else {
			// CREDIT PL
			balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.PL_INVEST, execStex.getAmtExec(), EBookingDir.CREDIT,
					orderSell.getParty()));

			// CREDIT EQUITY
			balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_E_RET_EARN, execStex.getAmtExec(),
					EBookingDir.CREDIT, orderSell.getParty()));
		}

		// BOOKING
		return (ObjPosMacc) bookingSvc.createBooking(orderSell, maccSeller, EBookingDir.CREDIT, balTrxList,
				execStex.getAmtExec(), execStex.getBookText());
	}

	// -----------------------------------------------------------------
	// STEX - DEBIT POS MACC (BUY Order)
	// -----------------------------------------------------------------
	public ObjPosMacc debitPosMacc(OrderStex orderBuy, ExecStex execStex) {

		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// CREDIT CASH- SELLER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_A_CASH, execStex.getAmtExec(), EBookingDir.DEBIT,
				orderBuy.getParty()));

		// DEBIT EQUITY
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_E_RET_EARN, execStex.getAmtExec(), EBookingDir.DEBIT,
				orderBuy.getParty()));

		// BOOKING
		ObjPosMacc maccBuyer = partySvc.getMacc(orderBuy.getParty());
		return (ObjPosMacc) bookingSvc.createBooking(orderBuy, maccBuyer, EBookingDir.DEBIT, balTrxList,
				execStex.getAmtExec(), execStex.getBookText());
	}

	public List<ObjPos> getByParty(ObjParty jpaParty) {
		return posRepo.findByParty(jpaParty);
	}

	public ObjPos getById(Long posId) {
		return posRepo.getOne(posId);
	}

}
