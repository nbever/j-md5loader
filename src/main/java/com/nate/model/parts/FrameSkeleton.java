package com.nate.model.parts;

import com.nate.model.types.Vector3d;
import com.nate.model.types.Vector4d;

public class FrameSkeleton<T extends Number> {

	private SkeletonJoint<T>[] joints;
	
	public FrameSkeleton( FrameSkeleton<T> copy ){
		
		this( copy.getSkeletonJoints().length );
		
		for ( int i = 0; i < copy.getSkeletonJoints().length; i++ ){
			SkeletonJoint<T> oldJoint = copy.getSkeletonJoints()[i];
			SkeletonJoint<T> newJoint = new SkeletonJoint<T>();
			
			newJoint.setOrientation( new Vector4d<T>( oldJoint.getOrientation().getU(),
													  oldJoint.getOrientation().getV(),
													  oldJoint.getOrientation().getZ(),
													  oldJoint.getOrientation().getW() ) );
			
			newJoint.setPosition(  new Vector3d<T>( oldJoint.getPosition().getU(),
													oldJoint.getPosition().getV(),
													oldJoint.getPosition().getZ() ) );
			
			newJoint.setParent( oldJoint.getParent().intValue() );
			
			joints[i] = newJoint;
		}
	}
	
	public FrameSkeleton( int size ){
		joints = new SkeletonJoint[ size ];
	}
	
	public SkeletonJoint<T>[] getSkeletonJoints(){
		return joints;
	}
}
