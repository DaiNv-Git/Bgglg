package com.example.itspower.repository.repositoryjpa;

import com.example.itspower.model.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TransferJpaRepository extends JpaRepository<TransferEntity, Integer> {
    List<TransferEntity> findByReportId(Integer reportId);

    @Query(value = "select *  from transfer t where DATE_FORMAT(t.transfer_date , '%Y%m%d') = DATE_FORMAT(:transferDate, '%Y%m%d')", nativeQuery = true)
    List<TransferEntity> findTransferDate(@Param("transferDate") String transferDate);

    @Modifying
    @Transactional
    @Query(value = "update transfer t set t.is_access = :isAccess where t.group_id = :groupId ", nativeQuery = true)
    void updateTransfer(@Param("isAccess") boolean isAccess, @Param("groupId") int groupId);
}
