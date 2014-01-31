package com.sl.apollo.parsers;

import com.sl.apollo.model.Resource;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Interface that defines common methods for all HTML element parsers
 * User: SYudenkov
 * Date: 30.01.14
 */
public interface IParser {
    public List<Resource> parseTable(Elements tables, String initialURL);
}
