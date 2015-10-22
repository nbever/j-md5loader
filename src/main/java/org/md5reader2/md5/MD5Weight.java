/**
 * 
 */
package org.md5reader2.md5;

import com.jme3.math.Vector3f;

/**
 * @author Marco Frisan
 * 
 */
public class MD5Weight {
	private int joint;
	private float bias;
	private Vector3f pos;

	/**
	 * 
	 */
	public MD5Weight() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the joint
	 */
	public int getJoint() {
		return joint;
	}

	/**
	 * @param joint
	 *            the joint to set
	 */
	public void setJoint(int joint) {
		this.joint = joint;
	}

	/**
	 * @return the bias
	 */
	public float getBias() {
		return bias;
	}

	/**
	 * @param bias
	 *            the bias to set
	 */
	public void setBias(float bias) {
		this.bias = bias;
	}

	/**
	 * @return the pos
	 */
	public Vector3f getPos() {
		return pos;
	}

	/**
	 * @param pos
	 *            the pos to set
	 */
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

}
