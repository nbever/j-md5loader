package com.nate.model;

import java.util.HashMap;
import java.util.Map;

import com.nate.model.parts.Joint;
import com.nate.model.parts.Mesh;
import com.nate.model.parts.Triangle;
import com.nate.model.parts.Weight;
import com.nate.model.types.Vector3d;
import com.nate.model.types.Vertice;

public class MD5Model {

	private Joint[] joints;
	private Mesh[] meshes;
	private int version;
	private String commandLine;
	
	private Map<Mesh, Vector3d<Float>[]> preparedMesh;
	private Map<Mesh, Vector3d<Float>[]> preparedNormals;
	
	public void initialize( int joints, int meshes ){
		this.joints = new Joint[joints];
		this.meshes = new Mesh[meshes];
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
		
		Vector3d<Float>[] positions = getPreparedMeshes().get( mesh );
		
		if ( positions == null ){
			positions = new Vector3d[mesh.getVertices().length];
		}
		
		for ( int i = 0; i < mesh.getVertices().length; i++ ){
			
			Vertice<Float> vert = mesh.getVertices()[i];
			
			Vector3d<Float> position = new Vector3d<Float>( 0.0f, 0.0f, 0.0f );
			
			for ( int j = 0; j < vert.getWeightCount(); j++ ){
				Weight<Float> weight = mesh.getWeights()[ vert.getStartingWeight() + j ];
				Joint joint = getJoints()[ weight.getJointIndex() ];
				
				Vector3d<Float> rotationalPosition = new Vector3d<Float>( joint.getOrientation().getU() * weight.getPosition().getU(), 
						joint.getOrientation().getV() * weight.getPosition().getV(),
						joint.getOrientation().getZ() * weight.getPosition().getZ() ); 
				
				Float x = position.getU() + ((rotationalPosition.getU() + joint.getPosition().getU() ) * weight.getWeightBias() );
				Float y = position.getV() + ((rotationalPosition.getV() + joint.getPosition().getV() ) * weight.getWeightBias() );
				Float z = position.getZ() + ((rotationalPosition.getZ() + joint.getPosition().getZ() ) * weight.getWeightBias() );
				
				position = new Vector3d<Float>( x, y, z );
				
			}// all the weights and joints
			
			positions[i] = position;
		}
		
		getPreparedMeshes().put( mesh, positions );
	}
	
	public void prepareNormals( Mesh mesh ){
		
		Vector3d<Float>[] normals = getPreparedNormals().get( mesh );
		Vector3d<Float>[] vertPositions = getPreparedMeshes().get( mesh );
		
		for ( int i = 0; i < mesh.getTriangles().length; i++ ){
			
			Triangle tri = mesh.getTriangles()[i];
			
			Vector3d<Float> v0 = vertPositions[ tri.getVertices().getU() ];
			Vector3d<Float> v1 = vertPositions[ tri.getVertices().getV() ];
			Vector3d<Float> v2 = vertPositions[ tri.getVertices().getZ() ];
			
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
			Vector3d<Float> newNormal = new Vector3d<Float>( 0.0f, 0.0f, 0.0f );
			
			getPreparedNormals().get( mesh )[j] = normal;
			vert.setNormal( newNormal );
			
			for ( int k = 0; k < mesh.getWeights().length; j++ ){
				
				Weight<Float> weight = mesh.getWeights()[ vert.getStartingWeight() + k ];
				Joint joint = getJoints()[ weight.getJointIndex() ];
				
				Vector3d<Float> product = normal.multiplyf( joint.getOrientation() );
				Vector3d<Float> vertNormal = new Vector3d<Float>(
						product.getU() * weight.getWeightBias(),
						product.getV() * weight.getWeightBias(),
						product.getZ() * weight.getWeightBias() );
				
				vert.setNormal( vertNormal );
			}
		}
	}
	
	private Map<Mesh, Vector3d<Float>[]> getPreparedMeshes(){
		
		if ( preparedMesh == null ){
			preparedMesh = new HashMap<Mesh, Vector3d<Float>[] >();
		}
		
		return preparedMesh;
	}
	
	private Map<Mesh, Vector3d<Float>[]> getPreparedNormals(){
		
		if ( preparedNormals == null ){
			preparedNormals = new HashMap<Mesh, Vector3d<Float>[]>();
		}
		
		return preparedNormals;
	}
	
}
