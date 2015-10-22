/**
 * 
 */
package org.md5reader2.md5;

/**
 * @author Marco Frisan
 * 
 */
public class MD5Mesh {
	private String shader;
	private int numVerts;
	private int numTris;
	private int numWeights;

	private MD5Vertex[] vertices;
	private MD5Triangle[] triangles;
	private MD5Weight[] weights;
	
	private int[] vertexIndices;

	/**
	 * 
	 */
	public MD5Mesh() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the shader
	 */
	public String getShader() {
		return shader;
	}

	/**
	 * @param shader
	 *            the shader to set
	 */
	public void setShader(String shader) {
		this.shader = shader;
	}

	/**
	 * @return the numVerts
	 */
	public int getNumVerts() {
		return numVerts;
	}

	/**
	 * @param numVerts
	 *            the numVerts to set
	 */
	public void setNumVerts(int numVerts) {
		this.numVerts = numVerts;
	}

	/**
	 * @return the numTris
	 */
	public int getNumTris() {
		return numTris;
	}

	/**
	 * @param numTris
	 *            the numTris to set
	 */
	public void setNumTris(int numTris) {
		this.numTris = numTris;
	}

	/**
	 * @return the numWeights
	 */
	public int getNumWeights() {
		return numWeights;
	}

	/**
	 * @param numWeights
	 *            the numWeights to set
	 */
	public void setNumWeights(int numWeights) {
		this.numWeights = numWeights;
	}

	/**
	 * @return the vertices
	 */
	public MD5Vertex[] getVertices() {
		return vertices;
	}

	/**
	 * @param vertices
	 *            the vertices to set
	 */
	public void setVertices(MD5Vertex[] vertices) {
		this.vertices = vertices;
	}

	/**
	 * @return the weights
	 */
	public MD5Weight[] getWeights() {
		return weights;
	}

	/**
	 * @param weights
	 *            the weights to set
	 */
	public void setWeights(MD5Weight[] weights) {
		this.weights = weights;
	}
	
	/**
	 * @return the triangles
	 */
	public MD5Triangle[] getTriangles() {
		return triangles;
	}

	/**
	 * @param triangles the triangles to set
	 */
	public void setTriangles(MD5Triangle[] triangles) {
		this.triangles = triangles;
	}

	/**
	 * @return the vertexIndices
	 */
	public int[] getVertexIndices() {
		return vertexIndices;
	}

	/**
	 * @param vertexIndices the vertexIndices to set
	 */
	public void setVertexIndices(int[] vertexIndices) {
		this.vertexIndices = vertexIndices;
	}

//	public void constructMesh() {
//		// Init vertex indices array
//		int[] vertexIndices = new int[this.numTris * 3];
//		
//		// Populate the vertex indices array with a list of all triangles indices
//		for (int i = 0, k = 0; i < this.numTris; i++) {
//			for (int j = 0; j < 3; j++, k++) {
////				System.out.println("Test i: " + i + "; test k: " + k);
//				vertexIndices[k] = this.triangles[i].getIndex()[j];
//			}
//		}
//		
//		// Computes vertices positions.
//		for (int i = 0; i < this.numVerts; i++) {
//			Vector3f result = new Vector3f();
//			
//			// cycle through weights
//			for (int j = 0; j < this.getVertices()[i].getWeightsCount(); j++) {
//				MD5Weight weight = this.getWeights()[this.getVertices()[i].getWeightsStart() + j]; // current weight
//				MD5Joint joint = 
//			}
//		}
//	}
	
}
