package com.joseph.biblecrawler.crawler;

import com.joseph.biblecrawler.model.Verse;
import com.joseph.biblecrawler.util.ExcelExporter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ResurseCrestineCrawler implements Crawler {
    private String filePath;
    private List<Verse> storedVerseList = new ArrayList<>();

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String exportToExcelFile() throws Exception {
        String fileName;
        List<Verse> verseList = new ArrayList<>();
        verseList = getAllVerseList(filePath);
        fileName = ExcelExporter.exportToExcel(verseList, filePath);
        return fileName;
    }

    public List<Verse> getAllVerseList(String filePath) {
        List<Verse> allVerseList = new ArrayList<>();
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        for (File file:files) {
            if (file.isFile() && isTxtFile(file)) {
                List<Verse> verseList = getVerseListFromFile(file);
                allVerseList.addAll(verseList);
            }
        }
        System.out.println(allVerseList.toString());
        return allVerseList;
    }

    private boolean isTxtFile(File file) {
        int extPointIndex = file.getName().lastIndexOf(".");
        String fileExtension = file.getName().substring(extPointIndex + 1);
        boolean result = fileExtension.equals("txt");
        return result;
    }

    private List<Verse> getVerseListFromFile(File file) {
        String bookName = getBookName(file);
        int chapter = getChapter(file);
        Document html = getHtml(file);

        return getVerseList(html, bookName, chapter);
    }

    public Document getHtml(File file) {
        String strHtml = fileToJSString(file.getPath());
        Document html = Jsoup.parse(strHtml);
        return html;
    }

    private int getChapter(File file) {
        String fileName = file.getName();
        String strChapter = fileName.substring(fileName.indexOf("-") + 1,fileName.lastIndexOf("."));
        int chapter = Integer.parseInt(strChapter);
        return chapter;
    }

    private String getBookName(File file) {
        String fileName = file.getName();
        String bookName = fileName.substring(fileName.indexOf(".") + 1, fileName.indexOf("-"));
        return bookName;
    }

    private List<Verse> getVerseList(Document html, String bookName, int chapter) {
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
            verseList.add(pickedVerse);
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

    private Verse checkExist(Verse verse) {
        Verse result = null;
        for (Verse storedVerse:storedVerseList) {
            if (storedVerse.getBook().equals(verse.getBook()) &&
                    storedVerse.getChapter() == verse.getChapter() &&
                    storedVerse.getVerseNum() == verse.getVerseNum()) {
                result = storedVerse;
            }
        }
        return result;
    }

    private String fileToJSString(final String inScriptFile) {
        Path p = Paths.get(inScriptFile);
        if (!Files.exists(p)) {
            System.err.println("fileToJSString : error, file does not exist");
            return null;
        }
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(inScriptFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (lines == null) {
            System.err.println("fileToJSString : error, no lines read from file");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : lines)
            sb.append(s.trim() + " ");
        return sb.toString();
    }
}
