package com.journalsystem.backend.repository;

import com.journalsystem.backend.entity.Condition;
import com.journalsystem.backend.entity.Patient;
import com.journalsystem.backend.entity.compositIds.ConditionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IConditionRepository extends JpaRepository<Condition, Long> {
    boolean existsByConditionId(ConditionId conditionId);
    List<Condition> findByPatient(Patient patient);
}
