
package eu.earthobservatory.utils;

import org.openrdf.query.resultio.TupleQueryResultFormat;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class stSPARQLQueryResultFormat extends TupleQueryResultFormat {

    /**
     * XML format (extension of {@link TupleQueryResultFormat#SPARQL} format to include geometries)
     */
    public static final stSPARQLQueryResultFormat XML = new stSPARQLQueryResultFormat("XML",
            Arrays.asList("application/sparql-results+xml", "application/xml"), Charset.forName("UTF-8"), Arrays.asList("xml"));

    /**
     * KML format (see http://www.opengeospatial.org/standards/kml/)
     */
    public static final stSPARQLQueryResultFormat KML = new stSPARQLQueryResultFormat("KML",
            Arrays.asList("application/vnd.google-earth.kml+xml", "application/kml"), Charset.forName("UTF-8"), Arrays.asList("kml"));

    /**
     * KMZ format (a zipped KML content)
     */
    public static final stSPARQLQueryResultFormat KMZ = new stSPARQLQueryResultFormat("KMZ",
            Arrays.asList("application/vnd.google-earth.kmz", "application/kmz"), Charset.forName("UTF-8"), Arrays.asList("kmz"));

    /**
     * GeoJSON format (see http://www.geojson.org/geojson-spec.html)
     */
    public static final stSPARQLQueryResultFormat GEOJSON = new stSPARQLQueryResultFormat("GeoJSON",
            Arrays.asList("application/json", "application/geojson"), Charset.forName("UTF-8"), Arrays.asList("json"));

    /**
     * Tab separated value format (extension of {@link TupleQueryResultFormat#TSV} format to include geometries)
     */
    public static final stSPARQLQueryResultFormat TSV = new stSPARQLQueryResultFormat("TSV",
            Arrays.asList("text/tab-separated-values"), Charset.forName("UTF-8"), Arrays.asList("tsv"));

    /**
     * HTML format (encoded as an HTML table, without the <tt>&lt;TABLE&gt;</tt> tag)
     */
    public static final stSPARQLQueryResultFormat HTML = new stSPARQLQueryResultFormat("HTML",
            Arrays.asList("text/html"), Charset.forName("UTF-8"), Arrays.asList("html", "htm"));

    /**
     * CHART format (so that results can be displayed using google charts)
     */
    public static final stSPARQLQueryResultFormat PIECHART = new stSPARQLQueryResultFormat("PIECHART",
            Arrays.asList("text/plain"), Charset.forName("UTF-8"), Arrays.asList("piechart", "piechart"));

    public static final stSPARQLQueryResultFormat AREACHART = new stSPARQLQueryResultFormat("AREACHART",
            Arrays.asList("text/plain"), Charset.forName("UTF-8"), Arrays.asList("areachart", "areachart"));

    public static final stSPARQLQueryResultFormat COLUMNCHART = new stSPARQLQueryResultFormat("COLUMNCHART",
            Arrays.asList("text/plain"), Charset.forName("UTF-8"), Arrays.asList("columnchart", "columnchart"));

    /**
     * The available stSPARQLQuery Result Formats
     */
    private static final List<stSPARQLQueryResultFormat> VALUES = new ArrayList<stSPARQLQueryResultFormat>(9);

    // registers stSPARQL/GeoSPARQL formats
    static {
        register(XML);
        register(KML);
        register(KMZ);
        register(GEOJSON);
        register(TSV);
        register(HTML);
        register(PIECHART);
        register(AREACHART);
        register(COLUMNCHART);

    }

    /**
     * Register the specified stSPARQLQueryResultFormat.
     *
     * @param format
     */
    public static void register(stSPARQLQueryResultFormat format) {
        //TupleQueryResultFormat.register(format);
        VALUES.add(format);
    }

    /**
     * Gets the TupleQueryResultFormat given its name. It may be an
     * stSPARQLQueryResultFormat.
     *
     * @param formatName
     * @return
     */
    public static TupleQueryResultFormat valueOf(String formatName) {
        for (TupleQueryResultFormat format : values()) {
            if (format.getName().equalsIgnoreCase(formatName)) {
                return format;
            }
        }

        return null;
    }

    /**
     * Returns all known/registered tuple query result formats.
     * @return
     */
    public static List<stSPARQLQueryResultFormat> values() {
        return VALUES;
    }

    public static stSPARQLQueryResultFormat forMIMEType(String mimeType) {
        //TODO handle
        return XML;
    }

    //public static stSPARQLQueryResultFormat forMIMEType(String mimeType, stSPARQLQueryResultFormat fallback) {
    //    return matchMIMEType(mimeType, VALUES, fallback);
    //}

    public stSPARQLQueryResultFormat(String name, String mimeType, String fileExt) {
        super(name, mimeType, fileExt);
    }

    public stSPARQLQueryResultFormat(String name, String mimeType, Charset charset, String fileExt) {
        super(name, mimeType, charset, fileExt);
    }

    public stSPARQLQueryResultFormat(String name, Collection<String> mimeTypes, Charset charset, Collection<String> fileExtensions) {
        super(name, mimeTypes, charset, fileExtensions);
    }
}