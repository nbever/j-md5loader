package com.nate.model.parts;

import com.nate.model.types.Vector3d;

public class Triangle {

	private int index;
	private Vector3d<Integer> vertices;
	
	public Triangle(){}
	
	public static Triangle parse( String line ) throws Exception{
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length < 5 ){
			throw new Exception( "Bad format for triangle: " + line );
		}
		
		Triangle tri = new Triangle();
		tri.setIndex( Integer.parseInt( tokens[1] ) );
		Vector3d<Integer> vertices = new Vector3d<Integer>( Integer.parseInt( tokens[2] ),
															Integer.parseInt( tokens[3] ),
															Integer.parseInt( tokens[4] ));
		tri.setVertices( vertices );
		
		return tri;
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex( int index ){
		this.index = index;
	}
	
	public Vector3d<Integer> getVertices(){
		return vertices;
	}
	
	public void setVertices( Vector3d<Integer> vertices ){
		this.vertices = vertices;
	}
}
