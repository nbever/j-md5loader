package com.nate.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import sun.security.action.GetLongAction;

public class MD5Animation {

	private int numberOfFrames;
	private int numberOfJoints;
	private int frameRate;
	
	private MD5Joint[][] skeletonFrames;
	private MD5BoundingBox[] boundingBoxes;
	
	public static MD5Animation loadAnimation( String file ) throws Exception{
		
		MD5Animation anim = new MD5Animation();
		File animFile = new File( file );
		
		BufferedReader fileIn = new BufferedReader( new FileReader( animFile ) );
		
		String line = getNextLine( fileIn );
		
		String[] lineTokens = line.split( " " );
		
		int version = Integer.parseInt( lineTokens[1] );
		
		if ( version != 10 ){
			throw new Exception( "MD5 version is not supported: " + version );
		}
		
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
		anim.setNumberOfJoints( numJoints );
		anim.setNumberOfFrames( numFrames );
		
		MD5Joint[][] skeletonFrames = new MD5Joint[ anim.getNumberOfFrames() ][ anim.getNumberOfJoints() ];
		MD5BoundingBox[] bboxes = new MD5BoundingBox[ anim.getNumberOfFrames() ];
		float[] animationData = new float[ numAnimatedComponents ];
		MD5JointInfo[] jointInfos = new MD5JointInfo[ anim.getNumberOfJoints() ];
		MD5BaseFrameJoint[] baseFrameJoints = new MD5BaseFrameJoint[ anim.getNumberOfJoints() ];
		
		anim.setSkeletonFrames( skeletonFrames );
		anim.setBoundingBoxes( bboxes );
		
		// hierarchy (joint infos)
		getNextLine( fileIn );
		
		for ( int i = 0; i < numJoints; i++ ){
			line = getNextLine( fileIn );
			
			jointInfos[i] = MD5JointInfo.parseLine( line );
		}
		
		getNextLine( fileIn );
		getNextLine( fileIn );
		
		// bounds
		for ( int j = 0; j < numFrames; j++ ){
			line = getNextLine( fileIn );
			
			anim.getBoundingBoxes()[j] = MD5BoundingBox.parseLine( line );
		}
		
		// base frame
		getNextLine( fileIn );
		getNextLine( fileIn );
		
		for ( int k = 0; k < numJoints; k++ ){
			line = getNextLine( fileIn );
			
			MD5BaseFrameJoint data = MD5BaseFrameJoint.parseBaseFrameLine( line );
			data.getOrientation().computeW();
			baseFrameJoints[k] = data;
		}
		
		getNextLine( fileIn );
		
		// now do all the other frames
		for ( int l = 0; l < numFrames; l++ ){
			
			line = getNextLine( fileIn );
			
			// fill animated data with the floats for the frame
			for ( int m = 0; m < numJoints; m++ ){
				
				line = getNextLine( fileIn );
				
				String[] datas = line.split( " " );
				
				for ( int i = 0; i < datas.length; i++ ){
					float f = Float.parseFloat( datas[i] );
					animationData[m*6 + i] = f;
				}
			}
			
			buildFrameSkeleton( jointInfos, baseFrameJoints, animationData,
				anim.getSkeletonFrames()[l], anim.getNumberOfJoints() );
			
			line = getNextLine( fileIn );
		}
	
		return anim;
	}
	
	/**
	 * Put the skeletons together 
	 * 
	 * @param jointInfos
	 * @param baseFrame
	 * @param animationData
	 * @param skeletonFrame
	 * @param numberOfJoints
	 */
	private static void buildFrameSkeleton( MD5JointInfo[] jointInfos, 
			MD5BaseFrameJoint[] baseFrame, 
			float[] animationData, 
			MD5Joint[] skeletonFrame, 
			int numberOfJoints ){
		
		for ( int i = 0; i < numberOfJoints; i++ ){
			
			MD5BaseFrameJoint baseJoint = baseFrame[i];
			
			Vector3f animatedPos = new Vector3f( baseJoint.getPosition().getX(), 
												 baseJoint.getPosition().getY(), 
												 baseJoint.getPosition().getZ() );
			Quaternarion animatedOrient = new Quaternarion( baseJoint.getOrientation().getX(), 
															baseJoint.getOrientation().getY(), 
															baseJoint.getOrientation().getZ(), 
															baseJoint.getOrientation().getW() );
			int j = 0;
			
			if ( (jointInfos[i].getFlags() & 1) == 1 ){
				animatedPos.setX( animationData[ jointInfos[i].getStartIndex() + j] );
				j++;
			}
			
			if ( (jointInfos[i].getFlags() & 2) == 2 ){
				animatedPos.setY( animationData[ jointInfos[i].getStartIndex() + j] );
				j++;
			}
			
			if ( (jointInfos[i].getFlags() & 4) == 4 ){
				animatedPos.setZ( animationData[ jointInfos[i].getStartIndex() + j] );
				j++;
			}
			
			if ( (jointInfos[i].getFlags() & 8) == 8 ){
				animatedOrient.setX( animationData[ jointInfos[i].getStartIndex() + j] );
				j++;
			}
			
			if ( (jointInfos[i].getFlags() & 16) == 16 ){
				animatedOrient.setY( animationData[ jointInfos[i].getStartIndex() + j] );
				j++;
			}
			
			if ( (jointInfos[i].getFlags() & 32) == 32 ){
				animatedOrient.setZ( animationData[ jointInfos[i].getStartIndex() + j] );
				j++;
			}
			
			animatedOrient.computeW();
			
		    /* NOTE: we assume that this joint's parent has
			already been calculated, i.e. joint's ID should
			never be smaller than its parent ID. */
			MD5Joint thisJoint = new MD5Joint();
			
			int parent = jointInfos[i].getParent();
			thisJoint.setParent( parent );
			thisJoint.setName( jointInfos[i].getName() );
			
			if ( thisJoint.getParent() < 0 ){
				Vector3f thisPosition = new Vector3f( animatedPos.getX(), animatedPos.getY(), animatedPos.getZ() );
				Quaternarion thisOrient = new Quaternarion( animatedOrient.getX(), animatedOrient.getY(), animatedOrient.getZ(), animatedOrient.getW() );
				
				thisJoint.setOrientation( thisOrient );
				thisJoint.setPosition( thisPosition );
			}
			else {
				
				MD5Joint parentJoint = skeletonFrame[parent];
				Vector3f rotatedPosition = Quaternarion.rotatePoint( parentJoint.getOrientation(), animatedPos );
				
				Vector3f jointPosition = new Vector3f( 0.0f, 0.0f, 0.0f );
				
				jointPosition.setX( rotatedPosition.getX() + parentJoint.getPosition().getX()  );
				jointPosition.setY( rotatedPosition.getY() + parentJoint.getPosition().getY()  );
				jointPosition.setZ( rotatedPosition.getZ() + parentJoint.getPosition().getZ()  );
				
				thisJoint.setPosition( jointPosition );
				
				Quaternarion multQuat = Quaternarion.multiply( parentJoint.getOrientation(), animatedOrient );
				thisJoint.setOrientation( multQuat );
				Quaternarion normal = Quaternarion.normalize( thisJoint.getOrientation() );
				thisJoint.setOrientation( normal );
			}
			
			skeletonFrame[i] = thisJoint;
		}
		
	}
	
	public static MD5Joint[] interpolateSkeletons( MD5Joint[] skeletonA, MD5Joint[] skeletonB, int numJoints, float interp ){
		
		MD5Joint[] newSkeleton = new MD5Joint[skeletonA.length];
		
		for ( int i = 0; i < numJoints; i++ ){
			
			MD5Joint joint = new MD5Joint();
			joint.setParent( skeletonA[i].getParent() );
			
			// linear interpolation for position
			float x = skeletonA[i].getPosition().getX() + interp * ( skeletonB[i].getPosition().getX() - skeletonA[i].getPosition().getX() );
			float y = skeletonA[i].getPosition().getY() + interp * ( skeletonB[i].getPosition().getY() - skeletonA[i].getPosition().getY() );
			float z = skeletonA[i].getPosition().getZ() + interp * ( skeletonB[i].getPosition().getZ() - skeletonA[i].getPosition().getZ() );
			
			joint.setPosition( new Vector3f( x, y, z ) );
			
			Quaternarion slerped = Quaternarion.slerp( skeletonA[i].getOrientation(), skeletonB[i].getOrientation(), interp );
			joint.setOrientation( slerped );
		
			newSkeleton[i] = joint;
		}
		
		return newSkeleton;
	}
	
	public void advanceAnimation( MD5AnimationInfo info, double elapsedTime ){
		
		int maxFrames = getNumberOfFrames() - 1;
		info.setLastTime( info.getLastTime() + elapsedTime );
		
		// go to next frame
		if ( info.getLastTime() >= info.getMaxTime() ){
			
			info.setCurrentFrame( info.getCurrentFrame() + 1 );
			info.setNextFrame( info.getNextFrame() + 1 );
			info.setLastTime( 0.0 );
			
			if ( info.getCurrentFrame() > maxFrames ){
				info.setCurrentFrame( 0 );
			}
			
			if ( info.getNextFrame() > maxFrames ){
				info.setNextFrame( 0 );
			}
		}
	}
	
	private static String getNextLine( BufferedReader fileIn ) throws IOException{
		
		String line = "";
		
		do {
			line = fileIn.readLine();
		}
		while( line == null || line.trim().length() == 0 );
		
		return line.trim();
		
	}
	
	public Boolean checkAnimationValidity( MD5Model model ){
		
		if ( model.getNumberOfJoints() != getNumberOfJoints() ){
			return Boolean.FALSE;
		}
		
		for ( int i = 0; i < model.getNumberOfJoints(); i++ ){
			
			if ( model.getBaseSkeleton()[0].getParent() != getSkeletonFrames()[0][i].getParent() ){
				return Boolean.FALSE;
			}
			
			if ( !model.getBaseSkeleton()[i].getName().equals( getSkeletonFrames()[0][i].getName() ) ){
				return Boolean.FALSE;
			}
		}
		
		return Boolean.TRUE;
	}
	
	public int getNumberOfFrames() {
		return numberOfFrames;
	}
	
	public void setNumberOfFrames( int numberOfFrames ) {
		this.numberOfFrames = numberOfFrames;
	}
	
	public int getNumberOfJoints() {
		return numberOfJoints;
	}
	
	public void setNumberOfJoints( int numberOfJoints ) {
		this.numberOfJoints = numberOfJoints;
	}
	
	public int getFrameRate() {
		return frameRate;
	}
	
	public void setFrameRate( int frameRate ) {
		this.frameRate = frameRate;
	}
	
	public MD5Joint[][] getSkeletonFrames() {
		return skeletonFrames;
	}
	
	public void setSkeletonFrames( MD5Joint[][] skeletonFrames ) {
		this.skeletonFrames = skeletonFrames;
	}
	
	public MD5BoundingBox[] getBoundingBoxes() {
		return boundingBoxes;
	}
	
	public void setBoundingBoxes( MD5BoundingBox[] boundingBoxes ) {
		this.boundingBoxes = boundingBoxes;
	}
}
