package com.nate.model;

public class MD5Vertex {

	private Vector2f textureCoordinates;
	private Vector3f normal = new Vector3f( 0.0f, 0.0f, 0.0f );
	private Vector3f position = new Vector3f( 0.0f, 0.0f, 0.0f );
	
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
	
	public Vector3f getNormal() {
		return normal;
	}
	
	public void setNormal( Vector3f aNormal ) {
		normal = aNormal;
	}
	
	public Vector3f getPosition(){
		return position;
	}
	
	public void setPosition( Vector3f aPos ){
		position = aPos;
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
