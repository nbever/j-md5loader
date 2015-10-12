package com.nate.model;

import com.nate.model.parts.Bound;
import com.nate.model.parts.Frame;
import com.nate.model.parts.FrameSkeleton;
import com.nate.model.parts.JointInfo;
import com.nate.model.parts.SkeletonJoint;
import com.nate.model.types.Vector3d;
import com.nate.model.types.Vector4d;

public class MD5Animation {

	private int version;
	
	private JointInfo[] joints;
	private Bound<Float>[] bounds;
	private Frame<Float> baseFrame;
	private Frame<Float>[] frames;
	private FrameSkeleton<Float>[] skeletons;
	private FrameSkeleton<Float> animatedSkeleton;
	
	private Float frameDuration;
	private Float animationDuration;
	private Float animationTime;
	
	private Integer frameRate;
	
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
	
	public void update( Float deltaTime ){
		
		if ( getFrames().length < 1 ){
			return;
		}
		
		setAnimationTime( getAnimationTime() + deltaTime );
		
		while ( getAnimationTime() > getAnimationDuration() ){
			setAnimationTime( getAnimationTime() - getAnimationDuration() );
		}
		
		while( getAnimationTime() < 0.0f ){
			setAnimationTime( getAnimationTime() + getAnimationDuration() );
		}
		
		Float frameNumber = getAnimationTime() * (float)getFrameRate();
		Integer frame0 = (int)Math.floor( (double)frameNumber );
		Integer frame1 = (int)Math.ceil( (double)frameNumber );
		
		frame0 = frame0 % getFrames().length;
		frame1 = frame1 % getFrames().length;
		
		Float interpolaateIndex = (getAnimationTime() % getFrameDuration() ) / getFrameDuration();
		
		interpolateSkeletons( getAnimatedSkeleton(), getSkeletons()[frame0], getSkeletons()[frame1], interpolaateIndex );
	}
	
	private void interpolateSkeletons( FrameSkeleton<Float> aSkeleton, FrameSkeleton<Float> skel0, FrameSkeleton<Float> skel1, Float interpolationIndex ){
		
		for ( int i = 0; i < aSkeleton.getSkeletonJoints().length; i++ ){
			SkeletonJoint<Float> finalJoint = aSkeleton.getSkeletonJoints()[i];
			SkeletonJoint<Float> joint0 = skel0.getSkeletonJoints()[i];
			SkeletonJoint<Float> joint1 = skel1.getSkeletonJoints()[i];
			
			finalJoint.setParent( joint0.getParent() );
			
			// linear interpolation
			Vector3d<Float> lerpVec = joint0.getPosition().lerpf( joint1.getPosition(), interpolationIndex );
			finalJoint.setPosition( lerpVec );
			
			// spherical interpolation
			Vector4d<Float> slerpVec = joint0.getOrientation().slerpf( joint1.getOrientation(), interpolationIndex );
			finalJoint.setOrientation( slerpVec );
		}
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
	
	public Integer getFrameRate(){
		return frameRate;
	}
	
	public void setFrameRate( Integer rate ){
		frameRate = rate;
	}
	
	public FrameSkeleton<Float> getAnimatedSkeleton(){
		return animatedSkeleton;
	}
	
	public void setAnimatedSkeleton( FrameSkeleton skel ){
		animatedSkeleton = skel;
	}
}
