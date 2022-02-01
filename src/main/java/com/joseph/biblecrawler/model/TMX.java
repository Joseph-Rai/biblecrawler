package com.joseph.biblecrawler.model;

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
        TMX tmx = (TMX) o;
        return VerseComparator.comparing(this, tmx);
    }
}
