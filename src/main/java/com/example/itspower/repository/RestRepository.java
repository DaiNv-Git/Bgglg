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
        for (RestRequest request : requests) {
            RestEntity entity = new RestEntity();
            entity.setRestName(request.getRestName());
            entity.setReasonId(request.getReasonId());
            entity.setEmployeeLabor(request.getRestEmployeeLabor());
            entity.setWorkTime(request.getWorkTime());
            entity.setSession(request.getSession());
            entity.setReportId(reportId);
            restEntities.add(entity);
        }
        return restJpaRepository.saveAll(restEntities);
    }

    @Transactional
    public List<RestEntity> updateRest(List<RestRequest> requests, Integer reportId) {
        List<RestEntity> restEntities = new ArrayList<>();
        List<Integer> restIds = new ArrayList<>();
        for (RestRequest request : requests) {
            if (request.isDelete()) {
                restIds.add(request.getRestId());
            } else {
                RestEntity entity = new RestEntity();
                if (request.getRestId() == 0) {
                    entity.setRestName(request.getRestName());
                    entity.setReasonId(request.getReasonId());
                    entity.setReportId(reportId);
                    entity.setEmployeeLabor(request.getRestEmployeeLabor());
                    entity.setWorkTime(request.getWorkTime());
                    entity.setSession(request.getSession());
                    restEntities.add(entity);
                } else {
                    entity.setRestId(request.getRestId());
                    entity.setRestName(request.getRestName());
                    entity.setReasonId(request.getReasonId());
                    entity.setReportId(reportId);
                    entity.setEmployeeLabor(request.getRestEmployeeLabor());
                    entity.setWorkTime(request.getWorkTime());
                    entity.setSession(request.getSession());
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
