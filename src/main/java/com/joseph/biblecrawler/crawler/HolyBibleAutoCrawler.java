package com.joseph.biblecrawler.crawler;

import com.joseph.biblecrawler.model.BibleIndex;
import com.joseph.biblecrawler.repository.BibleIndexRepository;

import java.util.HashMap;
import java.util.Map;

public class HolyBibleAutoCrawler extends AutoCrawler {

    private Map<String, Integer> bibleCodeMap = new HashMap<>();

    public HolyBibleAutoCrawler(BibleIndexRepository bibleIndexRepository) {
        super(bibleIndexRepository);
        bibleCodeMap.put("B_RHV", 0);
        bibleCodeMap.put("B_COGNEW", 1);
        bibleCodeMap.put("B_SAENEW", 2);
        bibleCodeMap.put("B_NIV", 3);
        bibleCodeMap.put("B_KJV", 4);
        bibleCodeMap.put("B_NASB", 5);
        bibleCodeMap.put("B_KSNKI", 6);
        bibleCodeMap.put("B_KKUG", 7);
        bibleCodeMap.put("B_KSNKD", 8);
        bibleCodeMap.put("B_GAE", 9);
        bibleCodeMap.put("B_HDB", 10);
    }

    @Override
    public void getTargetUrlList(String folderPath, String url) {

        String bookName = getBookCode(url);

        //이동을 원하는 url
        String basicUrl = "http://www.holybible.or.kr";

        for (BibleIndex bibleIndex : bibleIndexList) {
            for (int chapter = 1; chapter <= bibleIndex.getMaxChapter(); chapter++) {
                String targetUrl;
                targetUrl = String.format("%s/%s/cgi/bibleftxt.php?VR=%d&VL=%d&CN=%d",
                        basicUrl, bookName, bibleCodeMap.get(bookName), bibleIndex.getId(), chapter);
                targetUrlList.add(targetUrl);

                addFilePath(folderPath, bibleIndex, chapter);
            }
        }
    }

    private String getBookCode(String url) {
        String basicURL = "www.holybible.or.kr";
        int startIndex = url.indexOf(basicURL) + basicURL.length() + 1;
        int endIndex = url.indexOf("/", startIndex);

        return url.substring(startIndex, endIndex);
    }

}
