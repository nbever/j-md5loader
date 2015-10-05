package com.nate.display;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.nate.model.MD5Loader;
import com.nate.model.MD5Model;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.time.Instant;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class MainDisplay {

	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	
	int WIDTH = 800;
	int HEIGHT = 600;

	// The window handle
	private long window;
	
	private MD5Model model;
	
	private Float currentAngle = 0.0f;
	private Float turnAmount = 0.0f;
	
	public void run() {
		System.out.println("Hello LWJGL " + Sys.getVersion() + "!");
		
		URL url = MD5Model.class.getResource( "/boblampclean.md5mesh" );
//		URL url = MD5Model.class.getResource( "/hakuhoseamed.md5mesh" );
		
		
		try {

			model = MD5Loader.loadModel( url.getFile() );
			
			init();
			loop();

			// Release window and window callbacks
			glfwDestroyWindow(window);
			keyCallback.release();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Terminate GLFW and release the GLFWerrorfun
			glfwTerminate();
			errorCallback.release();
		}
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		
		// 	Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( glfwInit() != GL11.GL_TRUE )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
		
		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				
				if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ){
					glfwSetWindowShouldClose(window, GL_TRUE); 
					// We will detect this in our rendering loop
				}
				else if ( key == GLFW_KEY_RIGHT ){
					
					if ( action == GLFW_PRESS ){
						setTurn( -5.0f );
					}
					else if ( action == GLFW_RELEASE ){
						setTurn( 0.0f );
					}
				}
				else if ( key == GLFW_KEY_LEFT ){
					
					if ( action == GLFW_PRESS ){
						setTurn( 5.0f );
					}
					else if ( action == GLFW_RELEASE ){
						setTurn( 0.0f );
					}
				}
			
			}
		});

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(
			window,
			(GLFWvidmode.width(vidmode) - WIDTH) / 2,
			(GLFWvidmode.height(vidmode) - HEIGHT) / 2
		);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the ContextCapabilities instance and makes the OpenGL
		// bindings available for use.
		//GL.createCapabilities( true );
		// valid for latest build
		GLContext.createFromCurrent(); // use this line instead with the 3.0.0a build

		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	
		glMatrixMode( GL_PROJECTION );
		glLoadIdentity();
		glOrtho( 0, 800, 0, 600, 0, -1000 );
		glMatrixMode( GL_MODELVIEW );
		glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
		
		long time = Instant.now().toEpochMilli();
		long msPerFrame = 1000 / 60;
		long msSince = 0;

		float rotateZ = 0.0f;
		
		glTranslatef( (WIDTH / 2.0f ), 0.0f, 0.0f );
		
		float[] coords = {
			0.0f, -1.0f, 50.0f,
			1.0f, 1.0f, 50.0f,
			-1.0f, 1.0f, 50.0f
		};
			
		int[] indices = {
			0, 1, 2
		};
		
		FloatBuffer coordB = BufferUtils.createFloatBuffer( coords.length );
		IntBuffer indiceB = BufferUtils.createIntBuffer( indices.length );
		
		coordB.put( coords );
		indiceB.put( indices );
		
		coordB.flip();
		indiceB.flip();
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( glfwWindowShouldClose(window) == GL_FALSE ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			//time 
			long now = Instant.now().toEpochMilli();
			msSince += now - time;
			
			if ( msSince > msPerFrame ){
				rotateZ += 1f;
				rotateZ %= 360.0f;
				time = now;
				msSince = 0;
			}
			
			currentAngle += getTurnAmount();
			currentAngle %= 360.0f;
		
			glRotatef( getTurnAmount(), 0.0f, 1.0f, 0.0f );
			glColor3f( 1.0f, 1.0f, 1.0f );
			
			glPushMatrix();
			
			glTranslatef( 0.0f, 300.0f, 100.0f );
			glRotatef( rotateZ, 0.0f, 0.0f, 1.0f );
			glScalef( 15.0f, 15.0f, 15.0f);
			
			glEnableClientState( GL_VERTEX_ARRAY );
			
			glVertexPointer( 3, 0, coordB );
			glDrawElements( GL_TRIANGLES, indiceB );
			
			glDisableClientState( GL_VERTEX_ARRAY );
			
			glPopMatrix();
			
			glPushMatrix();
			model.render();
			glPopMatrix();
			
			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			
			
		}
		
	}
	
	private void setTurn( Float amount ){
		turnAmount = amount;
	}
	
	private Float getCurrentAngle(){
		return currentAngle;
	}
	
	private Float getTurnAmount(){
		return turnAmount;
	}
	
}
