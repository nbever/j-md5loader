package com.nate.model;

import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

public class TestModelLoader {

	@Test
	public void testMD5Loading() throws Exception{
		
		URL url = MD5Model.class.getResource( "/boblampclean.md5mesh" );
		
		MD5Model model = MD5Loader.loadModel( url.getFile() );
		
		assertTrue( model.getVersion() == 10 );
		assertTrue( model.getJoints().length == 33 );
		assertTrue( model.getMeshes().length == 6 );
	}
	
}
