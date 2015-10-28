package com.nate.model;

public class MD5AnimationInfo {

	private int currentFrame;
	private int nextFrame;
	
	private double lastTime;
	private double maxTime;
	
	public int getCurrentFrame() {
		return currentFrame;
	}
	
	public void setCurrentFrame( int currentFrame ) {
		this.currentFrame = currentFrame;
	}
	
	public int getNextFrame() {
		return nextFrame;
	}
	
	public void setNextFrame( int nextFrame ) {
		this.nextFrame = nextFrame;
	}
	
	public double getLastTime() {
		return lastTime;
	}
	
	public void setLastTime( double lastTime ) {
		this.lastTime = lastTime;
	}
	
	public double getMaxTime() {
		return maxTime;
	}
	
	public void setMaxTime( double maxTime ) {
		this.maxTime = maxTime;
	}
}
