package com.nate.model;

import com.nate.model.parts.Bound;
import com.nate.model.parts.Frame;
import com.nate.model.parts.JointInfo;

public class MD5Animation {

	private int version;
	
	private JointInfo[] joints;
	private Bound<Float>[] bounds;
	private Frame<Float> baseFrame;
	private Frame<Float>[] frames;
	
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
	
	public void setVersion( int aVersion ){
		version = aVersion;
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
}
