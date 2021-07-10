package com.joseph.biblecrawler.model;

import com.joseph.biblecrawler.util.ExcelExporter;
import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public class Verse implements Comparable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verse_id")
    private Long id;
    private String book;
    private int chapter;

    @Column(name = "verse_num")
    private int verseNum;

    private String content;

    @Override
    public int compareTo(Object o) {
        ExcelExporter exporter = new ExcelExporter();
        Verse verse = (Verse) o;
        if (exporter.bibleOrder.indexOf(book) < exporter.bibleOrder.indexOf(verse.getBook())) {
                return -1;
        } else if (exporter.bibleOrder.indexOf(book) > exporter.bibleOrder.indexOf(verse.getBook())) {
            return 1;
        } else {
            if (chapter < verse.getChapter()) {
                return -1;
            } else if (chapter > verse.getChapter()) {
                return 1;
            } else {
                if (verseNum < verse.getVerseNum()) {
                    return -1;
                } else if (verseNum > verse.getVerseNum()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }
}
