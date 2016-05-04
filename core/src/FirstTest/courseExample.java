package FirstTest;


import javax.vecmath.Vector3f;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import java.util.ArrayList;



public class courseExample extends InputAdapter implements ApplicationListener    {
    ShaderProgram shader;
    Mesh mesh;
    Matrix4 matrix = new Matrix4();
    PerspectiveCamera camera;
    terrainGraph terrain;
    Boolean firstClick = true;
    Vector3 position = new Vector3();
    Vector3 firstClickV;
    Model model;
    ModelBuilder modelBuilder;
    ArrayList<ModelInstance> instances= new ArrayList<ModelInstance>();
    ArrayList<Vector3> instanceTransforms = new ArrayList();
    Environment environment;
    ModelBatch modelBatch;
    private BitmapFont font;
    private SpriteBatch batch;

    
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

        modelBatch = new ModelBatch();
        modelBuilder = new ModelBuilder();
        
        camera = new PerspectiveCamera(50f, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        camera.far = 1000;
        camera.position.set(70f, -40f, 100f);
        camera.up.set(0, 1, 0);
        camera.direction.set(0, 0, -1);
        camera.rotateAround(new Vector3(), Vector3.X, 60);
        camera.update();
        
        int cwidth=width*5;
        int clength=length*5;
         model = modelBuilder.createBox(cwidth, 5f, 3f, 
                new Material(ColorAttribute.createDiffuse(Color.RED)),
            Usage.Position | Usage.Normal);
            ModelInstance e1 = new ModelInstance(model);
            ModelInstance e2 = new ModelInstance(model);
            e1.transform.setToTranslation(new Vector3(cwidth/2,0,0));
            e2.transform.setToTranslation(new Vector3(cwidth/2,-clength,0));
            instanceTransforms.add(new Vector3(cwidth/2,0,0));
            instanceTransforms.add(new Vector3(cwidth/2,-clength,0));
            instances.add(e1);
            instances.add(e2);
        
        model = modelBuilder.createBox(5f, clength, 3f, 
                new Material(ColorAttribute.createDiffuse(Color.RED)),
            Usage.Position | Usage.Normal);
            ModelInstance e3 = new ModelInstance(model);
            ModelInstance e4 = new ModelInstance(model);
            e3.transform.setToTranslation(new Vector3(0,-clength/2,0));
            e4.transform.setToTranslation(new Vector3(cwidth,-clength/2,0));
            instanceTransforms.add(new Vector3(0,-clength/2,0));
            instanceTransforms.add(new Vector3(cwidth,-clength/2,0));
            instances.add(e3);
            instances.add(e4);    
        
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
        
        camController = new customController(camera);
        
        Gdx.input.setInputProcessor(new InputMultiplexer(this,camController));

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
         
        terrain = new terrainGraph(width,length,squareLength);
        //terrain.raiseInRange(10, 30, 10, 30, 5);
        //terrain.raiseInRange(100, 150, 100, 150, -5);
        //terrain.raiseInRange(190, 230, 100, 150, 5);
        
        //mesh = GeometryBuilder.buildPlane(width,length,squareLength);
        camera.position.set(130f, -400f, 150f);
    }

       @Override
     public boolean touchDown (int screenX, int screenY, int pointer, int button) {
         Ray ray = camera.getPickRay(screenX, screenY);
         if(Gdx.input.isKeyPressed(Keys.R)){
             System.out.println(1);
            BoundingBox out = new BoundingBox() ;
            int intersect = -1;
            for (int i=0;i<instances.size();i++){ 
                out = instances.get(i).calculateBoundingBox(out);
                out.set(out.min.add(instanceTransforms.get(i)),out.max.add(instanceTransforms.get(i)));
                //out.transform.setToTranslation(istanceTransforms.get(i));
                //System.out.println(out.toString());
                //System.out.println(ray.toString());
                if(Intersector.intersectRayBoundsFast(ray, out)){
                     intersect =i;
                    System.out.println(21);
             }
         }
         if(intersect==-1){
           return true;  
         }else{
           if(button == Buttons.RIGHT){
               System.out.println(3);
              // modelInstance.transform.setToRotation(Vector3.Z, phoneAccel.y*9);
               instances.get(intersect).transform.rotate(Vector3.Z,-20f);
               instances.get(intersect).calculateTransforms();
           }
           if(button == Buttons.LEFT){
               System.out.println(4);
               //instances.get(intersect).transform.setToRotation(Vector3.Z, 0f);
               instances.get(intersect).transform.rotate(Vector3.Z,20f);
               instances.get(intersect).calculateTransforms();
           }
         }
         return true; 
         }
         if(button == Buttons.RIGHT){
             return false;
         }
         if(Gdx.input.isKeyPressed(Keys.Z)){
             BoundingBox out = new BoundingBox() ;
            int intersect = -1;
            for (int i=0;i<instances.size();i++){ 
                out = instances.get(i).calculateBoundingBox(out);
                out.set(out.min.add(instanceTransforms.get(i)),out.max.add(instanceTransforms.get(i)));
                //out.transform.setToTranslation(istanceTransforms.get(i));
                //System.out.println(out.toString());
                //System.out.println(ray.toString());
                if(Intersector.intersectRayBoundsFast(ray, out)){
                     intersect =i;
                    System.out.println(22);
             }
            }
            if(intersect==-1){
                return true;  
            }else{
               if(intersect>=4){
               instances.remove(intersect);
               instanceTransforms.remove(intersect);
            }
               return true;
            }
         }
         if(!Intersector.intersectRayBoundsFast(camera.getPickRay(screenX, screenY), mesh.calculateBoundingBox())){
             return false;
         }
         
         if(Gdx.input.isKeyPressed(Keys.U)){
             
             //System.out.println(terrain.intersectRay(ray).toString());
             terrain.raiseInRange(terrain.intersectRay(ray).x-20+width/2,terrain.intersectRay(ray).x+width/2, -terrain.intersectRay(ray).y-20+length/2, -terrain.intersectRay(ray).y+length/2, 1);
             return true;
         }
         if(Gdx.input.isKeyPressed(Keys.D)){
             
             //System.out.println(terrain.intersectRay(ray).toString());
             terrain.raiseInRange(terrain.intersectRay(ray).x-20+width/2,terrain.intersectRay(ray).x+width/2, -terrain.intersectRay(ray).y-20+length/2, -terrain.intersectRay(ray).y+length/2, -1);
             return true;
         }
         
         if(firstClick){
            
            
            Vector3 close2 = terrain.intersectRay(ray);
            firstClickV=new Vector3(close2.x,close2.y,close2.z);
            firstClick = false;
            
         }else{
             
             Vector3 close = terrain.intersectRay(ray);
             float dist2X = close.x-firstClickV.x;
            float dist2Y = close.y-firstClickV.y;
            float dist2Z = close.z-firstClickV.z;

            model = modelBuilder.createBox(dist2X, dist2Y, 3f, 
                new Material(ColorAttribute.createDiffuse(Color.RED)),
            Usage.Position | Usage.Normal);
            ModelInstance tmp = new ModelInstance(model);
            Vector3 tmpv = firstClickV.add(new Vector3(dist2X/2,dist2Y/2,dist2Z/2));
            tmp.transform.setToTranslation(tmpv);
            instances.add(tmp);
            instanceTransforms.add(tmpv);
            firstClick=true;
            
         }
         return true;
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

        /*Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl20.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        */
        
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        modelBatch.begin(camera);
        modelBatch.render(instances,environment);
        shader.begin();
        shader.setUniformMatrix("u_worldView", camera.combined);
        mesh.render(shader, GL20.GL_LINES);
        //mesh.render(shader, GL20.GL_TEXTURE_CUBE_MAP);
        font.draw(batch, "Hello World", 200, 200);
        shader.end();
        modelBatch.end();
        batch.end();
    }
    

    @Override
    public void dispose() {
        mesh.dispose();
        shader.dispose();
    }

    @Override
    public void resize(int i, int i1) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pause() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}