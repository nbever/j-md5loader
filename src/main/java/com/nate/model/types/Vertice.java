package com.nate.model.types;

public class Vertice<T> {

	private Vector2d<T> textureCoordinates;
	private int index;
	private int[] weights = new int[2];
	
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
		
		int[] weights = { Integer.parseInt( tokens[6] ), Integer.parseInt( tokens[7] ) };
		vertice.setWeights( weights );
		
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
	
	public int[] getWeights(){
		return weights;
	}
	
	public void setWeights( int[] weights ){
		this.weights = weights;
	}
}
