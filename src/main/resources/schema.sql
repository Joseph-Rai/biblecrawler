CREATE TABLE URL (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     domain VARCHAR(255) NOT NULL);

CREATE TABLE IF NOT EXISTS bible_index (
     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     book VARCHAR(50) NOT NULL,
     max_chapter int(11) NOT NULL);

CREATE TABLE IF NOT EXISTS source_verse (
    verse_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    book VARCHAR(50) NOT NULL,
    chapter int(11) NOT NULL,
    verse_num int(11) NOT NULL,
    content TEXT);

CREATE TABLE IF NOT EXISTS target_verse (
    verse_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    book VARCHAR(50) NOT NULL,
    chapter int(11) NOT NULL,
    verse_num int(11) NOT NULL,
    content TEXT);

CREATE INDEX ON source_verse(book, chapter, verse_num);
CREATE INDEX ON target_verse(book, chapter, verse_num);