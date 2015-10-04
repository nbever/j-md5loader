package com.nate.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.nate.model.parts.Joint;
import com.nate.model.parts.Mesh;
import com.nate.model.parts.Triangle;
import com.nate.model.parts.Weight;
import com.nate.model.types.Vertice;

public class MD5Loader {

	private MD5Loader(){}
	
	public static MD5Model loadModel( String file ) throws Exception{
		
		MD5Model model = new MD5Model();
		File modelFile = new File( file );
		
		BufferedReader fileIn = new BufferedReader( new FileReader( modelFile ) );
		
		// get the version
		String line = getNextLine( fileIn );
		model.setVersion( Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) ) );
		
		if ( model.getVersion() != 10 ){
			throw new Exception( "MD5 version is not supported: " + model.getVersion() );
		}
		
		// get the command line
		line = getNextLine( fileIn );
		model.setCommandLine( line );
		
		// number of joints
		line = getNextLine( fileIn );
		int numJoints = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		
		// number of meshes
		line = getNextLine( fileIn );
		int numMeshes = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		
		model.initialize( numJoints, numMeshes );
		
		// now get the joints
		line = getNextLine( fileIn );
		if ( line.startsWith( "joints {" ) ){
			
			for ( int i = 0; i < numJoints; i++ ){
				
				line = getNextLine( fileIn );
				model.getJoints()[i] = Joint.parse( line );
			}
			
			// closing brace
			getNextLine( fileIn );
		}
		
		// huge blocks here for each mesh
		for ( int j = 0; j < numMeshes; j++ ){
			
			line = getNextLine( fileIn );
			
			if ( !line.startsWith( "mesh {" ) ){
				continue;
			}
			
			Mesh mesh = loadMesh( fileIn );
			model.getMeshes()[j] = mesh;
			model.prepareMesh( mesh );
			model.prepareNormals( mesh );
			
			mesh.getPositionBuffer().flip();
			mesh.getNormalBuffer().flip();
			mesh.getIndexBuffer().flip();
		}
		
		
		return model;
	}
	
	private static Mesh loadMesh( BufferedReader fileIn ) throws Exception {
		
		Mesh mesh = new Mesh();
		
		String line = getNextLine( fileIn );
		mesh.setShader( line.substring( line.indexOf( " " ) + 1 ) );
		
		line = getNextLine( fileIn );
		int numVerts = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		mesh.initializeVertices( numVerts );
		
		// the verts
		for ( int i = 0; i < numVerts; i++ ){
			line = getNextLine( fileIn );
			Vertice vert = Vertice.parseFloat( line );
			mesh.getVertices()[i] = vert;
		}
		
		// the triangles
		line = getNextLine( fileIn );
		int numTris = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		mesh.initializeTriangles( numTris );
		
		for ( int j = 0; j < numTris; j++ ){
			line = getNextLine( fileIn );
			Triangle tri = Triangle.parse( line );
			mesh.getTriangles()[j] = tri;
			mesh.getIndexBuffer().put( tri.getVertices().getU() );
			mesh.getIndexBuffer().put( tri.getVertices().getV() );
			mesh.getIndexBuffer().put( tri.getVertices().getZ() );
		}
		
		// the weights
		line = getNextLine( fileIn );
		int numWeights = Integer.parseInt( line.substring( line.indexOf( " " ) +1 ) );
		mesh.initializeWeights( numWeights );
		
		for ( int k = 0; k < numWeights; k++ ){
			line = getNextLine( fileIn );
			mesh.getWeights()[k] = Weight.parseFloat( line );
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
}
