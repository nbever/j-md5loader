package com.nate.model.types;

public class Vertice<T extends Number> {

	private Vector2d<T> textureCoordinates;
	private int index;
	private int startWeight;
	private int weightCount;
	private Vector3d<Float> normal = new Vector3d<Float>( 0.0f, 0.0f, 0.0f );
	
	public static Vertice<Float> parseFloat( String line ) throws Exception{
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length < 8 ){
			throw new Exception( "Bad format for vertice: " + line );
		}
		
		Float u = Float.parseFloat( tokens[3] );
		Float v = Float.parseFloat( tokens[4] );
		
		Vertice<Float> vertice = new Vertice<Float>();
		vertice.setTextureCoordinates( new Vector2d<Float>( u, v ) );
		vertice.setIndex( Integer.parseInt( tokens[1] ) );
		
		vertice.setStartWeight( Integer.parseInt( tokens[6] ) );
		vertice.setWeightCount( Integer.parseInt( tokens[7] ) );
		
		return vertice;
	}
	
	public Vertice(){
		
	}
	
	public Vector2d<T> getTextureCoordinates(){
		return textureCoordinates;
	}
	
	public void setTextureCoordinates( Vector2d<T> coordinates ){
		textureCoordinates = coordinates;
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex( int index ){
		this.index = index;
	}
	
	public int getStartingWeight(){
		return startWeight;
	}
	
	public void setStartWeight( int startWeight ){
		this.startWeight = startWeight;
	}
	
	public int getWeightCount(){
		return weightCount;
	}
	
	public void setWeightCount( int weightCount ){
		this.weightCount = weightCount;
	}
	
	public void setNormal( Vector3d<Float> normal ){
		this.normal = normal;
	}
	
	public Vector3d<Float> getNormal(){
		return this.normal;
	}
}
