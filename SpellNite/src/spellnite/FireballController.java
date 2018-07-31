/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellnite;

import Entities.Entity;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author JohnnyWaity
 */
public class FireballController implements AttachableScript{
    
    private Entity transform;
    private Vector3f direction;
    private static final float MOVE_SPEED = 0.6f;
    private static final float SHRINK_AMOUNT = 0.008f;

    public FireballController(Vector3f direction) {
        this.direction = direction;
    }
    
    
    

    @Override
    public void start(Entity t) {
        transform = t;
    }

    @Override
    public void update() {
        move();
    }
    
    private void move(){
        //System.out.println(transform.getForwardVector());
        //Moves fireball in direction
        transform.increasePosition(Helpers.multiplyVector(direction, MOVE_SPEED));
        transform.setScale(transform.getScale() - SHRINK_AMOUNT);
        if(transform.getScale() <= 0){
            transform.getGame().destroy(transform);
        }
    }
    
}
