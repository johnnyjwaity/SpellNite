/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParticleSystems;

import Entities.Entity;

/**
 *
 * @author waity1560
 */
 //Differnet particle controllers are made from this template
public abstract class ParticleController{

    private Entity entity;

    public ParticleController() {
    }


    public void start(Entity e){
        entity = e;
    }

    public abstract void update();


    public Entity getEntity() {
        return entity;
    }
}
