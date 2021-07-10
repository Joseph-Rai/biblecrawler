package com.joseph.biblecrawler.util;

import com.joseph.biblecrawler.model.TMX;
import com.joseph.biblecrawler.repository.BibleIndexRepository;
import com.joseph.biblecrawler.repository.SourceVerseRepository;
import com.joseph.biblecrawler.repository.TargetVerseRepository;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TMXCreator {

    private BibleIndexRepository bibleIndexRepository;
    private SourceVerseRepository sourceVerseRepository;
    private TargetVerseRepository targetVerseRepository;
    @PersistenceContext
    EntityManager em;

    public TMXCreator(BibleIndexRepository bibleIndexRepository,
                      SourceVerseRepository sourceVerseRepository,
                      TargetVerseRepository targetVerseRepository) {
        this.bibleIndexRepository = bibleIndexRepository;
        this.sourceVerseRepository = sourceVerseRepository;
        this.targetVerseRepository = targetVerseRepository;
    }

    public List<TMX> createTMX() {
        String sql = "" +
                "WITH cte AS (\n" +
                "\tSELECT\n" +
                "\t\ta.book AS book\n" +
                "\t\t,a.chapter AS chapter\n" +
                "\t\t,a.verse_num AS verse\n" +
                "\t\t,a.content AS source_text\n" +
                "\t\t,b.content AS target_text\n" +
                "\tFROM source_verse a\n" +
                "\tLEFT OUTER JOIN target_verse b\n" +
                "\t\tON a.book=b.book\n" +
                "\t\t\tAND a.chapter=b.chapter\n" +
                "\t\t\tAND a.verse_num=b.verse_num\n" +
                "\t\n" +
                "\tUNION\n" +
                "\t\n" +
                "\tSELECT\n" +
                "\t\ta.book\n" +
                "\t\t,a.chapter\n" +
                "\t\t,a.verse_num\n" +
                "\t\t,NULL \n" +
                "\t\t,a.content\n" +
                "\tFROM target_verse a\n" +
                "\tWHERE (a.book,a.chapter,a.verse_num) \n" +
                "\t\tNOT IN (\n" +
                "\t\t\tSELECT b.book,b.chapter,b.verse_num FROM source_verse b\n" +
                "\t\t)\n" +
                ")\n" +
                "\n" +
                "SELECT\n" +
                "\ta.*\n" +
                "FROM cte a\n" +
                "LEFT JOIN bible_index b\n" +
                "\tON a.book=b.book\n" +
                "ORDER BY b.id ";

        Query nativeQuery = em.createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        List<TMX> tmxList = new ArrayList<>();
        for (Object[] row : resultList) {
            TMX tmx = new TMX();
            tmx.setBook((String) row[0]);
            tmx.setChapter((Integer) row[1]);
            tmx.setVerseNum((Integer) row[2]);
            if (((Clob) row[3]) != null) {
                tmx.setSourceContent(clobToString((Clob) row[3]));
            }
            if (((Clob) row[4]) != null) {
                tmx.setTargetContent(clobToString((Clob) row[4]));
            }
            tmxList.add(tmx);
        }
        return tmxList;
    }

    private String clobToString(Clob data) {
        StringBuilder sb = new StringBuilder();
        try {
            Reader reader = data.getCharacterStream();
            BufferedReader br = new BufferedReader(reader);

            String line;
            while(null != (line = br.readLine())) {
                sb.append(line);
            }
            br.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
