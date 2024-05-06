package com.joseph.biblecrawler;

import com.joseph.biblecrawler.crawler.AutoCrawler;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BiblecrawlerApplicationTests {

    @Test
    void contextLoads() {
        String version = AutoCrawler.getChromeVersion();
        version = version.split("\\.")[0];
        System.out.println("version = " + version);
        WebDriverManager.chromedriver().driverVersion(version).setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.naver.com");
        driver.close();
        driver.quit();
    }
}
