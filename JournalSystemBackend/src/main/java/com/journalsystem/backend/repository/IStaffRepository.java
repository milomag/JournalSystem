package com.journalsystem.backend.repository;

import com.journalsystem.backend.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStaffRepository extends JpaRepository<Staff, Long> {
    boolean existsByEmail(String email);
}
