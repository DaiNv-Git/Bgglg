package com.example.itspower.repository.repositoryjpa;

import com.example.itspower.model.entity.EmployeeTransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeTransferRepository extends JpaRepository<EmployeeTransferEntity,Integer> {
}
