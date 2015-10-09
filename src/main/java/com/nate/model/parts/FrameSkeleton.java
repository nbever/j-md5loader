package com.nate.model.parts;

public class FrameSkeleton<T extends Number> {

	private SkeletonJoint<T>[] joints;
	
	public FrameSkeleton( int size ){
		joints = new SkeletonJoint[ size ];
	}
	
	public SkeletonJoint<T>[] getSkeletonJoints(){
		return joints;
	}
}
