package com.joseph.biblecrawler.model;

import com.joseph.biblecrawler.util.ExcelExporter;
import lombok.Data;

@Data
public class TMX implements Comparable {

    private Long id;
    private String book;
    private int chapter;
    private int verseNum;
    private String sourceContent;
    private String targetContent;

    @Override
    public int compareTo(Object o) {
        ExcelExporter exporter = new ExcelExporter();
        TMX tmx = (TMX) o;
        if (exporter.bibleOrder.indexOf(book) < exporter.bibleOrder.indexOf(tmx.getBook())) {
            return -1;
        } else if (exporter.bibleOrder.indexOf(book) > exporter.bibleOrder.indexOf(tmx.getBook())) {
            return 1;
        } else {
            if (chapter < tmx.getChapter()) {
                return -1;
            } else if (chapter > tmx.getChapter()) {
                return 1;
            } else {
                if (verseNum < tmx.getVerseNum()) {
                    return -1;
                } else if (verseNum > tmx.getVerseNum()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }
}
