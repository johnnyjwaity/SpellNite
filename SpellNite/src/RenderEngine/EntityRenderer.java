/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RenderEngine;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import Shaders.StaticShader;
import Textures.ModelTexture;
import ToolBox.Maths;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

/**
 *
 * @author johnn
 */
public class EntityRenderer {
    
    
    
    
    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        
        //createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    
    
    
    
    
    
    
    public void render(Map<TexturedModel, List<Entity>> entities){
        for(TexturedModel model : entities.keySet()){
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for(Entity entity: batch){
                prepareInstance(entity);
                //Draws Model
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
                
            }
            unbindTexturedModel();
        }
    }
    private void prepareTexturedModel(TexturedModel texModel){
        //Gets the vertex data about the model
        RawModel model = texModel.getRawModel();
        //Gets the VAO of the model
        GL30.glBindVertexArray(model.getVaoID());
        //Enables things in the attibutes
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = texModel.getTexture();
        if(texture.isHasTransparency()){
            MasterRenderer.toggleCulling(false);
        }
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        //Activates testures in the memory
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        //Binds Texture to teh model
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texModel.getTexture().getId());
    }
    private void unbindTexturedModel(){
        MasterRenderer.toggleCulling(true);
        //Disables Then after use
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
    private void prepareInstance(Entity entity){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }
    

    
}
