package com.nate.display;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.nate.model.MD5Animation;
import com.nate.model.MD5Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.time.Instant;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.macosx.Unistd.getpid;

public class MainDisplay {

	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	
	int WIDTH = 800;
	int HEIGHT = 600;

	// The window handle
	private long window;
	
	private Float turnAmount = 0.0f;
	private Float flipAmount = 0.0f;
	
	public void run() {
		System.out.println("Hello LWJGL " + Sys.getVersion() + "!");
		System.out.println( getpid() );
		System.out.println("Running on thread: " + (System.getenv().get("JAVA_STARTED_ON_FIRST_THREAD_" + getpid())));
		
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
						setTurn( -5.0f + turnAmount );
					}
//					else if ( action == GLFW_RELEASE ){
//						setTurn( 0.0f );
//					}
				}
				else if ( key == GLFW_KEY_LEFT ){
					
					if ( action == GLFW_PRESS ){
						setTurn( 5.0f + turnAmount);
					}
//					else if ( action == GLFW_RELEASE ){
//						setTurn( 0.0f );
//					}
				}
				else if ( key == GLFW_KEY_UP ){
					if ( action == GLFW_PRESS ){
						setFlipAmount( getFlipAmount() + 5.0f );
					}
				}
				else if ( key == GLFW_KEY_DOWN ){
					if ( action == GLFW_PRESS ){
						setFlipAmount( getFlipAmount() - 5.0f );
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
		
		glOrtho( aspect * -1.0f, aspect, -1, 1, -10, 10 );
		
		glMatrixMode( GL_MODELVIEW );
		glPolygonMode( GL_FRONT, GL_FILL );

		MD5Model newModel = null;
		MD5Animation anim = null;
		
		try {

//			Integer texId = loadTexture( "pinky_d.tga" );
//			
//			URL newUrl = MD5Model.class.getResource( "/pinky.md5mesh" );
//			newModel = MD5Model.loadModel( newUrl.getFile(), texId );
//			
//			URL animUrl = MD5Model.class.getResource( "/idle1.md5anim" );
//			anim = MD5Animation.loadAnimation( animUrl.getFile() );
//			
//			newModel.setAnimation( anim );

			Integer texId = loadTexture( "default_sumo_tex.png" );
			
			URL newUrl = MD5Model.class.getResource( "/base_mawashi.md5mesh" );
			newModel = MD5Model.loadModel( newUrl.getFile(), texId );
			
			URL animUrl = MD5Model.class.getResource( "/slowwalk.md5anim" );
			anim = MD5Animation.loadAnimation( animUrl.getFile() );
//			
			newModel.setAnimation( anim );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long currentTime = 0;
		long lastTime = 0;
	
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( glfwWindowShouldClose(window) == GL_FALSE ) {
			
//			advanceAnimation( animInfo );
			lastTime = currentTime;
			
			if ( anim != null ){
				
				currentTime = Instant.now().toEpochMilli();// / 5;
				
				if ( lastTime == 0 ){
					lastTime = currentTime;
				}
			}
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			glEnable( GL_DEPTH_TEST );
//			glCullFace( GL_CCW );
//			glEnable( GL_CULL_FACE );
			
			glPushMatrix();
			
				glColor3f( 1.0f, 1.0f, 0.2f );
	
				//pinky settings
//				glTranslatef( 0.0f, -0.5f, 0.0f );
//				glScalef( 0.01f, 0.01f, 0.01f );
				
				//sumosettings
				glTranslatef( 0.0f, -0.5f, 0.0f );
				glScalef( 0.3f, 0.3f, 0.3f );
				
//				glTranslatef( 5.0f, 130.0f, 0.0f );
				glRotatef( 270.0f + getFlipAmount(), 1.0f, 0.0f, 0.0f );
				glRotatef( getTurnAmount(), 0.0f, 0.0f, 1.0f );
			
				long delta = currentTime - lastTime;

				newModel.update( delta );
				newModel.render();
				
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
	
	private Integer loadTexture( String resourceName ) throws Exception{
		
		ByteBuffer byteBuffer;
		URL fileUrl = MainDisplay.class.getResource( "/" + resourceName );
		
		File file = new File( fileUrl.toURI() );
		
		if ( !file.exists() ){
			throw new FileNotFoundException();
		}
		
		FileInputStream fileIn = new FileInputStream( file );
		FileChannel fch = fileIn.getChannel();
		
		byteBuffer = BufferUtils.createByteBuffer((int)fch.size() + 1);

		while ( fch.read( byteBuffer) != -1 ) ;
		
		fileIn.close();
		fch.close();
		
		byteBuffer.flip();
		
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		
		// Decode the image
		ByteBuffer image = stbi_load_from_memory(byteBuffer, w, h, comp, 0);
		
		if ( image == null )
			throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
		
		int texId = glGenTextures();
		glBindTexture( GL_TEXTURE_2D, texId );
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		
		if ( comp.get( 0 ) == 3 ){
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get( 0 ), h.get( 0 ), 0, GL_RGB, GL_UNSIGNED_BYTE, image );
		}
		else {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w.get( 0 ), h.get( 0 ), 0, GL_RGBA, GL_UNSIGNED_BYTE, image );
		}
		
		stbi_image_free( image );
		
		return texId;
	}
	
	private void setTurn( Float amount ){
		turnAmount = (amount % 360.0f );
	}
	
	private Float getTurnAmount(){
		return turnAmount;
	}
	
	private Float getFlipAmount(){
		return flipAmount;
	}
	
	private void setFlipAmount( Float amount ){
		flipAmount = (amount % 360.0f);
	}
	
}
