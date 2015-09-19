package com.nate.model.parts;

import com.nate.model.types.Vector3d;

public class Weight<T> {

	private int index;
	private int jointIndex;
	private float weightBias;
	
	private Vector3d<T> position;
	
	public static Weight<Float> parseFloat( String line ) throws Exception{
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length < 9 ){
			throw new Exception( "Bad format for weight: " + line );
		}
		
		Weight<Float> weight = new Weight<Float>();
		
		weight.setIndex( Integer.parseInt( tokens[1] ) );
		weight.setJointIndex( Integer.parseInt( tokens[2] ) );
		weight.setWeightBias( Float.parseFloat( tokens[3] ) );
		weight.setPosition( new Vector3d<Float>( Float.parseFloat( tokens[5] ), 
												 Float.parseFloat( tokens[6] ),
												 Float.parseFloat( tokens[7] ) ) );
		
		return weight;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getJointIndex() {
		return jointIndex;
	}

	public void setJointIndex(int jointIndex) {
		this.jointIndex = jointIndex;
	}

	public float getWeightBias() {
		return weightBias;
	}

	public void setWeightBias(float weightBias) {
		this.weightBias = weightBias;
	}

	public Vector3d<T> getPosition() {
		return position;
	}

	public void setPosition(Vector3d<T> position) {
		this.position = position;
	}
	
	
}
