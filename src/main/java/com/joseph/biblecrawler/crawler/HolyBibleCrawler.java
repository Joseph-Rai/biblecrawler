package com.joseph.biblecrawler.crawler;

import com.joseph.biblecrawler.model.Verse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HolyBibleCrawler extends Crawler {

    @Override
    protected List<Verse> getVerseList(Document html, String bookName, int chapter) {
        List<Verse> verseList = new ArrayList<>();
        int verseNum = 0;
        Elements verses = html.getElementsByClass("tk4l");
        for (Element verse : verses) {
            if (verse.parent().tagName().equals("li")) {
                verseNum++;
                Verse pickedVerse = new Verse();
                pickedVerse.setBook(bookName);
                pickedVerse.setChapter(chapter);
                pickedVerse.setVerseNum(verseNum);
                pickedVerse.setContent(verse.text());
                if (storedVerseList.size() != 0) {
                    pickedVerse.setId(checkExist(pickedVerse).getId());
                }
                if (pickedVerse.getVerseNum() != 0) {
                    verseList.add(pickedVerse);
                }
//                System.out.println(verse.text());
            }
        }
        return verseList;
    }
}
