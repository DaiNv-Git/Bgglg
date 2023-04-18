package com.example.itspower.repository;

import com.example.itspower.model.entity.RestEntity;
import com.example.itspower.model.resultset.RestDto;
import com.example.itspower.repository.repositoryjpa.RestJpaRepository;
import com.example.itspower.request.RestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class RestRepository {
    @Autowired
    private RestJpaRepository restJpaRepository;

    public List<RestDto> getRests(Integer reportId) {
        return restJpaRepository.findByRest(reportId);
    }


    public List<RestEntity> saveRest(List<RestRequest> requests, Integer reportId) {
        List<RestEntity> restEntities = new ArrayList<>();
        for (RestRequest requestRest : requests) {
            RestEntity entity = new RestEntity();
            String[] restAndLabor = requestRest.getRestNameAndLabor().split("-");
            entity.setRestName(restAndLabor[0].trim());
            entity.setEmployeeLabor(restAndLabor[1].trim());
            entity.setReasonId(requestRest.getReasonId());
            entity.setWorkTime(requestRest.getWorkTime());
            entity.setSession(requestRest.getSession());
            entity.setReportId(reportId);
            restEntities.add(entity);
        }
        return restJpaRepository.saveAll(restEntities);
    }

    @Transactional
    public List<RestEntity> updateRest(List<RestRequest> requests, Integer reportId) {
        List<RestEntity> restEntities = new ArrayList<>();
        List<Integer> restIds = new ArrayList<>();
        for (RestRequest requestRest : requests) {
            if (requestRest.isDelete()) {
                restIds.add(requestRest.getRestId());
            } else {
                RestEntity entity = new RestEntity();

                if (requestRest.getRestId() == 0) {
                    String[] restAndLabor = requestRest.getRestNameAndLabor().split("-");
                    entity.setRestName(restAndLabor[0].trim());
                    entity.setEmployeeLabor(restAndLabor[1].trim());
                    entity.setReasonId(requestRest.getReasonId());
                    entity.setReportId(reportId);
                    entity.setWorkTime(requestRest.getWorkTime());
                    entity.setSession(requestRest.getSession());
                    restEntities.add(entity);
                } else {
                    String[] restAndLabor = requestRest.getRestNameAndLabor().split("-");
                    entity.setRestName(restAndLabor[0].trim());
                    entity.setEmployeeLabor(restAndLabor[1].trim());
                    entity.setRestId(requestRest.getRestId());
                    entity.setReasonId(requestRest.getReasonId());
                    entity.setReportId(reportId);
                    entity.setWorkTime(requestRest.getWorkTime());
                    entity.setSession(requestRest.getSession());
                    restEntities.add(entity);
                }
            }
        }
        restJpaRepository.deleteRestIds(restIds);
        return restJpaRepository.saveAll(restEntities);
    }

    public void deleteRestIdsAndReportId(Integer reportId, List<Integer> restIds) {
        restJpaRepository.deleteByReportIdAndRestIdIn(reportId, restIds);
    }
}
