package com.joseph.biblecrawler.util;


import com.joseph.biblecrawler.model.TMX;
import com.joseph.biblecrawler.model.Verse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ExcelExporter {
    public static String[] arrayBible = {"창세기","출애굽기","레위기","민수기","신명기","여호수아","사사기","룻기","사무엘상","사무엘하","열왕기상","열왕기하","역대상","역대하","에스라","느헤미야","에스더","욥기","시편","잠언","전도서","아가","이사야","예레미야","예레미야 애가","에스겔","다니엘","호세아","요엘","아모스","오바댜","요나","미가","나훔","하박국","스바냐","학개","스가랴","말라기","마태복음","마가복음","누가복음","요한복음","사도행전","로마서","고린도전서","고린도후서","갈라디아서","에베소서","빌립보서","골로새서","데살로니가전서","데살로니가후서","디모데전서","디모데후서","디도서","빌레몬서","히브리서","야고보서","베드로전서","베드로후서","요한일서","요한이서","요한삼서","유다서","요한계시록"};
    public static String[] arrayBibleEng = {"Genesis","Exodus","Leviticus","Numbers","Deuteronomy","Joshua","Judges","Ruth","1 Samuel","2 Samuel","1 Kings","2 Kings","1 Chronicles","2 Chronicles","Ezra","Nehemiah","Esther","Job","Psalm","Proverbs","Ecclesiastes","Song of Solomon","Isaiah","Jeremiah","Lamentations","Ezekiel","Daniel","Hosea","Joel","Amos","Obadiah","Jonah","Micah","Nahum","Habakkuk","Zephaniah","Haggai","Zechariah","Malachi","Matthew","Mark","Luke","John","Acts","Romans","1 Corinthians","2 Corinthians","Galatians","Ephesians","Philippians","Colossians","1 Thessalonians","2 Thessalonians","1 Timothy","2 Timothy","Titus","Philemon","Hebrews","James","1 Peter","2 Peter","1 John","2 John","3 John","Jude","Revelation"};
    public static List<String> bibleOrder = Arrays.asList(arrayBible);
    public static List<String> bibleOrderEng = Arrays.asList(arrayBibleEng);

    public static String exportToTMX(List<TMX> tmxList, String filePath) throws IOException {
        String fileName;

        Collections.sort(tmxList);

        Workbook xlsxWb = new HSSFWorkbook();
        Sheet sheet = xlsxWb.createSheet("Sheet1");

        insertTMXData(sheet, tmxList);
        fileName = exportFile(xlsxWb, filePath);
        return fileName;
    }

    public static String exportToExcel(List<Verse> verseList, String filePath) throws IOException {
        String fileName;

        Collections.sort(verseList);

        Workbook xlsxWb = new HSSFWorkbook();
        Sheet sheet = xlsxWb.createSheet("Sheet1");

        insertData(sheet, verseList);
        fileName = exportFile(xlsxWb, filePath);
        return fileName;
    }

    private static String exportFile(Workbook workbook, String filePath) throws IOException {
        String fileName;
        File xlsFile = null;

        File file = new File(filePath);
        if (file.isDirectory() && file.exists()) {
            String savedDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
            fileName = filePath + File.separator + "exportedBible_" + savedDate + ".xls";
            fileName = getSequencedFileName(fileName);
        } else {
            fileName = getSequencedFileName(filePath);
        }

        xlsFile = new File(fileName);
        if (xlsFile.isFile() && !xlsFile.exists()) {
            xlsFile.createNewFile();
        }

        FileOutputStream fileOut = new FileOutputStream(xlsFile,false);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();

        return xlsFile.getName();
    }

    private static String getSequencedFileName(String filePath) {

        File path = new File(filePath);
        File directory = path.getParentFile();
        String fileName = getFileName(path);
        String fileNameWithoutExtension = fileName.replace(".xls","");

        File[] filteredFileList = directory.listFiles((dir, name) -> name.contains(fileNameWithoutExtension));
        int sequence = filteredFileList.length + 1;
        String result;
        if (sequence <= 1) {
            result = directory + File.separator + fileName;
        } else {
            int indexFileExtension = fileName.lastIndexOf(".");
            result = directory + File.separator + fileName.substring(0, indexFileExtension) + "(" + sequence + ")"
                    + fileName.substring(indexFileExtension);
        }
        return result;
    }

    private static String getFileName(File path) {
        String fileName = path.getName();
        int indexOfExtension = fileName.lastIndexOf(".");
        if (indexOfExtension == -1 || !fileName.substring(indexOfExtension).contains("xls")) {
            fileName += ".xls";
        }
        return fileName;
    }

    private static void insertTMXData(Sheet sheet, List<TMX> tmxList) {
        setField(sheet);

        for (TMX tmx : tmxList) {
            setRowData(sheet, tmx);
        }
    }

    private static Row createNewRow(Sheet sheet) {
        int rowNum = sheet.getLastRowNum();
        if (rowNum == 0) {
            if (sheet.getRow(rowNum) == null) {
                return sheet.createRow(0);
            } else {
                return sheet.createRow(rowNum + 1);
            }
        } else {
            return sheet.createRow(rowNum + 1);
        }
    }

    private static void setField(Sheet sheet) {
        Row row = sheet.createRow(0);

        Cell cell;
        for (int i = 0; i <= 3; i++) {
            cell = row.createCell(i);
            switch (i) {
                case 0:
                    cell.setCellValue("source Language");
                    break;
                case 1:
                    cell.setCellValue("target Language");
                    break;
                case 2:
                    cell.setCellValue("book");
                    break;
                case 3:
                    cell.setCellValue("version");
                    break;
            }
        }
    }

    private static void setRowData(Sheet sheet, TMX tmx) {
        Row row = createNewRow(sheet);

        Cell cell;
        for (int i = 0; i <= 2; i++) {
            cell = row.createCell(i);
            switch (i) {
                case 0:
                    cell.setCellValue(tmx.getSourceContent());
                    break;
                case 1:
                    cell.setCellValue(tmx.getTargetContent());
                    break;
                case 2:
                    String bookFieldValue = makeBookName(tmx);
                    cell.setCellValue(bookFieldValue);
                    break;
            }
        }
    }

    private static String makeBookName(TMX tmx) {
        int index = bibleOrder.indexOf(tmx.getBook());
        return String.format("%s(%s) %d:%d",tmx.getBook(), bibleOrderEng.get(index), tmx.getChapter(), tmx.getVerseNum());
    }

    private static void insertData(Sheet sheet, List<Verse> verseList) {
        Row row;
        Cell cell;

        for (Verse verse : verseList) {
            row = createNewRow(sheet);
            for (int i = 0; i <= 3; i++) {
                cell = row.createCell(i);
                switch (i) {
                    case 0:
                        cell.setCellValue(verse.getBook());
                        break;
                    case 1:
                        cell.setCellValue(verse.getChapter());
                        break;
                    case 2:
                        cell.setCellValue(verse.getVerseNum());
                        break;
                    case 3:
                        cell.setCellValue(verse.getContent());
                        break;
                }
            }
        }
    }
}
