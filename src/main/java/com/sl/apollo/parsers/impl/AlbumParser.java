package com.sl.apollo.parsers.impl;

import com.sl.apollo.model.Resource;
import com.sl.apollo.parsers.AbstractParser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that is responsible for parsing pages with structure as on URL:     http://www.lpi.usra.edu/resources/apollo/catalog/70mm/
 * User: SYudenkov
 * Date: 30.01.14
 */
public class AlbumParser extends AbstractParser {


    @Override
    public List<Resource> parseTable(Elements tables, String initialURL) {
        List<Resource> result = new ArrayList<>();
        Element table = tables.first();  //parse only the first table
        Elements rows = table.select("tr");
        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            Resource doc = new Resource();
            Elements cells = getRowCells(row);
            Element anchor = cells.select("a").first();
            doc.setResourceURL(getUrl(initialURL, anchor.attr("href")));
            doc.setResourceName(anchor.text());
            String description = "";
            for (Element cell : cells) {
                description += cell.text();
            }
            doc.setDescription(StringEscapeUtils.unescapeHtml4(description).replaceAll("( | )", "_"));
            result.add(doc);
        }
        return result;
    }
}
