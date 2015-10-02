package com.nate.model.types;

public class Vector4d<T extends Number> extends Vector3d<T>{
	
	private T w;
	
	public Vector4d( T x, T y, T z, T w ){
		super( x, y, z );
		this.w = w;
	}
	
	public T getW(){
		return w;
	}
}
