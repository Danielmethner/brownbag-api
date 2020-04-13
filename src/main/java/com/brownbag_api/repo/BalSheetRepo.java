package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.Booking;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;

@Repository
public interface BalSheetRepo extends JpaRepository<BalSheet, Long> {
	BalSheet findByPartyAndFinYear(Party party, int finYear);
}