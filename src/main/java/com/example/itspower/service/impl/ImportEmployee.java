package com.example.itspower.service.impl;

import com.example.itspower.model.entity.EmployeeGroupEntity;
import com.example.itspower.model.entity.GroupEntity;
import com.example.itspower.repository.repositoryjpa.EmployeeGroupRepository;
import com.example.itspower.repository.repositoryjpa.GroupJpaRepository;
import com.example.itspower.util.DateUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImportEmployee {
    @Autowired
    GroupJpaRepository groupJpaRepository;
    @Autowired
    EmployeeGroupRepository employeeGroupRepository;
    public List<EmployeeGroupEntity> getSheetFileExcel(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        List<EmployeeGroupEntity> res = new ArrayList<>();
        List<GroupEntity> groupEntities = groupJpaRepository.findAll();
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            EmployeeGroupEntity employeeGroup = new EmployeeGroupEntity();
            Row row = sheet.getRow(i);
            if (row != null) {
                List<GroupEntity> check=groupEntities.stream().
                        filter(j ->j.getGroupName().
                                equalsIgnoreCase(row.getCell(2).
                                        getStringCellValue())).collect(Collectors.toList());
                if(check.size() >0){
                    employeeGroup.setGroupId(check.stream().map(k -> k.getId()).collect(Collectors.toList()).get(0));
                    employeeGroup.setName(row.getCell(0).getStringCellValue());
                    employeeGroup.setLaborCode(String.valueOf((int) row.getCell(1).getNumericCellValue()));
                    employeeGroup.setCreateDate(DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
                    res.add(employeeGroup);
                }
            }
        }
        employeeGroupRepository.saveAll(res);
        return null;
    }
}
