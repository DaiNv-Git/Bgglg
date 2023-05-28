package com.example.itspower.service.exportexcel;

import com.example.itspower.response.employee.EmployeeExportExcel;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class EmployeeExportExcelService {
    private Workbook workbook = new SXSSFWorkbook();
    private Sheet sheet;
    private Sheet sheet1;
    private Sheet sheet2;
    private Sheet sheet3;
    private Sheet sheet4;
    private Sheet sheet5;
    private  List<EmployeeExportExcel> exportExcels;
    private final ResourceLoader resourceLoader;

    public EmployeeExportExcelService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void initializeData( List<EmployeeExportExcel> exportExcels) {
         this.exportExcels=exportExcels;
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
        Resource resource = resourceLoader.getResource("classpath:template/employee.xls");
        InputStream inp = resource.getInputStream();
        workbook = WorkbookFactory.create(inp);
        sheet = workbook.getSheetAt(0);
        CellStyle style = workbook.createCellStyle();
        CellStyle styleFooter = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        XSSFFont font1 = (XSSFFont) workbook.createFont();
        font.setFontHeight(14);
        font1.setBold(true);
        font1.setFontHeight(14);
        style.setFont(font);
        XSSFCellStyle style3 =(XSSFCellStyle) workbook.createCellStyle();
        style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style3.setBorderBottom(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style3.setBorderLeft(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style3.setBorderRight(BorderStyle.THIN);
        style3.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
        style3.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style.setBorderLeft(BorderStyle.THIN); // Đường viền mỏng phía dưới
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        styleFooter.setFont(font1);
        styleFooter.setWrapText(true);
        styleFooter.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
        styleFooter.setVerticalAlignment(VerticalAlignment.CENTER); // Căn giữa dọc
        Row row1 = sheet.createRow(3);
        int rowCount = 3;
        for (EmployeeExportExcel data2 : exportExcels) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, String.valueOf(data2.getGroupName()), style);
            createCell(row, columnCount++, data2.getName(), style);
            createCell(row, columnCount++, data2.getLabor(), style);
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
