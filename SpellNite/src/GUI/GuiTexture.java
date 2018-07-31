/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author JohnnyWaity
 */
 //HOLDER for the variable of texture, position , and scale
public class GuiTexture {
    private int texture;
    private Vector2f position;
    private Vector2f scale;

    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;

    }
    public GuiTexture(int texture, Vector2f position, float width, float height) {
        this.texture = texture;
        //this.position = position;
        //this.position = new Vector2f(position.x - width/2, position.y - height/2);
        System.out.println(position);
        float scaleX = width / (Display.getWidth() / 2);
        float scaleY = height / (Display.getHeight()/ 2);
        this.position = new Vector2f(position.x + scaleX/2, position.y - scaleY/2);
        scale = new Vector2f(scaleX, scaleY);
    }



    public int getTexture(){
        return texture;
    }
    public Vector2f getPosition(){
        return position;
    }
    public Vector2f getScale(){
        return scale;
    }





}
