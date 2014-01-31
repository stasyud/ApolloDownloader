package com.sl.apollo.parsers.impl;

import com.sl.apollo.model.Resource;
import com.sl.apollo.parsers.AbstractParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that is used for fetching imageURLs
 * Example of URL: http://www.lpi.usra.edu/resources/apollo/frame/?AS17-160-23948
 * User: SYudenkov
 * Date: 30.01.14
 */
public class ImageParser extends AbstractParser {

    @Override
    public List<Resource> parseTable(Elements tables, String initialURL) {
        Resource res = new Resource();
        Element table = tables.first();
        Element smallImg = table.select("img[src]").first();
        if (smallImg != null) {
            res.setImage(true);
            res.setImageURL(getUrl(initialURL, smallImg.attr("src")));
        }
        String features = getFeatures(tables);
        String desc = getDescription(tables);
        String hiResUrl = getHiResImg(tables);
        if (hiResUrl != null) res.setImageURL(getUrl(initialURL, hiResUrl));

        res.setResourceName(getResourceName(features, desc, res.getImageURL()));
        List<Resource> result = new ArrayList<>(1);
        result.add(res);
        return result;
    }
}
