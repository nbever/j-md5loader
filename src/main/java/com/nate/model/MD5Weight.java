package com.nate.model;

public class MD5Weight {

	private int joint;
	private float bias = 0.0f;
	
	private Vector3f position;

	public static MD5Weight parseFloat( String line ) throws Exception{
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length < 9 ){
			throw new Exception( "Bad format for weight: " + line );
		}
		
		MD5Weight weight = new MD5Weight();
		
//		weight.setIndex( Integer.parseInt( tokens[1] ) );
		weight.setJoint( Integer.parseInt( tokens[2] ) );
		weight.setBias( Float.parseFloat( tokens[3] ) );
		weight.setPosition( new Vector3f( Float.parseFloat( tokens[5] ), 
										  Float.parseFloat( tokens[6] ),
										  Float.parseFloat( tokens[7] ) ) );
		
		return weight;
	}
	
	public int getJoint() {
		return joint;
	}

	public void setJoint( int joint ) {
		this.joint = joint;
	}

	public float getBias() {
		return bias;
	}

	public void setBias( float bias ) {
		this.bias = bias;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition( Vector3f position ) {
		this.position = position;
	}
	
	
}
