package com.joseph.biblecrawler.model;

import java.util.Comparator;

import static com.joseph.biblecrawler.util.ExcelExporter.bibleOrder;

public abstract class VerseComparator implements Comparator<String> {

    static ComparingObject object1;
    static ComparingObject object2;
    
    public static int comparing(Object o1, Object o2) {
        object1 = convertObject(o1);
        object2 = convertObject(o2);
        if (bibleOrder.indexOf(object1.getBook()) < bibleOrder.indexOf(object2.getBook())) {
            return -1;
        } else if (bibleOrder.indexOf(object1.getBook()) > bibleOrder.indexOf(object2.getBook())) {
            return 1;
        } else {
            if (object1.getChapter() < object2.getChapter()) {
                return -1;
            } else if (object1.getChapter() > object2.getChapter()) {
                return 1;
            } else {
                if (object1.getVerseNum() < object2.getVerseNum()) {
                    return -1;
                } else if (object1.getVerseNum() > object2.getVerseNum()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    private static ComparingObject convertObject(Object object) {
        ComparingObject comparingObject = new ComparingObject();
        if (isVerse(object)) {
            Verse verse = (Verse) object;
            comparingObject.setBook(verse.getBook());
            comparingObject.setChapter(verse.getChapter());
            comparingObject.setVerseNum(verse.getVerseNum());
        } else if (isTMX(object)) {
            TMX tmx = (TMX) object;
            comparingObject.setBook(tmx.getBook());
            comparingObject.setChapter(tmx.getChapter());
            comparingObject.setVerseNum(tmx.getVerseNum());
        }
        return comparingObject;
    }

    private static boolean isVerse(Object object) {
        return object instanceof Verse;
    }
    
    private static boolean isTMX(Object object) {
        return object instanceof TMX;
    }

    private static class ComparingObject {
        String book;
        int chapter;
        int verseNum;

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public int getChapter() {
            return chapter;
        }

        public void setChapter(int chapter) {
            this.chapter = chapter;
        }

        public int getVerseNum() {
            return verseNum;
        }

        public void setVerseNum(int verseNum) {
            this.verseNum = verseNum;
        }
    }
}
