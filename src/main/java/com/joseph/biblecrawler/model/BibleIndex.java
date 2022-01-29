package com.joseph.biblecrawler.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bible_index")
public class BibleIndex {

    @Id @GeneratedValue
    @Column(name = "bible_index_id")
    private Long id;
    private String book;
    private int maxChapter;

    @OneToOne
    @JoinColumn(name = "book_name_id")
    private BookName bookName;

}
