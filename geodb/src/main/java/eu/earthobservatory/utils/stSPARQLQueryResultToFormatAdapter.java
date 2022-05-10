
package eu.earthobservatory.utils;

import org.openrdf.query.resultio.QueryResultIO;
import org.openrdf.query.resultio.TupleQueryResultFormat;
import org.openrdf.query.resultio.TupleQueryResultWriter;

import java.io.OutputStream;
/*import org.openrdf.query.resultio.sparqlgeojson.stSPARQLResultsGeoJSONWriterFactory;
import org.openrdf.query.resultio.sparqlhtml.stSPARQLResultsHTMLWriterFactory;
import org.openrdf.query.resultio.sparqlkml.stSPARQLResultsKMLWriterFactory;
import org.openrdf.query.resultio.sparqlkml.stSPARQLResultsKMZWriterFactory;
import org.openrdf.query.resultio.sparqlxml.stSPARQLResultsXMLWriterFactory;
import org.openrdf.query.resultio.text.stSPARQLResultsTSVWriterFactory;
*/

public class stSPARQLQueryResultToFormatAdapter {

    /*private static TupleQueryResultWriterFactory html = new stSPARQLResultsHTMLWriterFactory();
    private static TupleQueryResultWriterFactory xml = new stSPARQLResultsXMLWriterFactory();
    private static TupleQueryResultWriterFactory kml = new stSPARQLResultsKMLWriterFactory();
    private static TupleQueryResultWriterFactory kmz = new stSPARQLResultsKMZWriterFactory();
    private static TupleQueryResultWriterFactory tsv = new stSPARQLResultsTSVWriterFactory();
    private static TupleQueryResultWriterFactory geojson = new stSPARQLResultsGeoJSONWriterFactory();
    */
    public static TupleQueryResultWriter createstSPARQLQueryResultWriter(Format format, OutputStream out) {
        TupleQueryResultWriter writer = null;

        switch (format) {
            /*case DEFAULT:
                writer = tsv.getWriter(out);
                break;

            case XML:
                writer = xml.getWriter(out);
                break;

            case KML:
                writer = kml.getWriter(out);
                break;

            case KMZ:
                writer = kmz.getWriter(out);
                break;

            case GEOJSON:
                writer = geojson.getWriter(out);
                break;

            case EXP:
                // TODO: add
                break;

            case HTML:
                writer = html.getWriter(out);
                break;

            case TSV:
                writer = tsv.getWriter(out);
                break;
            */
            case XML: //temporary
                writer = QueryResultIO.createTupleWriter(TupleQueryResultFormat.SPARQL, out);
                break;

            case SESAME_XML:
                writer = QueryResultIO.createTupleWriter(TupleQueryResultFormat.SPARQL, out);
                break;

            case SESAME_BINARY:
                writer = QueryResultIO.createTupleWriter(TupleQueryResultFormat.BINARY, out);
                break;

            case SESAME_JSON:
                writer = QueryResultIO.createTupleWriter(TupleQueryResultFormat.JSON, out);
                break;

            case SESAME_CSV:
                writer = QueryResultIO.createTupleWriter(TupleQueryResultFormat.CSV, out);
                break;

            case SESAME_TSV:
                writer = QueryResultIO.createTupleWriter(TupleQueryResultFormat.TSV, out);
                break;

            default:
                // return NULL
        }

        return writer;
    }

}
