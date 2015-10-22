package com.nate.model;

import com.nate.model.parts.FrameSkeleton;
import com.nate.model.parts.Joint;
import com.nate.model.parts.Mesh;
import com.nate.model.parts.SkeletonJoint;
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
	
	private MD5Animation animation;
	
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
		
		for ( Mesh mesh : getMeshes() ){
			
			if ( mesh == null ){
				continue;
			}
			
			mesh.render();
		}
		
		if ( getAnimation() != null ){
			getAnimation().render();
		}
		
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
	
	public MD5Animation getAnimation(){
		return animation;
	}
	
	public void setAnimation( MD5Animation animation ){
		this.animation = animation;
	}
	
	public void update( Float deltaTime ){
		
		if ( getAnimation() != null ){
			
			getAnimation().update( deltaTime );
			FrameSkeleton<Float> skeleton = getAnimation().getAnimatedSkeleton();
			
			for ( int i = 0; i < getMeshes().length; i++ ){
				prepareMesh( getMeshes()[i], skeleton );
			}
		}
	}
	
	public void prepareMesh( Mesh mesh, FrameSkeleton skeleton ){
		
		for ( int i = 0; i < mesh.getVertices().length; i++ ){
			Vertice<Float> vert = mesh.getVertices()[i];
			
			Vector3d<Float> position = new Vector3d<Float>( 0.0f, 0.0f, 0.0f );
			Vector3d<Float> normal = new Vector3d<Float>( 0.0f, 0.0f, 0.0f );
			
			for ( int j = 0; j < vert.getWeightCount(); j++ ){
				
				Weight<Float> weight = mesh.getWeights()[vert.getStartingWeight() + j];
				SkeletonJoint<Float> joint = skeleton.getSkeletonJoints()[weight.getJointIndex()];
				Vector3d<Float> rotation = joint.getOrientation().multiplyf( weight.getPosition() );
				
				Vector3d<Float> sumVec = joint.getPosition().addf( rotation );
				position = position.addf( sumVec.scalarf( weight.getWeightBias() ) );
				
				Vector3d<Float> prodVec = joint.getOrientation().multiplyf( vert.getNormal() );
				normal = normal.addf( prodVec.scalarf( weight.getWeightBias() ) );
				
				// put them in the right spot in the position buffer.
				try {
					mesh.getPositionBuffer().put( (i*3), position.getU() );
					mesh.getPositionBuffer().put( (i*3) + 1, position.getV() );
					mesh.getPositionBuffer().put(  (i*3) + 2, position.getZ() );
					
					mesh.getNormalBuffer().put( (i*3), normal.getU() );
					mesh.getNormalBuffer().put( (i*3) + 1, normal.getV() );
					mesh.getNormalBuffer().put( (i*3) + 2, normal.getZ() );
				}
				catch( IndexOutOfBoundsException ie ){
					System.out.println( "i: " + i + ", size: " + mesh.getPositionBuffer().capacity() + ", position: " + mesh.getPositionBuffer().position() + ", limit: " + mesh.getPositionBuffer().limit() );
					ie.printStackTrace();
					System.exit( 1 );
				}
			}
		}
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
				
				Vector3d<Float> sumV = joint.getPosition().addf( rotationalPosition );
				Vector3d<Float> vPos = sumV.scalarf( weight.getWeightBias() );
				position = position.addf( vPos );
				vert.setPosition( position );
				vert.setNormal( new Vector3d<Float>( 0.0f, 0.0f, 0.0f ) );
				
			}// all the weights and joints
			
			mesh.getPositionBuffer().put( new float[]{ position.getU(), position.getV(), position.getZ() } );
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
		
		for ( int j = 0; j < mesh.getVertices().length; j++ ){
			
			Vertice<Float> vert = mesh.getVertices()[j];
			
			Vector3d<Float> normal = vert.getNormal().normalizef();
			
			mesh.getNormalBuffer().put( new float[]{ normal.getU(), normal.getV(), normal.getZ()} );
			
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
