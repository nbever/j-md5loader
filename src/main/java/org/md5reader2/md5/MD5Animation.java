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
public class MD5Animation {
	private int md5Version;
	private String commandLine;
	private int numFrames;
	private int numJoints;
	private int frameRate;
	private int numAnimatedComponents;

	// Others joint array variables, can be useful...
	private String[] jointName;
	private int[] jointParent;

	// I store in this arrays the flags and start indices data for each joint.
	// TODO: flags could be stored also in a byte array.
	private int[] flags;
	private int[] startIndex;

	private MD5BoundingBox[] boundings;
	private MD5Frame baseFrame;

	// I try a implementation of a per frames architecture like in MD5 format.
	// For more info read MD5Frame documentation.
	//
	// This variable stores all the frames associated with this animation.
	private MD5Frame[] frames;

	/**
	 * 
	 */
	public MD5Animation() {
		super();
	}

	/**
	 * @return the numFrames
	 */
	public int getNumFrames() {
		return numFrames;
	}

	/**
	 * Initializes the number of frames of this <code>MD5Animation</code>.
	 * Note that it also initialize the <code>frames Vector</code>: every
	 * previous stored <code>frames</code> will be discarded.
	 * 
	 * @param numFrames
	 *            the numFrames to set
	 */
	public void setNumFrames(int numFrames) {
		this.numFrames = numFrames;
		
		// TODO: maybe it will be better, in the future, to add another setter
		// that do not discard previous frames.
		
		// Initialize frames array.
		this.frames = new MD5Frame[numFrames];
		
		for (int f = 0; f < numFrames; f++) {
			// Prepares positions and rotations Vector objects and set their
			// sizes.
			Vector3f[] positions = new Vector3f[numJoints];
			Quaternion[] rotations = new Quaternion[numJoints];
			// TODO: we could initialize also positions and rotations elements
			// with Zero vectors and quaternions.
			
			// Initialize a variable to temporary hold the current frames.
			MD5Frame currentFrame = new MD5Frame();
			
			// Sets current frames rotations and positions to previously prepared
			// Vector objects.
			currentFrame.setPositions(positions);
			currentFrame.setRotations(rotations);
			
			// Adds currentFrame to this MD5Animation frames Vector.
			this.frames[f] = currentFrame;
		}
	}

	/**
	 * @return the numJoints
	 */
	public int getNumJoints() {
		return numJoints;
	}

	/**
	 * @param numJoints
	 *            the numJoints to set
	 */
	public void setNumJoints(int numJoints) {
		this.numJoints = numJoints;
	}

	/**
	 * @return the frameRate
	 */
	public int getFrameRate() {
		return frameRate;
	}

	/**
	 * @param frameRate
	 *            the frameRate to set
	 */
	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}

	/**
	 * @return the frames
	 */
	public MD5Frame[] getFrames() {
		return frames;
	}

	/**
	 * @param frames
	 *            the frames to set
	 */
	public void setFrames(MD5Frame[] frames) {
		this.frames = frames;
	}

	/**
	 * @return the flags
	 */
	public int[] getFlags() {
		return flags;
	}

	/**
	 * @param flags
	 *            the flags to set
	 */
	public void setFlags(int[] flags) {
		this.flags = flags;
	}

	/**
	 * @return the startIndex
	 */
	public int[] getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex
	 *            the startIndex to set
	 */
	public void setStartIndex(int[] startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the mD5Version
	 */
	public int getMD5Version() {
		return this.md5Version;
	}

	/**
	 * @param version
	 *            the mD5Version to set
	 */
	public void setMD5Version(int version) {
		this.md5Version = version;
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
	}

	/**
	 * @return the numAnimatedComponents
	 */
	public int getNumAnimatedComponents() {
		return numAnimatedComponents;
	}

	/**
	 * @param numAnimatedComponents
	 *            the numAnimatedComponents to set
	 */
	public void setNumAnimatedComponents(int numAnimatedComponents) {
		this.numAnimatedComponents = numAnimatedComponents;
	}

	/**
	 * @return the jointName
	 */
	public String[] getJointName() {
		return jointName;
	}

	/**
	 * @param jointName
	 *            the jointName to set
	 */
	public void setJointName(String[] jointName) {
		this.jointName = jointName;
	}

	/**
	 * @return the jointParent
	 */
	public int[] getJointParent() {
		return jointParent;
	}

	/**
	 * @param jointParent
	 *            the jointParent to set
	 */
	public void setJointParent(int[] jointParent) {
		this.jointParent = jointParent;
	}

	/**
	 * @return the boundings
	 */
	public MD5BoundingBox[] getBoundings() {
		return boundings;
	}

	/**
	 * @param boundings
	 *            the boundings to set
	 */
	public void setBoundings(MD5BoundingBox[] boundings) {
		this.boundings = boundings;
	}

	/**
	 * @return the baseFrame
	 */
	public MD5Frame getBaseFrame() {
		return baseFrame;
	}

	/**
	 * @param baseFrame the baseFrame to set
	 */
	public void setBaseFrame(MD5Frame baseFrame) {
		this.baseFrame = baseFrame;
	}

}
