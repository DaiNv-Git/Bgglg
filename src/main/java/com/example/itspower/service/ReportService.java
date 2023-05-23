package com.example.itspower.service;

import com.example.itspower.request.ReportRequest;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    Object reportDto(String reportDate, int groupId);
    Object search(String reportDate, int groupId);
    Object callDataByDate( int groupId);
    void save(ReportRequest request, int groupId);
    void update(ReportRequest request, int groupId);
    void deleteRestIdsAndReportId(Integer reportId,List<Integer> restIds);
    void deleteRestEmployee(Integer groupId, List<String> laborEmp);
    Integer getTransfer(String reportDate,Integer groupId);

    List<Integer> getIdsToMay();

    byte[] exportExcel() throws IOException;
}
