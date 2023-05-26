package com.example.itspower.service.exportexcel;


import com.example.itspower.response.ReportNangsuatResponse;
import com.example.itspower.response.export.EmployeeExportExcelContractEnd;
import com.example.itspower.response.export.ExportExcelDtoReport;
import com.example.itspower.response.export.ExportExcelEmpRest;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.awt.Color;
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
    private List<ReportNangsuatResponse> nangsuat;
    private List<EmployeeExportExcelContractEnd> reportEmpContractEnd;
    private List<ExportExcelEmpRest> exportExcelEmpRests;
    private final ResourceLoader resourceLoader;

    public ExportExcel(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void initializeData(List<ExportExcelDtoReport> reportExcel, List<EmployeeExportExcelContractEnd> reportEmpContractEnd, List<ExportExcelEmpRest> exportExcelEmpRests, List<ReportNangsuatResponse> nangsuat) {
        this.reportExcel = reportExcel;
        this.reportEmpContractEnd = reportEmpContractEnd;
        this.exportExcelEmpRests = exportExcelEmpRests;
        this.nangsuat=nangsuat;
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
        sheet3 = workbook.getSheetAt(3);
        sheet4 = workbook.getSheetAt(0);
        sheet5 = workbook.getSheetAt(0);
        int rowCount = 7;
        int rowCountSheet1 = 7;
        int rowCountSheet2 = 7;
        int rowCountSheet3 = 7;
        CellStyle style = workbook.createCellStyle();
        CellStyle styleFooter = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        XSSFFont font1 = (XSSFFont) workbook.createFont();
        font.setFontHeight(14);
        font1.setBold(true);
        font1.setFontHeight(14);
        style.setFont(font);

        XSSFCellStyle style3 =(XSSFCellStyle) workbook.createCellStyle();

        XSSFColor myColor = new XSSFColor(new Color(255,192,0));
        style3.setFillForegroundColor(myColor);
        style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style3.setBorderBottom(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style3.setBorderLeft(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style3.setBorderRight(BorderStyle.THIN);
        style3.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
        style3.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setBorderTop(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style.setBorderBottom(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style.setBorderLeft(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        styleFooter.setFont(font1);
        styleFooter.setWrapText(true);
        styleFooter.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
        styleFooter.setVerticalAlignment(VerticalAlignment.CENTER); // Căn giữa dọc
        Row row1 = sheet.createRow(4);
        Row row2 = sheet1.createRow(4);
        Row row3 = sheet2.createRow(4);
        Row row4 = sheet3.createRow(4);
        creatCellFormat(row1, String.valueOf(reportExcel.get(0).getReportDate()), style);
        creatCellFormat(row2, String.valueOf(reportExcel.get(0).getReportDate()), style);
        creatCellFormat(row3, String.valueOf(reportExcel.get(0).getReportDate()), style);
        creatCellFormat(row4, String.valueOf(reportExcel.get(0).getReportDate()), style);

        Integer sumEmp = 0;
        Integer sumCus = 0;
        int rowString;
        int rowKey;
        for (ReportNangsuatResponse employee : nangsuat) {
            Row row = sheet3.createRow(rowCountSheet3++);
            int columnCount = 0;
            createCell(row, columnCount++, employee.getId(), style);
            createCell(row, columnCount++, employee.getName(), style);
            createCell(row, columnCount++, employee.getDemarcation(), style);
            createCell(row, columnCount++, employee.getRestNum(), style);
            createCell(row, columnCount++, employee.getStopNumber(), style);
            createCell(row, columnCount++, employee.getNewNumber(), style);
            createCell(row, columnCount++, employee.getStudent(), style);
            createCell(row, columnCount++, employee.getPartTime(), style);
            createCell(row, columnCount, employee.getLaborProductivity(), style3);
        }
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
//        creatCellFormatStr(rowTotal, 0, "Tổng", style);
//        creatCellFormatStr(rowTotal, 1, sumEmp, style);
//        creatCellFormatStr(rowTotal, 2, sumCus, style);
//        creatCellFormatStr(rowTotal, 3, "", style);
        creatCellFormatStr(row10, 0, "Kế toán", styleFooter);
        creatCellFormatStr(row10, 1, "Giám sát", styleFooter);
        creatCellFormatStr(row10, 2, "Nhà bếp", styleFooter);
        creatCellFormatStr(row10, 3, "Người lập bảng", styleFooter);
        creatCellFormatStr(rowNameKey, 3, "Nguyễn Công Lương", styleFooter);
        for (ExportExcelEmpRest data2 : exportExcelEmpRests) {
            Row row = sheet1.createRow(rowCountSheet1++);
            int columnCount = 0;

            createCell(row, columnCount++, String.valueOf(data2.getReportDate()), style);
            createCell(row, columnCount++, data2.getRestName(), style);
            createCell(row, columnCount++, data2.getLabor(), style);
            createCell(row, columnCount++, data2.getGroupName(), style);
            createCell(row, columnCount, data2.getReasonName(), style);
        }
        for (EmployeeExportExcelContractEnd employee : reportEmpContractEnd) {
            Row row = sheet2.createRow(rowCountSheet2++);
            int columnCount = 0;
            createCell(row, columnCount++, employee.getStartDate(), style);
            createCell(row, columnCount++, employee.getEmployeeName(), style);
            createCell(row, columnCount++, employee.getLaborCode(), style);
            createCell(row, columnCount, employee.getGroupName(), style);
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
