package com.nate.model;

public class Vector2f {

	private float u = 0.0f;
	private float v = 0.0f;
	
	public Vector2f( float u, float v ){
		this.u = u;
		this.v = v;
	}
	
	public float getU(){
		return u;
	}
	
	public void setU( float u ){
		this.u = u;
	}
	
	public float getV(){
		return v;
	}
	
	public void setV( float v ){
		this.v = v;
	}
}
