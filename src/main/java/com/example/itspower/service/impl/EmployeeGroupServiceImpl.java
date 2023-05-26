package com.example.itspower.service.impl;

import com.example.itspower.model.entity.EmployeeGroupEntity;
import com.example.itspower.repository.repositoryjpa.EmployeeGroupRepository;
import com.example.itspower.request.userrequest.addUserRequest;
import com.example.itspower.response.dynamic.PageResponse;
import com.example.itspower.response.employee.EmployeeExportExcel;
import com.example.itspower.response.employee.EmployeeGroupResponse;
import com.example.itspower.service.EmployeeGroupService;
import com.example.itspower.util.DateUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EmployeeGroupServiceImpl implements EmployeeGroupService {
    @Autowired
    EmployeeGroupRepository groupRepository;

    private final ResourceLoader resourceLoader;

    public EmployeeGroupServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void saveAll(List<addUserRequest> addUser) {
        List<EmployeeGroupEntity> save = new ArrayList<>();
        for (addUserRequest addUserRequest : addUser) {
            EmployeeGroupEntity employeeGroup = new EmployeeGroupEntity();
            employeeGroup.setId(addUserRequest.getId());
            List<EmployeeGroupEntity> entity = groupRepository.findLaborCode(addUserRequest.getLaborCode());
            if (entity.size() >0) {
                throw new RuntimeException("labor code duplicate");
            }
            employeeGroup.setGroupId(addUserRequest.getGroupId());
            employeeGroup.setLaborCode(addUserRequest.getLaborCode());
            employeeGroup.setName(addUserRequest.getName());
            employeeGroup.setCreateDate(DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
            save.add(employeeGroup);
        }
        groupRepository.saveAll(save);
    }

    @Override
    public byte[] exportExcel() throws IOException {
        try {
            Resource resource = resourceLoader.getResource("classpath:template/employee.xls");
            InputStream inp = resource.getInputStream();
            Workbook workbook = new XSSFWorkbook(inp);
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet trong mẫu (template)

            // Ghi dữ liệu vào mẫu Excel
            List<EmployeeExportExcel> exportExcels = groupRepository.getExcelEmployee();
            int rowNum = 1;
            for (EmployeeExportExcel object : exportExcels) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(object.getGroupName());
                row.createCell(1).setCellValue(object.getName());
                row.createCell(2).setCellValue(object.getLabor());
            }

            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi Workbook vào ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            // Chuyển đổi ByteArrayOutputStream thành mảng byte
            byte[] excelBytes = outputStream.toByteArray();

            // Đóng các luồng và giải phóng tài nguyên
            outputStream.close();
            workbook.close();

            return excelBytes;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    @Override
    public void delete(List<Integer> ids) {
        groupRepository.deleteAllById(ids);
    }

    @Override
    public PageResponse getEmployee(String groupName, Integer groupId, String laborCode, String employeeName, int pageSize, int pageNo) {
        int offset = pageSize * (pageNo - 1);
        int countEmployee = groupRepository.countEmployee();
        List<EmployeeGroupResponse> res = groupRepository.getEmployee(groupId, groupName, laborCode, employeeName, pageSize, offset);
        Pageable pageable = PageRequest.of(offset, pageSize);
        final Page<EmployeeGroupResponse> page = new PageImpl<>(res, pageable, 0);
        return new PageResponse<>(page, (long) countEmployee);
    }
}
