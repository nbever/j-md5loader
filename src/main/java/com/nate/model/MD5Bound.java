package com.nate.model;

public class MD5Bound {

	private Vector3f minimum;
	private Vector3f maximum;
	
	public static MD5Bound parseLine( String line ) throws Exception{
		
		MD5Bound bound = new MD5Bound();
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length != 10 ){
			System.out.println( line );
			throw new Exception( "Invalid format for bounds.  There should be ten elements." );
		}
		
		Float mx = Float.parseFloat( tokens[1].trim() );
		Float my = Float.parseFloat( tokens[2].trim() );
		Float mz = Float.parseFloat( tokens[3].trim() );
		
		Float xx = Float.parseFloat( tokens[6].trim() );
		Float xy = Float.parseFloat( tokens[7].trim() );
		Float xz = Float.parseFloat( tokens[8].trim() );
		
		Vector3f mins = new Vector3f( mx, my, mz );
		Vector3f maxs = new Vector3f( xx, xy, xz );
		
		bound.setMaximum(  maxs );
		bound.setMinimum( mins );
		
		return bound;
	}
	
	public MD5Bound(){
		
	}

	public Vector3f getMinimum() {
		return minimum;
	}

	public void setMinimum(Vector3f minimum) {
		this.minimum = minimum;
	}

	public Vector3f getMaximum() {
		return maximum;
	}

	public void setMaximum(Vector3f maximum) {
		this.maximum = maximum;
	}
	
	
}
