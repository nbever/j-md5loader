package com.nate.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MD5Model {

	private MD5Joint[] baseSkeleton;
	private MD5Mesh[] meshes;
	
	private int numberOfJoints;
	private int numberOfMeshes;
	
	public static MD5Model loadModel( String file ) throws Exception{
		
		MD5Model model = new MD5Model();
		File modelFile = new File( file );
		
		BufferedReader fileIn = new BufferedReader( new FileReader( modelFile ) );
		
		// get the version
		String line = getNextLine( fileIn );

		line = line.substring( line.indexOf( " " ) + 1 );
		
		if ( line.indexOf( " " ) != -1 ){
			line = line.substring( 0, line.indexOf( " " ) );
		}
		int version = Integer.parseInt( line );
		
		if ( version != 10 ){
			throw new Exception( "MD5 version is not supported: " +version );
		}
		
		// get the command line
		line = getNextLine( fileIn );
		
		// number of joints
		line = getNextLine( fileIn );
		int numJoints = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		
		// number of meshes
		line = getNextLine( fileIn );
		int numMeshes = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		
		model.setNumberOfJoints( numJoints );
		model.setNumberOfMeshes( numMeshes );
		
		// now get the joints
		line = getNextLine( fileIn );
		if ( line.startsWith( "joints {" ) ){
			
			for ( int i = 0; i < numJoints; i++ ){
				
				line = getNextLine( fileIn );
				model.getBaseSkeleton()[i] = MD5Joint.parse( line );
			}
			
			// closing brace
			getNextLine( fileIn );
		}
		
		// huge blocks here for each mesh
		for ( int j = 0; j < numMeshes; j++ ){
			
			line = getNextLine( fileIn );
			
			if ( !line.startsWith( "mesh {" ) ){
				line = getNextLine( fileIn );
			}
			
			MD5Mesh mesh = MD5Mesh.loadMesh( fileIn );
			model.getMeshes()[j] = mesh;
			
		}
		
		return model;
	}
	
	private static String getNextLine( BufferedReader fileIn ) throws IOException{
		
		String line = "";
		
		do {
			line = fileIn.readLine();
		}
		while( line == null || line.trim().length() == 0 );
		
		return line.trim();
		
	}
	
	public void prepareModel( MD5Mesh mesh, MD5Joint[] skeleton ){
		
		mesh.initializeBuffers();
		
		for ( int t = 0; t < mesh.getNumberOfTriangles(); t++ ){
			
			mesh.getIndexArray().put( mesh.getTriangles()[t].getVertices()[0] );
			mesh.getIndexArray().put( mesh.getTriangles()[t].getVertices()[1] );
			mesh.getIndexArray().put( mesh.getTriangles()[t].getVertices()[2] );
		}
		
		for ( int v = 0; v < mesh.getNumberOfVertices(); v++ ){
			
			MD5Vertex vert = mesh.getVertices()[v];
			
			Vector3f finalVert = new Vector3f( 0.0f, 0.0f, 0.0f );
			
			for( int j = 0; j < vert.getWeightCount(); j++ ) {
				
				MD5Weight weight = mesh.getWeights()[ vert.getStartWeight() + j ];
				MD5Joint joint = skeleton[ weight.getJoint() ];
				
				Vector3f rot = Quaternarion.rotatePoint( joint.getOrientation(), weight.getPosition() );
				
				float x = finalVert.getX() + ( ( joint.getPosition().getX() + rot.getX() ) * weight.getBias() );
				float y = finalVert.getY() + ( ( joint.getPosition().getY() + rot.getY() ) * weight.getBias() );
				float z = finalVert.getZ() + ( ( joint.getPosition().getZ() + rot.getZ() ) * weight.getBias() );
				
				finalVert.setX( x );
				finalVert.setY( y );
				finalVert.setZ( z );
			}
			
			mesh.getVertexArray().put( finalVert.getX() );
			mesh.getVertexArray().put( finalVert.getY() );
			mesh.getVertexArray().put( finalVert.getZ() );
			
			mesh.getTexelArray().put( vert.getTextureCoordinates().getU() );
			mesh.getTexelArray().put( vert.getTextureCoordinates().getV() );
		}
	}
	
	public MD5Joint[] interpolateSkeletons( MD5Joint[] skeletonA, MD5Joint[] skeletonB, int numberOfJoints, float interp ){
		
		MD5Joint[] newSkeleton = new MD5Joint[ numberOfJoints ];
		
		for ( int i = 0; i < numberOfJoints; i++ ){
			
			MD5Joint joint = new MD5Joint();
			joint.setParent( skeletonA[i].getParent() );
			
			float x = skeletonA[i].getPosition().getX() + interp * ( skeletonB[i].getPosition().getX() - skeletonA[i].getPosition().getX() );
			float y = skeletonA[i].getPosition().getY() + interp * ( skeletonB[i].getPosition().getY() - skeletonA[i].getPosition().getY() );
			float z = skeletonA[i].getPosition().getZ() + interp * ( skeletonB[i].getPosition().getZ() - skeletonA[i].getPosition().getZ() );
			
			joint.setPosition( new Vector3f( x, y, z ) );
			
			Quaternarion slerped = Quaternarion.slerp( skeletonA[i].getOrientation(), 
													   skeletonB[i].getOrientation(), 
													   interp );
			
			joint.setOrientation( slerped );
			
			newSkeleton[i] = joint;
		}
		
		return newSkeleton;
	}
	
	public MD5Joint[] getBaseSkeleton() {
		return baseSkeleton;
	}
	
	public void setBaseSkeleton( MD5Joint[] baseSkeleton ) {
		this.baseSkeleton = baseSkeleton;
	}
	
	public MD5Mesh[] getMeshes() {
		return meshes;
	}
	
	public void setMeshes( MD5Mesh[] meshes ) {
		this.meshes = meshes;
	}
	
	public int getNumberOfJoints() {
		return numberOfJoints;
	}
	
	public void setNumberOfJoints( int numberOfJoints ) {
		
		this.numberOfJoints = numberOfJoints;
		baseSkeleton = new MD5Joint[ numberOfJoints ];
	}
	
	public int getNumberOfMeshes() {
		return numberOfMeshes;
	}
	
	public void setNumberOfMeshes( int numberOfMeshes ) {
		
		this.numberOfMeshes = numberOfMeshes;
		meshes = new MD5Mesh[ numberOfMeshes ];
	}
}
