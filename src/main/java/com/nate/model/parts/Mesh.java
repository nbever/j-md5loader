package com.nate.model.parts;

import com.nate.model.types.Vertice;

public class Mesh {

	private String shader;
	private Vertice<Float>[] vertices;
	private Triangle[] triangles;
	private Weight<Float>[] weights;
	
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
	
	public void initializeVertices( int num ){
		vertices = new Vertice[num];
	}
	
	public void initializeTriangles( int num ){
		triangles = new Triangle[num];
	}
	
	public void initializeWeights( int num ){
		weights = new Weight[num];
	}
}
