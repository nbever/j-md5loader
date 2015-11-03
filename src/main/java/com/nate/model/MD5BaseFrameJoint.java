package com.nate.model;

public class MD5BaseFrameJoint {

	private Vector3f position;
	private Quaternarion orientation;
	
	public static MD5BaseFrameJoint parseBaseFrameLine( String line ) throws Exception{
		
		MD5BaseFrameJoint data = new MD5BaseFrameJoint();
		
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
		
		Vector3f pos = new Vector3f( px, py, pz );
		Quaternarion ori = new Quaternarion( ox, oy, oz, 0.0f );
		
		data.setPosition( pos );
		data.setOrientation( ori );
		
		return data;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition( Vector3f position ) {
		this.position = position;
	}
	
	public Quaternarion getOrientation() {
		return orientation;
	}
	
	public void setOrientation( Quaternarion orientation ) {
		this.orientation = orientation;
	}
	
	
}
