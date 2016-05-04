package com.nate.model;

public class MD5SkeletonJoint extends MD5Joint{

	public MD5SkeletonJoint(){
		setParent( -1 );
		setPosition( new Vector3f( 0.0f, 0.0f, 0.0f ) );
	}
	
	public MD5SkeletonJoint( MD5BaseFrameJoint baseFrame ){
		setPosition( baseFrame.getPosition().copy() );
		setOrientation(  baseFrame.getOrientation().copy() );
	}

}
