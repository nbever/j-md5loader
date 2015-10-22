/**
 * 
 */
package org.md5reader2.md5;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * @author Marco Frisan
 * 
 */
public class MD5Vertex {

	private Vector2f uvCoords;
	private int weightsStart;
	private int weightsCount;
	private Vector3f position;

	/**
	 * 
	 */
	public MD5Vertex() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the uvCoords
	 */
	public Vector2f getUvCoords() {
		return uvCoords;
	}

	/**
	 * @param uvCoords
	 *            the uvCoords to set
	 */
	public void setUvCoords(Vector2f uvCoords) {
		this.uvCoords = uvCoords;
	}

	/**
	 * @return the weightsStart
	 */
	public int getWeightsStart() {
		return weightsStart;
	}

	/**
	 * @param weightsStart
	 *            the weightsStart to set
	 */
	public void setWeightsStart(int weightsStart) {
		this.weightsStart = weightsStart;
	}

	/**
	 * @return the weightsCount
	 */
	public int getWeightsCount() {
		return weightsCount;
	}

	/**
	 * @param weightsCount
	 *            the weightsCount to set
	 */
	public void setWeightsCount(int weightsCount) {
		this.weightsCount = weightsCount;
	}

	/**
	 * @return the position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
}
