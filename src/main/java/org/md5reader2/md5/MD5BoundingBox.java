/**
 * 
 */
package org.md5reader2.md5;

import com.jme3.math.Vector3f;

/**
 * @author Marco Frisan
 * 
 */
public class MD5BoundingBox {
	private Vector3f min;
	private Vector3f max;

	/**
	 * 
	 */
	public MD5BoundingBox() {
		super();
	}

	/**
	 * @return the min
	 */
	public Vector3f getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(Vector3f min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public Vector3f getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(Vector3f max) {
		this.max = max;
	}

}
