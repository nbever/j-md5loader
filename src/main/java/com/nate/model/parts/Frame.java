package com.nate.model.parts;

public class Frame<T extends Number> {

	private Integer index;
//	private FrameData<T>[] data;
	private Float[] data;
	
	@SuppressWarnings("unchecked")
	public void initializeData( int num ){
		
		data = new Float[num];
	}
	
	public Integer getIndex(){
		return index;
	}
	
	public void setIndex( Integer anIndex ){
		index = anIndex;
	}
	
	public Float[] getFrameData(){
		return data;
	}
	
	public void addFrameData( Float[] floats, int startIndex ){
		
		for ( int i = 0; i < floats.length; i++ ){
			getFrameData()[i + startIndex] = floats[i];
		}
	}
}
