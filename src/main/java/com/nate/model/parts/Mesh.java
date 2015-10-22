package com.nate.model.parts;

import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glVertexPointer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

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
	
	public void render(){
		
//		getPositionBuffer().flip();
//		getNormalBuffer().flip();
//		getIndexBuffer().flip();
		
		glPushMatrix();
		
		glEnableClientState( GL_VERTEX_ARRAY );
		glEnableClientState( GL_NORMAL_ARRAY );
//		glEnableClientState( GL_TEXTURE_COORD_ARRAY );
		
//		glBindTexture( GL_TEXTURE_2D, 0 );
		glVertexPointer( 3, 0, getPositionBuffer() );
		glNormalPointer( 0, getNormalBuffer() );
		//glTexCoordPointer( 2, GL_FLOAT, mesh.getTextureBuffer() );
		glDrawElements( GL_TRIANGLES, getIndexBuffer() );
		
//		glBegin( GL_TRIANGLES );
//		
//			for ( int i = 0; i < mesh.getTriangles().length; i++ ){
//				
//				Triangle tri = mesh.getTriangles()[i];
//				Vector3d<Integer> verts = tri.getVertices();
//				
//				Vertice<Float> v1 = mesh.getVertices()[verts.getU()];
//				Vertice<Float> v2 = mesh.getVertices()[verts.getV()];
//				Vertice<Float> v3 = mesh.getVertices()[verts.getZ()];
//				
//				glVertex3f( v1.getPosition().getU(), v1.getPosition().getV(), v1.getPosition().getZ() );
//				glNormal3f( v1.getNormal().getU(), v1.getNormal().getV(), v1.getNormal().getZ() );
//				
//				glVertex3f( v2.getPosition().getU(), v2.getPosition().getV(), v2.getPosition().getZ() );
//				glNormal3f( v2.getNormal().getU(), v2.getNormal().getV(), v2.getNormal().getZ() );
//				
//				glVertex3f( v3.getPosition().getU(), v3.getPosition().getV(), v3.getPosition().getZ() );
//				glNormal3f( v3.getNormal().getU(), v3.getNormal().getV(), v3.getNormal().getZ() );
//			}
//		
//		glEnd();
	
		glDisableClientState( GL_VERTEX_ARRAY );
		glDisableClientState( GL_NORMAL_ARRAY );
		glDisableClientState( GL_TEXTURE_COORD_ARRAY );
		
		glPopMatrix();
		
//		getPositionBuffer().flip();
//		getNormalBuffer().flip();
//		getIndexBuffer().flip();
	}
	
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
		
		positionBuffer = BufferUtils.createFloatBuffer( num*3 );
		normalBuffer = BufferUtils.createFloatBuffer( num*3 );
	}
	
	public void initializeTriangles( int num ){
		triangles = new Triangle[num];
		
		indexBuffer = BufferUtils.createIntBuffer( num*3 );
	}
	
	public void initializeWeights( int num ){
		weights = new Weight[num];
	}
}
