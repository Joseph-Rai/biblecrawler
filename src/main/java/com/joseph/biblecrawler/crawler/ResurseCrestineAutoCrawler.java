package com.joseph.biblecrawler.crawler;

import com.joseph.biblecrawler.model.BibleIndex;
import com.joseph.biblecrawler.repository.BibleIndexRepository;
import org.springframework.stereotype.Component;

@Component
public class ResurseCrestineAutoCrawler extends AutoCrawler {

    public ResurseCrestineAutoCrawler(BibleIndexRepository bibleIndexRepository) {
        super(bibleIndexRepository);
    }

    @Override
    public void getTargetUrlList(String folderPath, String url) {
        //이동을 원하는 url
        String basicUrl = "https://biblia.resursecrestine.ro/";

        for (BibleIndex bibleIndex : bibleIndexList) {
            for (int chapter = 1; chapter <= bibleIndex.getMaxChapter(); chapter++) {
                String targetUrl;
                targetUrl = basicUrl + "/" + bibleIndex.getBookName().getResursecrestine() + "/" + chapter;
                targetUrlList.add(targetUrl);

                addFilePath(folderPath, bibleIndex, chapter);
            }
        }
    }


}
