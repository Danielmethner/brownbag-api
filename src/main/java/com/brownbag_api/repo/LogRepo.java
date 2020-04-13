package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Log;
import com.brownbag_api.model.Role;

@Repository
public interface LogRepo extends JpaRepository<Log, Long> {
}
