/**
 * 
 */
package org.md5reader2.parser;

import java.io.IOException;
import java.io.StreamTokenizer;

import org.md5reader2.md5.MD5BoundingBox;
import org.md5reader2.md5.MD5Frame;
import org.md5reader2.md5.MD5Model;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * @author Marco Frisan
 * 
 */
public class MD5AnimParser extends MD5Parser {
	// Double dimensional array to temporary hold animated components data.
	// First dimension is the frame index; second is the component.
	private float[][] frame_components;

	/**
	 * 
	 */
	public MD5AnimParser() {
		super();
	}

	/**
	 * 
	 */
	public MD5AnimParser(MD5Model model) {
		this.model = model;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.md5reader2.parser.MD5Parser#simpleParse(java.io.StreamTokenizer)
	 */
	@Override
	protected void parseSections(StreamTokenizer st, int token)
			throws IOException {
		// TODO: Before the latest modification (before the introduction of
		// animation code)
		// I did not need to verify if "st.sval" was null.
		if (st.sval != null) {
			// System.out.println(st.sval);
			if (st.sval.equals("MD5Version")) {
				token = st.nextToken();
				animation.setMD5Version((int) st.nval);
			} else if (st.sval.equals("commandline")) {
				token = st.nextToken();
				animation.setCommandLine(st.sval);
			} else if (st.sval.equals("numFrames")) {
				token = st.nextToken();
				animation.setNumFrames((int) st.nval);
				animation.setBoundings(new MD5BoundingBox[(int) st.nval]);
			} else if (st.sval.equals("numJoints")) {
				token = st.nextToken();
				animation.setNumJoints((int) st.nval);
				animation.setJointName(new String[(int) st.nval]);
				animation.setJointParent(new int[(int) st.nval]);
				animation.setFlags(new int[(int) st.nval]);
				animation.setStartIndex(new int[(int) st.nval]);
			} else if (st.sval.equals("frameRate")) {
				token = st.nextToken();
				animation.setFrameRate((int) st.nval);
			} else if (st.sval.equals("numAnimatedComponents")) {
				token = st.nextToken();
				animation.setNumAnimatedComponents((int) st.nval);

				// Initializes frame_components according to number of frames
				// and number of animated components.
				this.frame_components = new float[animation.getNumFrames()][animation
						.getNumAnimatedComponents()];
			} else if (st.sval.equals("hierarchy")) {
				token = st.nextToken(); // '{'
				parseHierarchy(st, token);
			} else if (st.sval.equals("bounds")) {
				token = st.nextToken(); // '{'
				parseBounds(st, token);
			} else if (st.sval.equals("baseframe")) {
				token = st.nextToken(); // '{'
				// System.out.println("After \"baseframe\" token, next token is
				// a: " + (char) st.ttype);
				parseBaseFrame(st, token);
			} else if (st.sval.equals("frame")) {
				token = st.nextToken(); // frame number
				// System.out.println("After \"frame\" token, next token is a: "
				// + (int) st.nval);
				parseFrame(st, token);
			}
		}
	}

	private void parseHierarchy(StreamTokenizer st, int token)
			throws IOException {
		int jointIndex = 0; // current joint
		// int pointer = 0; // current token of the current joint

		// First token, should be the name of the joint
		token = st.nextToken();
		// System.out.println(st.sval); // Should print "null".

		while (st.ttype != '}') {
			// For more informations about this code, look
			// the MD5MeshParser.parseJoints() method.
			switch (st.ttype) {
			case '"': // Encountered a " quoted string.
				animation.getJointName()[jointIndex] = st.sval;
				// pointer++;
				break;
			case StreamTokenizer.TT_NUMBER: // Encountered number.
				// if (pointer == 1) {
				animation.getJointParent()[jointIndex] = (int) st.nval;
				// } else if (pointer == 2) {
				token = st.nextToken();
				animation.getFlags()[jointIndex] = (int) st.nval;
				// } else if (pointer == 3) {
				token = st.nextToken();
				animation.getStartIndex()[jointIndex] = (int) st.nval;
				// }
				// pointer++;

				System.out
						.println("Joint "
								+ jointIndex
								+ " name: "
								+ animation.getJointName()[jointIndex]
								+ "; parent: "
								+ animation.getJointParent()[jointIndex]
								+ "; flags: "
								+ animation.getFlags()[jointIndex]
								+ " (binary version: "
								+ Integer
										.toBinaryString(animation.getFlags()[jointIndex] >> 5
												& java.lang.Integer.parseInt(
														"000000", 2))
								+ "); start index: "
								+ animation.getStartIndex()[jointIndex] + ";\n");

				// NOTE: i placed this here instead of in the following case
				// because the parser reads also the first line "bounds {".
				// The first nextToken() call, just before the while loop,
				// sets "st.sval" to null because it finds a "{" (that I setted
				// as ordinaryChar in the initial StreamTokenizer config).
				// After that it enters the while loop, and the first token
				// encountered is a TT_EOL, just before to read the first joint
				// name:
				// this makes "jointIndex" starting at 1 instead of 0 and
				// at the end of the reading it tries to allocate a index 4
				// element of the joints arrays, that causes a
				// ArrayIndexOutOfBoundsException.
				jointIndex++; // Next joint.
				break;
			case StreamTokenizer.TT_EOL: // Encountered end of line.
				// pointer = 0; // Reset position to 0.
				break;
			default:
				break;
			}
			// This is in to the while brackets.
			token = st.nextToken();
		}

		// TODO: This check should not be performed here.
		// till logically connected, in fact, MD5Model and MD5Animation are
		// independent objects, and "model" reference points to nothing (null).
		// So this check would throw a NullPointerException probably.
		// if (!checkAnimation()) throw new java.lang.Exception("Animation has
		// not passed check.");
		// animationIsValid = checkAnimation();
	}

	private void parseBounds(StreamTokenizer st, int token)
			throws IOException {
		int boundIndex = 0; // Current bounding box.
		int boundValue = 0; // Index of the current value.

		// First token should be first value.
		token = st.nextToken();
		// System.out.println(st.sval); // Should print "null"

		while (st.ttype != '}') {

			// For every loop perform following tests.
			// 
			// Token can be (switch cases): 1) a number;
			// 2) an end of line (EOL).
			// 
			// In the case 1), token can be one of the
			// six values of a bound (3 for min and 3
			// for max), so, we test it using a if
			// statement.
			switch (st.ttype) {
			case StreamTokenizer.TT_NUMBER: // Encountered number.
				if (boundValue == 0) {
					// Initialize the current bounding box.
					animation.getBoundings()[boundIndex] = new MD5BoundingBox();

					// Initialize min and max vectors of the current bounding
					// box.
					animation.getBoundings()[boundIndex].setMin(new Vector3f());
					animation.getBoundings()[boundIndex].setMax(new Vector3f());

					// System.out.print(st.nval + " ");

					// Set min.x
					animation.getBoundings()[boundIndex].getMin().x = (float) st.nval;
				}

				else if (boundValue == 1) {
					// System.out.print(st.nval + " ");

					// Set min.y
					animation.getBoundings()[boundIndex].getMin().y = (float) st.nval;
				}

				else if (boundValue == 2) {
					// System.out.print(st.nval + " ");

					// Set min.z
					animation.getBoundings()[boundIndex].getMin().z = (float) st.nval;
				}

				else if (boundValue == 3) {
					// System.out.print(st.nval + " ");

					// Set max.x
					animation.getBoundings()[boundIndex].getMax().x = (float) st.nval;
				}

				else if (boundValue == 4) {
					// Set max.y

					// System.out.print(st.nval + " ");
					animation.getBoundings()[boundIndex].getMax().y = (float) st.nval;
				}

				else if (boundValue == 5) {
					// Set max.z

					// System.out.print(st.nval + "\n");
					animation.getBoundings()[boundIndex].getMax().z = (float) st.nval;

					// System.out.println("Bounding Box "
					// + boundIndex
					// + " Min: "
					// + animation.getBoundings()[boundIndex].getMin()
					// .toString()
					// + "; Max: "
					// + animation.getBoundings()[boundIndex].getMax()
					// .toString() + ";\n");

					boundIndex++; // Next bound.
				}

				boundValue++; // Next value.
				break;

			case StreamTokenizer.TT_EOL: // Encountered end of line.
				// System.out.println("EOL");
				boundValue = 0; // Reset bound value index.
				break;

			default:
				break;

			}

			// This is in to the while brackets.
			token = st.nextToken();
		}

	}

	private void parseBaseFrame(StreamTokenizer st, int token)
			throws IOException {
		// TODO: this method implementation is almost identical to bounds
		// implementation. Maybe is possible to extract a single method.
		
		// Initialize animation base frame.
		animation.setBaseFrame(new MD5Frame());
		
		// A reference to the newly initialized base frame.
		MD5Frame baseFrame = animation.getBaseFrame();
		
		// Initialize base frame positions and rotations arrays.
		baseFrame.setPositions(new Vector3f[animation.getNumJoints()]);
		baseFrame.setRotations(new Quaternion[animation.getNumJoints()]);
		
		int jointIndex = 0; // Current joint index.
		int componentIndex = 0; // Current value of the 6 components stored in *md5anim file for each joint.

		// First token should be first value.
		token = st.nextToken();
		// System.out.println(st.sval); // Should print "null"
		
		while (st.ttype != '}') {
			switch (st.ttype) {
			case StreamTokenizer.TT_NUMBER: // Encountered number.
				// Compute positions;
				if (componentIndex == 0) {
					// Initialize the joint position with a (0, 0, 0) vector.
					baseFrame.getPositions()[jointIndex] = new Vector3f();

					// Set position x.
					baseFrame.getPositions()[jointIndex].x = (float) st.nval;
				}
				
				else if (componentIndex == 1) {
					// Set position y.
					baseFrame.getPositions()[jointIndex].y = (float) st.nval;
				}
				
				else if (componentIndex == 2) {
					// Set position z.
					baseFrame.getPositions()[jointIndex].z = (float) st.nval;
				}

				// Compute rotations;
				else if (componentIndex == 3) {
					// Initialize the joint rotation with a (0, 0, 0, 1) vector.
					baseFrame.getRotations()[jointIndex] = new Quaternion();

					// Set rotation x.
//NATE					baseFrame.getRotations()[jointIndex].x = (float) st.nval;
					baseFrame.getRotations()[jointIndex].set( (float) st.nval, 
															  baseFrame.getRotations()[jointIndex].getY(), 
															  baseFrame.getRotations()[jointIndex].getZ(), 
															  baseFrame.getRotations()[jointIndex].getW() );
				}

				else if (componentIndex == 4) {
					// Set rotation y
//NATE					baseFrame.getRotations()[jointIndex].y = (float) st.nval;
					baseFrame.getRotations()[jointIndex].set( baseFrame.getRotations()[jointIndex].getX(), 
							  (float) st.nval, 
							  baseFrame.getRotations()[jointIndex].getZ(), 
							  baseFrame.getRotations()[jointIndex].getW() );
				}

				else if (componentIndex == 5) {
					// Set rotation z
//NATE					baseFrame.getRotations()[jointIndex].z = (float) st.nval;
					baseFrame.getRotations()[jointIndex].set( baseFrame.getRotations()[jointIndex].getX(), 
							  baseFrame.getRotations()[jointIndex].getY(), 
							  (float) st.nval, 
							  baseFrame.getRotations()[jointIndex].getW() );

					// Compute and assign w component
					computeW(baseFrame.getRotations()[jointIndex]);

					jointIndex++; // Next joint index.
				}

				componentIndex++; // Next component index.
				break;

			case StreamTokenizer.TT_EOL: // Encountered end of line.
				componentIndex = 0; // Next line = next joint; then component index restarts from 0.
				break;

			default:
				break;
			}

			token = st.nextToken(); // This is in to the while loop brackets.
		}
		
		// Convert baseframe transformations to Model Space coordinates.
//		for (int i = 0; i < this.animation.getNumJoints(); i++) {
//			Vector3f jointPos = baseFrame.getPositions()[i];
//			Quaternion jointRot = baseFrame.getRotations()[i];
//			// Get parent index of the current joint.
//			int parentIndex = model.getJoints()[i].getParent();
//			
//			if (parentIndex >= 0) {
//				Vector3f parentPos = baseFrame.getPositions()[parentIndex]; // Parent position.
//				Quaternion parentRot = baseFrame.getRotations()[parentIndex]; // Parent rotation.
//				
//				// Rotates joint position vector accordingly to its parent
//				// rotation.
//				
//				// Rotated position
//				//Vector3f jointRotatedPos = parentRot.toRotationMatrix().mult(currJointPos);
//				Vector3f jointRotatedPos = MD5Model.rotatePoint(parentRot, jointPos);
//				// Sum joint position to parent position.
//				jointPos.x = jointRotatedPos.x + parentPos.x;
//				jointPos.y = jointRotatedPos.y + parentPos.y;
//				jointPos.z = jointRotatedPos.z + parentPos.z;
//				
//				// Concatenates parent and this joint rotations.
//				jointRot = parentRot.mult(jointRot);
//				// Normalize this joint rotation.
//				jointRot.normalize();
//			}
//		}
	}

	private void parseFrame(StreamTokenizer st, int token) throws IOException {
		int currentFrameIndex = (int) st.nval;

		// Create a reference to current frame and initialize it with the values
		// of base frame.
		MD5Frame currentFrame = animation.getFrames()[currentFrameIndex];
		currentFrame.setPositions(new Vector3f[animation.getNumJoints()]);
		currentFrame.setRotations(new Quaternion[animation.getNumJoints()]);
		MD5Frame baseFrame = animation.getBaseFrame();
		for (int j = 0; j < animation.getNumJoints(); j++) {
			currentFrame.getPositions()[j] = new Vector3f(
					baseFrame.getPositions()[j].x,
					baseFrame.getPositions()[j].y,
					baseFrame.getPositions()[j].z);
			currentFrame.getRotations()[j] = new Quaternion(
					baseFrame.getRotations()[j].getX(),
					baseFrame.getRotations()[j].getY(),
					baseFrame.getRotations()[j].getZ(),
					baseFrame.getRotations()[j].getW());
		}

		token = st.nextToken(); // '{'
		// System.out.println("After frame number token, next token is a: " +
		// (char) st.ttype);

		// I could simply use EOL to know when a joint components are finished
		// but I think it is a little unsafe to relay on the exporter
		// capabilities.

		// Index of the current frame component.
		int currentComponent = 0;

		// Loads frame section content in to frame_components array.
		while (st.ttype != '}') { // Exits when encounters a '}' char.
			switch (st.ttype) {
			case StreamTokenizer.TT_NUMBER:
				frame_components[currentFrameIndex][currentComponent] = (float) st.nval;

				currentComponent++; // Next component index.
				break;
			}

			st.nextToken(); // Always at the end of the while() loop body.
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.md5reader2.parser.MD5Parser#finalizeParsing(java.io.StreamTokenizer,
	 *      int)
	 */
	@Override
	protected void finalizeParsing(StreamTokenizer st, int token) {
		for (int i = 0; i < animation.getNumFrames(); i++) {
			// TODO: This could be called at the end of parseFrames() method. It
			// is safe because buildFrame() method computes a single frame at
			// once and parseFrames already has a variable that holds current
			// frame index, that can be passedd as a parameter.
			buildFrame(i);
		}
		
		// Add the parsed animation to the model passed to the MD5AnimParser constructor.
		//model.setAnimation(animation);
	}

	/**
	 * Builds all frames data collecting informations from the joints flags.
	 */
	private void buildFrame(int frameIndex) {
		// A reference to the current frame.
		MD5Frame currFrame = animation.getFrames()[frameIndex];
		
		// Convenience lookup table for binary comparison with frame flags.
		// Accordingly to Dave's specification page (also available in doc
		// directory), flags must be read starting from right. So, first lookup
		// table value is 000001.
		int[] flagsTable = { Integer.valueOf("000001", 2),
				Integer.valueOf("000010", 2), Integer.valueOf("000100", 2),
				Integer.valueOf("001000", 2), Integer.valueOf("010000", 2),
				Integer.valueOf("100000", 2) };
		
		int componentIndex = 0;
		
		// For each joint ...
		for (int i = 0; i < this.animation.getNumJoints(); i++) {
			// Gets the flags of the current joint.
			int flags = this.animation.getFlags()[i];

			// Temp variables that reference to current joint position and rotation.
			Vector3f currJointPos = currFrame.getPositions()[i];
			Quaternion currJointRot = currFrame.getRotations()[i];
			
			// Substitutes frame components accordingly to flags informations.
			// Flags are 6 bits to be read from right to left. If a flag is set
			// to 1, it means that the corresponding frame component changes
			// during animation, then it have to be substituted with frame
			// section corresponding value. Else, if flag is set to 0, frame
			// component does not change and remain equal to the base frame
			// corresponding component value.
			for (int j = 0; j < 6; j++) {
				switch (flags & flagsTable[j]) { // Use binary & comparison.
				case 0: // It means that flag is not active; ...
					// ... then do not substitute anything.
					break;

				// Frames have already been initialized with the values taken
				// from base frame inside the parseFrames() method.
				// Now we are going to substitute only the frame components that
				// actually changed relative to base frame.
				// 
				// Any value > 0, means that the flag is active.
				// So, we can use 'default' case to handle all values that are
				// greater then 0.
				default:
					switch (j) {
					case 0: // First flag is active.
//						System.out.print("Tx ");
						currJointPos.x = frame_components[frameIndex][componentIndex];
						componentIndex++; // Next component index.
						break;
					case 1: // Second flag is active.
//						System.out.print("Ty ");
						currJointPos.y = frame_components[frameIndex][componentIndex];
						componentIndex++; // Next component index.
						break;
					case 2: // Third flag is active.
//						System.out.print("Tz ");
						currJointPos.z = frame_components[frameIndex][componentIndex];
						componentIndex++; // Next component index.
						break;
					case 3: // Fourth flag is active.
//						System.out.print("Qx ");
//NATE						currJointRot.x = frame_components[frameIndex][componentIndex];
						currJointRot.set( frame_components[frameIndex][componentIndex],
										  currJointRot.getY(),
										  currJointRot.getZ(),
										  currJointRot.getW() );
						componentIndex++; // Next component index.
						break;
					case 4: // Fifth flag is active.
//						System.out.print("Qy ");
//NATE						currJointRot.y = frame_components[frameIndex][componentIndex];
						currJointRot.set( currJointRot.getX(),
								  frame_components[frameIndex][componentIndex],
								  currJointRot.getZ(),
								  currJointRot.getW() );
						
						componentIndex++; // Next component index.
						break;
					case 5: // Sixth flag is active.
//						System.out.print("Qz");
//NATE						currJointRot.z = frame_components[frameIndex][componentIndex];
						currJointRot.set( currJointRot.getX(),
								  currJointRot.getY(),
								  frame_components[frameIndex][componentIndex],
								  currJointRot.getW() );

						
						componentIndex++; // Next component index.
						break;
					}

					break;
				}
			}
			
			// Recomputes W.
			computeW(currFrame.getRotations()[i]);
			
			
			// VERY IMPORTANT!, our implementation is not like David's one.
			// David stores each computed frame informations in to a different
			// skeleton instance. In David's implementations frames are not used
			// to hold translations and rotations data.
			// We, instead, use a single skeleton for each model and animation
			// couple and store frames data in several MD5Frame instances.
			// 
			// All frames transformations are stored in joint local space (i.e.
			// current joint parent space, root joint parent is model).
			// In our skeleton implementation, joint nodes are not parented each
			// other, they are all children of model (i.e. skeleton).
			// Then we need to convert frame sections transformations to model
			// space.
			//
			// Remember that this approach avoids any Rag-Doll algorithm
			// implementation.
			
			
			// As in David Hanry's code...
			/* NOTE: we assume that this joint's parent has
			already been calculated, i.e. joint's ID should
			never be smaller than its parent ID. */
			
			// Get parent index of the current joint.
			int currJointParent = model.getJoints()[i].getParent();
			
			if (currJointParent < 0) {
				currJointPos = new Vector3f(0f, 0f, 0f);
				currJointRot = new Quaternion();
			} else {
				Vector3f parentPos = currFrame.getPositions()[currJointParent]; // Parent position.
				Quaternion parentRot = currFrame.getRotations()[currJointParent]; // Parent rotation.
				
				// Rotates point (TODO: in the future try to use the
				// rotatePoint() method, currently defined in MD5Model)
				// Rotates joint position vector accordingly to its parent
				// rotation.
				Vector3f jointRotatedPos = parentRot.toRotationMatrix().mult(currJointPos); // Rotated position
				// Sum joint position to parent position.
				currJointPos.x = jointRotatedPos.x + parentPos.x;
				currJointPos.y = jointRotatedPos.y + parentPos.y;
				currJointPos.z = jointRotatedPos.z + parentPos.z;
				
				// Concatenates parent and this joint rotations.
				currJointRot = parentRot.mult(currJointRot);
				// Normalize this joint rotation.
//NATE				currJointRot.normalize();
				currJointRot.normalizeLocal();
				
				// Now frames are prepared.
			}
			
			// Debug.
			//System.out.println("");
		}
	}

	// private boolean checkAnimation() {
	//
	// return true;
	// }

}
