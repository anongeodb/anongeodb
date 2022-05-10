package it.unibz.krdb.obda.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * @author dimitris bilidas
 * @author herald kllapi
 */

public class GeoDBProperties {
    private static final Logger log = LoggerFactory.getLogger(GeoDBProperties.class);
    private static final GenericProperties geoDBProperties;
    private static String NEW_LINE = System.getProperty("line.separator");

    static {

        try {

            geoDBProperties = PropertiesFactory.loadMutableProperties("geodb");
            // load
            Properties properties = System.getProperties();
            for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
                properties.setProperty(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            log.error("Cannot initialize properties", e);
            throw new RuntimeException("can not init props!");
        }
    }

    public GeoDBProperties() {
        throw new RuntimeException("Cannot create instance of this class");
    }

    public static String getNewLine() {
        return NEW_LINE;
    }

    public static GenericProperties getGeoDBProperties() {
        return geoDBProperties;
    }
}
