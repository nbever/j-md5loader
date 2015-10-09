package com.nate.model.types;

public class Vector2d<T extends Number> {

	private T u;
	private T v;
	
	public Vector2d( T u, T v ){
		this.u = u;
		this.v = v;
	}
	
	public T getU(){
		return u;
	}
	
	public T getV(){
		return v;
	}
	
	public void setU( T u ){
		this.u = u;
	}
	
	public void setV( T v ){
		this.v = v;
	}
}
