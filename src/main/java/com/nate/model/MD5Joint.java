package com.nate.model;

public class MD5Joint {

	private String name;
	private int parent;
	
	private Vector3f position;
	private Quaternarion orientation;
	
	public static MD5Joint parse( String line ) throws Exception{
		
		MD5Joint joint = new MD5Joint();
		
		String[] tokens = line.split( "[ ,\t]" );

		if ( tokens.length < 12 ){
			throw new Exception( "Bad format for joint: " + line );
		}
		
		joint.setName( tokens[0] );
		joint.setParent( Integer.parseInt( tokens[1] ) );
		Vector3f position = new Vector3f( Float.parseFloat( tokens[3] ), 
										  Float.parseFloat( tokens[4] ), 
									      Float.parseFloat( tokens[5] ) );
		
		Float x = Float.parseFloat( tokens[8] ); 
		Float y = Float.parseFloat( tokens[9] );
		Float z = Float.parseFloat( tokens[10] );
		
		Quaternarion orientation = new Quaternarion( x, y, z, 0.0f );
		orientation.computeW();

		joint.setPosition( position );
		joint.setOrientation( orientation );
		
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
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition( Vector3f position ) {
		this.position = position;
	}
	
	public Quaternarion getOrientation() {
		return orientation;
	}
	
	public void setOrientation( Quaternarion orientation ) {
		this.orientation = orientation;
	}
	
	
}
