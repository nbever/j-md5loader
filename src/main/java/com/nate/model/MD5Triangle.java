package com.nate.model;

public class MD5Triangle {

	private int[] vertices = new int[3];
	
	public static MD5Triangle parse( String line ) throws Exception{
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length < 5 ){
			throw new Exception( "Bad format for triangle: " + line );
		}
		
		MD5Triangle tri = new MD5Triangle();
//		tri.setIndex( Integer.parseInt( tokens[1] ) );
		int[] verts = { Integer.parseInt( tokens[2] ),
						Integer.parseInt( tokens[3] ),
						Integer.parseInt( tokens[4] )};
		
		tri.setVertices( verts );
		
		return tri;
	}
	
	public int[] getVertices(){
		return vertices;
	}
	
	public void setVertices( int[] indices ){
		vertices = indices;
	}
}
