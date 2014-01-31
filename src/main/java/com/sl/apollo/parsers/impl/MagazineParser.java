package com.sl.apollo.parsers.impl;

import com.sl.apollo.model.Resource;
import com.sl.apollo.parsers.AbstractParser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that is responsible for parsing pages with structure as on URL:     http://www.lpi.usra.edu/resources/apollo/catalog/70mm/mission/?9
 * User: SYudenkov
 * Date: 30.01.14
 * Time: 14:52
 */
public class MagazineParser extends AbstractParser {

    @Override
    public List<Resource> parseTable(Elements tables, String initialURL) {
        List<Resource> result = new ArrayList<>();
        Element table = tables.first();  //parse only the first table
        Elements rows = table.select("tr");
        for (int i = 0; i < rows.size(); i++) {
            Resource doc = new Resource();
            Elements cells = getRowCells(rows.get(i));
            Element anchor = cells.select("a").first();
            if (anchor == null) continue;
            doc.setResourceURL(getUrl(initialURL, anchor.attr("href")));
            doc.setResourceName(anchor.text());
            String description = cells.get(0).text() + " " + cells.get(2).text();
            doc.setDescription(StringEscapeUtils.unescapeHtml4(description));
            result.add(doc);
        }
        return result;
    }
}
