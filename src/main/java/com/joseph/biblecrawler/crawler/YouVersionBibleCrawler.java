package com.joseph.biblecrawler.crawler;

import com.joseph.biblecrawler.model.Verse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class YouVersionBibleCrawler extends Crawler {

    @Override
    protected List<Verse> getVerseList(Document html, String bookName, int chapter) {
        List<Verse> verseList = new ArrayList<>();
        Elements verses = html.select("[data-usfm]:not([class^=ChapterContent_chapter])");
        String prevDataUsfm = null;
        for (Element verse : verses) {
            String curDataUsfm = verse.attr("data-usfm");
            if (curDataUsfm.equals(prevDataUsfm)) {
                String content = extractContent(verse);
                Verse prevVerse = verseList.get(verseList.size() - 1);
                prevVerse.setContent(prevVerse.getContent().concat(" ").concat(content));
                continue;
            }
            Verse pickedVerse = new Verse();
            pickedVerse.setBook(bookName);
            pickedVerse.setChapter(chapter);
            Elements children = verse.select("[class*=ChapterContent_label]");
            String strVerseNum;
            if (children.size() > 0) {
                strVerseNum = children.first().text();
            } else {
                continue;
            }
            int indexOfDash = strVerseNum.indexOf("-");
            if (indexOfDash != -1) {
                strVerseNum = strVerseNum.substring(0, indexOfDash);
            }
            strVerseNum = extractNumberFromString(strVerseNum);
            pickedVerse.setVerseNum(Integer.parseInt(strVerseNum));

            String content = extractContent(verse);
            pickedVerse.setContent(content);

            Verse existedVerse = checkExist(pickedVerse);
            if (storedVerseList.size() != 0 && existedVerse != null) {
                pickedVerse.setId(existedVerse.getId());
            }

            if (existedVerse != null && !pickedVerse.getContent().isBlank()) {
                String existedContent = existedVerse.getContent();
                existedVerse.getContent()
                        .concat(" ")
                        .concat(pickedVerse.getContent());
            } else {
                verseList.add(pickedVerse);
            }
            prevDataUsfm = verse.attr("data-usfm");
        }

        return verseList;
    }

    private String extractContent(Element verse) {
        Elements contents = verse.select("[class*=ChapterContent_content]");
        StringBuilder sb = new StringBuilder();
        for (Element content : contents) {
            if (!content.text().isBlank()) {
                sb.append(content.text());
            }
        }
        return sb.toString();
    }

    private String extractNumberFromString(String strVerseNum) {
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(strVerseNum);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            sb.append(matcher.group());
        }

        return sb.toString();
    }
}
