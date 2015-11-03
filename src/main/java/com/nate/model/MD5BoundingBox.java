package com.nate.model;

public class MD5BoundingBox {

	private Vector3f min;
	private Vector3f max;
	
	public static MD5BoundingBox parseLine( String line ) throws Exception{
		
		MD5BoundingBox bound = new MD5BoundingBox();
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length != 10 ){
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
		
		bound.setMax(  maxs );
		bound.setMin( mins );
		
		return bound;
	}
	
	public Vector3f getMin(){
		return min;
	}
	
	public void setMin( Vector3f min ){
		this.min = min;
	}
	
	public Vector3f getMax(){
		return max;
	}
	
	public void setMax( Vector3f max ){
		this.max = max;
	}
}
