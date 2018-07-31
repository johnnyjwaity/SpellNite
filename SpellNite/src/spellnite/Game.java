package spellnite;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Entities.Skeleton;
import GUI.GuiRenderer;
import GUI.GuiTexture;
import Models.RawModel;
import Models.TexturedModel;
import Network.Communication;
import Network.NetworkObject;
import Network.SendData;
import Parser.OBJFileLoader;
import ParticleSystems.Particle;
import ParticleSystems.ParticleSystem;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import RenderEngine.OBJLoader;
import Terrains.Terrain;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import javax.swing.JOptionPane;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;









public class Game
{
  
  private Loader loader = new Loader();
  private Map<String, TexturedModel> models;
  private List<String> staticModelNames = new ArrayList();
  private List<Entity> entities = new ArrayList<>();
  private List<GuiTexture> gui = new ArrayList<>();
  private List<Entity> instantiateQuene = new ArrayList();
  private List<Entity> destroyQuene = new ArrayList();
  private Terrain t;
  private Entity p;
  public static CollisionManager cm = new CollisionManager();
  public static Camera cam;
  public static PlayerController pc;
  private int health = 3;
  
  //Network
  private boolean usingNetwork = false;
  private Map<Entity, Boolean> networkQuene = new HashMap<>();
  private Map<Integer, Entity> mySync = new HashMap<>();
  private Map<Integer, Entity> otherSync = new HashMap<>();
  private boolean update = false;
  private List<NetworkObject> otherSyncQuene = new ArrayList<>();
  private List<Integer> avaibleObjSpots = new ArrayList<>();
  private List<Integer> idQue = new ArrayList<>();
  
  public void startGame() {
    createDisplay();
    setUpTerrain();
    models = setUpModels();
    setUpEntites();
    setUpGUI();
    gameLoop();
  }
  //Creates Display Window
  public void createDisplay() {
      DisplayManager.createDisplay();
  }
  //Creates Terrain for game
  public void setUpTerrain() {
    TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
    TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
    TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
    TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
    
    TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
    TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
    
    t = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
  }
  



//Adds TExtured models to Map for easy access
  public Map<String, TexturedModel> setUpModels(){
    Map<String, TexturedModel> allTexturedModels = new HashMap();
    
    RawModel model = OBJFileLoader.loadOBJ("myTree2", loader);
    ModelTexture texture = new ModelTexture(loader.loadTexture("myTree"));
    TexturedModel texModel = new TexturedModel(model, texture);
    texture.setShineDamper(10.0F);
    texture.setReflectivity(1.0F);
    allTexturedModels.put("MyTree2", texModel);
    
    RawModel model2 = OBJLoader.loadObjModel("lowPolyTree", loader);
    
    ModelTexture texture2 = new ModelTexture(loader.loadTexture("lowPolyTree"));
    TexturedModel texModel2 = new TexturedModel(model2, texture2);
    texture2.setShineDamper(10.0F);
    texture2.setReflectivity(1.0F);
    allTexturedModels.put("LP Tree", texModel2);
    staticModelNames.add("LP Tree");
    
    RawModel model3 = OBJLoader.loadObjModel("grassModel", loader);
    
    ModelTexture texture3 = new ModelTexture(loader.loadTexture("grassTexture"));
    TexturedModel texModel3 = new TexturedModel(model3, texture3);
    texture3.setShineDamper(10.0F);
    texture3.setReflectivity(1.0F);
    texture3.setHasTransparency(true);
    texture3.setUseFakeLighting(true);
    allTexturedModels.put("Grass", texModel3);
    staticModelNames.add("Grass");
    

    RawModel model4 = OBJLoader.loadObjModel("fern", loader);
    
    ModelTexture texture4 = new ModelTexture(loader.loadTexture("fern"));
    TexturedModel texModel4 = new TexturedModel(model4, texture4);
    texture4.setShineDamper(10.0F);
    texture4.setReflectivity(1.0F);
    texture4.setHasTransparency(true);
    allTexturedModels.put("Fern", texModel4);
    staticModelNames.add("Fern");
    

//    RawModel playerModel = OBJFileLoader.loadOBJ("Player3", loader);
//    ModelTexture playerTex = new ModelTexture(loader.loadTexture("playerTex"));
//    playerTex.setHasTransparency(true);
//    TexturedModel plr = new TexturedModel(playerModel, playerTex);
//    allTexturedModels.put("Player", plr);
    
    RawModel playerModel = OBJFileLoader.loadOBJ("broomPlayer", loader);
    ModelTexture playerTex = new ModelTexture(loader.loadTexture("player"));
    playerTex.setHasTransparency(true);
    TexturedModel plr = new TexturedModel(playerModel, playerTex);
    allTexturedModels.put("Player", plr);
    
    
    RawModel headModel = OBJFileLoader.loadOBJ("head", loader);
    ModelTexture headTex = new ModelTexture(loader.loadTexture("crystal"));
    TexturedModel head = new TexturedModel(headModel, headTex);
    allTexturedModels.put("Head", head);
    
    RawModel fireballModel = OBJFileLoader.loadOBJ("fireball", loader);
    ModelTexture fireballTex = new ModelTexture(loader.loadTexture("fireballTexture"));
    fireballTex.setReflectivity(1);
    fireballTex.setShineDamper(10);
    fireballTex.setUseFakeLighting(true);
    TexturedModel fireball = new TexturedModel(fireballModel, fireballTex);
    allTexturedModels.put("Fireball", fireball);
    
    
    RawModel smkeModel = OBJFileLoader.loadOBJ("fireball", loader);
    ModelTexture smokeTex = new ModelTexture(loader.loadTexture("smoke"));
    smokeTex.setReflectivity(1);
    smokeTex.setShineDamper(10);
    smokeTex.setUseFakeLighting(true);
    TexturedModel smoke = new TexturedModel(smkeModel, smokeTex);
    allTexturedModels.put("Smoke", smoke);
    
    
    RawModel fireModel = OBJFileLoader.loadOBJ("fireball", loader);
    ModelTexture fireTex = new ModelTexture(loader.loadTexture("fire"));
    fireTex.setReflectivity(1);
    fireTex.setShineDamper(10);
    fireTex.setUseFakeLighting(true);
    TexturedModel fire = new TexturedModel(fireModel, fireTex);
    allTexturedModels.put("Fire", fire);
    
    
    RawModel castleModel = OBJFileLoader.loadOBJ("castle2", loader);
    ModelTexture castleTex = new ModelTexture(loader.loadTexture("castleTexture"));
    fireTex.setReflectivity(1);
    fireTex.setShineDamper(10);
    TexturedModel castle = new TexturedModel(castleModel, castleTex);
    allTexturedModels.put("Castle", castle);
    staticModelNames.add("Castle");
    
    RawModel wallModel = OBJFileLoader.loadOBJ("wall", loader);
    ModelTexture wallTexture = new ModelTexture(loader.loadTexture("castleWall"));
    wallTexture.setReflectivity(1);
    wallTexture.setShineDamper(10);
    TexturedModel wall = new TexturedModel(wallModel, wallTexture);
    allTexturedModels.put("Wall", wall);
    
    
    return allTexturedModels;
  }
  //Adds certain entites like player and walls to the game
  public void setUpEntites() {
    Entity head = new Entity(models.get("Head"), new Vector3f(0, 0, 0), 0, 0, 0, 2, this);
    entities.add(head);
    p = new Skeleton((TexturedModel)models.get("Player"), new Vector3f(25.0F, 0.0F, -25.0F), 0.0F, 0.0F, 0.0F, 1.0F, this, head);
    Collideable c = new Collideable(cm, 2.5F, false);
    pc = new PlayerController(t);
    p.addScript(pc);
    cm.addCollideable(c);
    p.addScript(c);
    
    entities.add(p);
    networkQuene.put(p, Boolean.FALSE);
    
    
    
//    Entity testTree = new Entity((TexturedModel)models.get("MyTree2"), new Vector3f(35.0F, 10.0F, -25.0F), 0.0F, 0.0F, 0.0F, 1.0F, this);
//    Collideable c1 = new Collideable(cm, 2.5F, true);
//    cm.addCollideable(c1);
//    testTree.addScript(c1);
//    entities.add(testTree);
    
//    Entity castle = new Entity((TexturedModel)models.get("Castle"), new Vector3f(50.0F, 10.0F, -25.0F), 0.0F, 0.0F, 0.0F, 1.0F, this);
//    Collideable c2 = new Collideable(cm, 5F, true);
//    cm.addCollideable(c2);
//    castle.addScript(c2);
//    entities.add(castle);
    
    for(int i = 0; i < 11; i ++){
        Entity wall = new Entity(models.get("Wall"), new Vector3f(-1.4f, 11, -35.0f - (72.85f * i)), 0, 0, 0, 5, this);
        entities.add(wall);
    }
    for(int i = 0; i < 11; i ++){
        Entity wall = new Entity(models.get("Wall"), new Vector3f(800.4f, 11, -35.0f - (72.85f * i)), 0, 0, 0, 5, this);
        entities.add(wall);
    }
    for(int i = 0; i < 11; i++){
        Entity wall = new Entity(models.get("Wall"), new Vector3f(36 + (72.85f * i), 11, 0), 0, 90, 0, 5, this);
        entities.add(wall);
    }
    for(int i = 0; i < 11; i++){
        Entity wall = new Entity(models.get("Wall"), new Vector3f(36 + (72.85f * i), 11, -800), 0, 90, 0, 5, this);
        entities.add(wall);
    }
    
    
    
    
  }
  
  List<GuiTexture> hearts = new ArrayList<>();
  private void setUpGUI(){
      //Sets up guis to display
      GuiTexture cursor = new GuiTexture(loader.loadTexture("cursor"), new Vector2f(0f, 0f), 50, 50);
      gui.add(cursor);
      
      GuiTexture health1 = new GuiTexture(loader.loadTexture("heart"), new Vector2f(0.06f, -0.8f), new Vector2f(0.1f, 0.2f));
      hearts.add(health1);
      
      GuiTexture health2 = new GuiTexture(loader.loadTexture("heart"), new Vector2f(-0.13f, -0.8f), new Vector2f(0.1f, 0.2f));
      hearts.add(health2);
      
      GuiTexture health3 = new GuiTexture(loader.loadTexture("heart"), new Vector2f(0.25f, -0.8f), new Vector2f(0.1f, 0.2f));
      hearts.add(health3);
      
      
  }
  
  
  Communication com = null;
  public void gameLoop(){
      
      
      genObj();
      //Creates new light source
    Light light = new Light(new Vector3f(2000.0F, 2000.0F, 2000.0F), new Vector3f(1.0F, 1.0F, 1.0F));
    Camera camera = new Camera(p);
    cam = camera;
    MasterRenderer renderer = new MasterRenderer(loader);
    GuiRenderer guiRenderer = new GuiRenderer(loader);
    
    boolean attemptedConnect = false;
    
    List<GuiTexture> tGui = new ArrayList<>();
      GuiTexture menu = new GuiTexture(loader.loadTexture("menu"), new Vector2f(0.85f, -0.75f), new Vector2f(2f, 2f));
      tGui.add(menu);
      boolean finishedMenu = false;
      
    
    
    
    while (!Display.isCloseRequested()){
        
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
          break;
        }
        
        //Shows menu until game is started
        if(!finishedMenu){
            guiRenderer.render(tGui);
            DisplayManager.updateDisplay();
            if(Keyboard.isKeyDown(Keyboard.KEY_RETURN) && !attemptedConnect){
                com = new Communication(JOptionPane.showInputDialog("Server"), Integer.parseInt(JOptionPane.showInputDialog("Port")), this);
                attemptedConnect = true;
                finishedMenu = true;
            }
            continue;
        }
        
      
      
      
      
      //Checks for win and lose
      if(wonGame){
          dispWin();
      }
      else if(lostGame){
          dispLost();
      }
      
      //Handles network sync
      if(usingNetwork){
          handleNetwork(renderer);
          updateQuenes();
      }
      
      
        
      camera.move();
//Updates all Entity scripts
      for (Entity e : entities) {
        e.updateScripts();
        renderer.processEntity(e);
      }
      //Create new objects
      for (Entity e : instantiateQuene) {
        entities.add(e);
      }
      //Destorys objects
      for(Entity e : destroyQuene){
          entities.remove(e);
          if(mySync.containsValue(e)){
              Integer eKey = 0;
              for(Integer key : mySync.keySet()){
                  if(mySync.get(key).equals(e)){
                      eKey = key;
                      break;
                  }
              }
              mySync.remove(eKey, e);
          }
      }
      instantiateQuene.clear();
      destroyQuene.clear();
      //Shows health
      switch(health){
          case 3:
              gui.add(hearts.get(2));
          case 2:
              gui.add(hearts.get(0));
          case 1:
              gui.add(hearts.get(1));
      }
      guiRenderer.render(gui);
      //REmove hearts
      switch(health){
          case 3:
              gui.remove(hearts.get(2));
          case 2:
              gui.remove(hearts.get(0));
          case 1:
              gui.remove(hearts.get(1));
      }
      //REnder Terrain
      renderer.processTerain(t);
      //Update display
      DisplayManager.updateDisplay();
      //REnder everything
      renderer.render(light, camera);
    }
    guiRenderer.cleanUp();
    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();
  }
  //Creates random trees and bushes fomr a seed
  private void genObj(){
    Random r = new Random(174843628476493l);
    for (int i = 0; i < 400; i++) {
        float x = r.nextInt(800);
        float z = -r.nextInt(800);
        float y = t.getHeightOfTerrain(x, z);
        String modelName = (String)staticModelNames.get(r.nextInt(staticModelNames.size()));
        Entity e = new Entity((TexturedModel)models.get(modelName), new Vector3f(x, y, z), 0.0F, 0.0F, 0.0F, 1.0F, this);
        entities.add(e);
        addCollisions(e, modelName);
    }
  }
  //Adds collsions to static objects so the player can collide
  private void addCollisions(Entity e, String modelName){
      float distance = 0;
      switch(modelName){
          case "LP Tree":
              distance = 2;
              break;
          case "Grass":
              return;
          case "Fern":
              return;
          case "Castle":
              distance = 5.5f;
              break;
      }
      Collideable c = new Collideable(cm, distance, true);
      e.addScript(c);
      cm.addCollideable(c);
  }
  
  private void handleNetwork(MasterRenderer render){
      //Requests objects
      for(Entity e : networkQuene.keySet()){
          if(!networkQuene.get(e)){
              networkQuene.put(e, Boolean.TRUE);
              SendData d = new SendData("OID");
              com.sendMessage(d);
          }
      }

      
      //Adds things to the mysync
      for(Integer i : avaibleObjSpots){
          if(networkQuene.isEmpty()){
              break;
          }
          Entity e = (Entity) networkQuene.keySet().toArray()[0];
          networkQuene.remove(e);
          mySync.put(i, e);
      }
      //sends objects to server
      avaibleObjSpots.clear();
      List<NetworkObject> objsToSend = new ArrayList<>();
      for(Entry<Integer, Entity> entry : mySync.entrySet()){
          NetworkObject obj = entry.getValue().toNetObj();
          obj.setClientID(com.getId());
          obj.setObjID(entry.getKey());
          for(String key: models.keySet()){
              if(entry.getValue().getModel().equals(models.get(key))){
                  obj.setTexModel(key);
                  break;
              }
          }
          objsToSend.add(obj);
      }
      //Sends Objects
      SendData obj = new SendData("OBJ");
      obj.setObjs(objsToSend);
      com.sendMessage(obj);
      //Updates the entities recieved from networks
      for(Entity e : otherSync.values()){
          render.processEntity(e);
          e.updateScripts();
      }
      
  }
  //Updates net Quenes
  private void updateQuenes(){
      avaibleObjSpots.addAll(idQue);
      idQue.clear();
      
      //CHecks to see if they need to be updated
      if(!update){
          return;
      }
      //Processes entites trecieved and either updates or adds new ones
      List<Integer> objIds = new ArrayList<>();
      for(NetworkObject n : otherSyncQuene){
          if(otherSync.containsKey(n.getObjID())){
              otherSync.get(n.getObjID()).updateFromNetObj(n);
          }
          else if(!otherSync.containsKey(n.getObjID()) && n.getClientID() != com.getId()){
              Entity e = new Entity(models.get(n.getTexModel()), n, this);
              addAppropriateScripts(e, n);
              otherSync.put(n.getObjID(), e);
          }
          
          if(n.getClientID() != com.getId()){
              objIds.add(n.getObjID());
          }
          
      }
      //REmoves things that have been destroyed
      List<Integer> idToRem = new ArrayList<>();
      for(Integer id : otherSync.keySet()){
          if(!objIds.contains(id)){
              idToRem.add(id);
          }
      }
      for(Integer id : idToRem){
          otherSync.remove(id);
      }
      update = false;
      
  }
  //Adds scripts to icpming Network Objects like paticle systems
  private void addAppropriateScripts(Entity e, NetworkObject o){
      switch(o.getTexModel()){
          case "Fireball":
              System.out.println("Adding Systems");
                Particle smoke = new Particle(models.get("Smoke"), 0.5f, "SmokeController", true);
                ParticleSystem smokeSystem = new ParticleSystem(smoke, 50, 1);
                smoke.setParticleSystem(smokeSystem);
                e.addScript(smokeSystem);
                Particle fire = new Particle(models.get("Fire"), 0.5f, "SmokeController", true);
                ParticleSystem fireSystem = new ParticleSystem(fire, 20, 1);
                fire.setParticleSystem(fireSystem);
                e.addScript(fireSystem);
      }
  }
  
  public void addObjId(int id){
      idQue.add(id);
  }
  
  public void updateNetworkObj(List<NetworkObject> newObj){
      otherSyncQuene = newObj;
      update = true;
  }
  
  public void toggleNetGame(boolean start){
      if(start){
          usingNetwork = true;
      }
  }

  public void instanstiate(Entity obj){
    instantiateQuene.add(obj);
    if(obj.getModel().equals(models.get("Fireball"))){
        networkQuene.put(obj, Boolean.FALSE);
    }
  }
  public void addToNet(Entity e){
      networkQuene.put(e, Boolean.FALSE);
  }
  public void destroy(Entity obj){
      destroyQuene.add(obj);
  }
  public void destroy(int objID){
      if(mySync.containsKey(objID)){
          entities.remove(mySync.get(objID));
          mySync.remove(objID);
      }
  }
  boolean showedWin = false;
  public void dispWin(){
      if(showedWin){
          return;
      }
      GuiTexture tex = new GuiTexture(loader.loadTexture("vr"), new Vector2f(0.1f, 0.5f), new Vector2f(0.7f, 0.3f));
      gui.add(tex);
      showedWin = true;
  }
  boolean showedLost = false;
  public void dispLost(){
      if(showedLost){
          return;
      }
      GuiTexture tex = new GuiTexture(loader.loadTexture("lost"),new Vector2f(0.3f, 0.5f), new Vector2f(0.7f, 0.3f));
      gui.add(tex);
      showedLost = true;
  }
  
  public void damage(){
      health--;
      if(health <= 0){
          com.sendMessage(new SendData("DEAD"));
          lost();
      }
  }
  private boolean lostGame = false;
  public void lost(){
      lostGame = true;
  }
  private boolean wonGame = false;
  public void win(){
      wonGame = true;
  }
  
  public Map<String, TexturedModel> getModelMap() {
    return models;
  }
}