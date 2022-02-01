package com.joseph.biblecrawler.model;

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
        Verse verse = (Verse) o;
        return VerseComparator.comparing(this, verse);
    }
}
