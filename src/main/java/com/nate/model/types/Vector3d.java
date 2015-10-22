package com.nate.model.types;

public class Vector3d<T extends Number> extends Vector2d<T>{

	private T z;
	
	public Vector3d( T x, T y, T z ){
		super( x, y );
		this.z = z;
	}
	
	public T getZ(){
		return z;
	}
	
	public void setZ( T z ){
		this.z = z;
	}
	
	protected Vector3d<Float> makeFloat( Vector3d<T> vec ){
		
		Vector3d<Float> floatV = new Vector3d<Float>( vec.getU().floatValue(), vec.getV().floatValue(), vec.getZ().floatValue() );
		return floatV;
	}
	
	public Vector3d<Float> lerpf( Vector3d<T> endVec, Float percentage ){
		
		Float x = getU().floatValue() + (( endVec.getU().floatValue() - getU().floatValue() ) * percentage );
		Float y = getV().floatValue() + (( endVec.getV().floatValue() - getV().floatValue() ) * percentage );
		Float z = getZ().floatValue() + (( endVec.getZ().floatValue() - getZ().floatValue() ) * percentage );
		
		Vector3d<Float> vec = new Vector3d<Float>( x, y, z );
		vec = vec.normalizef();
		
		return vec;
	}
	
	public Vector3d<Float> crossf( Vector3d<T> rightV ){
		
		Vector3d<Float> lfv = makeFloat( this );
		Vector3d<Float> rfv = makeFloat( rightV );
		
		Vector3d<Float> normal = new Vector3d<Float>( 
				(lfv.getV()*rfv.getZ()) - (lfv.getZ()*rfv.getV()), 
				-1*((lfv.getU()*rfv.getZ()) - (lfv.getZ()*rfv.getU())),
				(lfv.getU()*rfv.getV()) - (lfv.getV()*rfv.getU()) );
		
		return normal;
	}
	
	public Vector3d<Float> scalarf( Float factor ){
		
		Vector3d<Float> floatMe = makeFloat( this );
		
		Vector3d<Float> scalar = new Vector3d<Float>(
				floatMe.getU() * factor,
				floatMe.getV() * factor,
				floatMe.getZ() * factor );
		
		return scalar;
	}
	
	public Vector3d<Float> multiplyf( Vector3d<T> rightV ){
		
		Vector3d<Float> lfv = makeFloat( this );
		Vector3d<Float> rfv = makeFloat( rightV );
		
		Vector3d<Float> product = new Vector3d<Float>(
				lfv.getU() * rfv.getU(),
				lfv.getV() * rfv.getV(),
				lfv.getZ() * rfv.getZ() );
		
		return product;
	}
	
	public Vector3d<Float> subtractf( Vector3d<T> rightV ){
		
		Vector3d<Float> lfv = makeFloat( this );
		Vector3d<Float> rfv = makeFloat( rightV );
		
		Vector3d<Float> subbed = new Vector3d<Float>(
				lfv.getU() - rfv.getU(),
				lfv.getV() - rfv.getV(),
				lfv.getZ() - rfv.getZ() );
		
		return subbed;
	}
	
	public Vector3d<Float> addf( Vector3d<T> rightV ){
		
		Vector3d<Float> lfv = makeFloat( this );
		Vector3d<Float> rfv = makeFloat( rightV );
		
		Vector3d<Float> added = new Vector3d<Float>(
				lfv.getU() + rfv.getU(),
				lfv.getV() + rfv.getV(),
				lfv.getZ() + rfv.getZ() );
		
		return added;
	}
	
	public Vector3d<Float> normalizef(){
		
		Float lengthOfthistor = new Float( Math.sqrt( (this.getU().floatValue()*this.getU().floatValue()) +
										  (this.getV().floatValue()*this.getV().floatValue()) +
										  (this.getZ().floatValue()*this.getZ().floatValue()) ) );
		
		Vector3d<Float> vecn = new Vector3d<Float>( this.getU().floatValue() / lengthOfthistor,
								   					this.getV().floatValue() / lengthOfthistor,
								   					this.getZ().floatValue() / lengthOfthistor );
		
		return vecn;
	}
	
	public static Float computeW( Vector3d<Float> vec ){
		Float w = 1.0f - (vec.getU()*vec.getU()) - (vec.getV()*vec.getV()) - (vec.getZ()*vec.getZ());
		
		if ( w < 0.0f ){
			w = 0.0f;
		}
		else {
			w = (float) (-1.0f * Math.sqrt( w ));
		}
		
		return w;
	}
}
