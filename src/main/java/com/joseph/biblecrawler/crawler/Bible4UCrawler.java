package com.joseph.biblecrawler.crawler;

import com.joseph.biblecrawler.model.Verse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Bible4UCrawler extends Crawler {

    @Override
    protected List<Verse> getVerseList(Document html, String bookName, int chapter) {
        List<Verse> verseList = new ArrayList<>();
        Elements verses = html.select("table[bgcolor=#FFFFFF] > tbody > tr");

        for (Element verse : verses) {
            Verse pickedVerse = new Verse();
            pickedVerse.setBook(bookName);
            pickedVerse.setChapter(chapter);

            int verseNum = Integer.parseInt(verse.child(0).text());
            pickedVerse.setVerseNum(verseNum);

            pickedVerse.setContent(verse.child(1).text());

            if (storedVerseList.size() != 0) {
                pickedVerse.setId(checkExist(pickedVerse).getId());
            }
            if (pickedVerse.getVerseNum() != 0) {
                verseList.add(pickedVerse);
            }
        }
        return verseList;
    }
}
