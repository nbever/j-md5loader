package com.nate.model;

public class MD5Vertex {

	private Vector2f textureCoordinates;
	private Vector3f baseNormal;
	
	private int startWeight;
	private int weightCount;
	
	public static MD5Vertex parseFloat( String line ) throws Exception{
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length < 8 ){
			throw new Exception( "Bad format for vertice: " + line );
		}
		
		Float u = Float.parseFloat( tokens[3] );
		Float v = Float.parseFloat( tokens[4] );
		
		MD5Vertex vertice = new MD5Vertex();
		vertice.setTextureCoordinates( new Vector2f( u, v ) );
//		vertice.setIndex( Integer.parseInt( tokens[1] ) );
		
		vertice.setStartWeight( Integer.parseInt( tokens[6] ) );
		vertice.setWeightCount( Integer.parseInt( tokens[7] ) );
		
		return vertice;
	}
	
	public Vector2f getTextureCoordinates() {
		return textureCoordinates;
	}
	
	public void setTextureCoordinates( Vector2f textureCoordinates ) {
		this.textureCoordinates = textureCoordinates;
	}
	
	public Vector3f getBaseNormal() {
		return baseNormal;
	}
	
	public void setBaseNormal( Vector3f aNormal ) {
		baseNormal = aNormal;
	}
	
	public int getStartWeight() {
		return startWeight;
	}
	
	public void setStartWeight( int startWeight ) {
		this.startWeight = startWeight;
	}
	
	public int getWeightCount() {
		return weightCount;
	}
	
	public void setWeightCount( int weightCount ) {
		this.weightCount = weightCount;
	}
}
