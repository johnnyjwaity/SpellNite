/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.io.Serializable;

/**
 *
 * @author johnn
 */
 //ANother was to hold info about na ENtity
public class NetworkObject implements Serializable {
    private int clientID;
    private int objID;
    private String texModel;
    private float posX, posY, posZ;
    private float rotX, rotY, rotZ;
    private float scale;

    public NetworkObject(int clientID, int objID, String texModel, float posX, float posY, float posZ, float rotX, float rotY, float rotZ, float scale) {
        this.clientID = clientID;
        this.objID = objID;
        this.texModel = texModel;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }



    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getObjID() {
        return objID;
    }

    public void setObjID(int objID) {
        this.objID = objID;
    }

    public String getTexModel() {
        return texModel;
    }

    public void setTexModel(String texModel) {
        this.texModel = texModel;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPosZ() {
        return posZ;
    }

    public void setPosZ(float posZ) {
        this.posZ = posZ;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }




}
