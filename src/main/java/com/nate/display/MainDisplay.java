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
		glLoadIdentity();
		glOrtho( 0, 800, 0, 600, 300, -100 );
		glMatrixMode( GL_MODELVIEW );
		glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
		
		long time = Instant.now().toEpochMilli();
		float msPerFrame = 1000 / 60;
		float msSince = 0;

		float rotateZ = 0.0f;
		
		glTranslatef( (WIDTH / 2.0f ), 0.0f, 0.0f );
		
		float[] coords = { 0.5f, 0.5f, 5.0f, // V0
				-0.5f, 0.5f, 5.0f, // V1
				-0.5f, -0.5f, 5.0f, // V2
				0.5f, -0.5f, 5.0f, // V3
				0.5f, -0.5f, 4.5f, // V4
				0.5f, 0.5f, 4.5f, // V5
				-0.5f, 0.5f, 4.5f, // V6
				-0.5f, -0.5f, 4.5f, // V7
		};
			
		int[] indices = { 0, 1, 2, 3, // Front face
				5, 0, 3, 4, // Right face
				5, 6, 7, 4, // Back face
				5, 6, 1, 0, // Upper face
				1, 6, 7, 2, // Left face
				7, 4, 3, 2, // Bottom face
		};
		
		FloatBuffer coordB = BufferUtils.createFloatBuffer( coords.length );
		IntBuffer indiceB = BufferUtils.createIntBuffer( indices.length );
		
		coordB.put( coords );
		indiceB.put( indices );
		
		coordB.flip();
		indiceB.flip();
		
		URL url = MD5Model.class.getResource( "/fish/fish.md5mesh" );
		URL animUrl = MD5Animation.class.getResource( "/fish/fish.md5anim" );
		
		MD5Parser meshParser = new MD5MeshParser();
		org.md5reader2.md5.MD5Model jModel = meshParser.parseModel( url.getFile() );
		
		MD5Parser animParser = new MD5AnimParser( jModel );
		org.md5reader2.md5.MD5Animation jAnim = animParser.parseAnimation( animUrl.getFile() );
		
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
			
//		    static ElapsedTime elapsedTime( 1/30.0f );
//		    float fDeltaTime = elapsedTime.GetElapsedTime();
//		    
//		    g_Model.Update( fDeltaTime );
			
//			model.update( msSince );
			
			currentAngle += getTurnAmount();
			currentAngle %= 360.0f;
		
			glRotatef( getTurnAmount(), 0.0f, 1.0f, 0.0f );
			glColor3f( 1.0f, 1.0f, 1.0f );
			
			glPushMatrix();
			
			glTranslatef( 0.0f, 300.0f, 100.0f );
			glRotatef( rotateZ, 0.0f, 0.0f, 1.0f );
			glScalef( 20.0f, 20.0f, 20.0f);
			
			glEnableClientState( GL_VERTEX_ARRAY );
			
			glVertexPointer( 3, 0, coordB );
			glDrawElements( GL_TRIANGLES, indiceB );
			
			glDisableClientState( GL_VERTEX_ARRAY );
			
			glPopMatrix();
			
//			glPushMatrix();
//			model.render();
//			glPopMatrix();
			
			glPushMatrix();
			renderJModel( jModel );
			glPopMatrix();
			
			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			
			
		}
		
	}
	
	private void renderJModel( org.md5reader2.md5.MD5Model jModel ){
		
		
		glTranslatef( 0.0f, 300.0f, 0.0f );
		glScalef( 500.0f, 500.0f, 500.0f );
		glRotatef( -90.0f, 1.0f, 0.0f, 0.0f );
		
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
