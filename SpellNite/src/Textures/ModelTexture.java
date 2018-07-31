/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Textures;

/**
 *
 * @author johnn
 */
public class ModelTexture {
    //Holds Texture ID for the model
    private int textureID;
    //Shine vars to determne how it works
    private float shineDamper = 1;
    private float reflectivity = 0;
    
    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;

    public ModelTexture(int id) {
        textureID = id;
    }
    public int getId(){
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }
    
    
    
    
    
    
    
}
