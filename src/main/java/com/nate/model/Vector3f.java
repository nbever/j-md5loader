package com.nate.model;

public class Vector3f {

	private float x = 0.0f;
	private float y = 0.0f;
	private float z = 0.0f;
	
	public Vector3f( float x, float y, float z ){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX( float x ) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY( float y ) {
		this.y = y;
	}
	
	public float getZ() {
		return z;
	}
	
	public void setZ( float z ) {
		this.z = z;
	}
}
