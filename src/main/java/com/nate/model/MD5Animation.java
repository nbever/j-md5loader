package com.nate.model;

import com.nate.model.parts.Bound;
import com.nate.model.parts.Frame;
import com.nate.model.parts.FrameSkeleton;
import com.nate.model.parts.JointInfo;

public class MD5Animation {

	private int version;
	
	private JointInfo[] joints;
	private Bound<Float>[] bounds;
	private Frame<Float> baseFrame;
	private Frame<Float>[] frames;
	private FrameSkeleton<Float>[] skeletons;
	
	private Float frameDuration;
	private Float animationDuration;
	private Float animationTime;
	
	public void initializeJointInfo( int num ){
		
		joints = new JointInfo[num];
	}
	
	@SuppressWarnings("unchecked")
	public void initializeBounds( int num ){
		bounds = new Bound[ num ];
	}
	
	@SuppressWarnings("unchecked")
	public void initializeFrames( int num ){
		frames = new Frame[ num ];
	}
	
	@SuppressWarnings("unchecked")
	public void initializeSkeletons( int num ){
		skeletons = new FrameSkeleton[ num ];
	}
	
	public void setVersion( int aVersion ){
		version = aVersion;
	}
	
	public Float getFrameDuration(){
		return frameDuration;
	}
	
	public void setFrameDuration( Float duration ){
		frameDuration = duration;
	}
	
	public Float getAnimationDuration() {
		return animationDuration;
	}
	
	public void setAnimationDuration( Float duration ){
		animationDuration = duration;
	}
	
	public Float getAnimationTime(){
		return animationTime;
	}
	
	public void setAnimationTime( Float time ){
		animationTime = time;
	}
	
	public JointInfo[] getJoints(){
		return joints;
	}
	
	public Bound<Float>[] getBounds(){
		return bounds;
	}
	
	public Frame<Float> getBaseFrame(){
		return baseFrame;
	}
	
	public void setBaseFrame( Frame<Float> frame ){
		baseFrame = frame;
	}
	
	public Frame<Float>[] getFrames(){
		return frames;
	}
	
	public FrameSkeleton<Float>[] getSkeletons(){
		return skeletons;
	}
}
