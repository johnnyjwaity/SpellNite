/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengltest;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Entities.Player;
import GUI.GuiRenderer;
import GUI.GuiTexture;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import Models.RawModel;
import Models.TexturedModel;
import Parser.ModelData;
import Parser.OBJFileLoader;
import RenderEngine.MasterRenderer;
import RenderEngine.OBJLoader;

import Terrains.Terrain;

import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author johnn
 */
public class OpenGLTest {
   
//    public static void main(String[] args) {
//        //-Djava.library.path="H:\natives"
//        
//        
//        DisplayManager.createDisplay();
//        
//        Loader loader = new Loader();
//        
//        
//        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
//        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
//        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
//        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
//        
//        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
//        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
//        
//       
//        RawModel model = OBJFileLoader.loadOBJ("myTree2", loader);
//        ModelTexture texture = new ModelTexture(loader.loadTexture("myTree"));
//        TexturedModel texModel = new TexturedModel(model, texture);
//        texture.setShineDamper(10);
//        texture.setReflectivity(1);
//        Entity entity = new Entity(texModel, new Vector3f(50,10,-50), 0, 180, 0, 2);
//        
//        RawModel model2 = OBJLoader.loadObjModel("lowPolyTree", loader);
//        //RawModel model = OBJFileLoader.loadOBJ("brickCube45");
//        ModelTexture texture2 = new ModelTexture(loader.loadTexture("lowPolyTree"));
//        TexturedModel texModel2 = new TexturedModel(model2, texture2);
//        texture2.setShineDamper(10);
//        texture2.setReflectivity(1);
//        Entity entity2 = new Entity(texModel2, new Vector3f(100,0,-50), 0, 0, 0, 0.5f);
//        
//        RawModel model3 = OBJLoader.loadObjModel("grassModel", loader);
//        //RawModel model = OBJFileLoader.loadOBJ("brickCube45");
//        ModelTexture texture3 = new ModelTexture(loader.loadTexture("grassTexture"));
//        TexturedModel texModel3 = new TexturedModel(model3, texture3);
//        texture3.setShineDamper(10);
//        texture3.setReflectivity(1);
//        texture3.setHasTransparency(true);
//        texture3.setUseFakeLighting(true);
//        Entity entity3 = new Entity(texModel3, new Vector3f(100, 0, -25), 0, 0, 0, 1);
//        
//        
//        RawModel model4 = OBJLoader.loadObjModel("fern", loader);
//        //RawModel model = OBJFileLoader.loadOBJ("brickCube45");
//        ModelTexture texture4 = new ModelTexture(loader.loadTexture("fern"));
//        TexturedModel texModel4 = new TexturedModel(model4, texture4);
//        texture4.setShineDamper(10);
//        texture4.setReflectivity(1);
//        texture4.setHasTransparency(true);
//        Entity entity4 = new Entity(texModel4, new Vector3f(100, 0, -25), 0, 0, 0, 1);
//        
//        RawModel hwm = OBJLoader.loadObjModel("fern", loader);
//        ModelTexture hwt = new ModelTexture(loader.loadTexture("texture2"));
//        TexturedModel tmm = new TexturedModel(hwm, hwt);
//        hwt.setShineDamper(10);
//        hwt.setReflectivity(1);
//        hwt.setHasTransparency(true);
//        Entity hw = new Entity(tmm, new Vector3f(25, 10, -25), 0, 0, 0, 0);
//        
//        
//        
//        
//        RawModel playerModel = OBJFileLoader.loadOBJ("person", loader);
//        ModelTexture playerTex = new ModelTexture(loader.loadTexture("playerTexture"));
//        TexturedModel plr = new TexturedModel(playerModel, playerTex);
//        Player player = new Player(plr, new Vector3f(25, 0, -25), 0, 0, 0, 1);
//        
//        
//        RawModel voxel = OBJFileLoader.loadOBJ("voxelTest", loader);
//        ModelTexture tex = new ModelTexture(loader.loadTexture("voxelTest"));
//        TexturedModel vx = new TexturedModel(voxel, tex);
//        Entity v = new Entity(vx, new Vector3f(25, 0, -25), 0, 0, 0, 0);
//        
//        
//        Light light = new Light(new Vector3f(2000, 2000, 2000), new Vector3f(1, 1, 1));
//        
//        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
//        //Terrain terrain2 = new Terrain(1, -1, loader, texturePack, blendMap, "heightmap");
//        
//        Camera camera = new Camera(player);
//        
//        
//        MasterRenderer renderer = new MasterRenderer();
//        
//        TexturedModel[] allModels = {texModel4, texModel3, texModel2, texModel};
//        List<Entity> entitiesToRender = new ArrayList<>();
//        Random r = new Random();
//        for(int i = 0; i < 400; i++){
//            float x = r.nextInt(800);
//            float z = -r.nextInt(800);
//            float y = terrain.getHeightOfTerrain(x, z);
//            Entity e = new Entity(allModels[r.nextInt(allModels.length-1)], new Vector3f(x, y, z), 0, 0, 0, 1);
//            //e.getModel().getTexture().setHasTransparency(true);
//            entitiesToRender.add(e);
//        }
//        
//        entitiesToRender.add(v);
//        
//        
//        
//        
//        List<GuiTexture> guis = new ArrayList<>();
//        GuiTexture g = new GuiTexture(loader.loadTexture("texture2"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//        //guis.add(g);
//        
//        GuiRenderer guiRenderer = new GuiRenderer(loader);
//        
//        
//        
//        while(!Display.isCloseRequested()){
//            
//            //entity.increasePosition(0f, 0, -0.12f);
//            player.move(terrain);
//            camera.move();
//            
//            
//            for(Entity e : entitiesToRender){
//                renderer.processEntity(e);
//            }
//            
//            
//            
//            renderer.processEntity(hw);
//            renderer.processEntity(player);
//            renderer.processTerain(terrain);
//            
//            renderer.render(light, camera);
//            guiRenderer.render(guis);
//            DisplayManager.updateDisplay();
//        }
//        guiRenderer.cleanUp();
//        renderer.cleanUp();
//        loader.cleanUp();
//        DisplayManager.closeDisplay();
//        
//    }
    
    
    
}


