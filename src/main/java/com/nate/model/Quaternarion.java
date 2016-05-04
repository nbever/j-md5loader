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
	
	public Quaternarion copy(){
		return new Quaternarion( this.getX(), this.getY(), this.getZ(), this.getW() );
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
	
	public float getW() {
		return w;
	}
	
	public void setW( float w ) {
		this.w = w;
	}
	
	public void computeW(){
		 
		float t = 1.0f - ( getX() * getX() ) - ( getY() * getY() ) - ( getZ() * getZ() );

		  if (t < 0.0f)
		    this.w = 0.0f;
		  else
		    this.w = (float)(-1.0 * Math.sqrt( (double)t ) );
	}
	
	public static Vector3f rotatePoint( Quaternarion orientation, Vector3f position ){
		
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

		float w = ( qa.getW() * qb.getW() ) - ( qa.getX() * qb.getX() ) - ( qa.getY() * qb.getY() ) - ( qa.getZ() * qb.getZ() );
		float x = ( qa.getX() * qb.getW() ) + ( qa.getW() * qb.getX() ) + ( qa.getY() * qb.getZ() ) - ( qa.getZ() * qb.getY() );
		float y = ( qa.getY() * qb.getW() ) + ( qa.getW() * qb.getY() ) + ( qa.getZ() * qb.getX() ) - ( qa.getX() * qb.getZ() );
		float z = ( qa.getZ() * qb.getW() ) + ( qa.getW() * qb.getZ() ) + ( qa.getX() * qb.getY() ) - ( qa.getY() * qb.getX() );
		
		Quaternarion result = new Quaternarion( x, y, z, w );
		
		return result;
	}
	
	public static Quaternarion multiply( Quaternarion q, Vector3f v ){
		
		float w = -1.0f * ( q.getX() * v.getX() ) - ( q.getY() * v.getY() ) - ( q.getZ() * v.getZ() );
		float x = ( q.getW() * v.getX() ) + ( q.getY() * v.getZ() ) - ( q.getZ() * v.getY() );
		float y = ( q.getW() * v.getY() ) + ( q.getZ() * v.getX() ) - ( q.getX() * v.getZ() );
		float z = ( q.getW() * v.getZ() ) + ( q.getX() * v.getY() ) - ( q.getY() * v.getX() );
		
		Quaternarion result = new Quaternarion( x, y, z, w );
		
		return result;
	}
	
	public static float dotProduct( Quaternarion qa, Quaternarion qb ){
		
		float rz = ( ( qa.getX() * qb.getX() ) + 
				     ( qa.getY() * qb.getY() ) + 
				     ( qa.getZ() * qb.getZ() ) + 
				     ( qa.getW() * qb.getW() ) );
		
		return rz;
	}
	
	public static Quaternarion slerp( Quaternarion qa, Quaternarion qb, float t ){
		
		if ( t <= 0.0 ){
			return qa;
		}
		
		if ( t >= 1.0 ){
			return qb;
		}
		
		float cosOmega = Quaternarion.dotProduct( qa, qb );
		
		float bx = qb.getX();
		float by = qb.getY();
		float bz = qb.getZ();
		float bw = qb.getW();
		
		if ( cosOmega < 0.0f ){
			bx *= -1.0f;
			by *= -1.0f;
			bz *= -1.0f;
			bw *= -1.0f;
			cosOmega *= -1.0f;
		}
		
		// cosOmega needs to be less than 1.1f
		
		/* compute interpolation fraction - first check if they're basically the same **/
		float k0 = 0.0f;
		float k1 = 0.0f;
		
		if ( cosOmega > 0.9999f ){
			//very close, just use linear interpolation
			
			k0 = 1.0f - t;
			k1 = t;
		}
		else {
			float sinOmega = (float)Math.sqrt( 1.0 - ( (double)cosOmega * (double)cosOmega ) );
			float omega = (float)Math.atan2( sinOmega, cosOmega );
			
			float oneOverSinOmega = (float)(1.0 / (double)sinOmega);
			
			// compute interpolation parameters
			k0 = (float)Math.sin( (1.0 - (double)t) * (double)omega ) * oneOverSinOmega;
			k1 = (float)Math.sin( (double)t * (double)omega ) * oneOverSinOmega; 
		}
		
		// ineterpolate and return new quat
		float w = ( k0 * qa.getW() ) + ( k1 * bw );
		float x = ( k0 * qa.getX() ) + ( k1 * bx );
		float y = ( k0 * qa.getY() ) + ( k1 * by );
		float z = ( k0 * qa.getZ() ) + ( k1 * bz );
		
		Quaternarion rz = new Quaternarion( x, y, z, w );
		
		return rz;
	}
}
