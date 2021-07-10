package com.joseph.biblecrawler.model;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "source_verse", indexes = {
        @Index(name = "book", columnList = "book"),
        @Index(name = "chapter", columnList = "chapter"),
        @Index(name = "verse_num", columnList = "verse_num")
})
public class SourceVerse extends Verse {
}
