package com.example.itspower.service.exportexcel;


import com.example.itspower.response.export.EmployeeExportExcelContractEnd;
import com.example.itspower.response.export.ExportExcelDtoReport;
import com.example.itspower.response.export.ExportExcelEmpRest;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class ExportExcel {
    private Workbook workbook = new SXSSFWorkbook();
    private Sheet sheet;
    private Sheet sheet1;
    private Sheet sheet2;
    private Sheet sheet3;
    private Sheet sheet4;
    private Sheet sheet5;
    private List<ExportExcelDtoReport> reportExcel;
    private List<EmployeeExportExcelContractEnd> reportEmpContractEnd;
    private List<ExportExcelEmpRest> exportExcelEmpRests;
    private final ResourceLoader resourceLoader;

    public ExportExcel(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void initializeData(List<ExportExcelDtoReport> reportExcel, List<EmployeeExportExcelContractEnd> reportEmpContractEnd, List<ExportExcelEmpRest> exportExcelEmpRests) {
        this.reportExcel = reportExcel;
        this.reportEmpContractEnd = reportEmpContractEnd;
        this.exportExcelEmpRests = exportExcelEmpRests;
    }

    static void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Byte) {
            cell.setCellValue((Byte) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:template/BGGLG_EXCEL.xls");
        InputStream inp = resource.getInputStream();
        workbook = WorkbookFactory.create(inp);
        sheet = workbook.getSheetAt(0);
        sheet1 = workbook.getSheetAt(1);
        sheet2 = workbook.getSheetAt(2);
        sheet3 = workbook.getSheetAt(0);
        sheet4 = workbook.getSheetAt(0);
        sheet5 = workbook.getSheetAt(0);
        int rowCount = 7;
        int rowCount1 = 7;
        int rowCount2 = 7;
        CellStyle style = workbook.createCellStyle();
        CellStyle style1 = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        style.setBorderTop(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style.setBorderBottom(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style.setBorderLeft(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style1.setFont(font);
        style1.setWrapText(true);
        style1.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
        style1.setVerticalAlignment(VerticalAlignment.CENTER); // Căn giữa dọc
        Row row1 = sheet.createRow(4);
        Row row2 = sheet1.createRow(4);
        Row row3 = sheet2.createRow(4);
        creatCellFormat(row1, String.valueOf(reportExcel.get(0).getReportDate()), style1);
        creatCellFormat(row2, String.valueOf(reportExcel.get(0).getReportDate()), style1);
        creatCellFormat(row3, String.valueOf(reportExcel.get(0).getReportDate()), style1);
        Integer sumEmp = 0;
        Integer sumCus = 0;
        int rowString;
        int rowKey;
        for (ExportExcelDtoReport data1 : reportExcel) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            // Đường viền mỏng phía dưới
            createCell(row, columnCount++, data1.getGroupName(), style);
            createCell(row, columnCount++, data1.getRiceEmp(), style);
            createCell(row, columnCount++, data1.getRiceCus(), style);
            createCell(row, columnCount, "", style);
            sumEmp += data1.getRiceEmp();
            sumCus += data1.getRiceCus();
        }
        rowString = rowCount + 2;
        rowKey = rowCount + 6;
        Row rowTotal = sheet3.createRow(rowCount);
        Row row10 = sheet4.createRow(rowString);
        Row rowNameKey = sheet5.createRow(rowKey);
        creatCellFormatStr(rowTotal, 0, "Tổng", style);
        creatCellFormatStr(rowTotal, 1, sumEmp, style);
        creatCellFormatStr(rowTotal, 2, sumCus, style);
        creatCellFormatStr(rowTotal, 3, "", style);
        creatCellFormatStr(row10, 0, "Kế toán", style1);
        creatCellFormatStr(row10, 1, "Giám sát", style1);
        creatCellFormatStr(row10, 2, "Nhà bếp", style1);
        creatCellFormatStr(row10, 3, "Người lập bảng", style1);
        creatCellFormatStr(rowNameKey, 3, "Nguyễn Công Lương", style1);
        for (ExportExcelEmpRest data2 : exportExcelEmpRests) {
            Row row = sheet1.createRow(rowCount1++);
            int columnCount = 0;

            createCell(row, columnCount++, String.valueOf(data2.getReportDate()), style);
            createCell(row, columnCount++, data2.getRestName(), style);
            createCell(row, columnCount++, data2.getLabor(), style);
            createCell(row, columnCount++, data2.getGroupName(), style);
            createCell(row, columnCount, data2.getReasonName(), style);
        }
        for (EmployeeExportExcelContractEnd employee : reportEmpContractEnd) {
            Row row = sheet2.createRow(rowCount2++);
            int columnCount = 0;
            createCell(row, columnCount++, employee.getStartDate(), style);
            createCell(row, columnCount++, employee.getEmployeeName(), style);
            createCell(row, columnCount++, employee.getLaborCode(), style);
            createCell(row, columnCount, employee.getGroupName(), style);
            createCell(row, columnCount, "", style);
        }
    }

    private void creatCellFormatStr(Row row, int getCell, String value, CellStyle cellStyle) {
        Cell cell = row.createCell(getCell);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    private void creatCellFormatStr(Row row, int getCell, float value, CellStyle cellStyle) {
        Cell cell = row.createCell(getCell);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    private void creatCellFormat(Row row, String value, CellStyle cellStyle) {
        Cell cell = row.createCell(0);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    public byte[] export() throws IOException {
        writeDataLines();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } finally {
            bos.close();
            workbook.close();
        }
        return bos.toByteArray();
    }
}
