package com.nate.model;

public class MD5BoundingBox {

	private Vector3f min;
	private Vector3f max;
	
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
