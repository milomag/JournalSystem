package com.journalsystem.backend.repository;

import com.journalsystem.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPatientRepository extends JpaRepository<Patient, Long>{

}
