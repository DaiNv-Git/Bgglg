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
        for (RestRequest restRequest : requests) {
            String[] restAndLabor = restRequest.getRestNameAndLabor().split("-");
            RestEntity entity = new RestEntity();
            entity.setRestName(restAndLabor[0].trim());
            entity.setReasonId(restRequest.getReasonId());
            entity.setEmployeeLabor(restAndLabor[1].trim());
            entity.setWorkTime(restRequest.getWorkTime());
            entity.setSession(restRequest.getSession());
            entity.setReportId(reportId);
            restEntities.add(entity);
        }
        return restJpaRepository.saveAll(restEntities);
    }
    @Transactional
    public List<RestEntity> updateRest(List<RestRequest> requests, Integer reportId) {
        List<RestEntity> restEntities = new ArrayList<>();
        List<Integer> restIds = new ArrayList<>();
        for (RestRequest restRequest : requests) {
            if (restRequest.isDelete()) {
                restIds.add(restRequest.getRestId());
            } else {
                RestEntity entity = new RestEntity();
                if (restRequest.getRestId() == 0) {
                    String[] restAndLabor = restRequest.getRestNameAndLabor().split("-");
                    entity.setRestName(restAndLabor[0].trim());
                    entity.setEmployeeLabor(restAndLabor[1].trim());
                    entity.setReasonId(restRequest.getReasonId());
                    entity.setReportId(reportId);
                    entity.setWorkTime(restRequest.getWorkTime());
                    entity.setSession(restRequest.getSession());
                    restEntities.add(entity);
                } else {
                    entity.setRestId(restRequest.getRestId());
                    String[] restAndLabor = restRequest.getRestNameAndLabor().split("-");
                    entity.setRestName(restAndLabor[0].trim());
                    entity.setEmployeeLabor(restAndLabor[1].trim());
                    entity.setReasonId(restRequest.getReasonId());
                    entity.setReportId(reportId);
                    entity.setWorkTime(restRequest.getWorkTime());
                    entity.setSession(restRequest.getSession());
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
