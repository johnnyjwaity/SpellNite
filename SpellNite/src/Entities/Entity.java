/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Models.TexturedModel;
import Network.NetworkObject;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import spellnite.AttachableScript;
import spellnite.Game;
import spellnite.Helpers;

/**
 *
 * @author johnn
 */
public class Entity {


    private TexturedModel model;
    private Vector3f position = new Vector3f(0, 0, 0);
    private float rotX, rotY, rotZ;
    private float scale;
    private Game game;

    List<AttachableScript> myScripts = new ArrayList<>();

    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Game game) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
        this.game = game;
    }
    public Entity(TexturedModel model, NetworkObject n, Game game){
        this.model = model;
        this.game = game;
        updateFromNetObj(n);
    }

    public void increasePosition(float dx, float dy, float dz){
      //Adds to position
        position.x+=dx;
        position.y+=dy;
        position.z += dz;
    }
    //Also adds to position
    public void increasePosition(Vector3f direction){
        position.x+=direction.x;
        position.y+=direction.y;
        position.z += direction.z;
    }

    //Adds rotation to ENtity
    public void increaseRotation(float dx, float dy, float dz){
        rotX+=dx;
        rotY+=dy;
        rotZ+=dz;

        if(rotY >= 360){
            rotY = rotY - 360;
        }
        if(rotY < 0){
            rotY = 360 - rotY;
        }
    }

    //Adds scripts and runs the start method
    public void addScript(AttachableScript s){
        myScripts.add(s);
        s.start(this);
    }

    public void updateScripts(){
      //Updates the script
        for(AttachableScript s : myScripts){
            s.update();
        }
    }


    public TexturedModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public float getScale() {
        return scale;
    }

    public Game getGame(){
        return game;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public void setPosition(Vector3f position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getForwardVector(){
      //Calculates the forward position based on its current rotation
        Vector2f rot0 = new Vector2f(0, 1);
        Vector2f rot90 = new Vector2f(1, 0);
        Vector2f rot180 = new Vector2f(0, -1);
        Vector2f rot270 = new Vector2f(-1, 0);
        //Interpolates between the above vectors
        Vector2f directionXZ= new Vector2f(0, 0);
        if(rotY >= 0 && rotY <= 90){
            float favorability = 1 - (rotY/90);
            directionXZ = interpolateVectors(rot0, rot90, favorability);
        }
        else if (rotY >= 90 && rotY <= 180){
            float favorability = 1 - ((rotY - 90)/90);
            directionXZ = interpolateVectors(rot90, rot180, favorability);
        }
        else if (rotY >= 180 && rotY <= 270){
            float favorability = 1 - ((rotY-180)/90);
            directionXZ = interpolateVectors(rot180, rot270, favorability);
        }
        else if (rotY >= 270 && rotY <= 360){
            float favorability = 1 - ((rotY-270)/90);
            directionXZ = interpolateVectors(rot270, rot0, favorability);
        }
        return new Vector3f(directionXZ.x, 0, directionXZ.y);
    }

    public Vector3f getBackwardVector(){
        Vector3f vec = getForwardVector();
        vec = Helpers.multiplyVector(vec, -1);
        return vec;
    }

    public Vector3f getRightVector(){
        Vector3f vec = getForwardVector();
        float temp = vec.x;
        vec.x = -vec.z;
        vec.z = temp;
        return vec;
    }
    public Vector3f getLeftVector(){
        Vector3f vec = getForwardVector();
        float temp = vec.x;
        vec.x = vec.z;
        vec.z = -temp;
        return vec;
    }



    private Vector2f interpolateVectors(Vector2f vector1, Vector2f vector2, float favorability){
        //System.out.println(favorability);
        //Takes the favorability vector of the vcector
        float x = vector1.x * favorability;
        x += vector2.x * (1 - favorability);

        float z = vector1.y * favorability;
        z += vector2.y * (1 - favorability);
        return new Vector2f(x, z);
    }

    public AttachableScript getScript(String className){
       for(AttachableScript s : myScripts){
           if(s.getClass().getName() == className){
               return s;
           }
       }
       return null;
    }

    public NetworkObject toNetObj(){
      //Turns to object for network
        return new NetworkObject(0, 0, null, position.getX(), position.getY(), position.getZ(), rotX, rotY, rotZ, scale);
    }

    public void updateFromNetObj(NetworkObject n){
        position.set(n.getPosX(), n.getPosY(), n.getPosZ());
        rotX = n.getRotX();
        rotY = n.getRotY();
        rotZ = n.getRotZ();
        scale = n.getScale();

    }

}
