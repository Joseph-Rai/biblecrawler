package com.joseph.biblecrawler.util;

import com.joseph.biblecrawler.model.SourceVerse;
import com.joseph.biblecrawler.model.TargetVerse;
import com.joseph.biblecrawler.model.Verse;
import com.joseph.biblecrawler.repository.SourceVerseRepository;
import com.joseph.biblecrawler.repository.TargetVerseRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ExcelImporter {

    private SourceVerseRepository sourceVerseRepository;
    private TargetVerseRepository targetVerseRepository;

    public ExcelImporter(SourceVerseRepository sourceVerseRepository, TargetVerseRepository targetVerseRepository) {
        this.sourceVerseRepository = sourceVerseRepository;
        this.targetVerseRepository = targetVerseRepository;
    }

    public void importFromExcel(File file) throws IOException {
        Boolean isSource = isSource();
        List<SourceVerse> sourceVerses = new ArrayList<>();
        List<TargetVerse> targetVerses = new ArrayList<>();
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new HSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        Verse verse = null;
        for (Row row : sheet) {
            if (isSource) {
                verse = new SourceVerse();
            } else {
                verse = new TargetVerse();
            }
            for (Cell cell : row) {
                verse.setId(row.getRowNum() + 1L);
                switch(cell.getColumnIndex()) {
                    case 0:
                        verse.setBook(cell.getStringCellValue());
                        break;
                    case 1:
                        verse.setChapter((int) cell.getNumericCellValue());
                        break;
                    case 2:
                        verse.setVerseNum((int) cell.getNumericCellValue());
                        break;
                    case 3:
                        verse.setContent(cell.getStringCellValue());
                        break;
                    default:
                        break;
                }
            }
            if (isSource) {
                sourceVerses.add((SourceVerse) verse);
            } else {
                targetVerses.add((TargetVerse) verse);
            }
        }

        if (isSource) {
            Collections.sort(sourceVerses);
            List<SourceVerse> s = sourceVerseRepository.saveAll(sourceVerses);
        } else {
            Collections.sort(targetVerses);
            List<TargetVerse> s = targetVerseRepository.saveAll(targetVerses);
        }
    }

    private boolean isSource() {
        boolean isSource = false;
        if (sourceVerseRepository.findAll().stream().count() == 0) {
            isSource = true;
        }
        return isSource;
    }

}
