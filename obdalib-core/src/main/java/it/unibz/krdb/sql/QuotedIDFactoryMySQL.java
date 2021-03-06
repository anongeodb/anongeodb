package it.unibz.krdb.sql;


/*
 * #%L
 * ontop-obdalib-core
 * %%
 * Copyright (C) 2009 - 2014 Free University of Bozen-Bolzano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


/**
 * Creates QuotedIdentifiers following the rules of MySQL:<br>
 *    - unquoted table identifiers are preserved<br>
 *    - unquoted column identifiers are not case-sensitive<br>
 *    - quoted identifiers are preserved
 * 
 * @author Roman Kontchakov
 *
 */

public class QuotedIDFactoryMySQL implements QuotedIDFactory {

	/**
	 * used only in DBMetadataExtractor
	 */
	
	QuotedIDFactoryMySQL() {
		// NO-OP
	}

	@Override
	public QuotedID createAttributeID(String s) {
		if (s == null)
			return new QuotedID(s, QuotedID.NO_QUOTATION);
		
		if (s.startsWith("\"") && s.endsWith("\"")) 
			return new QuotedID(s.substring(1, s.length() - 1), QuotedID.QUOTATION, false);
		if (s.startsWith("`") && s.endsWith("`")) 
			return new QuotedID(s.substring(1, s.length() - 1), QuotedID.QUOTATION, false);
		if (s.startsWith("[") && s.endsWith("]")) 
			return new QuotedID(s.substring(1, s.length() - 1), QuotedID.QUOTATION, false);
		if (s.startsWith("'") && s.endsWith("'")) 
			return new QuotedID(s.substring(1, s.length() - 1), QuotedID.QUOTATION, false);

		return new QuotedID(s, QuotedID.NO_QUOTATION, false);
	}

	@Override
	public RelationID createRelationID(String schema, String table) {
		return new RelationID(createFromString(schema), createFromString(table));			
	}

	public QuotedID createFromString(String s) {
		if (s == null)
			return new QuotedID(s, QuotedID.NO_QUOTATION);
		
		if (s.startsWith("\"") && s.endsWith("\"")) 
			return new QuotedID(s.substring(1, s.length() - 1), QuotedID.QUOTATION);
		if (s.startsWith("`") && s.endsWith("`")) 
			return new QuotedID(s.substring(1, s.length() - 1), QuotedID.QUOTATION);
		if (s.startsWith("[") && s.endsWith("]")) 
			return new QuotedID(s.substring(1, s.length() - 1), QuotedID.QUOTATION);
		if (s.startsWith("'") && s.endsWith("'")) 
			return new QuotedID(s.substring(1, s.length() - 1), QuotedID.QUOTATION);

		return new QuotedID(s, QuotedID.NO_QUOTATION);
	}
}
