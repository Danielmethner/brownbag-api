package com.brownbag_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.enums.ECtrlVar;
import com.brownbag_api.model.enums.EFinStmtSectionType;
import com.brownbag_api.model.enums.EFinStmtType;
import com.brownbag_api.model.enums.ELegalForm;
import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.enums.EParty;
import com.brownbag_api.model.enums.EPartyType;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjFinStmt;
import com.brownbag_api.model.jpa.ObjFinStmtSection;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPosMacc;
import com.brownbag_api.model.jpa.ObjPosStex;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.OrderLoan;
import com.brownbag_api.model.jpa.OrderStex;
import com.brownbag_api.repo.PartyRepo;

@Service
public class PartySvc {

	@Autowired
	private PartySvc partySvc;

	@Autowired
	private PartyRepo partyRepo;

	@Autowired
	private PosSvc posSvc;

	@Autowired
	private AssetSvc assetSvc;

	@Autowired
	private UserSvc userSvc;

	@Autowired
	private LogSvc logSvc;

	@Autowired
	private ControlSvc controlSvc;

	@Autowired
	private OrderStexSvc orderStexSvc;

	@Autowired
	private OrderCreateMonSvc orderCreateMonSvc;
	@Autowired
	private OrderLoanSvc orderLoanSvc;

	@Autowired
	private FinStmtSectionSvc finStmtSectionSvc;

	/**
	 *
	 * @param eParty
	 * @return
	 */
	public ObjParty createParty(EParty eParty) {
		ObjUser user;
		user = userSvc.getByEnum(eParty.getUser());
		String partyName = eParty.getNameNonNaturalPerson();

		if (eParty.getPartyType() == EPartyType.ORG_GOVT) {

			ObjParty party = new ObjParty(partyName, eParty.getPartyType(), eParty.getLegalForm(), user,
					controlSvc.getFinDate());
			party = save(party);

			if (party != null && eParty.isCreateMACC()) {
				posSvc.createMacc(party, 0, null);
			}
			return party;
		}

		if (eParty.getPartyType() == EPartyType.PERSON_LEGAL) {
			ObjParty partyOwner = userSvc.getNaturalPerson(user);
			ObjParty partyLegalPerson = createLegalPerson(partyName, eParty.getLegalForm(), user, partyOwner, 0,
					100000);

			return partyLegalPerson;
		}
		return null;

	}

	public ObjParty save(ObjParty party) {
		if (party.getId() == null && partyRepo.findByName(party.getName()) != null) {
			logSvc.write("Party with name: '" + party.getName() + "' cannot be created: Name is not unique!");
			return null;
		} else {
			return partyRepo.save(party);
		}

	}

	public ObjParty createLegalPersonLLC(String name, ELegalForm legalForm, ObjUser objUser, ObjParty owner,
			double shareCapital) {
		return createLegalPerson(name, legalForm, objUser, owner, 0, shareCapital);
	}

	public ObjParty createLegalPerson(String name, ELegalForm legalForm, ObjUser objUser, ObjParty owner, int shareQty,
			double shareCapital) {

		ObjParty party = new ObjParty(name, EPartyType.PERSON_LEGAL, legalForm, objUser, controlSvc.getFinDate());
		party = save(party);

		if (party == null) {
			return null;
		}
		posSvc.createMacc(party, 0, null);
		shareQty = shareQty == 0 ? 1 : shareQty;

		ObjAsset asset = assetSvc.createAssetStex(party.getName() + " Shares", null, EAssetGrp.STOCK, 0, party, 1, 0,
				null);

		asset = assetSvc.save(asset);
		party.setAsset(asset);
		party = save(party);
		posSvc.createPosStex(asset, owner);
		double transferPrice = shareCapital / shareQty;

		OrderStex ipoSellOrder = issueShares(party, shareQty, transferPrice);
		OrderStex ipoBuyOrder = orderStexSvc.placeNewOrder(EOrderDir.BUY, EOrderType.STEX, asset, shareQty,
				transferPrice, objUser, owner);

		orderStexSvc.matchOrders(ipoBuyOrder, ipoSellOrder);

		ipoBuyOrder = orderStexSvc.getById(ipoBuyOrder.getId());

		EOrderStatus newOrderStatus = ipoBuyOrder.getOrderStatus();

		if (newOrderStatus != EOrderStatus.EXEC_FULL) {
			logSvc.write("PartySvc().createLegalPerson: Emission of new shares failed for Party ID: '" + party.getId()
					+ "'. Party Name: '" + party.getName() + "'. Order Buy ID: '" + ipoBuyOrder.getId()
					+ "' Order Sell ID: '" + ipoSellOrder.getId() + "'");
			return null;
		}
		return party;
	}

	public ObjParty getNaturalPerson(ObjUser user) {
		List<ObjParty> objPartyList = partyRepo.findByUserAndPartyType(user, EPartyType.PERSON_NATURAL);

		if (objPartyList.isEmpty()) {
			ObjParty natPerson = new ObjParty(user.getName(), EPartyType.PERSON_NATURAL, null, user,
					controlSvc.getFinDate());
			natPerson = save(natPerson);
			if (natPerson == null) {
				return null;
			}
			// ADD MACC
			double initialDeposit = controlSvc.getByEnum(ECtrlVar.NATP_INIT_DEPOSIT_AMT).getValDouble();
			ObjPosMacc newMacc = posSvc.createMacc(natPerson, 0, null);

			// ADD INITIAL DEPOSIT FROM CENTRAL BANK
			if (initialDeposit > 0) {

				ObjParty leSend = newMacc.getAsset().getIssuer();
				orderCreateMonSvc.createMon(leSend, initialDeposit);

				ObjPosMacc maccCentralBank = partySvc.getMacc(leSend);
				LocalDateTime matDate = controlSvc.getLastDayOfYear(controlSvc.getFinDate().plusYears(40));
				OrderLoan orderLoan = orderLoanSvc.createLoan(initialDeposit, natPerson.getUser(), maccCentralBank,
						newMacc, matDate, controlSvc.getIntrRate());
				orderLoanSvc.grantLoan(orderLoan);
			}

			return natPerson;
		} else {
			return objPartyList.get(0);
		}
	}

	public ObjParty createNaturalPerson(ObjUser user) {
		return getNaturalPerson(user);
	}

	public ObjPosMacc getMacc(ObjParty party) {
		return posSvc.findByParty(party);
	}

	public ObjParty getByName(String name) {
		return partyRepo.findByName(name);
	}

	public ObjParty getByEnum(EParty eParty) {
		return partyRepo.findByName(eParty.getNameNonNaturalPerson());
	}

	public ObjParty goPublic(ObjParty party, int qty) {

		// ENSURE ONLY LTDs CAN GO PUBLIC
		if (party.getLegalForm() == ELegalForm.LTD) {

			party.setLegalForm(ELegalForm.CORP);
			party = partySvc.save(party);
			if (party != null) {

				ObjAsset asset = assetSvc.getByIssuer(party);
				assetSvc.split(asset, qty);
			}
			return party;
		}
		if (party.getLegalForm() == ELegalForm.CORP) {
			logSvc.write("Party with name '" + party.getName() + "' is already a publicly traded company!");
			return null;
		} else {
			logSvc.write("Party with name '" + party.getName() + "' is NOT eligible to go public!");
			return null;
		}
	}

	public OrderStex issueShares(ObjParty party, int qty, double minPrice) {
		ObjAsset asset = assetSvc.getByIssuer(party);
		return orderStexSvc.placeNewOrder(EOrderDir.SELL, EOrderType.STEX_IPO, asset, qty, minPrice, party.getUser(),
				party);

	}

	public ObjParty getById(Long partyId) {
		return partyRepo.getOne(partyId);
	}

	public List<ObjParty> getLegalPersonByOwnerUser(ObjUser user) {
		return partyRepo.findByUserAndPartyType(user, EPartyType.PERSON_LEGAL);
	}

	public List<ObjPosStex> getOwnershipList(ObjParty jpaParty) {
		List<ObjPosStex> objPosList = posSvc.getByAsset(jpaParty.getAsset());
		return objPosList;
	}

	public double getCredFclty(ObjParty objParty) {
		double credFclty = 0;
		ObjFinStmtSection currentBalSheetSectionEquity = finStmtSectionSvc.getCurrentByPartyIdAndSectionType(objParty, EFinStmtSectionType.EQUITY);
		credFclty = currentBalSheetSectionEquity.getQty();
		return credFclty;
	}
}
