package com.brownbag_api.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.LegalEntity;
import com.brownbag_api.model.Position;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.ELE;
import com.brownbag_api.model.data.ELEType;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.LERepo;
import com.brownbag_api.repo.PosRepo;

@Service
public class LESvc {

	@Autowired
	private LERepo lERepo;
		
	@Autowired
	private PosSvc posSvc;
	
	public LegalEntity createLE(ELE eOrg, User manager, ELEType legalEntityType, boolean addMacc) {
		LegalEntity le = new LegalEntity(eOrg.toString(), manager, legalEntityType);
		le = lERepo.save(le);
		if (addMacc) { 
			posSvc.createMacc(0, le, 0);
		}
		return le;
	}

	public LegalEntity getNaturalPerson(User manager) {
		List<LegalEntity> lEs = lERepo.findByUserAndLegalEntityType(manager, ELEType.PERSON_NATURAL);
		if (!lEs.isEmpty()) {
			LegalEntity natPerson = lEs.get(0);
			return natPerson;
		}

		return null;
	}

	public void createNaturalPerson(User user) {
		if (getNaturalPerson(user) == null) {
			LegalEntity natPerson = new LegalEntity(user.getName(), user, ELEType.PERSON_NATURAL);
			lERepo.save(natPerson);
			posSvc.createMacc(0, natPerson, 0);			
		}
	}

	public Position getMacc(LegalEntity le) {
		return posSvc.findByOwnerAndIsMacc(le, true);
		
	}

}
