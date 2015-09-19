package com.nate.model.parts;

import com.nate.model.types.Vector3d;

public class Joint {
	
	private String name;
	private int parentIndex;
	private Vector3d<Float> position;
	private Vector3d<Float> orientation;

	public static Joint parse( String line ) throws Exception{
		
		Joint joint = new Joint();
		
		String[] tokens = line.split( "[ ,\t]" );

		if ( tokens.length < 12 ){
			throw new Exception( "Bad format for joint: " + line );
		}
		
		joint.setName( tokens[0] );
		joint.setParentIndex( Integer.parseInt( tokens[1] ) );
		Vector3d<Float> position = new Vector3d<Float>( Float.parseFloat( tokens[3] ), 
														Float.parseFloat( tokens[4] ), 
														Float.parseFloat( tokens[5] ) );
		
		Vector3d<Float> orientation = new Vector3d<Float>( Float.parseFloat( tokens[8] ), 
														   Float.parseFloat( tokens[9] ), 
														   Float.parseFloat( tokens[10] ) );

		joint.setPosition( position );
		joint.setOrientation( orientation );
		
		return joint;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName( String aName ){
		name = aName;
	}
	
	public int getParentIndex(){
		return parentIndex;
	}
	
	public void setParentIndex( int aParentIndex ){
		parentIndex = aParentIndex;
	}
	
	public Vector3d<Float> getPosition(){
		return position;
	}
	
	public void setPosition( Vector3d<Float> aPosition ){
		position = aPosition;
	}
	
	public Vector3d<Float> getOrientation(){
		return orientation;
	}
	
	public void setOrientation( Vector3d<Float> anOrientation ){
		orientation = anOrientation;
	}
}
