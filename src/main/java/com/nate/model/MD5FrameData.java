package com.nate.model;

import java.nio.FloatBuffer;

public class MD5FrameData {

	private int frameId;
	private FloatBuffer frameData;
	
	public MD5FrameData(){}

	public int getFrameId() {
		return frameId;
	}

	public void setFrameId(int frameId) {
		this.frameId = frameId;
	}

	public FloatBuffer getFrameData() {
		return frameData;
	}

	public void setFrameData(FloatBuffer frameData) {
		this.frameData = frameData;
	}
	
	
}
