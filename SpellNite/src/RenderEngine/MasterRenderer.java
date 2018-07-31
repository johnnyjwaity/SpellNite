/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RenderEngine;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Models.TexturedModel;
import Shaders.StaticShader;
import Shaders.TerrainShader;
import Skyboxes.SkyboxRenderer;
import Terrains.Terrain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

/**
 *
 * @author johnn
 */
public class MasterRenderer {
    
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
    
    private static final float SKY_RED = 0.12f;
    private static final float SKY_BLUE = 0.31f;
    private static final float SKY_GREEN = 0.68f;
    
    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();
    private SkyboxRenderer skyboxRenderer;
    
    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();
    
    private Matrix4f projectionMatrix;

    public MasterRenderer(Loader loader) {
        toggleCulling(true);
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }
    
    
    
    
    
    public void render(Light sun, Camera camera){
        //Starts shaders of seperate things and runs respective render methods for terrains and entites
        prepare();
        shader.start();
        shader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        terrainShader.loadLight(sun);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        
        skyboxRenderer.render(camera);
        
        terrains.clear();
        entities.clear();
    }
    
    public static void toggleCulling(boolean on){
        if(on){
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glCullFace(GL11.GL_BACK);
        }
        else{
            GL11.glDisable(GL11.GL_CULL_FACE);
        }
    }
    
    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if(batch!=null){
            batch.add(entity);
        }
        else{
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
        
    }
    public void processTerain(Terrain terrain){
        terrains.add(terrain);
    }
    
    
    public void cleanUp(){
        //Cleans up shaders
        shader.cleanUp();
        terrainShader.cleanUp();
    }
    
    public void prepare(){
        //Background Color
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(SKY_RED, SKY_GREEN, SKY_BLUE, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
    }
    
    private void createProjectionMatrix(){
        //Creates Projection matrix which cretes a perspective view for it
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
}
