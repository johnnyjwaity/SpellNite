/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParticleSystems;

import Entities.Entity;
import org.lwjgl.util.vector.Vector3f;
import spellnite.AttachableScript;
import spellnite.Helpers;

/**
 *
 * @author waity1560
 */
public class SmokeController implements AttachableScript {

    private Entity transform;
    private Vector3f direction;
    private float speed = 0.05f;
    private ParticleSystem mySystem;
    private static final float RANDOM_LEVEL = 0.5f;
    private static final float SCALE_DECREASE = 0.001f;

    public SmokeController(ParticleSystem mySystem) {
        this.mySystem = mySystem;
    }
    
    
    
    
    @Override
    public void start(Entity t) {
        transform = t;
        //direction = new Vector3f((float)Math.random() * 720 - 360, (float)Math.random() * 720 - 360, (float)Math.random() * 720 - 360);
        //Creates rnadom direction for particle to follow
        direction = mySystem.getTransform().getBackwardVector();
        Vector3f randomSeed = new Vector3f((float) Math.random() * (2 * RANDOM_LEVEL) - RANDOM_LEVEL, (float) Math.random() * (2 * RANDOM_LEVEL) - RANDOM_LEVEL, (float) Math.random() * (2 * RANDOM_LEVEL) - RANDOM_LEVEL);
        direction = Helpers.addVectors(direction, randomSeed);
    }
    
    
    @Override
    public void update() {
        //Moves the particle and Decreases scale every frame
        transform.increasePosition(Helpers.multiplyVector(direction, speed));
        transform.setScale(transform.getScale() - SCALE_DECREASE);
        if(transform.getScale() <= 0){
            transform.getGame().destroy(transform);
        }
    }

    
    
}
