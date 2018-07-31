/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellnite;

import Entities.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author JohnnyWaity
 */
public class CollisionManager {
    //List of everythong that collides
    private List<Collideable> allColidableObjects = new ArrayList<>();
    //List of things that are colliding
    private Map<Entity, Entity> currentClollisions = new HashMap<>();
    
    public void addCollideable(Collideable c){
        allColidableObjects.add(c);
    }
    public List<Collideable> getCollideables(){
        return allColidableObjects;
    }
    public void addCollision(Entity e1, Entity e2){
        //System.out.println("Added Collsion");
        currentClollisions.put(e1, e2);
    }
    public void removeCollision(Entity e){
        //System.out.println("Removed The Collsion");
        currentClollisions.remove(e);
    }
    public boolean isColliding(Entity e){
        return currentClollisions.containsKey(e);
    }
    public Entity getCollidingObject(Entity e){
        return currentClollisions.get(e);
    }
    
}
