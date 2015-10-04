package com.nate.model.parts;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.nate.model.types.Vertice;

public class Mesh {

	private String shader;
	private Vertice<Float>[] vertices;
	private Triangle[] triangles;
	private Weight<Float>[] weights;
	private int textureId;
	
	private FloatBuffer positionBuffer;
	private FloatBuffer normalBuffer;
	private IntBuffer indexBuffer;
	
	public String getShader(){
		return shader;
	}
	
	public void setShader( String shader ){
		this.shader = shader;
	}
	
	public Vertice<Float>[] getVertices(){
		return vertices;
	}
	
	public Triangle[] getTriangles(){
		return triangles;
	}
	
	public Weight<Float>[] getWeights(){
		return weights;
	}
	
	public int getTextureId(){
		return textureId;
	}
	
	public FloatBuffer getPositionBuffer(){
		return positionBuffer;
	}
	
	public FloatBuffer getNormalBuffer(){
		return normalBuffer;
	}
	
	public IntBuffer getIndexBuffer(){
		return indexBuffer;
	}
	
	public void initializeVertices( int num ){
		vertices = new Vertice[num];
		
		ByteBuffer bb = ByteBuffer.allocateDirect((num*4)*3);
		positionBuffer = bb.asFloatBuffer();
		positionBuffer.rewind();
		
		ByteBuffer nbb = ByteBuffer.allocateDirect( (num*4) * 3 );
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.rewind();
	}
	
	public void initializeTriangles( int num ){
		triangles = new Triangle[num];
		
		ByteBuffer bb = ByteBuffer.allocateDirect( ( num * 4 ) * 3 );
		indexBuffer = bb.asIntBuffer();
		indexBuffer.rewind();
	}
	
	public void initializeWeights( int num ){
		weights = new Weight[num];
	}
}
