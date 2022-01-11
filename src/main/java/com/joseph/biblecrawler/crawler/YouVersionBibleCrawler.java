package com.joseph.biblecrawler.crawler;

import com.joseph.biblecrawler.model.Verse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class YouVersionBibleCrawler extends Crawler {

    @Override
    protected List<Verse> getVerseList(Document html, String bookName, int chapter) {
        List<Verse> verseList = new ArrayList<>();
        int verseNum;
        Elements verses = getVerseElements(html.body().getElementsByClass("reader"));
        String oldClassName = "";
        int countFlag = 0;
        StringBuilder builder = new StringBuilder();
        Verse pickedVerse = null;
        for (Element verse : verses) {
            countFlag++;
            if (!oldClassName.equals(verse.className())) {
                if (countFlag != 1) {
                    pickedVerse.setContent(builder.toString().trim());
                    if (pickedVerse.getVerseNum() != 0) {
                        verseList.add(pickedVerse);
                    }
                }
                pickedVerse = new Verse();
                builder.setLength(0);
                pickedVerse.setBook(bookName);
                pickedVerse.setChapter(chapter);

                String verseNumArgument;
                try {
                    verseNumArgument = verse.getElementsByClass("label").get(0).text();
                } catch (Exception e) {
                    continue;
                }
                if (verseNumArgument.indexOf("-") != -1) {
                    int indexDash = verseNumArgument.indexOf("-");
                    verseNumArgument = verseNumArgument.substring(0, indexDash);
                }
                if (isInt(verseNumArgument)) {
                    pickedVerse.setVerseNum(Integer.parseInt(verseNumArgument));
                }
                if (storedVerseList.size() != 0) {
                    try {
                        pickedVerse.setId(checkExist(pickedVerse).getId());
                    } catch (NullPointerException e) {}
                }
                builder.append(verse.getElementsByClass("content").text() + " ");
                if (countFlag == verses.size()) {
                    pickedVerse.setContent(builder.toString().trim());
                    if (pickedVerse.getVerseNum() != 0) {
                        verseList.add(pickedVerse);
                    }
                }
            } else {
                builder.append(verse.getElementsByClass("content").text() + " ");
                if (countFlag == verses.size()) {
                    pickedVerse.setContent(builder.toString().trim());
                    if (pickedVerse.getVerseNum() != 0) {
                        verseList.add(pickedVerse);
                    }
                }
            }
            oldClassName = verse.className();
        }
        return verseList;
    }

    private Elements getVerseElements(Elements elements) {
        Elements result = elements.select("span.verse");
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
