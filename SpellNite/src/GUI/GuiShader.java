/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import org.lwjgl.util.vector.Matrix4f;

/**
 *
 * @author JohnnyWaity
 */
public class GuiShader extends Shaders.ShaderProgram {

    private static final String VERTEX_FILE = "src/GUI/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/GUI/guiFragmentShader.txt";



    private int location_transformationMatrix;

    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }



    //Load variables
    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }
//Gives posiiton
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
