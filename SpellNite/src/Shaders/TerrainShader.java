/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shaders;

import Entities.Camera;
import Entities.Light;
import ToolBox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author johnn
 */
public class TerrainShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.txt";
    
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColor;
    private int location_backgroundTexture;
    private int location_rTexture;
    private int location_gTexture;
    private int location_bTexture;
    private int location_blendMap;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        //Loads in all variables
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_skyColor = super.getUniformLocation("skyColor");
        location_backgroundTexture = super.getUniformLocation("textureSampler");
        location_rTexture = super.getUniformLocation("rTexture");
        location_gTexture = super.getUniformLocation("gTexture");
        location_bTexture = super.getUniformLocation("bTexture");
        location_blendMap = super.getUniformLocation("blendMap");
       
        
        
    }
    
    public void connectTextureUnits(){
        //In order to make the terrain how it is like a map this loads in the seperate textures
        super.loadInt(location_backgroundTexture, 0);
        super.loadInt(location_rTexture, 1);
        super.loadInt(location_gTexture ,2);
        super.loadInt(location_bTexture, 3);
        super.loadInt(location_blendMap, 4);
    }
    //Loads in aother variables
    public void loadSkyColor(float r, float g, float b){
        super.loadVector(location_skyColor, new Vector3f(r,g,b));
    }
    
    public void loadShineVariables(float damper, float reflect){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflect);
    }
    
    
    public void loadLight(Light light){
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColor, light.getColor());
    }
    
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }
    
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
    
    public void loadViewMatrix(Camera cam){
        Matrix4f viewMatrix = Maths.createViewMatrix(cam);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }
}
