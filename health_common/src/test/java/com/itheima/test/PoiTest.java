package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PoiTest {
//    @Test
//    public void test1() throws Exception {
//        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\gh\\Desktop\\其他\\poi.xlsx")));
//        XSSFSheet sheet = excel.getSheetAt(0);
//        for (Row row : sheet) {
//            for (Cell cell : row) {
//                System.out.println(cell.getStringCellValue());
//            }
//        }
//        excel.close();
//    }
//    @Test
//    public void test2() throws Exception {
//        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\gh\\Desktop\\其他\\poi.xlsx")));
//        XSSFSheet sheet = excel.getSheetAt(0);
//
//        int lastRowNum = sheet.getLastRowNum();
//        for (int i=0;i<=lastRowNum;i++){
//            XSSFRow row = sheet.getRow(i);
//            short lastCellNum = row.getLastCellNum();
//            for (int j=0;j<lastCellNum;j++){
//                XSSFCell cell = row.getCell(j);
//                System.out.println(cell.getStringCellValue());
//
//            }
//
//        }
//        excel.close();
//    }
//    @Test
//    public void test3() throws Exception{
//        XSSFWorkbook excel = new XSSFWorkbook();
//        XSSFSheet sheet = excel.createSheet("传智播客");
//        XSSFRow title = sheet.createRow(0);
//        title.createCell(0).setCellValue("姓名");
//        title.createCell(1).setCellValue("地址");
//        title.createCell(2).setCellValue("年龄");
//        XSSFRow dataRow = sheet.createRow(1);
//        dataRow.createCell(0).setCellValue("小明");
//        dataRow.createCell(1).setCellValue("北京");
//        dataRow.createCell(2).setCellValue("20");
//        FileOutputStream out = new FileOutputStream(new File("e:\\hello.xlsx"));
//        excel.write(out);
//        out.flush();
//        excel.close();
//    }
}
