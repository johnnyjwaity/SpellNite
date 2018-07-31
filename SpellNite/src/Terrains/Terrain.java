/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terrains;

import Models.RawModel;
import RenderEngine.Loader;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;
import ToolBox.Maths;
import com.sun.javafx.geom.Vec3f;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author johnn
 */
public class Terrain {
    
    private static final float SIZE = 800;
    private static final float MAX_HEIGHT = 80;
    private static final float MAX_PIXEL_COLOR = 256*256*256;
    
    private float x;
    private float z;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private float[][] heights;
    
    //Takes in the Textures to be used, position and the blend mpa to properly use the terrains
    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap){
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX*SIZE;
        this.z = gridZ*SIZE;
        this.model = generateTerrain(loader, heightMap);
        
    }
    //Generate terrani takes a height map and creates verticies for the terrain object
    private RawModel generateTerrain(Loader loader, String heightMap){
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(new File("src/res/" + heightMap + ".png"));
        } catch (IOException ex) {
            System.out.println("Image Read Error");
        }
        //System.out.println("Height: " + image.getHeight());
        int VERTEX_COUNT = image.getHeight();
        int count = VERTEX_COUNT * VERTEX_COUNT;
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];
        //Varaibles to hold the data it needs
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        //Goes through all the verticies its needs and creates the points
        for(int i=0;i<VERTEX_COUNT;i++){
                for(int j=0;j<VERTEX_COUNT;j++){
                        //x pos
                        vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                        //Y pos
                        float height = getHeight(j, i, image);
                        heights[j][i] = height;
                        vertices[vertexPointer*3+1] = height;
                        //Z Pos
                        vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                        Vector3f normal = calculateNormals(j, i, image);
                        normals[vertexPointer*3] = normal.x;
                        normals[vertexPointer*3+1] = normal.y;
                        normals[vertexPointer*3+2] = normal.z;
                        textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                        textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                        vertexPointer++;
                }
        }
        int pointer = 0;
                //Binds the indicies from the vertexes
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
                for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                        int topLeft = (gz*VERTEX_COUNT)+gx;
                        int topRight = topLeft + 1;
                        int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                        int bottomRight = bottomLeft + 1;
                        indices[pointer++] = topLeft;
                        indices[pointer++] = bottomLeft;
                        indices[pointer++] = topRight;
                        indices[pointer++] = topRight;
                        indices[pointer++] = bottomLeft;
                        indices[pointer++] = bottomRight;
                }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }
//Takes normals based on the height of the verticie that os being calclated
    private Vector3f calculateNormals(int x, int z, BufferedImage image){
        float heightL = getHeight(x-1, z, image);
        float heightR = getHeight(x+1, z, image);
        float heightD = getHeight(x, z-1, image);
        float heightU = getHeight(x, z+1, image);
        Vector3f normal = new Vector3f(heightL- heightR, 2f, heightD-heightU);
        normal.normalise();
        return normal;
    }
    
    //Returns height of terrain based on whiteness of the image at certain pixel
    private float getHeight(int x, int z, BufferedImage image){
        if(x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()){
            return 0;
        }
        //System.out.println("x: " + x + " z: " + z);
        Color c = new Color(image.getRGB(x, z), true);
        float height = MAX_HEIGHT * ((c.getRed() * c.getGreen() * c.getBlue())/MAX_PIXEL_COLOR);
        
        return height;
    }
    
    //Generates world coordinates of the height of terrain based on the pixel height
    public float getHeightOfTerrain(float worldX, float worldZ){
        float terrainX = worldX-x;
        float terrainZ = worldZ-z;
        float gridSize = SIZE / ((float)heights.length -1);
        int gridX = (int) Math.floor(terrainX/gridSize);
        int gridZ = (int) Math.floor(terrainZ/gridSize);
        if(gridX >= heights.length-1 || gridX < 0 || gridZ >= heights.length - 1 || gridZ < 0){
            return 0;
        }
        float xCoord = (terrainX % gridSize)/gridSize;
        float zCoord = (terrainZ % gridSize)/gridSize;
        float height;
        if(xCoord <= 1 - zCoord){
            //COnverts to world coords
            height = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        else{
            //TO world coords
            height = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        return height;
    }
    
}
