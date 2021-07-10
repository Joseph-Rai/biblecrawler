package com.joseph.biblecrawler.crawler;

public interface Crawler {
    public void setFilePath(String filePath);
    public String exportToExcelFile() throws Exception;
}
