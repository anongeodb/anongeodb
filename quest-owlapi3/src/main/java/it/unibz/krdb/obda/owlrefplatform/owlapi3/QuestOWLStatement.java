package it.unibz.krdb.obda.owlrefplatform.owlapi3;

/*
 * #%L
 * ontop-quest-owlapi3
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

import it.unibz.krdb.obda.model.GraphResultSet;
import it.unibz.krdb.obda.model.OBDAException;
import it.unibz.krdb.obda.model.TupleResultSet;
import it.unibz.krdb.obda.ontology.Assertion;
import it.unibz.krdb.obda.ontology.ClassAssertion;
import it.unibz.krdb.obda.ontology.DataPropertyAssertion;
import it.unibz.krdb.obda.ontology.ObjectPropertyAssertion;
import it.unibz.krdb.obda.owlapi3.OWLAPI3IndividualTranslator;
import it.unibz.krdb.obda.owlapi3.OntopOWLException;
import it.unibz.krdb.obda.owlrefplatform.core.GeoDBStatement;
import it.unibz.krdb.obda.owlrefplatform.core.queryevaluation.SPARQLQueryUtility;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.parser.ParsedQuery;
import org.openrdf.query.parser.QueryParser;
import org.openrdf.query.parser.QueryParserUtil;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.ArrayList;
import java.util.List;

/***
 * A Statement to execute queries over a QuestOWLConnection. The logic of this
 * statement is equivalent to that of JDBC's Statements.
 * 
 * <p>
 * <strong>Performance</strong> Note that you should not create multiple
 * statements over the same connection to execute parallel queries (see
 * {@link QuestOWLConnection}). Multiple statements over the same connection are
 * not going to be very useful until we support updates (then statements will
 * allow to implement transactions in the same way as JDBC Statements).
 * 
 * @author Mariano Rodriguez Muro <mariano.muro@gmail.com>
 * 
 */
public class QuestOWLStatement implements AutoCloseable {

	private final GeoDBStatement st;
	private final QuestOWLConnection conn;
	
	protected  QuestOWLStatement(GeoDBStatement st, QuestOWLConnection conn) {
		this.conn = conn;
		this.st = st;
	}

	public boolean isCanceled(){
		return st.isCanceled();
	}
	
	public void cancel() throws OWLException {
		try {
			st.cancel();
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}

	@Override
	public void close() throws OWLException {
		try {
			st.close();
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}

	public QuestOWLResultSet executeTuple(String query) throws OWLException {
		if (SPARQLQueryUtility.isSelectQuery(query) || SPARQLQueryUtility.isAskQuery(query)) {
		try {
			TupleResultSet executedQuery = (TupleResultSet) st.execute(query);
			QuestOWLResultSet questOWLResultSet = new QuestOWLResultSet(executedQuery, this);

	 		
			return questOWLResultSet;
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}} else {
			throw new RuntimeException("Query is not tuple query (SELECT / ASK).");
		}
	}

	public List<OWLAxiom> executeGraph(String query) throws OWLException {
		if (SPARQLQueryUtility.isConstructQuery(query) || SPARQLQueryUtility.isDescribeQuery(query)) {
		try {
			GraphResultSet resultSet = (GraphResultSet) st.execute(query);
			return createOWLIndividualAxioms(resultSet);
		} catch (Exception e) {
			throw new OWLOntologyCreationException(e);
		}} else {
			throw new RuntimeException("Query is not graph query (CONSTRUCT / DESCRIBE).");
		}
	}

	public int executeUpdate(String query) throws OWLException {
		try {
			return st.executeUpdate(query);
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}


	public QuestOWLConnection getConnection() throws OWLException {
		return conn;
	}

	public int getFetchSize() throws OWLException {
		try {
			return st.getFetchSize();
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}

	public int getMaxRows() throws OWLException {
		try {
			return st.getMaxRows();
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}

	public void getMoreResults() throws OWLException {
		try {
			st.getMoreResults();
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}

	public QuestOWLResultSet getResultSet() throws OWLException {
		try {
			return new QuestOWLResultSet(st.getResultSet(), this);
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}

	public int getQueryTimeout() throws OWLException {
		try {
			return st.getQueryTimeout();
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}

	public void setFetchSize(int rows) throws OWLException {
		try {
			st.setFetchSize(rows);
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}

	public void setMaxRows(int max) throws OWLException {
		try {
			st.setMaxRows(max);
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}

	public boolean isClosed() throws OWLException {
		try {
			return st.isClosed();
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}

	public void setQueryTimeout(int seconds) throws Exception {
		try {
			st.setQueryTimeout(seconds);
		} catch (OBDAException e) {
			throw new OntopOWLException(e);
		}
	}

	public long getTupleCount(String query) throws OWLException {
		try {
			return st.getTupleCount(query);
		} catch (Exception e) {
			throw new OntopOWLException(e);
		}
	}

	public String getRewriting(String query) throws OWLException {
		try {
			//Query jenaquery = QueryFactory.create(query);
			QueryParser qp = QueryParserUtil.createParser(QueryLanguage.SPARQL);
			ParsedQuery pq = qp.parseQuery(query, null); // base URI is null
			
			//SparqlAlgebraToDatalogTranslator tr = st.questInstance.getSparqlAlgebraToDatalogTranslator();	
			//List<String> signatureContainer = tr.getSignature(pq);
			
			return st.getRewriting(pq/*, signatureContainer*/);
		} 
		catch (Exception e) {
			throw new OntopOWLException(e);
		}
	}

	public String getUnfolding(String query) throws OWLException {
		try {
			return st.getUnfolding(query, true).getMainQuery();
		} catch (Exception e) {
			throw new OntopOWLException(e);
		}
	}

	private List<OWLAxiom> createOWLIndividualAxioms(GraphResultSet resultSet) throws Exception {
		
		OWLAPI3IndividualTranslator translator = new OWLAPI3IndividualTranslator();
		
		List<OWLAxiom> axiomList = new ArrayList<OWLAxiom>();
		if (resultSet != null) {
			while (resultSet.hasNext()) {
				for (Assertion assertion : resultSet.next()) {
					if (assertion instanceof ClassAssertion) {
						OWLAxiom classAxiom = translator.translate((ClassAssertion)assertion);
						axiomList.add(classAxiom);
					} 
					else if (assertion instanceof ObjectPropertyAssertion) {
						OWLAxiom objectPropertyAxiom = translator.translate((ObjectPropertyAssertion)assertion);
						axiomList.add(objectPropertyAxiom);
					}
					else if (assertion instanceof DataPropertyAssertion) {
						OWLAxiom objectPropertyAxiom = translator.translate((DataPropertyAssertion)assertion);
						axiomList.add(objectPropertyAxiom);							
					} 
				}
			}
		}
		return axiomList;
	}


	
	// Davide> Benchmarking
	public long getUnfoldingTime(){
		return st.getUnfoldingTime();
	}

	public long getRewritingTime(){
		return st.getRewritingTime();
	}
	
	public int getUCQSizeAfterUnfolding(){
		return st.getUCQSizeAfterUnfolding();
	}

}
