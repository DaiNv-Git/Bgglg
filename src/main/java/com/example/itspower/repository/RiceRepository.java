package com.example.itspower.repository;

import com.example.itspower.model.entity.RiceEntity;
import com.example.itspower.repository.repositoryjpa.RiceJpaRepository;
import com.example.itspower.request.RiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RiceRepository {
    @Autowired
    private RiceJpaRepository riceJpaRepository;

    public RiceEntity getByRiceDetail(Integer reportId) {
        return riceJpaRepository.findByReportId(reportId);
    }

    public RiceEntity saveRice(RiceRequest riceRequest,Integer reportId) {
            RiceEntity entity = new RiceEntity();
            entity.setReportId(reportId);
            entity.setRiceEmp(riceRequest.getRiceEmp());
            entity.setRiceCus(riceRequest.getRiceCus());
            entity.setRiceVip(riceRequest.getRiceVip());
        return riceJpaRepository.save(entity);
    }
}