package com.nate.model;

public class MD5Animation {

	private int numberOfFrames;
	private int numberOfJoints;
	private int frameRate;
	
	private MD5Joint[][] skeletonFrames;
	private MD5BoundingBox[] boundingBoxes;
	
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
