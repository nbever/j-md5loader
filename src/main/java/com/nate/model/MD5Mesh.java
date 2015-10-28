package com.nate.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class MD5Mesh {

	private MD5Vertex[] vertices;
	private MD5Triangle[] triangles;
	private MD5Weight[] weights;
	
	private int numberOfVertices;
	private int numberOfTriangles;
	private int numberOfWeights;
	
	private String shader;
	
	private FloatBuffer vertexArray;
	private IntBuffer indexArray;

	protected static MD5Mesh loadMesh( BufferedReader fileIn ) throws Exception {
		
		MD5Mesh mesh = new MD5Mesh();
		
		String line = getNextLine( fileIn );
		mesh.setShader( line.substring( line.indexOf( " " ) + 1 ) );
		
		line = getNextLine( fileIn );
		int numVerts = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		mesh.setNumberOfVertices( numVerts );
		
		// the verts
		for ( int i = 0; i < numVerts; i++ ){
			line = getNextLine( fileIn );
			MD5Vertex vert = MD5Vertex.parseFloat( line );
			mesh.getVertices()[i] = vert;
		}
		
		// the triangles
		line = getNextLine( fileIn );
		int numTris = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		mesh.setNumberOfTriangles( numTris );
		
		for ( int j = 0; j < numTris; j++ ){
			line = getNextLine( fileIn );
			MD5Triangle tri = MD5Triangle.parse( line );
			mesh.getTriangles()[j] = tri;
		}
		
		// the weights
		line = getNextLine( fileIn );
		int numWeights = Integer.parseInt( line.substring( line.indexOf( " " ) +1 ) );
		mesh.setNumberOfWeights( numWeights );
		
		for ( int k = 0; k < numWeights; k++ ){
			line = getNextLine( fileIn );
			mesh.getWeights()[k] = MD5Weight.parseFloat( line );
		}
		
		return mesh;
	}
	
	private static String getNextLine( BufferedReader fileIn ) throws IOException{
		
		String line = "";
		
		do {
			line = fileIn.readLine();
		}
		while( line == null || line.trim().length() == 0 );
		
		return line.trim();
		
	}
	
	public MD5Vertex[] getVertices() {
		return vertices;
	}

	public void setVertices( MD5Vertex[] vertices ) {
		this.vertices = vertices;
	}

	public MD5Triangle[] getTriangles() {
		return triangles;
	}

	public void setTriangles( MD5Triangle[] triangles ) {
		this.triangles = triangles;
	}

	public MD5Weight[] getWeights() {
		return weights;
	}

	public void setWeights( MD5Weight[] weights ) {
		this.weights = weights;
	}

	public int getNumberOfVertices() {
		return numberOfVertices;
	}

	public void setNumberOfVertices( int numberOfVertices ) {
		this.numberOfVertices = numberOfVertices;
		this.vertices = new MD5Vertex[ numberOfVertices ];
	}

	public int getNumberOfTriangles() {
		return numberOfTriangles;
	}

	public void setNumberOfTriangles( int numberOfTriangles ) {
		this.numberOfTriangles = numberOfTriangles;
		this.triangles = new MD5Triangle[ numberOfTriangles ];
	}

	public int getNumberOfWeights() {
		return numberOfWeights;
	}

	public void setNumberOfWeights( int numberOfWeights ) {
		this.numberOfWeights = numberOfWeights;
		this.weights = new MD5Weight[ numberOfWeights ];
	}

	public String getShader() {
		return shader;
	}

	public void setShader( String shader ) {
		this.shader = shader;
	}
	
	public void initializeBuffers(){
		
		if ( indexArray != null ){
			indexArray.clear();
		}
		else {
			indexArray = BufferUtils.createIntBuffer( getNumberOfTriangles() * 3 );
		}
		
		if ( vertexArray != null ){
			vertexArray.clear();
		}
		else {
			vertexArray = BufferUtils.createFloatBuffer( getNumberOfVertices() * 3 );
		}
	}
	
	public IntBuffer getIndexArray(){
		return indexArray;
	}
	
	public FloatBuffer getVertexArray(){
		return vertexArray;
	}
}
