/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RenderEngine;

import Models.RawModel;
import Textures.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author johnn
 */
public class Loader {
    //VAO Vertici Array Aobject has an ID and and a list of attributes
    private List<Integer> vaos = new ArrayList<>();
    //VBO Vertici Buffer Object Stored in the attribute list of a VAO holds information like vertex position
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();
    
    
    public RawModel loadToVAO(float[] postitions, float[] textureCoords, float[] normals, int[] indecies){
        //Creates basic information for the VAO
        int vasoID = createVAO();
        //Binds the indicies to the postions Every three positions in one index
        bindIndiciesBuffer(indecies);
        //Stores positions in an attribute List of the VAO 
        storeDataInAttributeList(0, 3, postitions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        //Unbinds the VAO so its no longer being edited
        unbindVAO();
        return new RawModel(vasoID, indecies.length);
    }
    
    public RawModel loadToVAO(float[] positions, int dimensions){
        int vaoId = createVAO();
        storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new RawModel(vaoId, positions.length/dimensions);
    }
    
    
    public int loadTexture(String fileName){
        //Loads Texture into OPENGL Memory
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("src/res/"+fileName+".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }
    //Loads image data for skybox
    public int loadCubeMap(String[] textureFiles){
        
        int texId = GL11.glGenTextures();
        GL13.glActiveTexture(0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texId);
        //Takes texture data and adds it to Cube Map
        for(int i = 0; i < textureFiles.length; i++){
            TextureData data = decodeTextureFile("src/res/" + textureFiles[i] + ".png");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }
        //Sets filters for the map to make it look nice and remove edges
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        textures.add(texId);
        return texId;
    }
    
    private TextureData decodeTextureFile(String filename){
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try{
            FileInputStream in = new FileInputStream(filename);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Tried To load texture" + filename);
            System.exit(-1);
        }
        return new TextureData(buffer, width, height);
    }
    
    
    
    private int createVAO(){
        //Gets id for VAO so its unique
        int vaoID = GL30.glGenVertexArrays();
        //Adds it to the list of created VAOS
        vaos.add(vaoID);
        //BINDs VAO for editing
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }
    private void storeDataInAttributeList(int attributreNumber, int coordinateSize, float[] data){
        //Creates a VBO with inque id
        int vboID = GL15.glGenBuffers();
        //Adds it to THe VBO
        vbos.add(vboID);
        //Binds VBO for editing
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        //A float Buffer is basically a different way storing an array floats
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        //Buffers or loads data int o opengl First param is type of data, second is the data, and third syays its not going to be edited
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
       //Sets the vbo as part of the attribute list in the vao the positions  data,size,type,editable,start,offset
        GL20.glVertexAttribPointer(attributreNumber, coordinateSize,GL11.GL_FLOAT, false,0,0);
        //Binds it to the vao
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    private void unbindVAO(){
        //Removes locks on it
        GL30.glBindVertexArray(0);
    }
    
    private void bindIndiciesBuffer(int[] indicies){
        //gets custom vbo
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        //Locks it int o the system
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        //creates a buffer for it
        IntBuffer buffer = storeDataInIntBuffer(indicies);
        //
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        
    }
    
    //Creates Int Buffer 
    private IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    //Creates Float Buffer
    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    //Clean Up Helps With Memory deletes everything loaded
    public void cleanUp(){
        for(Integer current: vaos){
            GL30.glDeleteVertexArrays(current);
        }
        
        for(Integer current: vbos){
            GL15.glDeleteBuffers(current);
        }
        for(Integer current: textures){
            GL11.glDeleteTextures(current);
        }
    }
    
}
