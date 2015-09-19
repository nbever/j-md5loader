package com.nate.model.types;

public class Vector3d<T> {

	private T x;
	private T y;
	private T z;
	
	public Vector3d( T x, T y, T z ){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public T getX(){
		return x;
	}
	
	public T getY(){
		return y;
	}
	
	public T getZ(){
		return z;
	}
}
