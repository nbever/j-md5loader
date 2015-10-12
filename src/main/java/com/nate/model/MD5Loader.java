package com.nate.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.nate.model.parts.Bound;
import com.nate.model.parts.Frame;
import com.nate.model.parts.FrameData;
import com.nate.model.parts.FrameSkeleton;
import com.nate.model.parts.Joint;
import com.nate.model.parts.JointInfo;
import com.nate.model.parts.Mesh;
import com.nate.model.parts.SkeletonJoint;
import com.nate.model.parts.Triangle;
import com.nate.model.parts.Weight;
import com.nate.model.types.Vector3d;
import com.nate.model.types.Vector4d;
import com.nate.model.types.Vertice;

public class MD5Loader {

	private MD5Loader(){}
	
	public static MD5Model loadModel( String file ) throws Exception{
		
		MD5Model model = new MD5Model();
		File modelFile = new File( file );
		
		BufferedReader fileIn = new BufferedReader( new FileReader( modelFile ) );
		
		// get the version
		String line = getNextLine( fileIn );
		model.setVersion( Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) ) );
		
		if ( model.getVersion() != 10 ){
			throw new Exception( "MD5 version is not supported: " + model.getVersion() );
		}
		
		// get the command line
		line = getNextLine( fileIn );
		model.setCommandLine( line );
		
		// number of joints
		line = getNextLine( fileIn );
		int numJoints = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		
		// number of meshes
		line = getNextLine( fileIn );
		int numMeshes = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		
		model.initialize( numJoints, numMeshes );
		
		// now get the joints
		line = getNextLine( fileIn );
		if ( line.startsWith( "joints {" ) ){
			
			for ( int i = 0; i < numJoints; i++ ){
				
				line = getNextLine( fileIn );
				model.getJoints()[i] = Joint.parse( line );
			}
			
			// closing brace
			getNextLine( fileIn );
		}
		
		// huge blocks here for each mesh
		for ( int j = 0; j < numMeshes; j++ ){
			
			line = getNextLine( fileIn );
			
			if ( !line.startsWith( "mesh {" ) ){
				continue;
			}
			
			Mesh mesh = loadMesh( fileIn );
			model.getMeshes()[j] = mesh;
			model.prepareMesh( mesh );
			model.prepareNormals( mesh );
			
			mesh.getPositionBuffer().flip();
			mesh.getNormalBuffer().flip();
			mesh.getIndexBuffer().flip();
		}
		
		
		return model;
	}
	
	private static Mesh loadMesh( BufferedReader fileIn ) throws Exception {
		
		Mesh mesh = new Mesh();
		
		String line = getNextLine( fileIn );
		mesh.setShader( line.substring( line.indexOf( " " ) + 1 ) );
		
		line = getNextLine( fileIn );
		int numVerts = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		mesh.initializeVertices( numVerts );
		
		// the verts
		for ( int i = 0; i < numVerts; i++ ){
			line = getNextLine( fileIn );
			Vertice<Float> vert = Vertice.parseFloat( line );
			mesh.getVertices()[i] = vert;
		}
		
		// the triangles
		line = getNextLine( fileIn );
		int numTris = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		mesh.initializeTriangles( numTris );
		
		for ( int j = 0; j < numTris; j++ ){
			line = getNextLine( fileIn );
			Triangle tri = Triangle.parse( line );
			mesh.getTriangles()[j] = tri;
			
			mesh.getIndexBuffer().put( new int[]{ tri.getVertices().getU(), tri.getVertices().getV(), tri.getVertices().getZ() } );
//			mesh.getIndexBuffer().put( tri.getVertices().getU() );
//			mesh.getIndexBuffer().put( tri.getVertices().getV() );
//			mesh.getIndexBuffer().put( tri.getVertices().getZ() );
		}
		
		// the weights
		line = getNextLine( fileIn );
		int numWeights = Integer.parseInt( line.substring( line.indexOf( " " ) +1 ) );
		mesh.initializeWeights( numWeights );
		
		for ( int k = 0; k < numWeights; k++ ){
			line = getNextLine( fileIn );
			mesh.getWeights()[k] = Weight.parseFloat( line );
		}
		
		return mesh;
	}
	
	public MD5Animation loadAnimation( String animationFile ) throws Exception{
		
		MD5Animation anim = new MD5Animation();
		File animFile = new File( animationFile );
		
		BufferedReader fileIn = new BufferedReader( new FileReader( animFile ) );
		
		String line = getNextLine( fileIn );
		
		int version = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		
		if ( version != 10 ){
			throw new Exception( "MD5 version is not supported: " + version );
		}
		
		anim.setVersion( version );
		
		// command
		getNextLine( fileIn );
		line = getNextLine( fileIn );
		
		int numFrames = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		line = getNextLine( fileIn );
		int numJoints = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		line = getNextLine( fileIn );
		int frameRate = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		line = getNextLine( fileIn );
		int numAnimatedComponents = Integer.parseInt( line.substring( line.indexOf( " " ) + 1 ) );
		
		anim.setFrameRate( frameRate );
		
		// hierarchy (joint infos)
		getNextLine( fileIn );
		
		anim.initializeJointInfo( numJoints );
		
		for ( int i = 0; i < numJoints; i++ ){
			line = getNextLine( fileIn );
			
			anim.getJoints()[i] = JointInfo.parseLine( line );
		}
		
		getNextLine( fileIn );
		getNextLine( fileIn );
		
		// bounds
		
		anim.initializeBounds( numFrames );
		
		for ( int j = 0; j < numFrames; j++ ){
			line = getNextLine( fileIn );
			
			anim.getBounds()[j] = Bound.parseLine( line );
		}
		
		// base frame
		getNextLine( fileIn );
		getNextLine( fileIn );
		
		Frame<Float> baseFrame = new Frame<Float>();
		baseFrame.initializeData( numJoints );
		
		for ( int k = 0; k < numJoints; k++ ){
			line = getNextLine( fileIn );
			
			FrameData<Float> data = FrameData.parseBaseFrameLine( line );
			baseFrame.getFrameData()[k] = data;
		}
		
		anim.setBaseFrame( baseFrame );
		
		getNextLine( fileIn );
		
		anim.initializeFrames( numFrames );
		anim.initializeSkeletons( numFrames );
		
		// now do all the other frames
		for ( int l = 0; l < numFrames; l++ ){
			
			getNextLine( fileIn );
			Frame<Float> frame = new Frame<Float>();
			frame.initializeData( numJoints );
			
			for ( int m = 0; m < numJoints; m++ ){
				
				line = getNextLine( fileIn );
				
				FrameData<Float> data = FrameData.parseFrameLine( line );
				frame.getFrameData()[m] = data;
			}
			
			anim.getFrames()[l] = frame;
			
			FrameSkeleton<Float> frameSkels = buildSkeleton( anim.getJoints(), anim.getBaseFrame(), frame );
			anim.getSkeletons()[l] = frameSkels;
		}
		
		FrameSkeleton<Float> aSkel = new FrameSkeleton<Float>( anim.getSkeletons()[0] );
		
		anim.setAnimatedSkeleton( aSkel );
		anim.setFrameDuration( 1.0f / (float)frameRate );
		anim.setAnimationDuration( anim.getFrameDuration() * (float)numFrames );
		anim.setAnimationTime( 0.0f );
		
		return anim;
	}
	
	private FrameSkeleton<Float> buildSkeleton( JointInfo[] jointInfos, Frame<Float> baseFrame, Frame<Float> currentFrame ){
		
		FrameSkeleton<Float> skeleton = new FrameSkeleton<Float>( jointInfos.length );
		
		for ( int i = 0; i < jointInfos.length; i++ ){
			
			int j = 0;
			
			JointInfo joint = jointInfos[i];
			SkeletonJoint<Float> skelJoint = new SkeletonJoint<Float>();
			
			skelJoint.setPosition( baseFrame.getFrameData()[i].getPosition() );
			
			Vector4d<Float> oQuat = new Vector4d<Float>( baseFrame.getFrameData()[i].getOrientation().getU(),
														 baseFrame.getFrameData()[i].getOrientation().getV(),
														 baseFrame.getFrameData()[i].getOrientation().getZ(),
														 0.0f );
			
			skelJoint.setOrientation( oQuat );
			
			skelJoint.setParent( joint.getParentIndex() );
			
			if ( (joint.getFlags() & 1) == 1 ){
				skelJoint.getPosition().setU( currentFrame.getFrameData()[ joint.getStartIndex() + j ].getPosition().getU() );
				j++;
			}
			
			if ( (joint.getFlags() & 2) == 2 ){
				skelJoint.getPosition().setU( currentFrame.getFrameData()[ joint.getStartIndex() + j ].getPosition().getV() );
				j++;
			}
			
			if ( (joint.getFlags() & 4) == 4 ){
				skelJoint.getPosition().setU( currentFrame.getFrameData()[ joint.getStartIndex() + j ].getPosition().getZ() );
				j++;
			}
			
			if ( (joint.getFlags() & 8) == 8 ){
				skelJoint.getOrientation().setU( currentFrame.getFrameData()[ joint.getStartIndex() + j ].getOrientation().getU() );
				j++;
			}
			
			if ( (joint.getFlags() & 16) == 16 ){
				skelJoint.getOrientation().setU( currentFrame.getFrameData()[ joint.getStartIndex() + j ].getOrientation().getV() );
				j++;
			}
			
			if ( (joint.getFlags() & 32) == 32 ){
				skelJoint.getOrientation().setU( currentFrame.getFrameData()[ joint.getStartIndex() + j ].getOrientation().getZ() );
				j++;
			}
			
			skelJoint.getOrientation().setW( Vector3d.computeW( skelJoint.getOrientation() ) );
			
			if ( skelJoint.getParent() >= 0 ){
				SkeletonJoint<Float> parentJoint = skeleton.getSkeletonJoints()[ skelJoint.getParent() ];
				Vector3d<Float> rotationalPosition = parentJoint.getOrientation().multiplyf( skelJoint.getPosition() );
				
				skelJoint.setPosition( parentJoint.getPosition().addf( rotationalPosition ) );
				Vector4d<Float> newOrient = parentJoint.getOrientation().multiplyf( skelJoint.getOrientation() );
				newOrient = newOrient.normalizef();
				
				skelJoint.setOrientation( newOrient );
			}
		
			skeleton.getSkeletonJoints()[i] = skelJoint;
		}
		
		return skeleton;
	}
	
	private static String getNextLine( BufferedReader fileIn ) throws IOException{
		
		String line = "";
		
		do {
			line = fileIn.readLine();
		}
		while( line == null || line.trim().length() == 0 );
		
		return line.trim();
		
	}
}
