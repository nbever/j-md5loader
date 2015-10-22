package com.nate.model.parts;

import com.nate.model.types.Vector3d;

public class BaseFrameData<T extends Number> {

	private Vector3d<T> position;
	private Vector3d<T> orientation;
	
	public static BaseFrameData<Float> parseBaseFrameLine( String line ) throws Exception{
		
		BaseFrameData<Float> data = new BaseFrameData<Float>();
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length != 10 ){
			throw new Exception( "Invalid format for frame data.  Must be 10 elements." );
		}
		
		Float px = Float.parseFloat( tokens[1].trim() );
		Float py = Float.parseFloat( tokens[2].trim() );
		Float pz = Float.parseFloat( tokens[3].trim() );
		
		Float ox = Float.parseFloat( tokens[6].trim() );
		Float oy = Float.parseFloat( tokens[7].trim() );
		Float oz = Float.parseFloat( tokens[8].trim() );
		
		Vector3d<Float> pos = new Vector3d<Float>( px, py, pz );
		Vector3d<Float> ori = new Vector3d<Float>( ox, oy, oz );
		
		data.setPosition( pos );
		data.setOrientation( ori );
		
		return data;
	}
	
	public static BaseFrameData<Float> parseFrameLine( String line ) throws Exception {
		
		BaseFrameData<Float> data = new BaseFrameData<Float>();
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length != 6 ){
			throw new Exception( "Invalid format for frame data.  Must be 6 elements." );
		}
		
		Float px = Float.parseFloat( tokens[0].trim() );
		Float py = Float.parseFloat( tokens[1].trim() );
		Float pz = Float.parseFloat( tokens[2].trim() );
		
		Float ox = Float.parseFloat( tokens[3].trim() );
		Float oy = Float.parseFloat( tokens[4].trim() );
		Float oz = Float.parseFloat( tokens[5].trim() );
		
		Vector3d<Float> pos = new Vector3d<Float>( px, py, pz );
		Vector3d<Float> ori = new Vector3d<Float>( ox, oy, oz );
		
		data.setPosition( pos );
		data.setOrientation( ori );
		
		return data;
	}
	
	public Vector3d<T> getPosition(){
		return position;
	}
	
	public void setPosition( Vector3d<T> position ){
		this.position = position;
	}
	
	public Vector3d<T> getOrientation(){
		return orientation;
	}
	
	public void setOrientation( Vector3d<T> orientation ){
		this.orientation = orientation;
	}
}
