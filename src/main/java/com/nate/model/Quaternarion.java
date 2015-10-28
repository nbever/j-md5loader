package com.nate.model;

public class Quaternarion {

	private float x = 0.0f;
	private float y = 0.0f;
	private float z = 0.0f;
	private float w = 0.0f;
	
	public Quaternarion( float x, float y, float z, float w ){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternarion(){}
	
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
	
	public float getW() {
		return w;
	}
	
	public void setW( float w ) {
		this.w = w;
	}
	
	public void computeW(){
		 
		float t = 1.0f - ( getX() * getX() ) - ( getY() * getY() ) - ( getZ() * getZ() );

		  if (t < 0.0f)
		    w = 0.0f;
		  else
		    w = (float)(-1.0 * Math.sqrt( (double)t ) );
	}
	
	public static Vector3f rotatePoint( Quaternarion orientation, Vector3f position ){
//		  quat4_t tmp, inv, final;
//
//		  inv[X] = -q[X]; inv[Y] = -q[Y];
//		  inv[Z] = -q[Z]; inv[W] =  q[W];
//
//		  Quat_normalize (inv);
//
//		  Quat_multVec (q, in, tmp);
//		  Quat_multQuat (tmp, inv, final);
//
//		  out[X] = final[X];
//		  out[Y] = final[Y];
//		  out[Z] = final[Z];
		
		Quaternarion inverse = new Quaternarion();
		inverse.setX( -1.0f * orientation.getX() );
		inverse.setY( -1.0f * orientation.getY() );
		inverse.setZ( -1.0f * orientation.getZ() );
		inverse.setW( 1.0f * orientation.getW() );
		
		inverse = Quaternarion.normalize( inverse );
		
		Quaternarion rz = Quaternarion.multiply( orientation, position );
		rz = Quaternarion.multiply( rz, inverse );
		
		Vector3f finalPoint = new Vector3f( rz.getX(), rz.getY(), rz.getZ() );
		
		return finalPoint;
	}
	
	public static Quaternarion normalize ( Quaternarion quat ){
	  
		float magnitude = (float)Math.sqrt( ( (double)quat.getX() * (double)quat.getX() ) + 
											( (double)quat.getY() * (double)quat.getY() ) + 
											( (double)quat.getZ() * (double)quat.getZ() ) + 
											( (double)quat.getW() * (double)quat.getW() ) );
		
		if ( magnitude > 0.0f ){
			/* normalize it */
			float oneOverMag = 1.0f / magnitude;
			
			quat.setX( quat.getX() * oneOverMag );
			quat.setY( quat.getY() * oneOverMag );
			quat.setZ( quat.getZ() * oneOverMag );
			quat.setW( quat.getW() * oneOverMag );
		}
		
		return quat;
	}

	public static Quaternarion multiply( Quaternarion qa, Quaternarion qb ){
//	void
//	Quat_multQuat (const quat4_t qa, const quat4_t qb, quat4_t out)
//	{
//	  out[W] = (qa[W] * qb[W]) - (qa[X] * qb[X]) - (qa[Y] * qb[Y]) - (qa[Z] * qb[Z]);
//	  out[X] = (qa[X] * qb[W]) + (qa[W] * qb[X]) + (qa[Y] * qb[Z]) - (qa[Z] * qb[Y]);
//	  out[Y] = (qa[Y] * qb[W]) + (qa[W] * qb[Y]) + (qa[Z] * qb[X]) - (qa[X] * qb[Z]);
//	  out[Z] = (qa[Z] * qb[W]) + (qa[W] * qb[Z]) + (qa[X] * qb[Y]) - (qa[Y] * qb[X]);
//	}
		float w = ( qa.getW() * qb.getW() ) - ( qa.getX() * qb.getX() ) - ( qa.getY() * qb.getY() ) - ( qa.getZ() * qb.getZ() );
		float x = ( qa.getX() * qb.getW() ) + ( qa.getW() * qb.getX() ) + ( qa.getY() * qb.getZ() ) - ( qa.getZ() * qb.getY() );
		float y = ( qa.getY() * qb.getW() ) + ( qa.getW() * qb.getY() ) + ( qa.getZ() * qb.getX() ) - ( qa.getX() * qb.getZ() );
		float z = ( qa.getZ() * qb.getW() ) + ( qa.getW() * qb.getZ() ) + ( qa.getX() * qb.getY() ) - ( qa.getY() * qb.getX() );
		
		Quaternarion result = new Quaternarion( x, y, z, w );
		
		return result;
	}
	
	public static Quaternarion multiply( Quaternarion q, Vector3f v ){
//
//	void
//	Quat_multVec (const quat4_t q, const vec3_t v, quat4_t out)
//	{
//	  out[W] = - (q[X] * v[X]) - (q[Y] * v[Y]) - (q[Z] * v[Z]);
//	  out[X] =   (q[W] * v[X]) + (q[Y] * v[Z]) - (q[Z] * v[Y]);
//	  out[Y] =   (q[W] * v[Y]) + (q[Z] * v[X]) - (q[X] * v[Z]);
//	  out[Z] =   (q[W] * v[Z]) + (q[X] * v[Y]) - (q[Y] * v[X]);
//	}
		
		float w = -1.0f * ( q.getX() * v.getX() ) - ( q.getY() * v.getY() ) - ( q.getZ() * v.getZ() );
		float x = ( q.getW() * v.getX() ) + ( q.getY() * v.getZ() ) - ( q.getZ() * v.getY() );
		float y = ( q.getW() * v.getY() ) + ( q.getZ() * v.getX() ) - ( q.getX() * v.getZ() );
		float z = ( q.getW() * v.getZ() ) + ( q.getX() * v.getY() ) - ( q.getY() * v.getX() );
		
		Quaternarion result = new Quaternarion( x, y, z, w );
		
		return result;
	}
}
