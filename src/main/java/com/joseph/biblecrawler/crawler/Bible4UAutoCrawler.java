package com.joseph.biblecrawler.crawler;

import com.joseph.biblecrawler.model.BibleIndex;
import com.joseph.biblecrawler.repository.BibleIndexRepository;

public class Bible4UAutoCrawler extends AutoCrawler {

    public Bible4UAutoCrawler(BibleIndexRepository bibleIndexRepository) {
        super(bibleIndexRepository);
    }

    @Override
    public void getTargetUrlList(String folderPath, String url) {

        String bibleCode = getBibleCode(url);

        //이동을 원하는 url
        String basicUrl = "http://bible4u.pe.kr/zbxe/";

        for (BibleIndex bibleIndex : bibleIndexList) {
            StringBuilder sb = new StringBuilder()
                    .append(basicUrl)
                    .append("?ver=%s&book=%d&chapter=%d");
            for (int chapter = 1; chapter <= bibleIndex.getMaxChapter(); chapter++) {
                String targetUrl = String.format(sb.toString(), bibleCode, bibleIndex.getId(), chapter);
                targetUrlList.add(targetUrl);

                addFilePath(folderPath, bibleIndex, chapter);
            }
        }
    }

    private String getBibleCode(String url) {
        int startIndex = url.indexOf("ver=") + "ver=".length();
        int endIndex = url.indexOf("&", startIndex);

        return url.substring(startIndex, endIndex);
    }
}
