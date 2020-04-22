package com.brownbag_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.PosMacc;
import com.brownbag_api.model.PosStex;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAssetGrp;
import com.brownbag_api.model.data.EParty;
import com.brownbag_api.model.data.EPartyType;
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

	/**
	 *
	 * @param eParty
	 * @param user      - managing user
	 * @param partyType - Legal or natural person
	 * @param addMacc   - create MACC for newly created Legal Entity
	 * @return
	 */
	public Party createParty(EParty eParty) {
		User mgr;
		mgr = userSvc.getByEnum(eParty.getUser());
		Party party = new Party(eParty.toString(), mgr, eParty.getPartyType());
		party = partyRepo.save(party);
		if (eParty.createMACC) {
			posSvc.createMacc(0, party, 0);
		}
		return party;
	}

	public Party getNaturalPerson(User manager) {
		List<Party> lEs = partyRepo.findByUserAndPartyType(manager, EPartyType.PERSON_NATURAL);
		return lEs.isEmpty() ? null : lEs.get(0);
	}

	public void createNaturalPerson(User user) {
		if (getNaturalPerson(user) == null) {

			Party natPerson = new Party(user.getName(), user, EPartyType.PERSON_NATURAL);
			partyRepo.save(natPerson);

			// ADD MACC
			posSvc.createMacc(100000, natPerson, 0);
		}
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
	
	public Asset goPublic(Party party, double nomVal, int qty) {
		return goPublic(party, null, nomVal, qty);
	}

	public Asset goPublic(Party party, String isin, double nomVal, int qty) {
		Asset asset = assetSvc.createAssetStex(party.getName(), isin, EAssetGrp.STOCK, party, nomVal);
		posSvc.createPosStex(asset, party);
		
		return asset;
	}
}
