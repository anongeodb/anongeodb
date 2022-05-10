
package eu.earthobservatory.constants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class OGCConstants {

	/**
	 * Namespace for OGC Units of Measure 1.0
	 */
	public static final String UOM	= "http://www.opengis.net/def/uom/OGC/1.0/";
	
	public static final String OGCdegree		= UOM + "degree";
	public static final String OGCgridSpacing 	= UOM + "GridSpacing";
	public static final String OGCmetre			= UOM + "metre";
	public static final String OGCradian		= UOM + "radian";
	public static final String OGCunity			= UOM + "unity";
	
	public static final List<String> supportedUnitsOfMeasure = new ArrayList<String>();
	
	static {
		Class<OGCConstants> geoConstants = OGCConstants.class;	
		
		try {
			Field[] field = geoConstants.getDeclaredFields();
		
			for (int i = 0; i < field.length; i++) {
				if (field[i].getName().startsWith("OGC")) {
					supportedUnitsOfMeasure.add((String) field[i].get(null));
				}
			}
					
		} catch (SecurityException e) {
			// suppress exception; it should not reach here
		} catch (IllegalArgumentException e) {
			// suppress exception; it should not reach here 
		} catch (IllegalAccessException e) {
			// suppress exception; it should not reach here
		}
	}
}