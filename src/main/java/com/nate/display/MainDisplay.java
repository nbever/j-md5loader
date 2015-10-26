package com.nate.display;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.md5reader2.md5.MD5Mesh;
import org.md5reader2.md5.MD5Triangle;
import org.md5reader2.md5.MD5Vertex;
import org.md5reader2.parser.MD5AnimParser;
import org.md5reader2.parser.MD5MeshParser;
import org.md5reader2.parser.MD5Parser;

import com.nate.model.MD5Animation;
import com.nate.model.MD5Loader;
import com.nate.model.MD5Model;

import java.io.File;
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
		URL animUrl = MD5Animation.class.getResource( "/boblampclean.md5anim" );
//		URL url = MD5Model.class.getResource( "/hakuhoseamed.md5mesh" );
		
		
		try {

			model = MD5Loader.loadModel( url.getFile() );
			model.setAnimation( MD5Loader.loadAnimation( animUrl.getFile() ) );
			
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
		
		URL url = MD5Model.class.getResource( "/boblampclean.md5mesh" );
		URL animUrl = MD5Animation.class.getResource( "/boblampclean.md5anim" );
		
		MD5Parser meshParser = new MD5MeshParser();
		org.md5reader2.md5.MD5Model jModel = meshParser.parseModel( url.getFile() );
		
		MD5Parser animParser = new MD5AnimParser( jModel );
		org.md5reader2.md5.MD5Animation jAnim = animParser.parseAnimation( animUrl.getFile() );
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( glfwWindowShouldClose(window) == GL_FALSE ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			
			glPushMatrix();
			
				glPointSize( 12.0f );
				glColor3f( 1.0f, 1.0f, 1.0f );
				glBegin( GL_POINTS );
					glVertex3f( 0.0f, 0.0f, 0.0f );
				glEnd();
			
//			glScalef( 0.9f, 0.9f, 0.9f );
//			glBegin( GL_QUADS );
//				glColor3f( 1.0f, 0.0f, 0.0f );
//				glVertex3f( -1.0f, 1.0f, 0.0f );
//				glColor3f( 0.0f, 1.0f, 0.0f );
//				glVertex3f( 1.0f, 1.0f, 0.0f );
//				glColor3f( 0.0f, 0.0f, 1.0f );
//				glVertex3f( 1.0f, -1.0f, 0.0f );
//				glColor3f( 1.0f, 1.0f, 1.0f );
//				glVertex3f( -1.0f, -1.0f, 0.0f );
//			glEnd();
			
			glPopMatrix();
			
			glPushMatrix();
				glScalef( 0.01f, 0.01f, 0.01f );
				glRotatef( -90.0f, 1.0f, 0.0f, 0.0f );
				glColor3f( 1.0f, 0.0f, 0.0f );
				renderJModel( jModel );
			glPopMatrix();
			
			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			
			
		}
		
	}
	
	private void renderJModel( org.md5reader2.md5.MD5Model jModel ){
		
		for ( MD5Mesh mesh : jModel.getMeshes() ){
		
			jModel.constructMesh( mesh );
			
			glBegin( GL_TRIANGLES );
			
				for ( int i = 0; i < mesh.getTriangles().length; i++ ){
					
					MD5Triangle tri = mesh.getTriangles()[i];
					
					MD5Vertex v1 = mesh.getVertices()[tri.getIndex()[0]];
					MD5Vertex v2 = mesh.getVertices()[tri.getIndex()[1]];
					MD5Vertex v3 = mesh.getVertices()[tri.getIndex()[2]];

					glVertex3f( v1.getPosition().getX(), v1.getPosition().getY(), v1.getPosition().getZ() );
//					glNormal3f( v1.getNormal().getU(), v1.getNormal().getV(), v1.getNormal().getZ() );
					
					glVertex3f( v2.getPosition().getX(), v2.getPosition().getY(), v2.getPosition().getZ() );
//					glNormal3f( v2.getNormal().getU(), v2.getNormal().getV(), v2.getNormal().getZ() );
					
					glVertex3f( v3.getPosition().getX(), v3.getPosition().getY(), v3.getPosition().getZ() );
//					glNormal3f( v3.getNormal().getU(), v3.getNormal().getV(), v3.getNormal().getZ() );
				}
			
			glEnd();
		
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
