/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParticleSystems;

import Entities.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spellnite.AttachableScript;
import spellnite.Helpers;

/**
 *
 * @author waity1560
 */
public class ParticleSystem implements AttachableScript{

    private Entity transform;
    private Particle particle;
    Map<Entity, Integer> activeParticles = new HashMap<>();

    private int frameRate = 20;
    private int currentFrame = 0;
    private int destroyTime;




    public ParticleSystem(Particle particle, int destroyTime, int frameRate) {
        this.particle = particle;
        this.destroyTime = destroyTime;
        this.frameRate = frameRate;
    }



    @Override
    public void start(Entity t) {
        transform = t;
    }

    @Override
    public void update() {
        //Counts down to see if the frame rate was reached before spawn
        currentFrame ++;
        if(currentFrame >= frameRate){
            currentFrame = 0;
            spawnParticle();
        }
        //Destroys particles if their lifespan reaches 0
        List<Entity> toRemove = new ArrayList<>();
        for(Entity part : activeParticles.keySet()){
            int timeLeft = activeParticles.get(part);
            timeLeft --;

            activeParticles.put(part, timeLeft);
            if(timeLeft < 0){
                toRemove.add(part);
                transform.getGame().destroy(part);
            }
        }
        for(Entity e : toRemove){
            activeParticles.remove(e);
        }
        toRemove.clear();
    }
    //Ran when the timer reachees the frame rate and creates the particle with its controller
    private void spawnParticle(){
        //Creates Entity Particle
        Entity part = new Entity(particle.getModel(), Helpers.Vector3(transform.getPosition()), 0, 0, 0, particle.getScale(), transform.getGame());
        //ParticleController cont =
        part.addScript(particle.getParticleController());
        activeParticles.put(part, destroyTime);
        transform.getGame().instanstiate(part);
        //transform.getGame().addToNet(part);
    }

    public Entity getTransform() {
        return transform;
    }



}