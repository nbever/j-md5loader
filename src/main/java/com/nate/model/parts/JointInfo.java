package com.nate.model.parts;

public class JointInfo {

	private String name;
	private Integer parentIndex;
	private Integer flags;
	private Integer startIndex;
	
	public static JointInfo parseLine( String line ) throws Exception{
		
		JointInfo joint = new JointInfo();
		
		String[] tokens = line.split( "[ ,\t]" );
		
		if ( tokens.length < 4 ){
			throw new Exception( "Invalid line format - there must be 4 items." );
		}
		
		joint.setName( tokens[0].substring(  1, tokens[0].length() - 1 ) );
		joint.setParentIndex( Integer.parseInt( tokens[1].trim() ) );
		joint.setFlags( Integer.parseInt( tokens[2].trim() ) );
		joint.setStartIndex( Integer.parseInt(  tokens[3].trim() ) );
		
		return joint;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getParentIndex() {
		return parentIndex;
	}
	
	public void setParentIndex(Integer parentIndex) {
		this.parentIndex = parentIndex;
	}
	
	public Integer getFlags() {
		return flags;
	}
	
	public void setFlags(Integer flags) {
		this.flags = flags;
	}
	
	public Integer getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
}
