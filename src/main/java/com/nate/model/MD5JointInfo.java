package com.nate.model;

public class MD5JointInfo {

	private String name;
	private int parent;
	private int startIndex;
	private int flags;

	public static MD5JointInfo parseLine( String line ) throws Exception{
		
		MD5JointInfo joint = new MD5JointInfo();
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length < 4 ){
			throw new Exception( "Invalid line format - there must be 4 items." );
		}
		
		joint.setName( tokens[0].substring(  1, tokens[0].length() - 1 ) );
		joint.setParent( Integer.parseInt( tokens[1].trim() ) );
		joint.setFlags( Integer.parseInt( tokens[2].trim() ) );
		joint.setStartIndex( Integer.parseInt(  tokens[3].trim() ) );
		
		return joint;
	}
	
	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public int getParent() {
		return parent;
	}

	public void setParent( int parent ) {
		this.parent = parent;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex( int startIndex ) {
		this.startIndex = startIndex;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags( int flags ) {
		this.flags = flags;
	}
	
	
}
