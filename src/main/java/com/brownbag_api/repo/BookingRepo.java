package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.jpa.Booking;
import com.brownbag_api.model.jpa.ObjPos;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {

	List<Booking> findByPos(ObjPos pos);
}