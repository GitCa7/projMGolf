package com.mygdx.map;

import javax.vecmath.Vector3f;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;



public class courseExample extends ApplicationAdapter {
    ShaderProgram shader;
    Mesh mesh;
    Matrix4 matrix = new Matrix4();
    PerspectiveCamera camera;
    terrainGraph terrain;
    
    customController camController;
    private int width,length,squareLength;
    
    private int start = -5;
    
    public courseExample(int width, int length, int squareLength)	{
    	this.width = width;
    	this.length = length;
    	this.squareLength = squareLength;
    }
    
    @Override
    public void create() {
        String vertexShader = "attribute vec4 a_position;    \n"
                + "attribute vec4 a_color;\n"
                + "attribute vec2 a_texCoord0;\n"
                + "uniform mat4 u_worldView;\n"
                + "varying vec4 v_color;"
                + "varying vec2 v_texCoords;"
                + "void main()                  \n" + "{                            \n"
                + "   v_color = vec4(0.5,0.5,0.5,1); \n"
                + "   v_texCoords = a_texCoord0; \n"
                + "   gl_Position =  u_worldView * a_position;  \n"
                + "}                            \n";
        String fragmentShader = "#ifdef GL_ES\n"
                + "precision mediump float;\n"
                + "#endif\n"
                + "varying vec4 v_color;\n"
                + "varying vec2 v_texCoords;\n"
                + "uniform sampler2D u_texture;\n"
                + "void main()                                  \n"
                + "{                                            \n"
                + "  gl_FragColor = v_color;\n"
                + "}";

        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) {
            Gdx.app.log("ShaderTest", shader.getLog());
            Gdx.app.exit();
        }

        camera = new PerspectiveCamera(50f, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        camera.far = 1000;
        camera.position.set(70f, -40f, 100f);
        camera.up.set(0, 1, 0);
        camera.direction.set(0, 0, -1);
        camera.rotateAround(new Vector3(), Vector3.X, 60);
        camera.update();
        

        //camController = new customController(camera);
        camController = new customController(camera);
        
        Gdx.input.setInputProcessor(camController);

        
         
        terrain = new terrainGraph(width,length,squareLength);
        terrain.raiseInRange(10, 30, 10, 30, 5);
        terrain.raiseInRange(100, 150, 100, 150, -5);
        terrain.raiseInRange(190, 230, 100, 150, 5);
        
        //mesh = GeometryBuilder.buildPlane(width,length,squareLength);
        camera.position.set(130f, -300f, 55f);
    }


    @Override
    public void render() {
    	mesh = terrain.getMesh();
    	camera.update();
        
    	//Vector3 camCoord = camera.position;
    	//System.out.println("Position of camera:\nX = " + camCoord.x + "\t\tY = " + camCoord.y + "\t\tZ = " + camCoord.z + "\n");
    	//
    	
    	
    	
        /*
        if(camController.placeDragButtonpressed)	{
        	System.out.println("X: " + camController.getxClicked() + "\nY: " + camController.getyClicked());
        	
        	try {
        	    Thread.sleep(250);                 //1000 milliseconds is one second.
        	} catch(InterruptedException ex) {
        	    Thread.currentThread().interrupt();
        	}
        }
        
        if(camController.placeDragButtonpressed)	{
        	getCursorPos(camera.getPickRay(camController.clickedX, camController.clickedY));
        	try {
        	    Thread.sleep(250);                 //1000 milliseconds is one second.
        	} catch(InterruptedException ex) {
        	    Thread.currentThread().interrupt();
        	}
        }
        */

        Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl20.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shader.begin();
        shader.setUniformMatrix("u_worldView", camera.combined);
        mesh.render(shader, GL20.GL_LINES);
        //mesh.render(shader, GL20.GL_TEXTURE_CUBE_MAP);
        shader.end();
    }
    
    
    public void getCursorPos(Ray cameraRay)	{
    	
    	float distance = -cameraRay.origin.y / cameraRay.direction.y;
    	Vector3 tmpVector = new Vector3();
    	tmpVector.set(cameraRay.direction).scl(distance).add(cameraRay.origin);
    	
    	System.out.println("X: " + tmpVector.x + "\tY: " + tmpVector.y + "\tZ: " + tmpVector.z + "\n\n");
    	
    	
    }

    @Override
    public void dispose() {
        mesh.dispose();
        shader.dispose();
    }

}