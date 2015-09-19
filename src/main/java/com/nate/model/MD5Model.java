package com.nate.model;

import com.nate.model.parts.Joint;
import com.nate.model.parts.Mesh;

public class MD5Model {

	private Joint[] joints;
	private Mesh[] meshes;
	private int version;
	private String commandLine;
	
	public void initialize( int joints, int meshes ){
		this.joints = new Joint[joints];
		this.meshes = new Mesh[meshes];
	}
	
	public Joint[] getJoints() {
		return joints;
	}
	
	public void setJoints( Joint[] joints ) {
		this.joints = joints;
	}
	
	public Mesh[] getMeshes() {
		return meshes;
	}
	
	public void setMeshes( Mesh[] meshes ) {
		this.meshes = meshes;
	}
	
	public int getVersion() {
		return version;
	}
	
	public void setVersion( int version ) {
		this.version = version;
	}
	
	public String getCommandLine() {
		return commandLine;
	}
	
	public void setCommandLine( String commandLine ) {
		this.commandLine = commandLine;
	}
	
	
	
}
