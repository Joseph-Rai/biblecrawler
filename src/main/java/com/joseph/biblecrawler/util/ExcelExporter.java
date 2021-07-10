package com.joseph.biblecrawler.util;


import com.joseph.biblecrawler.model.TMX;
import com.joseph.biblecrawler.model.Verse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ExcelExporter {
    public static String[] arrayBible = {"창세기","출애굽기","레위기","민수기","신명기","여호수아","사사기","룻기","사무엘상","사무엘하","열왕기상","열왕기하","역대상","역대하","에스라","느헤미야","에스더","욥기","시편","잠언","전도서","아가","이사야","예레미야","예레미야 애가","에스겔","다니엘","호세아","요엘","아모스","오바댜","요나","미가","나훔","하박국","스바냐","학개","스가랴","말라기","마태복음","마가복음","누가복음","요한복음","사도행전","로마서","고린도전서","고린도후서","갈라디아서","에베소서","빌립보서","골로새서","데살로니가전서","데살로니가후서","디모데전서","디모데후서","디도서","빌레몬서","히브리서","야고보서","베드로전서","베드로후서","요한일서","요한이서","요한삼서","유다서","요한계시록"};
    public List<String> bibleOrder;

    public ExcelExporter() {
        bibleOrder = Arrays.asList(arrayBible);
    }

    public static String exportToTMX(List<TMX> tmxList, String filePath) {
        String fileName;

        Collections.sort(tmxList);

        Workbook xlsxWb = new HSSFWorkbook();
        Sheet sheet = xlsxWb.createSheet("Sheet1");

        insertTMXData(sheet, tmxList);
        fileName = exportFile(xlsxWb, filePath);
        return fileName;
    }

    public static String exportToExcel(List<Verse> verseList, String filePath) {
        String fileName;

        Collections.sort(verseList);

        Workbook xlsxWb = new HSSFWorkbook();
        Sheet sheet = xlsxWb.createSheet("Sheet1");

        insertData(sheet, verseList);
        fileName = exportFile(xlsxWb, filePath);
        return fileName;
    }

    private static String exportFile(Workbook workbook, String filePath) {
        String fileName = "";
        try {
            fileName = getSequancedFileName(filePath);
            if (!filePath.substring(filePath.length()-1).equals(File.separator)) {
                filePath = filePath + File.separator;
            }
            File xlsFile = new File(filePath + fileName);
            FileOutputStream fileOut = new FileOutputStream(xlsFile,false);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private static String getSequancedFileName(String filePath) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        File dir = new File(filePath);
        String defaultFileName = "exportedBible";
        File[] filteredFileList = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(defaultFileName)
                        && name.contains(sdf.format(now));
            }
        });
        int sequance = filteredFileList.length + 1;
        String fileName;
        if (sequance <= 1) {
            fileName = defaultFileName + "_" + sdf.format(now) + ".xls";
        } else {
            fileName = defaultFileName + "(" + sequance + ")_" + sdf.format(now) + ".xls";
        }
        return fileName;
    }

    private static void insertTMXData(Sheet sheet, List<TMX> tmxList) {
        Row row = null;
        Cell cell = null;
        int count=0;
        for (TMX tmx : tmxList) {
            row = sheet.createRow(count);
            cell = row.createCell(0);
            cell.setCellValue(tmx.getBook());

            cell = row.createCell(1);
            cell.setCellValue(tmx.getChapter());

            cell = row.createCell(2);
            cell.setCellValue(tmx.getVerseNum());

            cell = row.createCell(3);
            cell.setCellValue(tmx.getSourceContent());

            cell = row.createCell(4);
            cell.setCellValue(tmx.getTargetContent());

            count++;
        }
    }

    private static void insertData(Sheet sheet, List<Verse> verseList) {
        Row row = null;
        Cell cell = null;
        int count=0;
        for (Verse verse : verseList) {
            row = sheet.createRow(count);
            cell = row.createCell(0);
            cell.setCellValue(verse.getBook());

            cell = row.createCell(1);
            cell.setCellValue(verse.getChapter());

            cell = row.createCell(2);
            cell.setCellValue(verse.getVerseNum());

            cell = row.createCell(3);
            cell.setCellValue(verse.getContent());
            count++;
        }
    }
}
