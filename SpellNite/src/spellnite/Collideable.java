/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellnite;

import Entities.Entity;
import com.sun.jndi.toolkit.ctx.HeadTail;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author JohnnyWaity
 */
public class Collideable implements AttachableScript{
    
    private static final int FRAME_SKIP = 5;
    private int frameCounter;
    
    private final CollisionManager cm;
    private Entity transform;
    
    private final List<Vector3f> circlePoints = new ArrayList<>();
    
    private final float maxDistance;
    private final boolean isStatic;
    
    private static final float Z_MIN = -796.7f;
    private static final float Z_MAX = -4.7f;
    private static final float X_MIN = 2.7f;
    private static final float X_MAX = 795.56f;
    
    

    public Collideable(CollisionManager cm, float maxDistance, boolean isStatic){
        this.cm = cm;
        this.maxDistance = maxDistance;
        this.isStatic = isStatic;
    }
    
    
    
    @Override
    public void start(Entity t) {
        transform = t;
        
        if(isStatic){
            //Creates circle points around entity
            float increment = 360 / (20*maxDistance);
            for(int i = 0; i  < 10 * maxDistance; i++){
                circlePoints.add(Helpers.circlePoint(transform.getPosition(), maxDistance, i * increment));
            }
        }
        
    }

    @Override
    public void update() {
        
        
        if(isStatic){
            //oldCollision();
            collide();
            
        }else{
            //System.out.println(transform.getPosition());
            //For the player
            //Keeps in bounds of the mp
            if(transform.getPosition().getX() > X_MAX){
                transform.setPosition(new Vector3f(X_MAX, transform.getPosition().getY(), transform.getPosition().getZ()));
            }
            else if(transform.getPosition().getX() < X_MIN){
                transform.setPosition(new Vector3f(X_MIN, transform.getPosition().getY(), transform.getPosition().getZ()));
            }
            
            if(transform.getPosition().getZ() > Z_MAX){
                transform.setPosition(new Vector3f(transform.getPosition().getX(), transform.getPosition().getY(), Z_MAX));
            }
            else if(transform.getPosition().getZ() < Z_MIN){
                transform.setPosition(new Vector3f(transform.getPosition().getX(), transform.getPosition().getY(), Z_MIN));
            }
        }
    }
    
    private void oldCollision(){
        for(Collideable c : cm.getCollideables()){
                if(c.equals(this)){
                    continue;
                }
                if(!c.getIsStatic() && Helpers.checkDistance(transform.getPosition(), c.getTransform().getPosition()) < maxDistance){
                    Vector3f closestCirclePoint = circlePoints.get(0);
                    float distance = 999;
                    for(Vector3f cPoint : circlePoints){
                        float d2 = (float) Helpers.checkDistance(cPoint, c.getTransform().getPosition());
                        if(d2 < distance){
                            distance = d2;
                            closestCirclePoint = cPoint;
                        }
                    }
                    c.getTransform().setPosition(closestCirclePoint);
                    
                    
                }
        }
    }
    
    private void collide(){
        //For static
       for(Collideable c : cm.getCollideables()){
           if(c.equals(this) || c.getIsStatic()){
               continue;
           }
           
           float xMax = transform.getPosition().getX() + maxDistance;
           float xMin = transform.getPosition().getX() - maxDistance;
           float zMax = transform.getPosition().getZ() + maxDistance;
           float zMin = transform.getPosition().getZ() - maxDistance;
           Vector3f pos = c.getTransform().getPosition();
           //Checks to see if the dynamic is witin bounds
           if(pos.getX() > xMin && pos.getX() < xMax && pos.getZ() > zMin && pos.getZ() < zMax){
//               pos.setX((transform.getPosition().getX() > pos.getX()) ? xMin : xMax);
//               pos.setZ((transform.getPosition().getZ() > pos.getZ()) ? zMin : zMax);
                //System.out.println("Collide " + pos);
                
                //c.getTransform().setPosition(Helpers.addVectors(pos, Helpers.multiplyVector(Game.pc.getVelocity(), -1)));
                //Game.pc.setVelocity(0, 0, 0);
                //Adds to collsion manager
                cm.addCollision(c.getTransform(), transform);
           }
           else{
               if(cm.isColliding(c.getTransform()) && cm.getCollidingObject(c.getTransform()).equals(transform)){
                   //removes form collision manger if no longer colliding
                   cm.removeCollision(c.getTransform());
               }
           }
           
           
//           if(Helpers.checkDistance(transform.getPosition(), c.getTransform().getPosition()) < maxDistance){
//               
//               
//               if(c.getTransform().getPosition().getX() < xMax && c.getTransform().getPosition().getX() > xMin){
//                   c.getTransform().getPosition().setX((c.getTransform().getPosition().getX() < transform.getPosition().getX()) ? xMin : xMax);
//                  
//               }
//               if(c.getTransform().getPosition().getZ() < zMax && c.getTransform().getPosition().getZ() > zMin){
//                   c.getTransform().getPosition().setZ((c.getTransform().getPosition().getZ() < transform.getPosition().getZ()) ? zMin : zMax);
//                  
//               }
//               
//           }
           
       }
    }
    
    public Entity getTransform(){
        return transform;
    }
    public float getMaxDistance(){
        return maxDistance;
    }
    public boolean getIsStatic(){
        return isStatic;
    }

    
    
    
}
