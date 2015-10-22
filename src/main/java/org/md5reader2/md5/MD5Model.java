/**
 * 
 */
package org.md5reader2.md5;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * @author Marco Frisan
 * 
 */
public class MD5Model {

	private int md5Version; // MD5Version - should be 10
	private String commandLine; // commandline
	private int numJoints; // numJoints
	private int numMeshes; // numMeshes

	private MD5Joint[] joints;
	private MD5Mesh[] meshes;
	
	private MD5Animation animation;
	
//	private int[] vertexIndices;

	/**
	 * 
	 */
	public MD5Model() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the md5Version
	 */
	public int getMd5Version() {
		return md5Version;
	}

	/**
	 * @param md5Version
	 *            the md5Version to set
	 */
	public void setMd5Version(int md5Version) {
		this.md5Version = md5Version;

//		System.out.println("MD5Model: md5Version setted!\n");
	}

	/**
	 * @return the commandLine
	 */
	public String getCommandLine() {
		return commandLine;
	}

	/**
	 * @param commandLine
	 *            the commandLine to set
	 */
	public void setCommandLine(String commandLine) {
		this.commandLine = commandLine;

//		System.out.println("MD5Model: commandLine setted!\n");
	}

	/**
	 * @return the numJoints
	 */
	public int getNumJoints() {
		return numJoints;
	}

	/**
	 * Sets the number of joints and the lenght of the array containing joints.
	 * 
	 * @param numJoints
	 *            the numJoints to set
	 */
	public void setNumJoints(int numJoints) {
		this.numJoints = numJoints;
		this.joints = new MD5Joint[numJoints];

//		System.out
//				.println("MD5Model: numJoints setted and joints array initialized with lenght "
//						+ numJoints + " !\n");
	}

	/**
	 * @return the numMeshes
	 */
	public int getNumMeshes() {
		return numMeshes;
	}

	/**
	 * @param numMeshes
	 *            the numMeshes to set
	 */
	public void setNumMeshes(int numMeshes) {
		this.numMeshes = numMeshes;

		// Also initialize the array.
		this.meshes = new MD5Mesh[numMeshes];

//		System.out.println("MD5Model: numMeshes setted!\n");
	}

	/**
	 * @return the joints
	 */
	public MD5Joint[] getJoints() {
		return joints;
	}

	/**
	 * @param joints
	 *            the joints to set
	 */
	public void setJoints(MD5Joint[] joints) {
		this.joints = joints;
	}

	/**
	 * @return the meshes
	 */
	public MD5Mesh[] getMeshes() {
		return meshes;
	}

	/**
	 * @param meshes
	 *            the meshes to set
	 */
	public void setMeshes(MD5Mesh[] meshes) {
		this.meshes = meshes;
	}
	
//	/**
//	 * @return the vertexIndices
//	 */
//	public int[] getVertexIndices() {
//		return vertexIndices;
//	}
//
//	/**
//	 * @param vertexIndices the vertexIndices to set
//	 */
//	public void setVertexIndices(int[] vertexIndices) {
//		this.vertexIndices = vertexIndices;
//	}
	
	//-----------------------------------------------------------------------------------------------------//
	// The following methods are resposible to calculate Vertices positions using Weights and Joints data. //
	//-----------------------------------------------------------------------------------------------------//
	
	// I decided to place this method in MD5Model to avoid adding a reference to MD5Joint[] in MD5Mesh
	// Questa � la magic function
	public void constructMesh(MD5Mesh mesh) {
		// Init vertex indices array
		mesh.setVertexIndices(new int[mesh.getNumTris() * 3]);
		
		// Populate the vertex indices array with a list of all triangles indices
		for (int i = 0, k = 0; i < mesh.getNumTris(); i++) {
			for (int j = 0; j < 3; j++, k++) {
				mesh.getVertexIndices()[k] = mesh.getTriangles()[i].getIndex()[j];
//				System.out.println(mesh.getTriangles()[i].getIndex()[0]);
				mesh.getVertexIndices()[k] = mesh.getTriangles()[i].getIndex()[j];
				mesh.getVertexIndices()[k] = mesh.getTriangles()[i].getIndex()[j];
			}
		}
		
		// Computes vertices positions.
		for (int i = 0; i < mesh.getNumVerts(); i++) {
			Vector3f result = new Vector3f();
			
			// cycle through weights of a vertex and calculate vertex position 
			for (int j = 0; j < mesh.getVertices()[i].getWeightsCount(); j++) {
				MD5Weight weight = mesh.getWeights()[mesh.getVertices()[i].getWeightsStart() + j]; // current weight
				MD5Joint joint = joints[weight.getJoint()];
				
				// Calculate transformed vertex for current weight
				// 
				// (does jME already provide a method to rotate a Vector3f?).
				Vector3f wv = MD5Model.rotatePoint(joint.getOrient(), weight.getPos());
				
				// Sum of all weights bias should be 1
				result.x += (joint.getPos().x + wv.x) * weight.getBias();
				result.y += (joint.getPos().y + wv.y) * weight.getBias();
				result.z += (joint.getPos().z + wv.z) * weight.getBias();
			}
			
			mesh.getVertices()[i].setPosition(result);
		}
	}
	
	// This are methods not implemented by jME.
	// Though it could exist another way to obtain the same result.
	public static Quaternion quatMultVec(Quaternion q, Vector3f v) {
		Quaternion result = new Quaternion();
//NATE		result.w = - (q.x * v.x) - (q.y * v.y) - (q.z * v.z);
//		result.x =   (q.w * v.x) + (q.y * v.z) - (q.z * v.y);
//		result.y =   (q.w * v.x) + (q.y * v.x) - (q.x * v.z);
//		result.z =   (q.w * v.z) + (q.x * v.y) - (q.y * v.x);
		
		float w = - (q.getX() * v.getX()) - (q.getY() * v.getY()) - (q.getZ() * v.getZ());
		float x =   (q.getW() * v.getX()) + (q.getY() * v.getZ()) - (q.getZ() * v.getY());
		float y =   (q.getW() * v.getX()) + (q.getY() * v.getX()) - (q.getX() * v.getZ());
		float z =   (q.getW() * v.getZ()) + (q.getX() * v.getY()) - (q.getY() * v.getX());
		
		result.set( x, y, z, w );
		return result;
	}
	
	public static Quaternion quatMultQuat(Quaternion qa, Quaternion qb) {
		Quaternion result = new Quaternion();
//NATE		result.w = (qa.w * qb.w) - (qa.x * qb.x) - (qa.y * qb.y) - (qa.z * qb.z);
//		result.x = (qa.x * qb.w) + (qa.w * qb.x) + (qa.y * qb.z) - (qa.z * qb.y);
//		result.y = (qa.y * qb.w) + (qa.w * qb.y) + (qa.z * qb.x) - (qa.x * qb.z);
//		result.z = (qa.z * qb.w) + (qa.w * qb.z) + (qa.x * qb.y) - (qa.y * qb.x);
		
		float w = (qa.getW() * qb.getW()) - (qa.getX() * qb.getX()) - (qa.getY() * qb.getY()) - (qa.getZ() * qb.getZ());
		float x = (qa.getX() * qb.getW()) + (qa.getW() * qb.getX()) + (qa.getY() * qb.getZ()) - (qa.getZ() * qb.getY());
		float y = (qa.getY() * qb.getW()) + (qa.getW() * qb.getY()) + (qa.getZ() * qb.getX()) - (qa.getX() * qb.getZ());
		float z = (qa.getZ() * qb.getW()) + (qa.getW() * qb.getZ()) + (qa.getX() * qb.getY()) - (qa.getY() * qb.getX());
		
		result.set( x, y, z, w );
		return result;
	}
	
	// TODO: this should be declared static and moved in some util class.
	public static Vector3f rotatePoint(Quaternion q, Vector3f v) {
		Vector3f result = new Vector3f();
		Quaternion inv = q.inverse();
//NATE		inv.normalize();
		inv.normalizeLocal();
		Quaternion tmp = MD5Model.quatMultVec(q, v);
		Quaternion quatResult = MD5Model.quatMultQuat(tmp, inv);
//NATE		result.x = quatResult.x;
//		result.y = quatResult.y;
//		result.z = quatResult.z;
//		
		result.setX( quatResult.getX() );
		result.setY( quatResult.getY() );
		result.setZ( quatResult.getZ() );
		
//		result = q.toRotationMatrix().mult(v); // try this because I got some strange results :-)
		return result;
	}

	/**
	 * @return the animation
	 */
	public MD5Animation getAnimation() {
		return animation;
	}

	/**
	 * @param animation the animation to set
	 */
	public void setAnimation(MD5Animation animation) {
		this.animation = animation;
	}
	
}
