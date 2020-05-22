package com.brownbag_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.enums.ELegalForm;
import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.enums.EParty;
import com.brownbag_api.model.enums.EPartyType;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPosMacc;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.OrderStex;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.util.UtilDate;

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
	private OrderStexSvc orderStexSvc;

	/**
	 *
	 * @param eParty
	 * @return
	 */
	public ObjParty createParty(EParty eParty) {
		ObjUser user;
		user = userSvc.getByEnum(eParty.getUser());

		ObjParty party = new ObjParty(eParty.getName(), eParty.getPartyType(), eParty.getLegalForm(), user, UtilDate.getFinDate());
		party = save(party);
		
		if (eParty.getPartyType() == EPartyType.ORG_GOVT) {
			
			if (party != null && eParty.createMACC) {
				posSvc.createMacc(0, party, 0);
			}
			return party;
		}

		if (eParty.getPartyType() == EPartyType.PERSON_LEGAL) {
			ObjParty natPerson = getNaturalPerson(user);
			
			if (party == null) {
				return null;
			}
			
			posSvc.createMacc(0, party, 0);
			ObjAsset asset = assetSvc.createAssetStex(party.getName() + " Shares", null, EAssetGrp.STOCK, party, 1);

			switch (eParty.getLegalForm()) {
			case LTD:
				asset.setTotalShares(1);
				break;
			case CORP:
				asset.setTotalShares(1);
				break;
			case NOT_APPL:
				asset.setTotalShares(0);
				break;
			}
			asset = assetSvc.save(asset);
			party.setAsset(asset);
			posSvc.createPosStex(asset, natPerson, 1);
			party = save(party);
			return party;
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

//	public ObjParty createLegalPerson(String name, ELegalForm legalForm, ObjUser user, ObjParty owner, int shareQty) {
//
//		ObjParty party = new ObjParty(name, EPartyType.PERSON_LEGAL, legalForm, user);
//
//		party = save(party);
//
//		if (party == null) {
//			return null;
//		}
//		posSvc.createMacc(0, party, 0);
//		ObjAsset asset = assetSvc.createAssetStex(party.getName() + " Shares", null, EAssetGrp.STOCK, party, 1);
//
//		switch (legalForm) {
//		case LTD:
//			asset.setTotalShares(1);
//			break;
//		case CORP:
//			asset.setTotalShares(shareQty);
//			break;
//		case NOT_APPL:
//			asset.setTotalShares(0);
//			break;
//		}
//		asset = assetSvc.save(asset);
//		party.setAsset(asset);
//		posSvc.createPosStex(asset, owner, 1);
//		party = save(party);
//		return party;
//	}

	public ObjParty getNaturalPerson(ObjUser user) {
		List<ObjParty> lEs = partyRepo.findByUserAndPartyType(user, EPartyType.PERSON_NATURAL);
		if (lEs.isEmpty()) {
			ObjParty natPerson = new ObjParty(user.getName(), EPartyType.PERSON_NATURAL, ELegalForm.NOT_APPL, user, UtilDate.getFinDate());
			natPerson = save(natPerson);
			if (natPerson == null) {
				return null;
			}
			// ADD MACC
			posSvc.createMacc(200000, natPerson, 0);
			return natPerson;
		} else {
			return lEs.get(0);
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
		return partyRepo.findByName(eParty.getName());
	}

	public ObjParty goPublic(ObjParty party, int qty) {

		System.err.println("Party: " + party.getName() + " Qty: " + qty);
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

		if (party.getLegalForm() != ELegalForm.CORP) {
			logSvc.write(
					"PartySvc.issueShares(): A party must have the legal form 'corporation' to issue new shares. Party: "
							+ party.getName());
			return null;
		}
		ObjAsset asset = assetSvc.getByIssuer(party);
		return orderStexSvc.placeNewOrder(EOrderDir.SELL, EOrderType.STEX_IPO, asset, qty, minPrice, party.getUser(),
				party);

	}

	public ObjParty getById(Long partyId) {
		return partyRepo.getOne(partyId);
	}
}
