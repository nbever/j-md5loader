package com.nate.model.types;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestVertMath {

	@Test
	public void testMultiply(){
		
		Vector3d<Float> one = new Vector3d<Float>( 3.0f, 2.0f, 1.0f );
		Vector3d<Float> two = new Vector3d<Float>( 1.0f, 5.0f, 1.0f );
		
		Vector3d<Float> product = one.multiplyf( two );
		
		assertTrue( product.getU().equals( 3.0f ) );
		assertTrue( product.getV().equals( 10.0f ) );
		assertTrue( product.getZ().equals( 1.0f ) );
	}
	
	@Test
	public void testAdd(){
		
		Vector3d<Float> one = new Vector3d<Float>( 3.0f, 2.0f, 1.0f );
		Vector3d<Float> two = new Vector3d<Float>( 1.0f, 5.0f, 1.0f );
		
		Vector3d<Float> product = one.addf( two );
		
		assertTrue( product.getU().equals( 4.0f ) );
		assertTrue( product.getV().equals( 7.0f ) );
		assertTrue( product.getZ().equals( 2.0f ) );
	}
	
	@Test
	public void testSubtract(){
		
		Vector3d<Float> one = new Vector3d<Float>( 3.0f, 2.0f, 1.0f );
		Vector3d<Float> two = new Vector3d<Float>( 1.0f, 5.0f, 1.0f );
		
		Vector3d<Float> product = one.subtractf( two );
		
		assertTrue( product.getU().equals( 2.0f ) );
		assertTrue( product.getV().equals( -3.0f ) );
		assertTrue( product.getZ().equals( 0.0f ) );
	}
	
	@Test
	public void testScalar(){
		
		Vector3d<Float> one = new Vector3d<Float>( 3.0f, 2.0f, 1.0f );
		
		Vector3d<Float> product = one.scalarf( 2.0f );
		
		assertTrue( product.getU().equals( 6.0f ) );
		assertTrue( product.getV().equals( 4.0f ) );
		assertTrue( product.getZ().equals( 2.0f ) );
	}
	
	@Test
	public void testCross(){
		
		Vector3d<Float> one = new Vector3d<Float>( 3.0f, -3.0f, 1.0f );
		Vector3d<Float> two = new Vector3d<Float>( 4.0f, 9.0f, 2.0f );
		
		Vector3d<Float> product = one.crossf( two );
		
		assertTrue( product.getU().equals( -15.0f ) );
		assertTrue( product.getV().equals( -2.0f ) );
		assertTrue( product.getZ().equals( 39.0f ) );
	}
}
