//package com.example.itspower.component.util;
//
//
//import lombok.Data;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFFont;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//@Data
//public class ExportExcel {
//    private XSSFWorkbook workbook;
//    private XSSFSheet sheet;
//    private List<?> data;
//    private String file;
//
//    public ExportExcel(List<?> data, String file) {
//        this.data = data;
//        this.file = file;
//    }
//
//    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
//        sheet.autoSizeColumn(columnCount);
//        Cell cell = row.createCell(columnCount);
//        if (value instanceof java.lang.Integer) {
//            cell.setCellValue((java.lang.Integer) value);
//        } else if (value instanceof Boolean) {
//            cell.setCellValue((Boolean) value);
//        } else {
//            cell.setCellValue((String) value);
//        }
//        cell.setCellStyle(style);
//    }
//
//    private void writeDataLines() throws IOException {
//        FileInputStream inputStream = new FileInputStream(file);
//        workbook = new XSSFWorkbook(inputStream);
//        sheet = workbook.getSheetAt(0);
//        int rowCount = 1;
//        CellStyle style = workbook.createCellStyle();
//        XSSFFont font = workbook.createFont();
//        font.setFontHeight(14);
//        style.setFont(font);
////        for (Object data : data) {
////            Row row = sheet.getRow(rowCount++);
////            int columnCount = 0;
////            createCell(row, columnCount++, data, style);
////        }
//    }
//
//    public void export() throws IOException {
//        writeDataLines();
//        FileOutputStream outputStream = new FileOutputStream(file);
//        workbook.write(outputStream);
//        workbook.close();
//        outputStream.close();
//    }
//}
