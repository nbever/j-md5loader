package com.nate.model.parts;

public class Frame<T extends Number> {

	private Integer index;
	private FrameData<T>[] data;
	
	@SuppressWarnings("unchecked")
	public void initializeData( int num ){
		
		data = new FrameData[num];
	}
	
	public Integer getIndex(){
		return index;
	}
	
	public void setIndex( Integer anIndex ){
		index = anIndex;
	}
	
	public FrameData<T>[] getFrameData(){
		return data;
	}
}
