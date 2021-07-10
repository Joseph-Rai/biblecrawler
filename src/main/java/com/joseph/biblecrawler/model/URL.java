package com.joseph.biblecrawler.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class URL {

    @Id @GeneratedValue
    @Column(name = "id")
    private Long id;

    private String name;
    private String domain;

    @Override
    public String toString() {
        return id + ". " + name + " - " + domain;
    }
}
