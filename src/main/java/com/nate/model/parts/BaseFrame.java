package com.nate.model.parts;


public class BaseFrame<T extends Number>{

	private Integer index;
	private BaseFrameData<T>[] data;
	
	@SuppressWarnings("unchecked")
	public void initializeData( int num ){
		
		data = new BaseFrameData[num];
	}
	
	public Integer getIndex(){
		return index;
	}
	
	public void setIndex( Integer anIndex ){
		index = anIndex;
	}
	
	public BaseFrameData<T>[] getFrameData(){
		return data;
	}
}
