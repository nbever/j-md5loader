package com.nate.display;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.nate.model.MD5Animation;
import com.nate.model.MD5AnimationInfo;
import com.nate.model.MD5Joint;
import com.nate.model.MD5Mesh;
import com.nate.model.MD5Model;

import java.net.URL;
import java.nio.ByteBuffer;
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
	
	private Float currentAngle = 0.0f;
	private Float turnAmount = 0.0f;
	
	public void run() {
		System.out.println("Hello LWJGL " + Sys.getVersion() + "!");
		
		
		try {
		
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
		
		float aspect = (float)WIDTH / (float)HEIGHT;
		
		glOrtho( aspect * -1.0f, aspect, -1, 1, -1, 1 );
		
		glMatrixMode( GL_MODELVIEW );
		glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );

		MD5Model newModel = null;
		MD5Animation anim = null;
		
		try {
			URL newUrl = MD5Model.class.getResource( "/obj_base2_bak.md5mesh" );
			newModel = MD5Model.loadModel( newUrl.getFile() );
			
			URL animUrl = MD5Model.class.getResource( "/obj_base2_bak.md5anim" );
			anim = MD5Animation.loadAnimation( animUrl.getFile() );
//			anim = null;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MD5AnimationInfo animInfo = new MD5AnimationInfo();
		
		if ( anim != null ){
			animInfo.setCurrentFrame( 0 );
			animInfo.setNextFrame( 1 );
			animInfo.setMaxTime( 1.0 / (double)anim.getFrameRate() );
		}
		
		MD5Joint[] skeleton = newModel.getBaseSkeleton();
		
		long currentTime = 0;
		long lastTime = 0;
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( glfwWindowShouldClose(window) == GL_FALSE ) {
			
//			advanceAnimation( animInfo );
			lastTime = currentTime;
			
			if ( anim != null ){
				currentTime = (Instant.now().toEpochMilli() / anim.getFrameRate());// / 5;
			}
			
			if ( lastTime == 0 ){
				lastTime = currentTime;
			}
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			glPushMatrix();
			
			glEnableClientState( GL_VERTEX_ARRAY );
			glColor3f( 1.0f, 1.0f, 0.2f );
			glScalef( 0.4f, 0.4f, 0.4f );
//			glTranslatef( 5.0f, 130.0f, 0.0f );
			glRotatef( 270.0f, 1.0f, 0.0f, 0.0f );
			glRotatef( 90.0f, 0.0f, 0.0f, 1.0f );
			
			if ( anim != null ){
				
				anim.advanceAnimation( animInfo, currentTime - lastTime );
				skeleton = MD5Animation.interpolateSkeletons( anim.getSkeletonFrames()[animInfo.getCurrentFrame()], 
												   anim.getSkeletonFrames()[animInfo.getNextFrame()], 
												   anim.getNumberOfJoints(), 
												   (float)(animInfo.getLastTime() * anim.getFrameRate()) );
			}
			else {
				skeleton = newModel.getBaseSkeleton();
			}
			
			for ( int mi = 0; mi < newModel.getNumberOfMeshes(); mi++ ){
				
				glPushMatrix();
				
				MD5Mesh mesh = newModel.getMeshes()[mi];
				
				newModel.prepareModel( mesh, skeleton );
				mesh.getIndexArray().flip();
				mesh.getVertexArray().flip();
				
				glVertexPointer( 3, 0, mesh.getVertexArray() );
				glDrawElements( GL_TRIANGLES, mesh.getIndexArray() );
				
				glPopMatrix();
			}
			
			glDisableClientState( GL_VERTEX_ARRAY );
			
			glPopMatrix();
			
			glPushMatrix();
			
				glPointSize( 6.0f );
				glColor3f( 1.0f, 1.0f, 1.0f );
				glBegin( GL_POINTS );
					glVertex3f( 0.0f, 0.0f, 0.0f );
				glEnd();

			
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
