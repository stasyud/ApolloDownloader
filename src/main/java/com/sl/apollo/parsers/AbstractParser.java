package com.sl.apollo.parsers;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Base class for all parser classes
 * User: SYudenkov
 * Date: 30.01.14
 */
public abstract class AbstractParser implements IParser {

    private static final String FEATURES = "Feature(s):";
    private static final String DESCRIPTION = "Description:";
    private static final String HI_RES_IMG = "Hi Resolution Image(s):";

    public static final String DELIMITER = "../";

    /**
     * Method forms URL basing on two input URLs
     * Example:
     * initialUrl = http://www.lpi.usra.edu/resources/apollo/catalog/35mm/mission/?17
     * objectUrl = ../magazine/?160
     * method will return: http://www.lpi.usra.edu/resources/apollo/catalog/35mm/magazine/?160
     *
     * @param initialUrl - URL of the current page
     * @param objectUrl  - URL of the next page or image (so this URL is placed on the current page)
     */
    protected String getUrl(String initialUrl, String objectUrl) {
        if (objectUrl.startsWith("http")) return objectUrl;
        int start = objectUrl.indexOf(DELIMITER);
        if (start < 0)
            return initialUrl + objectUrl;
        if (start != 0) { //case there is following URL /resources/apollo/frame/../images/print/AS11/36/5340.jpg
            objectUrl = objectUrl.substring(start);
        }
        int matches = 1;
        while ((start = objectUrl.indexOf(DELIMITER, start + 1)) >= 0) {
            matches++;
        }
        for (int i = 0; i <= matches; i++) {
            int position = initialUrl.lastIndexOf("/");
            if (position >= 0)
                initialUrl = initialUrl.substring(0, position);
        }
        objectUrl = objectUrl.replaceAll("\\.\\./", "");
        return initialUrl + "/" + objectUrl;
    }


    /**
     * method is used for returning cells of provided row
     * It selects either <th> elements or <td>
     *
     * @param row - initial row that is used for fetching cells
     * @return collection of cells
     */
    protected Elements getRowCells(Element row) {
        Elements cells = row.select("td");        //select either headers or plain cells
        if (cells == null || cells.size() == 0) cells = row.select("th");
        return cells;
    }


    protected String getResourceName(String features, String description, String imgUrl) {
        String res = "";
        res += imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.length() - 4);
        if (features != null) res += "_" + features;
        if (description != null) res += "_" + description;
        return res;
    }

    protected String getDescription(Elements tables) {
        Elements rows = tables.select("tr");
        for (Element row : rows) {
            if (row.text().contains(DESCRIPTION)) {
                Elements cells = getRowCells(row);
                return cells.last().text();
            }
        }
        return null;
    }

    protected String getFeatures(Elements tables) {
        Elements rows = tables.select("tr");
        for (Element row : rows) {
            if (row.text().contains(FEATURES)) {
                Elements cells = getRowCells(row);
                return cells.last().text();
            }
        }
        return null;
    }

    protected String getHiResImg(Elements tables) {
        Elements rows = tables.select("tr");
        for (Element row : rows) {
            if (row.text().contains(HI_RES_IMG)) {
                Elements cells = getRowCells(row);
                Element anchor = cells.select("a[href]").first();
                if (anchor != null) return anchor.attr("href");
            }
        }
        return null;
    }

}
