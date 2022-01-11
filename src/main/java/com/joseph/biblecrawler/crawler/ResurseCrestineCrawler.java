package com.joseph.biblecrawler.crawler;

import com.joseph.biblecrawler.model.Verse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ResurseCrestineCrawler extends Crawler {

    @Override
    protected List<Verse> getVerseList(Document html, String bookName, int chapter) {
        List<Verse> verseList = new ArrayList<>();
        int verseNum;
        Elements verses = getVerseElements(html.body().getElementsByClass("bible"));
        Verse pickedVerse = null;
        for (Element verse : verses) {
            if (!verse.className().contains("numar")) {
                continue;
            }
            pickedVerse = new Verse();
            pickedVerse.setBook(bookName);
            pickedVerse.setChapter(chapter);
            String verseNumArgument = verse.text();
            if (isInt(verseNumArgument)) {
                pickedVerse.setVerseNum(Integer.parseInt(verseNumArgument));
            }
            if (storedVerseList.size() != 0) {
                try {
                    pickedVerse.setId(checkExist(pickedVerse).getId());
                } catch (NullPointerException e) {}
            }
            String content = null;
            if (verse.nextElementSibling().className().contains("continut")) {
                content = verse.nextElementSibling().text();
            }
            pickedVerse.setContent(content.replace("*","").replace("†",""));
            if (pickedVerse.getVerseNum() != 0) {
                verseList.add(pickedVerse);
            }
        }
        return verseList;
    }

    private Elements getVerseElements(Elements elements) {
        Elements result = elements.select("span");
        return result;
    }

    private boolean isInt(String verseNum) {
        boolean isInt = false;
        try {
            Integer.parseInt(verseNum);
            isInt = true;
        } catch (NumberFormatException e) {}
        return isInt;
    }
}
