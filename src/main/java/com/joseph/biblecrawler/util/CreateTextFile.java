package com.joseph.biblecrawler.util;

import java.io.File;
import java.io.IOException;

public class CreateTextFile {
        private static int[] maxChapterList = {50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48, 12, 14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4, 28, 16, 24, 21, 28, 16, 16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22};
        private static String[] bibleNames = {"1.창세기","2.출애굽기","3.레위기","4.민수기","5.신명기","6.여호수아","7.사사기","8.룻기","9.사무엘상","10.사무엘하","11.열왕기상","12.열왕기하","13.역대상","14.역대하","15.에스라","16.느헤미야","17.에스더","18.욥기","19.시편","20.잠언","21.전도서","22.아가","23.이사야","24.예레미야","25.예레미야 애가","26.에스겔","27.다니엘","28.호세아","29.요엘","30.아모스","31.오바댜","32.요나","33.미가","34.나훔","35.하박국","36.스바냐","37.학개","38.스가랴","39.말라기","40.마태복음","41.마가복음","42.누가복음","43.요한복음","44.사도행전","45.로마서","46.고린도전서","47.고린도후서","48.갈라디아서","49.에베소서","50.빌립보서","51.골로새서","52.데살로니가전서","53.데살로니가후서","54.디모데전서","55.디모데후서","56.디도서","57.빌레몬서","58.히브리서","59.야고보서","60.베드로전서","61.베드로후서","62.요한일서","63.요한이서","64.요한삼서","65.유다서","66.요한계시록"};
    public static void createTextFile(String filePath) {

        if (filePath == null) { throw new NullPointerException();}

        for (int maxChapterIndex = 0; maxChapterIndex < maxChapterList.length; maxChapterIndex++) {
            for (int i = 0; i < maxChapterList[maxChapterIndex]; i++) {
                File dir = new File(filePath);
                if (!dir.exists()) {
                    dir.mkdirs();

                }

                String bibleName = bibleNames[maxChapterIndex];
                int chapter = i + 1;
                File textFile = new File(getTextFileName(filePath, bibleName, chapter));

                if (!textFile.exists()) {
                    try {
                        textFile.createNewFile();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        }
    }

    private static String getTextFileName(String filePath, String bibleName, int chapter) {
        return "" +
            filePath + File.separator + bibleName + "-" + chapter + ".txt";
    }

    public static String getDesktopPath() {
        return System.getProperty("user.home") + "/Desktop";
    }

}
