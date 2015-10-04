package com.nate.model;

import com.nate.model.parts.Joint;
import com.nate.model.parts.Mesh;
import com.nate.model.parts.Triangle;
import com.nate.model.parts.Weight;
import com.nate.model.types.Vector3d;
import com.nate.model.types.Vertice;

import static org.lwjgl.opengl.GL11.*;

public class MD5Model {

	private Joint[] joints;
	private Mesh[] meshes;
	private int version;
	private String commandLine;
	
	
	public void initialize( int joints, int meshes ){
		this.joints = new Joint[joints];
		this.meshes = new Mesh[meshes];
	}
	
	public void render(){
		
		glPushMatrix();
		
		glColor3f( 1.0f, 1.0f, 1.0f );
		glScalef( 5.0f, 5.0f, 5.0f);
		glTranslatef( 0.0f, 25.0f, 50.0f );
		glRotatef( -90.0f, 1.0f, 0.0f, 0.0f );

		glEnableClientState( GL_VERTEX_ARRAY );
//		glEnableClientState( GL_NORMAL_ARRAY );
//		glEnableClientState( GL_TEXTURE_COORD_ARRAY );
		
		for ( Mesh mesh : getMeshes() ){
			
			if ( mesh == null ){
				continue;
			}
			
			
//			glBindTexture( GL_TEXTURE_2D, 0 );
			glVertexPointer( 3, 0, mesh.getPositionBuffer() );
			glNormalPointer( 0, mesh.getNormalBuffer() );
			//glTexCoordPointer( 2, GL_FLOAT, mesh.getTextureBuffer() );
			glDrawElements( GL_TRIANGLES, mesh.getIndexBuffer() );
			
//			glBegin( GL_TRIANGLES );
//			
//				for ( int i = 0; i < mesh.getTriangles().length; i++ ){
//					
//					Triangle tri = mesh.getTriangles()[i];
//					Vector3d<Integer> verts = tri.getVertices();
//					
//					Vertice<Float> v1 = mesh.getVertices()[verts.getU()];
//					Vertice<Float> v2 = mesh.getVertices()[verts.getV()];
//					Vertice<Float> v3 = mesh.getVertices()[verts.getZ()];
//					
//					glVertex3f( v1.getPosition().getU(), v1.getPosition().getV(), v1.getPosition().getZ() );
//					glNormal3f( v1.getNormal().getU(), v1.getNormal().getV(), v1.getNormal().getZ() );
//					
//					glVertex3f( v2.getPosition().getU(), v2.getPosition().getV(), v2.getPosition().getZ() );
//					glNormal3f( v2.getNormal().getU(), v2.getNormal().getV(), v2.getNormal().getZ() );
//					
//					glVertex3f( v3.getPosition().getU(), v3.getPosition().getV(), v3.getPosition().getZ() );
//					glNormal3f( v3.getNormal().getU(), v3.getNormal().getV(), v3.getNormal().getZ() );
//				}
//			
//			glEnd();
			
		}
		
		glDisableClientState( GL_VERTEX_ARRAY );
		glDisableClientState( GL_NORMAL_ARRAY );
		glDisableClientState( GL_TEXTURE_COORD_ARRAY );
		
		glPopMatrix();
	}
	
	public Joint[] getJoints() {
		return joints;
	}
	
	public void setJoints( Joint[] joints ) {
		this.joints = joints;
	}
	
	public Mesh[] getMeshes() {
		return meshes;
	}
	
	public void setMeshes( Mesh[] meshes ) {
		this.meshes = meshes;
	}
	
	public int getVersion() {
		return version;
	}
	
	public void setVersion( int version ) {
		this.version = version;
	}
	
	public String getCommandLine() {
		return commandLine;
	}
	
	public void setCommandLine( String commandLine ) {
		this.commandLine = commandLine;
	}
	
	public void prepareMesh( Mesh mesh ){
		
		mesh.getPositionBuffer().clear();
		
		for ( int i = 0; i < mesh.getVertices().length; i++ ){
			
			Vertice<Float> vert = mesh.getVertices()[i];
			
			Vector3d<Float> position = vert.getPosition();
			
			for ( int j = 0; j < vert.getWeightCount(); j++ ){
				Weight<Float> weight = mesh.getWeights()[ vert.getStartingWeight() + j ];
				Joint joint = getJoints()[ weight.getJointIndex() ];
				
				Vector3d<Float> rotationalPosition = joint.getOrientation().multiplyf( weight.getPosition() );
				
				Vector3d<Float> vPos = joint.getPosition().addf( rotationalPosition ).scalarf( weight.getWeightBias() );
				position = position.addf( vPos );
				vert.setPosition( position );
				
			}// all the weights and joints
			
			mesh.getPositionBuffer().put( position.getU() );
			mesh.getPositionBuffer().put( position.getV() );
			mesh.getPositionBuffer().put( position.getZ() );
		}
	}
	
	public void prepareNormals( Mesh mesh ){
		
		mesh.getNormalBuffer().clear();
		
		for ( int i = 0; i < mesh.getTriangles().length; i++ ){
			
			Triangle tri = mesh.getTriangles()[i];
			
			Vector3d<Float> v0 = mesh.getVertices()[ tri.getVertices().getU() ].getPosition();
			Vector3d<Float> v1 = mesh.getVertices()[ tri.getVertices().getV() ].getPosition();
			Vector3d<Float> v2 = mesh.getVertices()[ tri.getVertices().getZ() ].getPosition();
			
			// cross v2-v0, v1-v0
			Vector3d<Float> leftV = v2.subtractf( v0 );
			Vector3d<Float> rightV = v1.subtractf( v0 );
			
			Vector3d<Float> normal = leftV.crossf( rightV );
			
			// updating the normals
			Vertice<Float> vert0 = mesh.getVertices()[ tri.getVertices().getU() ];
			Vertice<Float> vert1 = mesh.getVertices()[ tri.getVertices().getV() ];
			Vertice<Float> vert2 = mesh.getVertices()[ tri.getVertices().getZ() ];
			
			Vector3d<Float> n0 = vert0.getNormal().addf( normal ); 
			Vector3d<Float> n1 = vert1.getNormal().addf( normal ); 
			Vector3d<Float> n2 = vert2.getNormal().addf( normal ); 
			
			vert0.setNormal( n0 );
			vert1.setNormal( n1 );
			vert2.setNormal( n2 );
		}
		
		for ( int j = 0; j < mesh.getVertices().length; ++j ){
			
			Vertice<Float> vert = mesh.getVertices()[j];
			
			Vector3d<Float> normal = vert.getNormal().normalizef();
			
			mesh.getNormalBuffer().put( normal.getU() );
			mesh.getNormalBuffer().put( normal.getV() );
			mesh.getNormalBuffer().put( normal.getZ() );
			
			Vector3d<Float> newNormal = new Vector3d<Float>( 0.0f, 0.0f, 0.0f );
			vert.setNormal( newNormal );
			
			for ( int k = 0; k < vert.getWeightCount(); k++ ){

				Weight<Float> weight = mesh.getWeights()[ vert.getStartingWeight() + k ];
				Joint joint = getJoints()[ weight.getJointIndex() ];
				
				Vector3d<Float> product = normal.multiplyf( joint.getOrientation() );
				Vector3d<Float> vertNormal = product.scalarf( weight.getWeightBias() );
				
				vert.setNormal( vertNormal );
			}
		}
	}	
}
