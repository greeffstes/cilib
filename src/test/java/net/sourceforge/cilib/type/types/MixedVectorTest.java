/*
 * MixedVectorTest.java
 * 
 * Created on Jul 19, 2005
 *
 * Copyright (C) 2003 - 2006 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.sourceforge.cilib.type.types;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static java.lang.Math.sqrt;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Set;
import net.sourceforge.cilib.type.types.Vector;


/**
 * @author Gary Pampara
 */
public class MixedVectorTest {
	private static Logger log = Logger.getLogger(MixedVectorTest.class);
	private static Vector vector;
	private static Vector tmpVector;
	
	@BeforeClass
	public static void setUp() {
		vector = new MixedVector();
		
		vector.append(new Real(1));
		vector.append(new Real(2));
		vector.append(new Real(3));
		vector.append(new Real(4));
	}
	
	@AfterClass
	public static void tearDown() {
		vector = null;
		tmpVector = null;
	}
	
	
	private void recreateTmpVector() {
		tmpVector = new MixedVector();
		tmpVector.append(new Real(1.0));
		tmpVector.append(new Real(2.0));
		tmpVector.append(new Real(3.0));
	}
	
	
	@Test
	public void testClone() {
		
		Vector v = (Vector) vector.clone();
		
		assertEquals(v.size(), vector.size());
				
		for (int i = 0; i < vector.getDimension(); i++) {
			assertEquals(vector.getReal(i), v.getReal(i));
			assertNotSame(vector.get(i), v.get(i));
		}
	}
	
	
	@Test
	public void testSet() {
		vector.setReal(0, 3.0);
		assertEquals(3.0, vector.getReal(0));
		vector.setReal(0, 1.0);
		assertEquals(1.0, vector.getReal(0));
	}
	
	
	@Test
	public void testNumericGet() {
		recreateTmpVector();
		
		assertEquals(1.0, tmpVector.getReal(0));
		assertEquals(2.0, tmpVector.getReal(1));
		assertEquals(3.0, tmpVector.getReal(2));
		
		Real t = (Real) tmpVector.get(0);
		
		assertEquals(1.0, t.getReal());
	}
	
	
	@Test
	public void testNonNumericGet() {
		MixedVector m = new MixedVector();
		Set<Real> realSet = new Set<Real>();
		
		m.add(realSet);
		
		assertFalse(m.get(0) instanceof Numeric);
		assertSame(realSet, m.get(0));
		
		assertNotSame(m.get(0), vector.get(0));
	}
	
	
	@Test
	public void testNumericSet() {
		assertEquals(1.0, vector.getReal(0));
		vector.setReal(0, 99.9);
		assertEquals(99.9, vector.getReal(0));
	
		vector.setInt(0, 2);
		assertEquals((int) 2, vector.getInt(0));
		
		vector.setReal(0, 1.0);
		assertEquals(1.0, vector.getReal(0));
	}
	
	
	@Test
	public void testNonNumericSet() {
		MixedVector m = new MixedVector();
		Set<Object> s = new Set<Object>();
		MixedVector v = new MixedVector();
		
		m.add(s);
		m.add(v);
		
		assertSame(s, m.get(0));
		assertEquals(s, m.get(0));
		assertEquals(v, m.get(1));
	}
	
	
	@Test
	public void testDimension() {
		assertFalse(vector.getDimension() == 3);
		assertTrue(vector.getDimension() == 4);
		assertFalse(vector.getDimension() == 5);
	}
	
	
	@Test
	public void testInsert() {
		MixedVector m = new MixedVector();
		double [] targetResults = { 0.0, 1.0, 2.0, 3.0, 4.0 };
		
		m.add(new Real(1.0));
		m.add(new Real(3.0));
		assertEquals(2, m.getDimension());
		
		m.insert(1, new Real(2.0));
		assertEquals(3, m.getDimension());
		
		m.insert(0, new Real(0.0));
		assertEquals(4, m.getDimension());
		
		for (int i = 0; i < 4; i++) {
			assertEquals(targetResults[i], m.getReal(i));
		}
		
		// Test the invalid indexes
		try {
			m.insert(6, new Real(1.0));
			fail("Insert worked on an invalid index???");
		}
		catch (Exception e) {}
		
		try {
			m.insert(-1, new Real(1.0));
			fail("Insert worked on an invalid index???");
		}
		catch (Exception e) {}
	}
	
	
	@Test
	public void testRemove() {
		MixedVector m = new MixedVector();
		
		m.add(new Real(1.0));
		m.add(new Real(2.2));
		
		assertEquals(2, m.getDimension());
		
		m.remove(1);
		assertEquals(1.0, m.getReal(0));
		assertEquals(1, m.getDimension());
		
		// Invalid indexes
		try {
			m.remove(-1);
			fail("Remove accepted and invalid range!");
		}
		catch (Exception e) {}
		
		try {
			m.remove(10);
			fail("Remove accepted and invalid range!");
		}
		catch (Exception e) {}
	}

	
	@Test
	public void testAdd() {
		MixedVector m = new MixedVector();
		assertEquals(0, m.getDimension());
		
		m.add(new Real(1.0));
		assertEquals(1, m.getDimension());
	}
	
	
	@Test
	public void testGetReal() {
		Object tmp = vector.getReal(0);
		assertTrue(tmp instanceof Double);
	}
	
	
	@Test
	public void testSetReal() {
		MixedVector m = new MixedVector();
		m.add(new Real(-10.0, 10.0));
		m.setReal(0, 10.0);
		
		assertEquals(10.0, m.getReal(0));
	}
	
	
	@Test
	public void testGetInt() {
		Object tmp = vector.getInt(0);
		assertTrue(tmp instanceof Integer);
	}
	
	
	@Test
	public void testSetInt() {
		MixedVector m = new MixedVector();
		m.add(new Int(2));
		assertEquals(2, m.getInt(0));
		m.setInt(0, 5);
		assertEquals(5, m.getInt(0));
		
		m.add(new Real(-99.99));
		m.setInt(1, 1);
		assertEquals(1, m.getInt(1));
		
		m.add(new Bit());
		m.setBit(2, true);
		assertTrue(m.getBit(2));
	}
	
	
	@Test
	public void testGetBit() {
		Object tmp = vector.getBit(0);
		assertTrue(tmp instanceof Boolean);
	}
	
	
	@Test
	public void testSetBit() {
		MixedVector m = new MixedVector();
		m.add(new Bit());
		m.setBit(0, false);
		
		assertFalse(m.getBit(0));		
	}
	
	
	@Test
	public void testRandomize() {
		MixedVector m = new MixedVector();
		m.add(new Real(1.0));
		m.add(new Real(2.0));
		m.add(new Real(3.0));
		m.randomise();
		
		assertFalse(m.getReal(0) == 1.0);
		assertFalse(m.getReal(1) == 2.0);
		assertFalse(m.getReal(2) == 3.0);
	}
	
	
	@Test
	public void testVectorNorm() {
		MixedVector m = new MixedVector();
		
		m.add(new Real(1.0));
		m.add(new Real(1.0));
		m.add(new Real(1.0));
		m.add(new Real(1.0));
		m.add(new Real(1.0));
		assertEquals(sqrt(5.0), m.norm());
		
		m.clear();
		
		m.add(new Real(2.0));
		m.add(new Real(-2.0));
		m.add(new Real(2.0));
		m.add(new Real(-2.0));
		m.add(new Real(2.0));
		m.add(new Real(-2.0));
		assertEquals(sqrt(24.0), m.norm());
	}
	
	
	@Test
	public void testVectorDotProduct() {
		Vector v1 = new MixedVector();
		Vector v2 = new MixedVector();
		
		v1.add(new Real(1.0));
		v1.add(new Real(2.0));
		v1.add(new Real(3.0));
		
		v2.add(new Real(3.0));
		v2.add(new Real(2.0));
		v2.add(new Real(1.0));
		
		assertEquals(10.0, v1.dot(v2));
		
		v2.setReal(0, -3.0);
		assertEquals(4.0, v1.dot(v2));
	}
	
	
	@Test
	public void testSerialisation() {
		MixedVector m = new MixedVector();
		m.add(new Real(55.0));
		m.add(new Int(-12));
		m.add(new Bit(false));
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			
			m.serialise(oos);
			oos.close();
			
			byte [] data = bos.toByteArray();
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			
			MixedVector result = new MixedVector();
			result.add(new Real());
			result.add(new Int());
			result.add(new Bit());
			
			result.deserialise(ois);
			
			assertEquals(m.getReal(0), result.getReal(0));
			assertEquals(m.getInt(1), result.getInt(1));
			assertEquals(m.getBit(2), result.getBit(2));
		}
		catch (IOException e) {
			fail("Serialisation fails for Types.MixedVector!");
		}
		catch (ClassNotFoundException c) {
			fail("Class Not Found: " + c.getMessage());
		}
	}

	/**
	 * @author Theuns Cloete
	 */
	@Test
	public void testPlus() {
		Vector a = new MixedVector();
		Vector b = new MixedVector();

		for(int i = 0; i < 10; i++)
			a.append(new Real(i));
		for(int i = 0; i < 9; i++)
			b.prepend(new Real(i));

		Vector sum = null;
		try {
			sum = a.plus(b);
		}
		catch(UnsupportedOperationException u) {
			log.info("Catching UnsupportedOperationException as expected: " + u.getMessage());
		}

		b.prepend(new Real(9));
		sum = a.plus(b);

		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(sum);
		assertNotSame(a, b);
		assertNotSame(sum, a);
		assertNotSame(sum, b);

		for(int i = 0; i < 10; i++) {
			assertNotNull(a.getType(i));
			assertNotNull(b.getType(i));
			assertNotNull(sum.getType(i));
			assertNotSame(a.getType(i), b.getType(i));
			assertNotSame(sum.getType(i), a.getType(i));
			assertNotSame(sum.getType(i), b.getType(i));

			assertEquals(a.getReal(i), (double)i);
			assertEquals(b.getReal(i), (double)(9.0 - i));
			assertEquals(sum.getReal(i), 9.0);
		}
	}

	/**
	 * @author Theuns Cloete
	 */
	@Test
	public void testSubtract() {
		Vector a = new MixedVector();
		Vector b = new MixedVector();

		for(int i = 0; i < 10; i++)
			a.append(new Real(i));
		for(int i = 0; i < 9; i++)
			b.prepend(new Real(i));

		Vector difference = null;
		try {
			difference = a.subtract(b);
		}
		catch(UnsupportedOperationException u) {
			log.info("Catching UnsupportedOperationException as expected: " + u.getMessage());
		}

		b.prepend(new Real(9));
		difference = a.subtract(b);

		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(difference);
		assertNotSame(a, b);
		assertNotSame(difference, a);
		assertNotSame(difference, b);

		for(int i = 0; i < 10; i++) {
			assertNotNull(a.getType(i));
			assertNotNull(b.getType(i));
			assertNotNull(difference.getType(i));
			assertNotSame(a.getType(i), b.getType(i));
			assertNotSame(difference.getType(i), a.getType(i));
			assertNotSame(difference.getType(i), b.getType(i));

			assertEquals(a.getReal(i), (double)i);
			assertEquals(b.getReal(i), (double)(9.0 - i));
			assertEquals(difference.getReal(i), (double)(i - (9.0 - i)));
		}
	}

	/**
	 * @author Theuns Cloete
	 */
	@Test
	public void testVectorDivision() {
		Vector a = new MixedVector();
		Vector b = new MixedVector();

		for(int i = 1; i < 11; i++)
			a.append(new Real(i));
		for(int i = 1; i < 10; i++)
			b.prepend(new Real(i));

		Vector divided = null;
		try {
			divided = a.divide(b);
		}
		catch(UnsupportedOperationException u) {
			log.info("Catching UnsupportedOperationException as expected: " + u.getMessage());
		}

		b.prepend(new Real(0));
		try {
			divided = a.divide(b);
		}
		catch(ArithmeticException e) {
			log.info("Catching ArithmeticException as expected: " + e.getMessage());
		}

		((Numeric)b.getType(0)).setReal(10);
		divided = a.divide(b);

		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(divided);
		assertNotSame(a, b);
		assertNotSame(divided, a);
		assertNotSame(divided, b);

		for(int i = 0; i < 10; i++) {
			assertNotNull(a.getType(i));
			assertNotNull(b.getType(i));
			assertNotNull(divided.getType(i));
			assertNotSame(a.getType(i), b.getType(i));
			assertNotSame(divided.getType(i), a.getType(i));
			assertNotSame(divided.getType(i), b.getType(i));

			assertEquals(a.getReal(i), (double)(i + 1));
			assertEquals(b.getReal(i), (double)(10.0 - i));
			assertEquals(divided.getReal(i), (double)((i + 1) / (10.0 - i)));
		}
	}

	/**
	 * @author Theuns Cloete
	 */
	@Test
	public void testScalarDivision() {
		Vector a = new MixedVector();

		for(int i = 0; i < 10; i++)
			a.append(new Real(i));

		Vector divided = null;
		try {
			divided = a.divide(0);
		}
		catch(ArithmeticException e) {
			log.info("Catching ArithmeticException as expected: " + e.getMessage());
		}

		divided = a.divide(3.0);

		assertNotNull(a);
		assertNotNull(divided);
		assertNotSame(divided, a);

		for(int i = 0; i < 10; i++) {
			assertNotNull(a.getType(i));
			assertNotNull(divided.getType(i));
			assertNotSame(divided.getType(i), a.getType(i));

			assertEquals(a.getReal(i), (double)i);
			assertEquals(divided.getReal(i), (double)(i / 3.0));
		}
	}

	/**
	 * @author Theuns Cloete
	 */
	@Test
	public void testVectorMultiplication() {
		Vector a = new MixedVector();
		Vector b = new MixedVector();

		for(int i = 0; i < 10; i++)
			a.append(new Real(i));
		for(int i = 0; i < 9; i++)
			b.prepend(new Real(i));

		Vector product = null;
		try {
			product = a.multiply(b);
		}
		catch(UnsupportedOperationException u) {
			log.info("Catching UnsupportedOperationException as expected: " + u.getMessage());
		}

		b.prepend(new Real(9));
		product = a.multiply(b);

		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(product);
		assertNotSame(a, b);
		assertNotSame(product, a);
		assertNotSame(product, b);

		for(int i = 0; i < 10; i++) {
			assertNotNull(a.getType(i));
			assertNotNull(b.getType(i));
			assertNotNull(product.getType(i));
			assertNotSame(a.getType(i), b.getType(i));
			assertNotSame(product.getType(i), a.getType(i));
			assertNotSame(product.getType(i), b.getType(i));

			assertEquals(a.getReal(i), (double)i);
			assertEquals(b.getReal(i), (double)(9.0 - i));
			assertEquals(product.getReal(i), (double)(i * (9.0 - i)));
		}
	}

	/**
	 * @author Theuns Cloete
	 */
	@Test
	public void testScalarMultiplication() {
		Vector a = new MixedVector();

		for(int i = 0; i < 10; i++)
			a.append(new Real(i));

		Vector product = a.multiply(3.0);

		assertNotNull(a);
		assertNotNull(product);
		assertNotSame(product, a);

		for(int i = 0; i < 10; i++) {
			assertNotNull(a.getType(i));
			assertNotNull(product.getType(i));
			assertNotSame(product.getType(i), a.getType(i));

			assertEquals(a.getReal(i), (double)i);
			assertEquals(product.getReal(i), (double)(i * 3.0));
		}
	}
}