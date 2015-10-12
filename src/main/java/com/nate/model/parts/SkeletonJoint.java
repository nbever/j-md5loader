package com.nate.model.parts;

import com.nate.model.types.Vector3d;
import com.nate.model.types.Vector4d;

public class SkeletonJoint<T extends Number> {

	private Integer parent = -1;
	private Vector3d<T> position;
	private Vector4d<T> orientation;
//	private T w;
	
	public Integer getParent() {
		return parent;
	}
	
	public void setParent( Integer parent ){
		this.parent = parent;
	}
	
	public Vector3d<T> getPosition(){
		return position;
	}
	
	public void setPosition( Vector3d<T> position ){
		this.position = position;
	}
	
//	public T getW(){
//		return w;
//	}
//	
//	public void setW( T w ){
//		this.w = w;
//	}
	
	public Vector4d<T> getOrientation(){
		return orientation;
	}
	
	public void setOrientation( Vector4d<T> orientation ){
		this.orientation = orientation;
	}
}
