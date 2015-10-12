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
	
	public void setW( T w ){
		this.w = w;
	}
	
	private Vector4d<Float> makeFloat( Vector4d<T> vec ){
		
		Vector4d<Float> floatV = new Vector4d<Float>( vec.getU().floatValue(), vec.getV().floatValue(), vec.getZ().floatValue(), vec.getW().floatValue() );
		return floatV;
	}
	
	public Vector4d<Float> lerpf( Vector4d<T> endVec, Float percentage ){
		
		Float x = getU().floatValue() + (( endVec.getU().floatValue() - getU().floatValue() ) * percentage );
		Float y = getV().floatValue() + (( endVec.getV().floatValue() - getV().floatValue() ) * percentage );
		Float z = getZ().floatValue() + (( endVec.getZ().floatValue() - getZ().floatValue() ) * percentage );
		Float w = getW().floatValue() + (( endVec.getW().floatValue() - getW().floatValue() ) * percentage );
		
		Vector4d<Float> vec = new Vector4d<Float>( x, y, z, w );
		vec = vec.normalizef();
		
		return vec;
	}
	
	public Vector4d<Float> slerpf( Vector4d<T> vec, Float percentage ){
		
		Float dotProduct = dotf( vec );
		
		Float THRESHOLD = 0.9995f;
		
		// close enough
		if ( dotProduct > THRESHOLD ){
			return lerpf( vec, percentage );
		}
		
		// make the dot product within cosine range
		dotProduct = Math.min( Math.max( dotProduct, -1 ), 1 );
		
		Float theta = (float)Math.acos( dotProduct ) * percentage;
		
		Vector4d<Float> intVec = scalarf( dotProduct );
		intVec = vec.subtractf( (Vector4d<T>)intVec );
		intVec = intVec.normalizef();
		
		Float cX = (float)Math.cos( theta );
		Float cY = (float)Math.sin( theta );
		Vector4d<Float> retVec = scalarf( cX );
		Vector4d<Float> diffVec = intVec.scalarf( cY );
		
		retVec = retVec.addf( diffVec );
		
		return retVec;
	}
	
	public Vector4d<Float> multiplyf( Vector4d<T> rightV ) {
		
		Vector4d<Float> lfv = makeFloat( this );
		Vector4d<Float> rfv = makeFloat( rightV );
		
		Vector4d<Float> product = new Vector4d<Float>(
				lfv.getU() * rfv.getU(),
				lfv.getV() * rfv.getV(),
				lfv.getZ() * rfv.getZ(),
				lfv.getW() * rfv.getW() );
		
		return product;
	}
	
	public Vector4d<Float> scalarf( Float factor ){
		
		Vector4d<Float> floatMe = makeFloat( this );
		
		Vector4d<Float> scalar = new Vector4d<Float>(
				floatMe.getU() * factor,
				floatMe.getV() * factor,
				floatMe.getZ() * factor,
				floatMe.getW() * factor );
		
		return scalar;
	}
	
	public Float dotf( Vector4d<T> vec ){
		
		Float product = getU().floatValue() * vec.getU().floatValue() + 
					    getV().floatValue() * vec.getV().floatValue() + 
					    getZ().floatValue() * vec.getZ().floatValue() + 
					    getW().floatValue() * vec.getW().floatValue();
		
		return product;
	}
	
	public Vector4d<Float> normalizef(){
		
		Float lengthOfthistor = new Float( Math.sqrt( (this.getU().floatValue()*this.getU().floatValue()) +
										  (this.getV().floatValue()*this.getV().floatValue()) +
										  (this.getZ().floatValue()*this.getZ().floatValue()) +
										  (this.getW().floatValue()*this.getW().floatValue()) ) );
		
		Vector4d<Float> vecn = new Vector4d<Float>( this.getU().floatValue() / lengthOfthistor,
								   					this.getV().floatValue() / lengthOfthistor,
								   					this.getZ().floatValue() / lengthOfthistor,
								   					this.getW().floatValue() / lengthOfthistor );
		
		return vecn;
	}
	
	public Vector4d<Float> subtractf( Vector4d<T> rightV ){
		
		Vector4d<Float> lfv = makeFloat( this );
		Vector4d<Float> rfv = makeFloat( rightV );
		
		Vector4d<Float> subbed = new Vector4d<Float>(
				lfv.getU() - rfv.getU(),
				lfv.getV() - rfv.getV(),
				lfv.getZ() - rfv.getZ(),
				lfv.getW() - rfv.getW() );
		
		return subbed;
	}
	
	public Vector4d<Float> addf( Vector4d<T> rightV ){
		
		Vector4d<Float> lfv = makeFloat( this );
		Vector4d<Float> rfv = makeFloat( rightV );
		
		Vector4d<Float> added = new Vector4d<Float>(
				lfv.getU() + rfv.getU(),
				lfv.getV() + rfv.getV(),
				lfv.getZ() + rfv.getZ(),
				lfv.getW() + rfv.getW() );
		
		return added;
	}
}
