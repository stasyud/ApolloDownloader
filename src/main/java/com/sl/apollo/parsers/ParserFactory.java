package com.sl.apollo.parsers;

import com.sl.apollo.parsers.impl.AlbumParser;
import com.sl.apollo.parsers.impl.ImageParser;
import com.sl.apollo.parsers.impl.ImageSetParser;
import com.sl.apollo.parsers.impl.MagazineParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory that is used for getting certain instances of parsers depending on a parserType parameter
 * User: SYudenkov
 * Date: 30.01.14
 */
public class ParserFactory {
    public static final int ALBUM = 1;
    public static final int MAGAZINE = 2;
    public static final int IMAGE_SET = 3;
    public static final int IMAGE = 4;
    private Map<Integer, IParser> parserMap;


    private static ParserFactory instance = new ParserFactory();

    public static ParserFactory getInstance() {
        return instance;
    }

    private ParserFactory() {
        parserMap = new HashMap<>();
    }

    public IParser getParser(int parserType) {
        IParser parser = parserMap.get(parserType);
        if (parser != null) return parser;
        switch (parserType) {
            case ALBUM:
                parser = new AlbumParser();
                break;
            case MAGAZINE:
                parser = new MagazineParser();
                break;
            case IMAGE_SET:
                parser = new ImageSetParser();
                break;
            case IMAGE:
                parser = new ImageParser();
                break;
            default:
                throw new IllegalArgumentException("Provided parserType does not exist: " + parserType);
        }
        parserMap.put(parserType, parser);
        return parser;
    }

}
