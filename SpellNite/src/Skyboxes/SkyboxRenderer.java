/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Skyboxes;

import Entities.Camera;
import Models.RawModel;
import RenderEngine.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

/**
 *
 * @author johnn
 */
public class SkyboxRenderer {
    private static final float SIZE = 500f;
    //Veritices for the cube of the skybox
    private static final float[] VERTICES = {        
        -SIZE,  SIZE, -SIZE,
        -SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
         SIZE, -SIZE, -SIZE,
         SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,

        -SIZE, -SIZE,  SIZE,
        -SIZE, -SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE,  SIZE,
        -SIZE, -SIZE,  SIZE,

         SIZE, -SIZE, -SIZE,
         SIZE, -SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE, -SIZE,
         SIZE, -SIZE, -SIZE,

        -SIZE, -SIZE,  SIZE,
        -SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE, -SIZE,  SIZE,
        -SIZE, -SIZE,  SIZE,

        -SIZE,  SIZE, -SIZE,
         SIZE,  SIZE, -SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
        -SIZE,  SIZE,  SIZE,
        -SIZE,  SIZE, -SIZE,

        -SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE,  SIZE,
         SIZE, -SIZE, -SIZE,
         SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE,  SIZE,	   
    };
    //Names of files
    private static String[] TEXTURE_FILES = {"right", "left", "up", "down", "back", "front"};
    private RawModel cube;
    private int texture;
    private SkyboxShader shader;
    
    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix){
        cube = loader.loadToVAO(VERTICES, 3);
        texture = loader.loadCubeMap(TEXTURE_FILES);
        shader = new SkyboxShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    
    public void render(Camera camera){
        //Renders by using draw arrays to make the cube and creates a cube map as the texture
        shader.start();
        shader.loadViewMatrix(camera);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
    
    
}
