package com.nate.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;


public class MD5Animation {

	private List<MD5JointInfo> jointInfos;
	private List<MD5Bound> bounds;
	private List<MD5BaseFrameJoint> baseFrames;
	private List<MD5FrameData> frames;
	private List<MD5FrameSkeleton> skeletons;
	
	private MD5FrameSkeleton animatedSkeleton;
	
	private int md5Version;
	private int numberOfFrames;
	private int numberOfJoints;
	private int frameRate;
	private int numAnimatedComponents;
	
	float animationDuration;
	float frameDuration;
	float animationTime;

	private MD5FrameSkeleton transitionJoints;
	private boolean transitionJointsUsed = false;
	
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
		anim.setNumAnimatedComponents( numAnimatedComponents );
		
		anim.setBounds( new ArrayList<MD5Bound>() );
		anim.setJointInfos( new ArrayList<MD5JointInfo>() );
		anim.setBaseFrames( new ArrayList<MD5BaseFrameJoint>() );
		anim.setSkeletons( new ArrayList<MD5FrameSkeleton>() );
		
		// hierarchy (joint infos)
		getNextLine( fileIn );
		
		for ( int i = 0; i < numJoints; i++ ){
			line = getNextLine( fileIn );
			
			anim.getJointInfos().add( MD5JointInfo.parseLine( line ) );
		}
		
		line = getNextLine( fileIn );
		
		if ( line.indexOf( "bounds" ) == -1 ){
			line = getNextLine( fileIn );
		}
		
		// bounds
		for ( int j = 0; j < numFrames; j++ ){
			line = getNextLine( fileIn );
			
			anim.getBounds().add( MD5Bound.parseLine( line ) );
		}
		
		// base frame
		line = getNextLine( fileIn );
		
		if ( line.indexOf( "baseframe" ) == -1 ){
			getNextLine( fileIn );
		}
		
		for ( int k = 0; k < numJoints; k++ ){
			line = getNextLine( fileIn );
			
			MD5BaseFrameJoint data = MD5BaseFrameJoint.parseBaseFrameLine( line );
			data.getOrientation().computeW();
			anim.getBaseFrames().add( data );
		}
		
		line = getNextLine( fileIn );
		
		// now do all the other frames
		for ( int l = 0; l < numFrames; l++ ){

			while ( line != null && line.indexOf( "frame" ) == -1 ){
				line = getNextLine( fileIn );
			}
			
			MD5FrameData frame = new MD5FrameData();
			frame.setFrameData( BufferUtils.createFloatBuffer( anim.getNumAnimatedComponents() ) );
			
			// fill animated data with the floats for the frame
			line = getNextLine( fileIn );
			while ( line != null && line.indexOf( "frame" ) == -1 ){

				String[] datas = line.split( " " );
				
				
				for ( int i = 0; i < datas.length; i++ ){
					float f = Float.parseFloat( datas[i] );
					frame.getFrameData().put( f );
				}
				
				line = getNextLine( fileIn );
				
			}
			
			anim.getFrames().add( frame );
			
			MD5FrameSkeleton skel = buildFrameSkeleton( anim.getJointInfos(), anim.getBaseFrames(), frame );
			anim.getSkeletons().add( skel );
		}
	
		MD5FrameSkeleton animatedSkeleton = new MD5FrameSkeleton();
		
		for ( int skj = 0; skj < anim.getNumberOfJoints(); skj++ ){
			animatedSkeleton.getSkeletonJoints().add(  new MD5SkeletonJoint() );
		}
		
		anim.setAnimatedSkeleton( anim.getSkeletons().get( 0 ) );
		
		anim.setFrameDuration( (1000.0f / (float)anim.getFrameRate()) );
		anim.setAnimationDuration( anim.getFrameDuration() * anim.getNumberOfFrames() );
		anim.setAnimationTime( 0.0f );
		
		return anim;
	}
	
	private static String getNextLine( BufferedReader fileIn ) throws IOException{
		
		String line = "";
		
		do {
			line = fileIn.readLine();
			
			if ( line == null ){
				return line;
			}
		}
		while( line.trim().length() == 0 || line.trim().startsWith( "//") || line.trim().equals( "}" ) );
		
		if ( line != null ){
			line = line.trim();
		}
		
		return line;
		
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
	private static MD5FrameSkeleton buildFrameSkeleton(
			List<MD5JointInfo> jointInfos, 
			List<MD5BaseFrameJoint> baseFrames, 
			MD5FrameData frameData ){
		
		MD5FrameSkeleton skeleton = new MD5FrameSkeleton();
		
		for ( int i = 0; i < jointInfos.size(); i++ ){
			
			int j = 0;
			MD5JointInfo jointInfo = jointInfos.get( i );
			MD5SkeletonJoint animatedJoint = new MD5SkeletonJoint( baseFrames.get( i ) );
			
			animatedJoint.setParent( jointInfo.getParent() );
			
			if ( (jointInfo.getFlags() & 1) == 1 ){
				animatedJoint.getPosition().setX( frameData.getFrameData().get( jointInfo.getStartIndex() + j ) );
				j++;
			}
			
			if ( (jointInfo.getFlags() & 2) == 2 ){
				animatedJoint.getPosition().setY( frameData.getFrameData().get( jointInfo.getStartIndex() + j ) );
				j++;
			}
			
			if ( (jointInfo.getFlags() & 4) == 4 ){
				animatedJoint.getPosition().setZ( frameData.getFrameData().get( jointInfo.getStartIndex() + j ) );
				j++;
			}
			
			if ( (jointInfo.getFlags() & 8) == 8 ){
				animatedJoint.getOrientation().setX( frameData.getFrameData().get( jointInfo.getStartIndex() + j ) );
				j++;
			}
			
			if ( (jointInfo.getFlags() & 16) == 16 ){
				animatedJoint.getOrientation().setY( frameData.getFrameData().get( jointInfo.getStartIndex() + j ) );
				j++;
			}
			
			if ( (jointInfo.getFlags() & 32) == 32 ){
				animatedJoint.getOrientation().setZ( frameData.getFrameData().get( jointInfo.getStartIndex() + j ) );
				j++;
			}
			
			animatedJoint.getOrientation().computeW();
			
			if ( animatedJoint.getParent() >= 0 ){
				
				MD5SkeletonJoint parentJoint = skeleton.getSkeletonJoints().get( animatedJoint.getParent() );
				Vector3f rotPos = Quaternarion.rotatePoint( parentJoint.getOrientation(), animatedJoint.getPosition() );
				
				animatedJoint.setPosition( Vector3f.add( parentJoint.getPosition(), rotPos ) );
				animatedJoint.setOrientation( Quaternarion.multiply( parentJoint.getOrientation(), animatedJoint.getOrientation() ) );
				
				animatedJoint.setOrientation( Quaternarion.normalize( animatedJoint.getOrientation() ) );
			}
			
			skeleton.getSkeletonJoints().add( animatedJoint );
		}
		
		return skeleton;
	}
	
	public void update( float deltaTime ){
		update( deltaTime, null );
	}
	
	public void update( float deltaTime, MD5FrameSkeleton nextFrame ){
		
		if ( getNumberOfFrames() < 1 ){
			return;
		}
		
		setAnimationTime( getAnimationTime() + deltaTime );
		
		while( getAnimationTime() > getAnimationDuration() ){
			// no clue why this subtraction is here yo...
//			setAnimationTime( getAnimationTime() - getAnimationDuration() );
			setAnimationTime( 0 );
		}
		
		while( getAnimationTime() < 0.0f ){
			setAnimationTime(  getAnimationTime() + getAnimationDuration() );
		}
		
		//Figure out which frame we're on
		float frameNum = getAnimationTime() / (1000.0f / (float)getFrameRate());
		int frame0 = (int)Math.floor( frameNum );
		frame0 = frame0 % getNumberOfFrames();
		
		if ( nextFrame == null ){
			int frame1 = (int)Math.ceil( frameNum );
			frame1 = frame1 % getNumberOfFrames();
			nextFrame = getSkeletons().get( frame1 );
		}
		
		float interpolate = (getAnimationTime() % getFrameDuration()) / getFrameDuration();
		MD5FrameSkeleton frame0Skel = getSkeletons().get( frame0 );
		
		if ( isTransitionUsed() == false && getTransitionJoints() != null ){
			if ( frame0 == 0 ){
				frame0Skel = getTransitionJoints();
			}
			else if ( frame0 > 0 ){
				setTransitionUsed( true );
			}
		}
		
		interpolateSkeletons( getAnimatedSkeleton(), frame0Skel, nextFrame, interpolate );
	}
	
	public void interpolateSkeletons( MD5FrameSkeleton finalSkeleton, MD5FrameSkeleton skeleton0, MD5FrameSkeleton skeleton1, float interp ){
		
		for ( int i = 0; i < getNumberOfJoints(); i++ ){
			
			MD5SkeletonJoint finalJoint = finalSkeleton.getSkeletonJoints().get( i );
			MD5SkeletonJoint joint0 = skeleton0.getSkeletonJoints().get( i );
			MD5SkeletonJoint joint1 = skeleton1.getSkeletonJoints().get( i );
			
			finalJoint.setParent( joint0.getParent() );
			
			finalJoint.setPosition( Vector3f.lerp( joint0.getPosition(), joint1.getPosition(), interp ) );
			finalJoint.setOrientation( Quaternarion.slerp( joint0.getOrientation(), joint1.getOrientation(), interp ) );
		}
	}
	
	public void render(){
		
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
	
	public List<MD5JointInfo> getJointInfos() {
		
		if ( jointInfos == null ){
			jointInfos = new ArrayList<MD5JointInfo>();
		}
		return jointInfos;
	}

	public void setJointInfos(List<MD5JointInfo> jointInfos) {
		this.jointInfos = jointInfos;
	}

	public List<MD5Bound> getBounds() {
		if ( bounds == null ){
			bounds = new ArrayList<MD5Bound>();
		}
		return bounds;
	}

	public void setBounds(List<MD5Bound> bounds) {
		this.bounds = bounds;
	}

	public List<MD5BaseFrameJoint> getBaseFrames() {
		
		if ( baseFrames == null ){
			baseFrames = new ArrayList<MD5BaseFrameJoint>();
		}
		return baseFrames;
	}

	public void setBaseFrames(List<MD5BaseFrameJoint> baseFrames) {
		this.baseFrames = baseFrames;
	}

	public List<MD5FrameSkeleton> getSkeletons() {
		
		if ( skeletons == null ){
			skeletons = new ArrayList<MD5FrameSkeleton>();
		}
		return skeletons;
	}

	public void setSkeletons(List<MD5FrameSkeleton> skeletons) {
		this.skeletons = skeletons;
	}
	
	public List<MD5FrameData> getFrames(){
		
		if ( frames == null ){
			frames = new ArrayList<MD5FrameData>();
		}
		
		return frames;
	}
	
	public void setFrames( List<MD5FrameData> someFrames ){
		frames = someFrames;
	}

	public MD5FrameSkeleton getAnimatedSkeleton() {
		return animatedSkeleton;
	}

	public void setAnimatedSkeleton(MD5FrameSkeleton animatedSkeleton) {
		this.animatedSkeleton = animatedSkeleton;
	}

	public int getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(int md5Version) {
		this.md5Version = md5Version;
	}

	public int getNumAnimatedComponents() {
		return numAnimatedComponents;
	}

	public void setNumAnimatedComponents(int numAnimatedComponents) {
		this.numAnimatedComponents = numAnimatedComponents;
	}

	public float getAnimationDuration() {
		return animationDuration;
	}

	public void setAnimationDuration(float animationDuration) {
		this.animationDuration = animationDuration;
	}

	public float getFrameDuration() {
		return frameDuration;
	}

	public void setFrameDuration(float frameDuration) {
		this.frameDuration = frameDuration;
	}

	public float getAnimationTime() {
		return animationTime;
	}

	public void setAnimationTime(float animationTime) {
		this.animationTime = animationTime;
	}
	
	protected void setTransitionJoints( MD5FrameSkeleton joints ){
		transitionJoints = joints;
		setTransitionUsed( false );
	}
	
	private MD5FrameSkeleton getTransitionJoints(){
		return transitionJoints;
	}
	
	private void setTransitionUsed( boolean isIt ){
		transitionJointsUsed = isIt;
	}
	
	private boolean isTransitionUsed(){
		return transitionJointsUsed;
	}
}
