/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Models.RawModel;
import RenderEngine.Loader;
import ToolBox.Maths;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

/**
 *
 * @author JohnnyWaity
 */
public class GuiRenderer {

    private final RawModel quad;

    private GuiShader shader;

    public GuiRenderer(Loader loader) {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions, 2);
        shader = new GuiShader();

    }

    public void render(List<GuiTexture> guis){
        shader.start();
        //Loads quad vao
        GL30.glBindVertexArray(quad.getVaoID());
        //gets position
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //Binds textures
        for(GuiTexture tex : guis){
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTexture());
            Matrix4f matrix = Maths.createTransformationMatrix(tex.getPosition(), tex.getScale());
            shader.loadTransformationMatrix(matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }
        //Disables
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void cleanUp(){
        shader.cleanUp();
    }


}
