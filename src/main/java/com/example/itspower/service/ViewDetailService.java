package com.example.itspower.service;

import com.example.itspower.request.export.ExportExcelRequest;
import com.example.itspower.response.group.ViewDetailGroups;

import java.io.IOException;
import java.util.List;

public interface ViewDetailService {
    List<ViewDetailGroups> searchAllView(String reportDate);

    byte[]  exportExcel(List<ExportExcelRequest> request) throws IOException, NoSuchFieldException, IllegalAccessException;
}
