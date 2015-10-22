package org.md5reader2.parser;

import java.io.IOException;
import java.io.StreamTokenizer;

import org.md5reader2.md5.MD5Joint;
import org.md5reader2.md5.MD5Mesh;
import org.md5reader2.md5.MD5Triangle;
import org.md5reader2.md5.MD5Vertex;
import org.md5reader2.md5.MD5Weight;


import com.jme3.math.Quaternion;
//import com.jme.animation.Bone;
//import com.jme.math.FastMath;
//import com.jme.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
//import com.jme.scene.Node;

public class MD5MeshParser extends MD5Parser {
	private int currentMesh = 0; // Meshes can be more than 1. So, I init this counter to take note of the current mesh
	
	public MD5MeshParser() {
		super();
	}

	@Override
	protected void parseSections(StreamTokenizer st, int token)
			throws IOException {
		if (st.sval != null) { // alternativa, posso controllare usando
			// st.ttype
			
			if (st.sval.equals("MD5Version")) {
//				System.out.print("Key Name: " + st.sval + "; ");
				token = st.nextToken();
//				System.out.println("Key Value: " + (int) st.nval);
				model.setMd5Version((int) st.nval);
			}

			else if (st.sval.equals("commandline")) {
//				System.out.print("Key Name: " + st.sval + "; ");
				token = st.nextToken();
//				System.out.println("Key Value: " + st.sval);
				model.setCommandLine(st.sval);
			}

			else if (st.sval.equals("numJoints")) {
//				System.out.print("Key Name: " + st.sval + "; ");
				token = st.nextToken();
//				System.out.println("Key Value: " + (int) st.nval);

				model.setNumJoints((int) st.nval); // It also inits joints
				// lenght.
			}

			else if (st.sval.equals("numMeshes")) {
//				System.out.print("Key Name: " + st.sval + "; ");
				token = st.nextToken();
//				System.out.println("Key Value: " + (int) st.nval);
				model.setNumMeshes((int) st.nval);
				
				// init meshes
				model.setMeshes(new MD5Mesh[model.getNumMeshes()]);
				for (int i = 0; i < model.getNumMeshes(); i++) {
					model.getMeshes()[i] = new MD5Mesh();
				}
			}

			else if (st.sval.equals("joints")) {
//				System.out.print("Key Name: " + st.sval + "; ");
				token = st.nextToken();
//				System.out.println("Key Value: " + st.sval);

				// Parses joints and stores data in to MD5Joint object.
				parseJoints(st, token);
			}

			else if (st.sval.equals("mesh")) {
//				System.out.print("Key Name: " + st.sval + "; ");
				token = st.nextToken();
//				System.out.println("Key Value: " + st.sval);
				
				parseMeshes(st, token, currentMesh);
				currentMesh++;
			}
			// else {
			// return;
			// }
		}

	}

	private void parseJoints(StreamTokenizer st, int token) throws IOException {
		int jointIndex = 0; // Current joint index.
		int pointer = 0; // A index to know which of the 7 values of the
		// current joint we are reading.

		// First token, in the joints section, should be the name of the first
		// joint.
		token = st.nextToken();
		// Loops till it finds a '}' character
		while (st.ttype != '}') {

			switch (st.ttype) {

			// This means parser encountered a joint name. We configured '"' as
			// quotes.
			// StreamTokenizer sets ttype to the char value of the quote char
			// when finds a quoted string.
			// 
			// At the end of this case block pointer is incremented by 1.
			case '"':
//				System.out.println("Joint name (position " + pointer + "): "
//						+ st.sval);

				// Code initialize new MD5Joint and sets its name.
				model.getJoints()[jointIndex] = new MD5Joint(st.sval);

				pointer++;
				break;

			// This case is verified for all position vectors and rotation
			// quaternion coordinates.
			// 
			// At the end of this case pointer is incremented by 1.
			case StreamTokenizer.TT_NUMBER:
				// code to set joints values ...

				// Parsing joint name
				if (pointer == 1) {
					// sets parent index, the value must be casted to int
					model.getJoints()[jointIndex].setParent((int) st.nval);
				}

				// Parsing position vector.
				else if (pointer >= 2 && pointer <= 4) {
					// sets position vector, values must be casted to float

					// TODO: Questo potrebbe essere inizializzato direttamente
					// in MD5Joint
					// model.getJoints()[jointIndex].setBindPosePos(new
					// Vector3f());

					// Sets values
					model.getJoints()[jointIndex].getBindPosePos().x = (float) st.nval;
//					System.out.println(" @ (position = " + pointer + ") "
//							+ st.nval);

					// Inside this condition statement all the 3 values of the
					// joint
					// position vector are traveled. So we need to increment the
					// pointer
					// value to match the current position.
					pointer++;
					token = st.nextToken();

					model.getJoints()[jointIndex].getBindPosePos().y = (float) st.nval;
//					System.out.println(" @ (position = " + pointer + ") "
//							+ st.nval);

					pointer++;
					token = st.nextToken();

					model.getJoints()[jointIndex].getBindPosePos().z = (float) st.nval;
//					System.out.println(" @ (position = " + pointer + ") "
//							+ st.nval);

//					System.out.println("Position vector setted to: "
//							+ model.getJoints()[jointIndex].getBindPosePos()
//									.toString());

				}

				// Parsing rotation quaternion.
				else if (pointer >= 5) {
					// sets orientation quaternion, values must be casted to
					// float

					// TODO: Questo potrebbe essere inizializzato direttamente
					// in MD5Joint
					// model.getJoints()[jointIndex].setBindPoseOrient(new
					// Quaternion());

					// Sets quaternion components, values must be casted to
					// float
//NATE					model.getJoints()[jointIndex].getBindPoseOrient().x = (float) st.nval;
					
					Quaternion quat = model.getJoints()[jointIndex].getBindPoseOrient();
					
					model.getJoints()[jointIndex].getBindPoseOrient().set( (float) st.nval,
							quat.getY(),
							quat.getZ(),
							quat.getW() );
					
//					System.out.println(" @ (position = " + pointer + ") "
//							+ st.nval);

					pointer++;
					token = st.nextToken();

//NATE					model.getJoints()[jointIndex].getBindPoseOrient().y = (float) st.nval;
					model.getJoints()[jointIndex].getBindPoseOrient().set( quat.getX(),
							(float) st.nval,
							quat.getZ(),
							quat.getW() );
//					System.out.println(" @ (position = " + pointer + ") "
//							+ st.nval);

					pointer++;
					token = st.nextToken();

//NATE					model.getJoints()[jointIndex].getBindPoseOrient().z = (float) st.nval;
					
					model.getJoints()[jointIndex].getBindPoseOrient().set( quat.getX(),
							quat.getY(),
							(float) st.nval,
							quat.getW() );
					
//					System.out.println(" @ (position = " + pointer + ") "
//							+ st.nval);

					// Computes w component.
					// NOTE: I moved this method to the superclass MD5Parser
					// because it is needed also by MD5AnimParser.
					computeW(model.getJoints()[jointIndex].getBindPoseOrient());

//					System.out.println("Rotation quaternion setted to: "
//							+ model.getJoints()[jointIndex].getBindPoseOrient()
//									.toString());

					// Then apply the pos and orientation values to the current
					// position and orientation of the joint.
					model.getJoints()[jointIndex].applyBindPose();
//					System.out
//							.println("Position vector setted to: "
//									+ model.getJoints()[jointIndex].getPos()
//											.toString());
//					System.out.println("Position vector setted to: "
//							+ model.getJoints()[jointIndex].getOrient()
//									.toString());

					jointIndex++;

					break;

				}

				pointer++;
				break;

			// Reached end of line.
			case StreamTokenizer.TT_EOL:
				// Reset position to 0.
				pointer = 0;

				break;

			default:
				break;

			}

			// This is in to the while brackets.
			token = st.nextToken();

		}

//		System.out.println("\n\nEnd of joints.");

	}

	private void parseMeshes(StreamTokenizer st, int token, int currentMesh) throws IOException {
//		System.out.println(st.ttype); // Returns 123, ASCII code of {
		
		token = st.nextToken(); // move to the first significant token, usaly shader
		
		// Loops till it finds a '}' character
		while (st.ttype != '}') {
			switch (st.ttype) {
			case StreamTokenizer.TT_WORD:
				if (st.sval.equals("shader")) {
//					System.out.println(st.sval);
					st.nextToken();
//					System.out.println(st.sval);
					model.getMeshes()[currentMesh].setShader(st.sval);
//					System.out.println(model.getMeshes()[currentMesh].getShader());
				} else if (st.sval.equals("numverts")) {
//					System.out.println(st.sval);
					st.nextToken();
					model.getMeshes()[currentMesh].setNumVerts((int) st.nval);
					model.getMeshes()[currentMesh].setVertices(new MD5Vertex[(int) st.nval]);
//					System.out.println("Number of vertices: " + model.getMeshes()[currentMesh].getNumVerts());
				} else if (st.sval.equals("vert")) {
//					System.out.println(st.sval);
					st.nextToken();
					int index = (int) st.nval;
					
					Vector2f uv = new Vector2f();
					
					st.nextToken(); // Bracket (
					st.nextToken();
					uv.x = (float) st.nval;
					st.nextToken();
					uv.y = (float) st.nval;
					
					st.nextToken(); // Bracket )
					st.nextToken();
					int start = (int) st.nval;
					st.nextToken();
					int count = (int) st.nval;
//					System.out.println("Vertex: " + index + "; UV: " + uv + "; start: " + start + "; count: " + count);
					
					model.getMeshes()[currentMesh].getVertices()[index] = new MD5Vertex();
					model.getMeshes()[currentMesh].getVertices()[index].setUvCoords(uv);
					model.getMeshes()[currentMesh].getVertices()[index].setWeightsStart(start);
					model.getMeshes()[currentMesh].getVertices()[index].setWeightsCount(count);
				} else if (st.sval.equals("numtris")) {
//					System.out.println(st.sval);
					st.nextToken();
					model.getMeshes()[currentMesh].setNumTris((int) st.nval);
					model.getMeshes()[currentMesh].setTriangles(new MD5Triangle[(int) st.nval]);
					
//					System.out.println("Number of Triangles: " + model.getMeshes()[currentMesh].getTriangles().length);
				} else if (st.sval.equals("tri")) {
//					System.out.println(st.sval);
					st.nextToken();
					int index = (int) st.nval;
					st.nextToken();
					int a = (int) st.nval;
					st.nextToken();
					int b = (int) st.nval;
					st.nextToken();
					int c = (int) st.nval;
					
					model.getMeshes()[currentMesh].getTriangles()[index] = new MD5Triangle();
					model.getMeshes()[currentMesh].getTriangles()[index].getIndex()[0] = a;
					model.getMeshes()[currentMesh].getTriangles()[index].getIndex()[1] = b;
					model.getMeshes()[currentMesh].getTriangles()[index].getIndex()[2] = c;
					
//					System.out.println("Triangle: " + index + "; indices: " + model.getMeshes()[currentMesh].getTriangles()[index].getIndex()[0] + ", " + model.getMeshes()[currentMesh].getTriangles()[index].getIndex()[1] + ", " + model.getMeshes()[currentMesh].getTriangles()[index].getIndex()[2]);
				} else if (st.sval.equals("numweights")) {
//					System.out.println(st.sval);
					st.nextToken();
					model.getMeshes()[currentMesh].setNumWeights((int) st.nval);
					model.getMeshes()[currentMesh].setWeights(new MD5Weight[(int) st.nval]);
					
//					System.out.println("Number of weights: " + model.getMeshes()[currentMesh].getNumWeights());
				} else if (st.sval.equals("weight")) {
					// weight weightIndex joint bias ( pos.x pos.y pos.z )
//					System.out.println(st.sval);
					st.nextToken();
					int index = (int) st.nval;
					st.nextToken();
					int joint = (int) st.nval;
					st.nextToken();
					float bias = (float) st.nval;
					st.nextToken(); // Bracket (
					
					Vector3f pos = new Vector3f();
					st.nextToken();
					pos.x = (float) st.nval;
					st.nextToken();
					pos.y = (float) st.nval;
					st.nextToken();
					pos.z = (float) st.nval;
					
					st.nextToken(); // Bracket )
					
					model.getMeshes()[currentMesh].getWeights()[index] = new MD5Weight();
					model.getMeshes()[currentMesh].getWeights()[index].setJoint(joint);
					model.getMeshes()[currentMesh].getWeights()[index].setBias(bias);
					model.getMeshes()[currentMesh].getWeights()[index].setPos(pos);
					
//					System.out.println("Weight: " + index + "; joint" + 
//							model.getMeshes()[currentMesh].getWeights()[index].getJoint() + "; bias: " +
//							model.getMeshes()[currentMesh].getWeights()[index].getBias() + "; position: " +
//							model.getMeshes()[currentMesh].getWeights()[index].getPos());
				}
			}
			
			token = st.nextToken();
		}
	}

	@Override
	protected void finalizeParsing(StreamTokenizer st, int token) {
		// do nothing...
	}

}
