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
	
	@Override
	public String toString() {
	
		return getX() + ", " + getY() + ", " + getZ();
	}
	
	public static Vector3f multiply( Vector3f a, Vector3f b ){
		float x = a.getY()*b.getZ() - a.getZ()*b.getY();
		float y = a.getZ()*b.getX() - a.getX()*b.getZ();
		float z = a.getX()*b.getY() - a.getY()*b.getX();
		
		return new Vector3f( x, y, z );
	}
	
	public static float dotProduct( Vector3f a, Vector3f b ){
		float rez = a.getX()*b.getX() + a.getY()*b.getY() + a.getZ()*b.getZ();
		
		return rez;
	}
	
	public static Vector3f normalize( Vector3f v ){
		
		float magnitude = (float)Math.sqrt( ((double)v.getX()*(double)v.getX()) + 
									 ((double)v.getY()*(double)v.getY()) + 
									 ((double)v.getZ()*(double)v.getZ()) );
		
		Vector3f ret = v;
		
		if ( magnitude > 0.0f ){
			float oneOver = 1.0f / magnitude;
			
			float x = v.getX()*oneOver;
			float y = v.getY()*oneOver;
			float z = v.getZ()*oneOver;
			
			ret = new Vector3f( x, y, z);
		}
		
		return ret;
	}
	
	public static Vector3f add( Vector3f a, Vector3f b ){
		return new Vector3f( a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ() );
	}
	
	public static Vector3f subtract( Vector3f a, Vector3f b ){
		return new Vector3f( a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ() );
	}
}
