package com.joseph.biblecrawler.crawler;

import com.joseph.biblecrawler.model.BibleIndex;
import com.joseph.biblecrawler.repository.BibleIndexRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class AutoCrawler {
    protected WebDriver driver;
    protected BibleIndexRepository bibleIndexRepository;
    protected List<BibleIndex> bibleIndexList;
    protected List<String> targetUrlList = new ArrayList<>();
    protected List<String> filePathList = new ArrayList<>();

    public AutoCrawler(BibleIndexRepository bibleIndexRepository) {
        this.bibleIndexRepository = bibleIndexRepository;
    }

    public void getDefaultDriver() {
        bibleIndexList = bibleIndexRepository.findAll();

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("window-size=1920,1080");
        driver = new ChromeDriver(options);
    }

    public void closeWebDriver() {
        try {
            //드라이버가 null이 아니라면
            if(driver != null) {
                //드라이버 연결 종료
                driver.close(); //드라이버 연결 해제

                //프로세스 종료
                driver.quit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void saveHtmlSource(String filePath) {
        String html = driver.getPageSource();
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);

        Path path = Paths.get(filePath);
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void autoCrawling(String folderPath, String url) throws RuntimeException {
        getTargetUrlList(folderPath, url);

        for (int index = 0; index < targetUrlList.size(); index++) {

            //WebDriver을 해당 url로 이동한다.
            driver.get(targetUrlList.get(index));

            //브라우저 이동시 생기는 로드시간을 기다린다.
            //HTTP응답속도보다 자바의 컴파일 속도가 더 빠르기 때문에 임의적으로 1초를 대기한다.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            // HTML Source를 Text File로 저장
            saveHtmlSource(filePathList.get(index));
        }
    };

    public void addFilePath(String folderPath, BibleIndex bibleIndex, int chapter) {
        String filePath = folderPath + File.separator
                + bibleIndex.getId() + "." + bibleIndex.getBook() + "-" + chapter + ".txt";
        filePathList.add(filePath);
    }

    public abstract void getTargetUrlList(String folderPath, String url);

}
