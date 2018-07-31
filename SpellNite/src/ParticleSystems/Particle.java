/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParticleSystems;

import Entities.Entity;
import Models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import spellnite.AttachableScript;
import spellnite.Game;

/**
 *
 * @author waity1560
 */
 //Holds info about a particle
public class Particle{
    private TexturedModel model;
    private float scale;
    private String particleController;
    private boolean worldSpace;
    private ParticleSystem particleSystem;


    public Particle(TexturedModel model, float scale, String particleController, boolean worldSpace) {
        this.model = model;
        this.scale = scale;
        this.particleController = particleController;
        this.worldSpace = worldSpace;
    }




    public TexturedModel getModel() {
        return model;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setParticleSystem(ParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
    }


//Retuns a new controller for this type of particle
    public AttachableScript getParticleController() {
        if(particleController.equals("SmokeController")){
            return new SmokeController(particleSystem);
        }
        return null;
    }
}
