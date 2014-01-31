package com.sl.apollo;

import com.sl.apollo.core.HtmlUtils;
import com.sl.apollo.core.ParallelBrowser;
import com.sl.apollo.model.Resource;
import org.apache.log4j.Logger;

import static com.sl.apollo.parsers.ParserFactory.*;

import java.util.*;

/**
 * Main class of the application that downloads all the high res images of Apollo program from NASA website
 * User: SYudenkov
 * Date: 29.01.14
 */
public class ApolloDownloader {
    private static final Logger log = Logger.getLogger(ApolloDownloader.class);


    private static ApolloDownloader instance;

    public static ApolloDownloader getInstance() {
        if (instance == null) instance = new ApolloDownloader();
        return instance;
    }

    private ApolloDownloader() {
    }


    private static final Map<String, String> ALBUM_URL_MAP = new HashMap<String, String>() {{
        put("70mm Hasselblad", "http://www.lpi.usra.edu/resources/apollo/catalog/70mm/");
//        put("Mapping (Metric)", "http://www.lpi.usra.edu/resources/apollo/catalog/metric/");
//        put("Panoramic", "http://www.lpi.usra.edu/resources/apollo/catalog/pan/");
//        put("Apollo Lunar Surface Closeup Camera (ALSCC)", "http://www.lpi.usra.edu/resources/apollo/catalog/alscc/");
//        put("35mm Nikon", "http://www.lpi.usra.edu/resources/apollo/catalog/35mm/");
    }};

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ApolloDownloader apolloDownloader = getInstance();
        apolloDownloader.startProcessing();
        log.info("Download finished!");
        start = (System.currentTimeMillis() - start) / 1000;
        log.info("It took: " + start + " seconds");
    }

    public void startProcessing() {
        HtmlUtils utils = HtmlUtils.getInstance();
        Map<String, List<Resource>> albumsMap = new HashMap<>(ALBUM_URL_MAP.size());
        for (String key : ALBUM_URL_MAP.keySet()) {
            String url = ALBUM_URL_MAP.get(key);
            albumsMap.put(key, utils.parsePage(url, ALBUM));
        }
        for (String key : albumsMap.keySet()) {
            List<Resource> albums = albumsMap.get(key);
            for (int i = 0; i < albums.size(); i++) {
                Resource album = albums.get(i);
                List<Resource> magazines = utils.parsePage(album.getResourceURL(), MAGAZINE);
                album.setResourceContentURLs(magazines);
                for (int j = 0; j < magazines.size(); j++) {
                    Resource magazine = magazines.get(j);
                    List<Resource> imageSets = utils.parsePage(magazine.getResourceURL(), IMAGE_SET);
                    magazine.setResourceContentURLs(imageSets);
                    ParallelBrowser browser = ParallelBrowser.getInstance();
                    browser.browse(imageSets, album, magazine);
                }
            }
        }
    }


}
