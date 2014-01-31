package com.sl.apollo.core;

import com.sl.apollo.model.Resource;
import com.sl.apollo.parsers.ParserFactory;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Class that is responsible for fetching and parsing HTML pages
 * User: SYudenkov
 * Date: 30.01.14
 */
public class HtmlUtils {
    private static final Logger log = Logger.getLogger(HtmlUtils.class);
    public int counter = 0;

    private static HtmlUtils instance = new HtmlUtils();

    public static HtmlUtils getInstance() {
        return instance;
    }

    private HtmlUtils() {
    }

    /**
     * Method downloads page by the givenURL
     *
     * @param initialURL - url to fetch and parse
     * @param type       - type of page content
     * @return list of parsed resources found on page
     */
    public List<Resource> parsePage(String initialURL, int type) {
        Document doc = null;
        try {
            doc = Jsoup.connect(initialURL).get();
        } catch (IOException e) {
            log.error("Error while getting content of: " + initialURL, e);
            throw new RuntimeException(e);
        }
        log.info("Iterations passed: " + (++counter));

        return parseTable(doc, initialURL, type);
    }

    /**
     * Method is used for parsing tables that were found in initial document to the list of objects that represents each row of the table
     *
     * @param doc - parsed HTML page
     * @return - list of populated objects that represent values of the table
     */
    private List<Resource> parseTable(Document doc, String initialURL, int type) {
        Elements tables = doc.select("table");
        if (tables == null) return Collections.emptyList();
        return ParserFactory.getInstance().getParser(type).parseTable(tables, initialURL);
    }


}
