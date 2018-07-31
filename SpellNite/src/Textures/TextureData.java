/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Textures;

import java.nio.ByteBuffer;

/**
 *
 * @author johnn
 */
public class TextureData {
    private int width;
    private int height;
    private ByteBuffer buffer;
//HOlds th e textures as bytes to get 
    public TextureData(ByteBuffer buffer, int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
    
    
    
    
}
