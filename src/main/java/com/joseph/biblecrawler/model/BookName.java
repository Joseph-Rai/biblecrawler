package com.joseph.biblecrawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "book_name")
@NoArgsConstructor
@AllArgsConstructor
public class BookName {

    @Id @GeneratedValue
    @Column(name = "book_name_id")
    private Long id;
    private String youversion;
    private String resursecrestine;

}
