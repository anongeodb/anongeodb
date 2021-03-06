package it.unibz.krdb.obda.owlrefplatform.core.unfolding;

/*
 * #%L
 * ontop-reformulation-core
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

import it.unibz.krdb.obda.model.Function;
import it.unibz.krdb.obda.model.OBDADataFactory;
import it.unibz.krdb.obda.model.Term;
import it.unibz.krdb.obda.model.URIConstant;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class URIToFunctionMatcher implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3179193407457725565L;
	private Map<String, Function>	functTermMap	= null;

	public URIToFunctionMatcher(Map<String, Function> functionTermMap) {
		this.functTermMap = functionTermMap;
	}
	
	public void put(String st, Function f) {
		functTermMap.put(st, f);
	}

	/***
	 * Looks in the list of functional terms registred during the creation of
	 * the compilatino of the mappings and tries to see if there is afuncitonal
	 * term that could be a match for the given URI.
	 * 
	 * Given our current way to interpret functional terms. A possible match is
	 * a functional term that has a predicate that matches the content of the
	 * URI to the left of the first "-" symbols. For example:
	 * 
	 * http://www.obda.com/onto#individual-mariano-rodriguez
	 * 
	 * is a possible match for the functinonal term
	 * 
	 * http://www.obda.com/onto#individual('mariano','rodriguez')
	 * 
	 * Note that this will change in the future.
	 * 
	 * @param uri
	 * @return
	 */
	public Function getPossibleFunctionalTermMatch(URIConstant uri) {
		String uristr = uri.getIRI().toString();
		int pos = uristr.lastIndexOf('#');
		if (pos == -1) {
			/*
			 * this uri is not generated by our functors, so there must be no
			 * match
			 */
			return null;
		}
		int pos2 = uristr.substring(pos).indexOf('-');
		if (pos2 == -1) {
			/*
			 * this uri is not generated by our functors, so there must be no
			 * match
			 */
			return null;
		}
		pos = pos + pos2;
		String base = uristr.substring(0, pos);
		OBDADataFactory tFact = OBDADataFactoryImpl.getInstance();

		String[] constanturis = uristr.substring(pos + 1, uristr.length()).split("-");

		Function existing = functTermMap.get(base);
		if (existing == null)
			return null;
		if (existing.getFunctionSymbol().getArity() != constanturis.length)
			return null;

		List<Term> constantTerms = new LinkedList<Term>();
		for (String constantstr : constanturis) {
			constantTerms.add(tFact.getConstantLiteral(constantstr));
		}
		return tFact.getFunction(existing.getFunctionSymbol(), constantTerms);

	}
}
