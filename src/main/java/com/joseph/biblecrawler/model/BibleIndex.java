package com.joseph.biblecrawler.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bible_index")
public class BibleIndex {

    @Id @GeneratedValue
    private Long id;
    private String book;

    @Column(name = "max_chapter")
    private int maxChapter;

}
