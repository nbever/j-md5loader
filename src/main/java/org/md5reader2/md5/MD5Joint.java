package org.md5reader2.md5;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * @author Marco Frisan
 * 
 */
public class MD5Joint {
	private String name;
	private int parent;

	// This 2 variables contain the current transformation of the joint.
	private Vector3f pos = new Vector3f();
	private Quaternion orient = new Quaternion();

	// Bind pose transformations were stored in each joint into "pos" and
	// "orient" variables.
	// This was not correct. "pos" and "orient" variables should be used as the
	// current (as to say temp)
	// transformation of this joint.
	// So, I added "bindPosePos" and "bindPoseOrient" variables to permanently
	// store bind pose
	// data for this joint.
	// applyBindPose method have been provided to copy bind pose data to "pos"
	// and "orient".
	private Vector3f bindPosePos = new Vector3f();
	private Quaternion bindPoseOrient = new Quaternion();

	// Animation informations
	// TODO: this data can be confusing if we have more than one animation
	// private int flags;
	// private int startIndex;
	// I try to store this data in MD5Animation

	public MD5Joint() {
		this("Unnamed joint");
	}

	public MD5Joint(String name) {
		this.name = name;

//		System.out.println("MD5Joint: Created new MD5Joint \"" + name + "\"");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parent
	 */
	public int getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(int parent) {
		this.parent = parent;

//		System.out.println("MD5Joint: parent of MD5Joint \"" + name
//				+ "\" setted to " + parent);
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
		// System.out.println("Position vector setted to: " + pos.toString());
	}

	/**
	 * @return the orient
	 */
	public Quaternion getOrient() {
		return orient;
	}

	/**
	 * @param orient
	 *            the orient to set
	 */
	public void setOrient(Quaternion orient) {
		this.orient = orient;
	}

	/**
	 * @return the bindPosePos
	 */
	public Vector3f getBindPosePos() {
		return bindPosePos;
	}

	/**
	 * @param bindPosePos
	 *            the bindPosePos to set
	 */
	public void setBindPosePos(Vector3f bindPosePos) {
		this.bindPosePos = bindPosePos;
	}

	/**
	 * @return the bindPoseOrient
	 */
	public Quaternion getBindPoseOrient() {
		return bindPoseOrient;
	}

	/**
	 * @param bindPoseOrient
	 *            the bindPoseOrient to set
	 */
	public void setBindPoseOrient(Quaternion bindPoseOrient) {
		this.bindPoseOrient = bindPoseOrient;
	}

	/**
	 * This method is called internally by the MD5MeshParser to actually
	 * initialize current transformation of the joint to the bind pose values.
	 */
	public void applyBindPose() {
		this.pos.x = this.bindPosePos.x;
		this.pos.y = this.bindPosePos.y;
		this.pos.z = this.bindPosePos.z;
		
		this.orient.set( this.bindPoseOrient.getX(), this.bindPoseOrient.getY(), this.bindPoseOrient.getZ(), this.bindPoseOrient.getW() );
//		this.orient.w = this.bindPoseOrient.w;
//		this.orient.x = this.bindPoseOrient.x;
//		this.orient.y = this.bindPoseOrient.y;
//		this.orient.z = this.bindPoseOrient.z;
	}

}
