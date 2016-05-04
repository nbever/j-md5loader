package com.nate.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class MD5Model {

	private int numberOfJoints;
	private int numberOfMeshes;

	private boolean hasAnimation;
	List<MD5Joint> joints;
	List<MD5Mesh> meshes;
	
	private MD5Animation animation;
	
	public static MD5Model loadModel( String file, Integer textureId ) throws Exception{
		
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
				model.getJoints().add( MD5Joint.parse( line ) );
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
			
			MD5Mesh mesh = MD5Mesh.loadMesh( fileIn, textureId );
			model.getMeshes().add( mesh );
			
			model.prepareMesh( mesh, model.getJoints() );
			model.prepareNormals( mesh );
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
	
	public void prepareMesh( MD5Mesh mesh, List<? extends MD5Joint> joints ){
		
		mesh.initializeBuffers();
		
		for ( int v_i = 0; v_i < mesh.getNumberOfVertices(); v_i++ ){
			
			Vector3f finalPosition = new Vector3f( 0.0f, 0.0f, 0.0f );
			MD5Vertex vert = mesh.getVertices()[v_i];
			
			vert.setPosition( new Vector3f( 0.0f, 0.0f, 0.0f ) );
			vert.setNormal( new Vector3f( 0.0f, 0.0f, 0.0f ) );
			
			// sum the position of the weights
			for ( int w_i = 0; w_i < vert.getWeightCount(); w_i++ ){
				
				MD5Weight weight = mesh.getWeights()[vert.getStartWeight() + w_i];
				MD5Joint joint = joints.get( weight.getJoint() );
				
				// convert the weight position from joint local space to object spaces
				Vector3f rotPos3 = Quaternarion.rotatePoint( joint.getOrientation(), weight.getPosition() );
				
				Vector3f j_r = Vector3f.add( joint.getPosition(), rotPos3 );
				vert.setPosition( Vector3f.scalar( j_r, weight.getBias() ) );
				
				finalPosition = Vector3f.add( finalPosition, vert.getPosition() );
			}
			
			mesh.getVertexArray().put( finalPosition.getX() );
			mesh.getVertexArray().put( finalPosition.getY() );
			mesh.getVertexArray().put( finalPosition.getZ() );
			
			mesh.getTexelArray().put( vert.getTextureCoordinates().getU() );
			mesh.getTexelArray().put( vert.getTextureCoordinates().getV() );
		}
	}
	
	public void prepareNormals( MD5Mesh mesh ){
		
		mesh.getNormalArray().clear();
		
		for ( int i = 0; i < mesh.getNumberOfTriangles(); i++ ){
			
			MD5Triangle tri = mesh.getTriangles()[i];
			Vector3f v0 = mesh.getVertices()[ tri.getVertices()[0]].getPosition();
			Vector3f v1 = mesh.getVertices()[ tri.getVertices()[1]].getPosition();
			Vector3f v2 = mesh.getVertices()[ tri.getVertices()[2]].getPosition();
			
			Vector3f v2m0 = Vector3f.subtract( v2, v0 );
			Vector3f v1m0 = Vector3f.subtract( v1, v0 );
			Vector3f normal = Vector3f.multiply( v2m0, v1m0 );
			
			Vector3f n0 = Vector3f.add( mesh.getVertices()[tri.getVertices()[0]].getNormal(), normal );
			Vector3f n1 = Vector3f.add( mesh.getVertices()[tri.getVertices()[1]].getNormal(), normal );
			Vector3f n2 = Vector3f.add( mesh.getVertices()[tri.getVertices()[2]].getNormal(), normal );
			
			mesh.getVertices()[tri.getVertices()[0]].setNormal( n0 );
			mesh.getVertices()[tri.getVertices()[1]].setNormal( n1 );
			mesh.getVertices()[tri.getVertices()[2]].setNormal( n2 );
			
		}
		
		//Now normalize all the normals
		for ( int j = 0; j < mesh.getNumberOfVertices(); j++ ){
			MD5Vertex vert = mesh.getVertices()[j];
			
			Vector3f normal = Vector3f.normalize( vert.getNormal() );
			mesh.getNormalArray().put( normal.getX() );
			mesh.getNormalArray().put( normal.getY() );
			mesh.getNormalArray().put( normal.getZ() );
			
			//reset the normal to calculate the bind-pose normal in joint space
			vert.setNormal( new Vector3f( 0.0f, 0.0f, 0.0f ) );
			
			//put the bind-pose normal into joint-local space
			//so the animated normal can be computed faster later
			for ( int k = 0; k < vert.getWeightCount(); k++ ){
				MD5Weight weight = mesh.getWeights()[vert.getStartWeight()+k];
				MD5Joint joint = getJoints().get( weight.getJoint() );
				
				Quaternarion qNO = Quaternarion.multiply( joint.getOrientation(), normal );
				Vector3f n_o = new Vector3f( qNO.getX(), qNO.getY(), qNO.getZ() );
				Vector3f n_scale = Vector3f.scalar( n_o, weight.getBias() );
				vert.setNormal( Vector3f.add( n_scale, vert.getNormal() ) );
			}
		}
	}
	
	public void update( float deltaTime ){

		if ( hasAnimation() ){
			
			getAnimation().update( deltaTime );
			MD5FrameSkeleton skeleton = getAnimation().getAnimatedSkeleton();
			
//			renderSkeleton( skeleton.getSkeletonJoints() );
			
			for ( int i = 0; i < getMeshes().size(); i++ ){
				prepareMesh( getMeshes().get( i ), skeleton.getSkeletonJoints() );
			}
		}
	}
	
	public void render(){
	    glPushMatrix();

	    // Render the meshes
	    for ( int i = 0; i < getMeshes().size(); ++i )
	    {
	        renderMesh(getMeshes().get( i ) );
	    }
	    
//	    m_Animation.Render();

	    for ( int i = 0; i < getMeshes().size(); ++i )
	    {
//	        renderNormals( getMeshes().get( i ) );
	    }

	    renderSkeleton( getAnimation().getAnimatedSkeleton().getSkeletonJoints() );
	    
	    
	    glPopMatrix();
	}
	
	public void renderMesh( MD5Mesh mesh ){
		
	    glColor3f( 1.0f, 1.0f, 1.0f );
	    
	    glEnableClientState( GL_VERTEX_ARRAY );
	    glEnableClientState( GL_TEXTURE_COORD_ARRAY );
	    glEnableClientState( GL_NORMAL_ARRAY );
	    glEnable( GL_TEXTURE_2D );
	    
	    FloatBuffer dvBuffer = mesh.getVertexArray().duplicate();
	    FloatBuffer dnBuffer = mesh.getNormalArray().duplicate();
	    FloatBuffer dtBuffer = mesh.getTexelArray().duplicate();
	    IntBuffer diBuffer = mesh.getIndexArray().duplicate();
	    
	    mesh.getVertexArray().flip();
	    mesh.getNormalArray().flip();
	    mesh.getIndexArray().flip();
	    mesh.getTexelArray().flip();

	    glBindTexture( GL_TEXTURE_2D, mesh.getTextureId() );
	    glVertexPointer( 3, 0, mesh.getVertexArray() );
	    glNormalPointer( 3, mesh.getNormalArray() );
	    glTexCoordPointer( 2, 0, mesh.getTexelArray() );
	    
	    glDrawElements( GL_TRIANGLES, mesh.getIndexArray() );

	    glDisableClientState( GL_NORMAL_ARRAY );
	    glDisableClientState( GL_TEXTURE_COORD_ARRAY );
	    glDisableClientState( GL_VERTEX_ARRAY );
	 
	    glDisable( GL_TEXTURE_2D );
	    
	    glBindTexture( GL_TEXTURE_2D, 0 );
	    
	    mesh.setVertexArray( dvBuffer );
	    mesh.setIndexArray( diBuffer );
	    mesh.setNormalArray( dnBuffer );
	    mesh.setTexelArray( dtBuffer );
	 
	}
	
	public void renderNormals( MD5Mesh mesh ){
		
	    glPushAttrib( GL_ENABLE_BIT );
	    glDisable( GL_LIGHTING );
	    
	    glColor3f( 1.0f, 1.0f, 0.0f );// Yellow

	    glBegin( GL_LINES );
	    {
	    	
	        for ( int i = 0; i < mesh.getVertices().length; i++ )
	        {
	        	int index = i*3;
	        	
	            Vector3f p0 = new Vector3f( mesh.getVertexArray().get( index ),
	            	mesh.getVertexArray().get( index+1 ),
	            	mesh.getVertexArray().get( index+2 ) );
	            
	            Vector3f n0 = new Vector3f( mesh.getNormalArray().get( index ),
	            	mesh.getNormalArray().get( index+1 ),
	            	mesh.getNormalArray().get( index+2 ) );
	            
	            Vector3f p1 = Vector3f.add( p0, n0 );

	            glVertex3f( p0.getX(), p0.getY(), p0.getZ() );
	            glVertex3f( p1.getX(), p1.getY(), p1.getZ() );
	        }
	    }
	    glEnd();

	    glPopAttrib();
	}
	
	public void renderSkeleton( List<? extends MD5Joint> joints ){
	    
		glPointSize( 5.0f );
	    glColor3f( 1.0f, 0.0f, 0.0f );
	    
	    glPushAttrib( GL_ENABLE_BIT );

	    glDisable(GL_LIGHTING );
	    glDisable( GL_DEPTH_TEST );
	    
	 // Draw the joint positions
	    glBegin( GL_POINTS );
	    {
	        for ( int i = 0; i < joints.size(); ++i )
	        {
	        	Vector3f pos = joints.get( i ).getPosition();
	        	glVertex3f( pos.getX(), pos.getY(), pos.getZ() );
	        }
	    }
	    glEnd();

	    // Draw the bones
	    glColor3f( 0.0f, 1.0f, 0.0f );
	    glBegin( GL_LINES );
	    {
	        for ( int i = 0; i < joints.size(); ++i )
	        {
	        	MD5Joint j0 = joints.get( i );
	            if ( j0.getParent() != -1 )
	            {
	            	MD5Joint j1 = joints.get( j0.getParent() );
	                Vector3f j0pos = j0.getPosition();
	                Vector3f j1pos = j1.getPosition();
	                glVertex3f( j0pos.getX(), j0pos.getY(), j0pos.getZ() );
	                glVertex3f( j1pos.getX(), j1pos.getY(), j1pos.getZ() );
	            }
	        }
	    }
	    glEnd();

	    glPopAttrib();
	}
	
	public List<MD5Joint> getJoints() {
		
		if ( joints == null ){
			joints = new ArrayList<MD5Joint>();
		}
		return joints;
	}
	
	public void setJoints( List<MD5Joint> baseSkeleton ) {
		this.joints = baseSkeleton;
	}
	
	public List<MD5Mesh> getMeshes() {
		
		if ( meshes == null ){
			meshes = new ArrayList<MD5Mesh>();
		}
		return meshes;
	}
	
	public void setMeshes( List<MD5Mesh> meshes ) {
		this.meshes = meshes;
	}
	
	public int getNumberOfJoints() {
		return numberOfJoints;
	}
	
	public void setNumberOfJoints( int numberOfJoints ) {
		
		this.numberOfJoints = numberOfJoints;
	}
	
	public int getNumberOfMeshes() {
		return numberOfMeshes;
	}
	
	public void setNumberOfMeshes( int numberOfMeshes ) {
		
		this.numberOfMeshes = numberOfMeshes;
	}
	
	public void setAnimation( MD5Animation anAnim ){
		this.animation = anAnim;
		this.hasAnimation = true;
	}
	
	public MD5Animation getAnimation(){
		return this.animation;
	}
	
	public boolean hasAnimation(){
		return hasAnimation;
	}
}
