package com.nate.model.parts;

import com.nate.model.types.Vector3d;

public class Bound<T extends Number> {

	private Vector3d<T> minimums;
	private Vector3d<T> maximums;
	
	public static Bound<Float> parseLine( String line ) throws Exception{
		
		Bound<Float> bound = new Bound<Float>();
		
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
		
		Vector3d<Float> mins = new Vector3d<Float>( mx, my, mz );
		Vector3d<Float> maxs = new Vector3d<Float>( xx, xy, xz );
		
		bound.setMaximums(  maxs );
		bound.setMinimums( mins );
		
		return bound;
	}
	
	public Vector3d<T> getMinimums() {
		return minimums;
	}
	
	public void setMinimums(Vector3d<T> minimums) {
		this.minimums = minimums;
	}
	
	public Vector3d<T> getMaximums() {
		return maximums;
	}
	
	public void setMaximums(Vector3d<T> maximums) {
		this.maximums = maximums;
	}
	
	
}
