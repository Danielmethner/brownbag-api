package com.brownbag_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.OrderStex;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.PosMacc;
import com.brownbag_api.model.PosStex;
import com.brownbag_api.model.User;
import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.enums.ELegalForm;
import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.enums.EParty;
import com.brownbag_api.model.enums.EPartyType;
import com.brownbag_api.repo.PartyRepo;

@Service
public class PartySvc {

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
	 * @param user      - managing user
	 * @param partyType - Legal or natural person
	 * @param addMacc   - create MACC for newly created Legal Entity
	 * @return
	 */
	public Party createParty(EParty eParty) {
		User user;
		user = userSvc.getByEnum(eParty.getUser());

		if (eParty.getPartyType() == EPartyType.ORG_GOVT) {
			return createOrgGovt(eParty.toString(), eParty.getLegalForm(), user, eParty.createMACC);
		}

		if (eParty.getPartyType() == EPartyType.PERSON_LEGAL) {
			Party natPerson = getNaturalPerson(user);
			return createLegalPerson(eParty.toString(), eParty.getLegalForm(), user, natPerson);
		}
		return null;
	}

	public Party createOrgGovt(String name, ELegalForm legalForm, User user, boolean addMacc) {
		Party party = new Party(name, EPartyType.ORG_GOVT, legalForm, user);
		party = partyRepo.save(party);
		if (addMacc) {
			posSvc.createMacc(0, party, 0);
		}
		return party;
	}

	public Party createLegalPerson(String name, ELegalForm legalForm, User user, Party owner) {
		Party party = new Party(name, EPartyType.PERSON_LEGAL, legalForm, user);
		party = partyRepo.save(party);
		posSvc.createMacc(0, party, 0);
		Asset asset = assetSvc.createAssetStex(party.getName(), null, EAssetGrp.STOCK, party, 1);
		party.setAsset(asset);
		Pos pos = posSvc.createPosStex(asset, owner, 1);
		party = partyRepo.save(party);
		return party;
	}

	public Party getNaturalPerson(User user) {
		List<Party> lEs = partyRepo.findByUserAndPartyType(user, EPartyType.PERSON_NATURAL);
		if( lEs.isEmpty()) {
			Party natPerson = new Party(user.getName(), EPartyType.PERSON_NATURAL, ELegalForm.NOT_APPL, user);
			natPerson = partyRepo.save(natPerson);

			// ADD MACC
			posSvc.createMacc(100000, natPerson, 0);
			return natPerson;
		} else {
			return lEs.get(0);
		}
	}
	
	public Party createNaturalPerson(User user) {
		return getNaturalPerson(user);
	}

	public PosMacc getMacc(Party party) {
		return posSvc.findByParty(party);
	}

	public Party getByName(String name) {
		return partyRepo.findByName(name);
	}

	public Party getByEnum(EParty eParty) {
		return partyRepo.findByName(eParty.toString());
	}

	public Party goPublic(Party party) {
		// ENSURE ONLY LTDs CAN GO PUBLIC - for more legal forms, introduce new
		// attribute on Enumeration
		if (party.getLegalForm() == ELegalForm.LTD) {
			party.setLegalForm(ELegalForm.CORP);
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

	public OrderStex issueShares(Party party, int qty, double minPrice) {

		if(party.getLegalForm() != ELegalForm.CORP) {
			logSvc.write("PartySvc.issueShares(): A party must have the legal form 'corporation' to issue new shares. Party: " + party.getName());
			return null;
		}
		Asset asset = assetSvc.getByIssuer(party);
		return orderStexSvc.placeNewOrder(EOrderDir.SELL, EOrderType.STEX_IPO, asset, qty, minPrice, party.getUser(),
				party);

	}
}
