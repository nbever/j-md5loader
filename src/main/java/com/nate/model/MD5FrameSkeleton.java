package com.nate.model;

import java.util.ArrayList;
import java.util.List;

public class MD5FrameSkeleton {

	List<MD5SkeletonJoint> skeletonJoints;
	
	public MD5FrameSkeleton(){}

	public List<MD5SkeletonJoint> getSkeletonJoints() {
		if ( skeletonJoints == null ){
			skeletonJoints = new ArrayList<MD5SkeletonJoint>();
		}
		return skeletonJoints;
	}

	public void setSkeletonJoints(List<MD5SkeletonJoint> skeletonJoints) {
		this.skeletonJoints = skeletonJoints;
	}
	
	
}
