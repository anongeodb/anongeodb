package it.unibz.krdb.obda.reformulation.tests;

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

import it.unibz.krdb.obda.ontology.*;
import it.unibz.krdb.obda.owlapi3.OWLAPI3TranslatorUtility;
import junit.framework.TestCase;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.util.Collection;
import java.util.Iterator;

public class TranslatorTest extends TestCase {

	public void test_1() throws Exception{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory(); 
		
		OWLClass class1 = factory.getOWLClass(IRI.create("http://example/A"));
		OWLObjectProperty prop =  factory.getOWLObjectProperty(IRI.create("http://example/prop1"));
		
		OWLObjectPropertyRangeAxiom ax = factory.getOWLObjectPropertyRangeAxiom(prop, class1);
		
		OWLOntology onto = manager.createOntology(IRI.create("http://example/testonto"));
		manager.addAxiom(onto, factory.getOWLDeclarationAxiom(class1));
		manager.addAxiom(onto, factory.getOWLDeclarationAxiom(prop));
		manager.addAxiom(onto, ax);
		
		Ontology dlliteonto = OWLAPI3TranslatorUtility.translate(onto);
		
		Collection<BinaryAxiom<ClassExpression>> ass = dlliteonto.getSubClassAxioms();
		Iterator<BinaryAxiom<ClassExpression>> assit = ass.iterator();
		assertEquals(1, ass.size());
		
		BinaryAxiom<ClassExpression> a = assit.next();
		ObjectSomeValuesFrom ex = (ObjectSomeValuesFrom) a.getSub();
		assertEquals(true, ex.getProperty().isInverse());
	}
	
	public void test_2() throws Exception{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory(); 
		
		OWLClass class1 = factory.getOWLClass(IRI.create("http://example/A"));
		OWLObjectProperty prop =  factory.getOWLObjectProperty(IRI.create("http://example/prop1"));
		
		OWLObjectPropertyDomainAxiom ax = factory.getOWLObjectPropertyDomainAxiom(prop, class1);
		
		OWLOntology onto = manager.createOntology(IRI.create("http://example/testonto"));
		manager.addAxiom(onto, factory.getOWLDeclarationAxiom(class1));
		
		manager.addAxiom(onto, ax);
		
		Ontology dlliteonto = OWLAPI3TranslatorUtility.translate(onto);
		
		Collection<BinaryAxiom<ClassExpression>> ass = dlliteonto.getSubClassAxioms();
		Iterator<BinaryAxiom<ClassExpression>> assit = ass.iterator();
		assertEquals(1, ass.size());
		
		BinaryAxiom<ClassExpression> a = assit.next();
		ObjectSomeValuesFrom ex = (ObjectSomeValuesFrom) a.getSub();
		assertEquals(false, ex.getProperty().isInverse());
	}
	
	public void test_3() throws Exception{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory(); 
		
		OWLObjectProperty prop =  factory.getOWLObjectProperty(IRI.create("http://example/R"));
		OWLObjectProperty invofprop =  factory.getOWLObjectProperty(IRI.create("http://example/S"));
		
		OWLInverseObjectPropertiesAxiom ax = factory.getOWLInverseObjectPropertiesAxiom(prop, invofprop);
		
		OWLOntology onto = manager.createOntology(IRI.create("http://example/testonto"));
		manager.addAxiom(onto, ax);
		
		Ontology dlliteonto = OWLAPI3TranslatorUtility.translate(onto);
		
		Collection<BinaryAxiom<ObjectPropertyExpression>> ass = dlliteonto.getSubObjectPropertyAxioms();
		Iterator<BinaryAxiom<ObjectPropertyExpression>> assit = ass.iterator();
		assertEquals(2, ass.size());
		
		BinaryAxiom<ObjectPropertyExpression> a = assit.next();
		BinaryAxiom<ObjectPropertyExpression> b = assit.next();
		ObjectPropertyExpression included =a.getSub();
		assertEquals(false, included.isInverse());
		assertEquals("http://example/R", included.getName());
		
		ObjectPropertyExpression indlucing = a.getSuper();
		assertEquals(true, indlucing.isInverse());
		assertEquals("http://example/S", indlucing.getName());
		
		included = b.getSub();
		assertEquals(false, included.isInverse());
		assertEquals("http://example/S", included.getName());
		
		indlucing = b.getSuper();
		assertEquals(true, indlucing.isInverse());
		assertEquals("http://example/R", indlucing.getName());
	}
	
	public void test_4() throws Exception{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory(); 
		
		OWLClass clsA = factory.getOWLClass(IRI.create("http://example/A"));
		OWLClass clsB = factory.getOWLClass(IRI.create("http://example/B"));
		
		OWLEquivalentClassesAxiom ax = factory.getOWLEquivalentClassesAxiom(clsA, clsB);
				
		OWLOntology onto = manager.createOntology(IRI.create("http://example/testonto"));
		manager.addAxiom(onto, ax);
		
		Ontology dlliteonto = OWLAPI3TranslatorUtility.translate(onto);
		
		Collection<BinaryAxiom<ClassExpression>> ass = dlliteonto.getSubClassAxioms();
		Iterator<BinaryAxiom<ClassExpression>> assit = ass.iterator();
		assertEquals(2, ass.size());
		
		BinaryAxiom<ClassExpression> c1 = assit.next();
		BinaryAxiom<ClassExpression> c2 = assit.next();
		OClass included = (OClass) c1.getSub();
		assertEquals("http://example/A", included.getName());
		
		OClass indlucing = (OClass) c1.getSuper();
		assertEquals("http://example/B", indlucing.getName());
		
		included = (OClass) c2.getSub();
		assertEquals("http://example/B", included.getName());
		
		indlucing = (OClass) c2.getSuper();
		assertEquals("http://example/A", indlucing.getName());
	}
}
