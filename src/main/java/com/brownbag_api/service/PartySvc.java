package com.brownbag_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.User;
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

	public Pos getMacc(Party party) {
		return posSvc.findByPartyAndIsMacc(party, true);
//TOOD: change to get by group
	}
	
	public Party findByName(String name) {
		return partyRepo.findByName(name);	
	}
	
	public Party findByEnum(EParty eParty) {
		return partyRepo.findByName(eParty.toString());	
	}

}
