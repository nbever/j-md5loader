package com.nate;

import com.nate.display.MainDisplay;
import com.nate.util.SharedLibraryLoader;


public class Main {

	public static void main(String[] args) {
		
		SharedLibraryLoader.load();
		
		MainDisplay display = new MainDisplay();
		display.run();
	}

}
