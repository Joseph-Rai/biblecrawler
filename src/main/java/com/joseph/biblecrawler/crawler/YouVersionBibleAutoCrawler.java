package com.joseph.biblecrawler.crawler;

import com.joseph.biblecrawler.model.BibleIndex;
import com.joseph.biblecrawler.repository.BibleIndexRepository;

public class YouVersionBibleAutoCrawler extends AutoCrawler {

    public YouVersionBibleAutoCrawler(BibleIndexRepository bibleIndexRepository) {
        super(bibleIndexRepository);
    }

    @Override
    public void getTargetUrlList(String folderPath, String url) {

        String bibleCode = getBibleCode(url);

        //이동을 원하는 url
        String basicUrl = "https://www.bible.com/bible/";

        for (BibleIndex bibleIndex : bibleIndexList) {
            for (int chapter = 1; chapter <= bibleIndex.getMaxChapter(); chapter++) {
                String targetUrl;
                targetUrl = basicUrl + bibleCode + "/" + bibleIndex.getBookName().getYouversion() + "." + chapter;
                targetUrlList.add(targetUrl);

                addFilePath(folderPath, bibleIndex, chapter);
            }
        }
    }

    private String getBibleCode(String url) {
        int startIndex = url.indexOf("/bible") + "/bible".length() + 1;
        int endIndex = url.indexOf("/", startIndex);

        return url.substring(startIndex, endIndex);
    }
}
