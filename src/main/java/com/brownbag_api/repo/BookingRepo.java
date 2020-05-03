package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.jpa.Booking;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {

}