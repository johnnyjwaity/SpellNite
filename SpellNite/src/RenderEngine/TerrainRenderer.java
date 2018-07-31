/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RenderEngine;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import Shaders.TerrainShader;
import Terrains.Terrain;
import Textures.ModelTexture;
import Textures.TerrainTexturePack;
import ToolBox.Maths;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author johnn
 */
public class TerrainRenderer {
    
    private TerrainShader shader;
    
    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        
        //createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }
    
    public void render(List<Terrain> terrains){
        for(Terrain terrain: terrains){
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTexturedModel();
        }
    }
    
    private void prepareTerrain(Terrain terrain){
        //Gets the vertex data about the model
        RawModel model = terrain.getModel();
        //Gets the VAO of the model
        GL30.glBindVertexArray(model.getVaoID());
        //Enables things in the attibutes
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        bindTextures(terrain);
        shader.loadShineVariables(1, 0);
       
    }
    
    private void bindTextures(Terrain terrain){
        TerrainTexturePack texturePack = terrain.getTexturePack();
         //Activates testures in the memory
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        //Binds Texture to teh model
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
        
        //Activates testures in the memory
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        //Binds Texture to teh model
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
        
        //Activates testures in the memory
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        //Binds Texture to teh model
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
        
        //Activates testures in the memory
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        //Binds Texture to teh model
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
        
        //Activates testures in the memory
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        //Binds Texture to teh model
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
        
    }
    
    private void unbindTexturedModel(){
        //Disables Then after use
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
    private void loadModelMatrix(Terrain terrain){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
