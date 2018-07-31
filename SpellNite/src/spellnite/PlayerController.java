/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellnite;

import Entities.Entity;
import ParticleSystems.Particle;
import ParticleSystems.ParticleSystem;
import ParticleSystems.SmokeController;
import RenderEngine.DisplayManager;
import Terrains.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author JohnnyWaity
 */
public class PlayerController implements AttachableScript {

    private Entity transform;
    private Terrain t;
    private static final float RUN_SPEED = 0.05f;
    private static final float GRAVITY = -0.15f;
    private static final float BOOSTER = .1f;
    private static final float FRICTION = 1.1f;
    private static final float SENSITIVITY = 0.1f;
    
    private Vector3f velocity;
    private int lastMouseX;
    private boolean alreadyShot = false;

    public PlayerController(Terrain t) {
        this.t = t;
    }
    
    
    
    
    @Override
    public void start(Entity t) {
        transform = t;
        velocity = new Vector3f(0, 0, 0);
        lastMouseX = Mouse.getX();
    }

    @Override
    public void update() {
        move();
    }
    
    
    private void move(){
        //Moves players from input recieved
        checkInputs();
        transform.increasePosition(velocity);
        velocity = Helpers.divideVector(velocity, FRICTION);
        if(transform.getPosition().getY() < t.getHeightOfTerrain(transform.getPosition().x, transform.getPosition().z)){
            transform.getPosition().y = t.getHeightOfTerrain(transform.getPosition().x, transform.getPosition().z);
            velocity.setY(0);
        }
        
    }
    
    private void attack(){
        //to attacks it creates fireballs and adds particle systems to it
        Vector3f direction = Helpers.Vector3(Game.cam.getForwardVector());
        Vector3f startPos = new Vector3f(transform.getPosition().getX(), transform.getPosition().getY()+5, transform.getPosition().getZ());
        startPos.setY(startPos.getY() + 5);
        Entity projectile = new Entity(transform.getGame().getModelMap().get("Fireball"), startPos, transform.getRotX(), transform.getRotY(), transform.getRotZ(), 1, transform.getGame());
        projectile.addScript(new FireballController(direction));
        Particle smoke = new Particle(transform.getGame().getModelMap().get("Smoke"), 0.5f, "SmokeController", true);
        ParticleSystem smokeSystem = new ParticleSystem(smoke, 50, 1);
        smoke.setParticleSystem(smokeSystem);
        projectile.addScript(smokeSystem);
        Particle fire = new Particle(transform.getGame().getModelMap().get("Fire"), 0.5f, "SmokeController", true);
        ParticleSystem fireSystem = new ParticleSystem(fire, 20, 1);
        fire.setParticleSystem(fireSystem);
        projectile.addScript(fireSystem);
        transform.getGame().instanstiate(projectile);
    }
    
    
    private void checkInputs(){
        //Check keyboard iputs
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            //velocity = Helpers.addVectors(velocity, Helpers.multiplyVector(transform.getForwardVector(), RUN_SPEED));
            addVelocity(transform.getForwardVector());
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            //velocity = Helpers.addVectors(velocity, Helpers.multiplyVector(transform.getBackwardVector(), RUN_SPEED));
            addVelocity(transform.getBackwardVector());
        }
        
        
        
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            //velocity = Helpers.addVectors(velocity, Helpers.multiplyVector(transform.getRightVector(), RUN_SPEED));
            addVelocity(transform.getRightVector());
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            //velocity = Helpers.addVectors(velocity, Helpers.multiplyVector(transform.getLeftVector(), RUN_SPEED));
            addVelocity(transform.getLeftVector());
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            velocity.setY(velocity.getY() + BOOSTER);
            
        } else{
            velocity.setY(velocity.getY() + GRAVITY);
        }
        
        
        if(Mouse.isButtonDown(0) && !alreadyShot){
            attack();
            alreadyShot = true;
        }
        
        if(!Mouse.isButtonDown(0)){
            alreadyShot = false;
        }
        
        
        Mouse.setGrabbed(true);
        int rotateAmount = lastMouseX - Mouse.getX();
        transform.increaseRotation(0, rotateAmount * SENSITIVITY, 0);
        lastMouseX = Mouse.getX();
        if(Mouse.getX() == 1279){
            Mouse.setCursorPosition(0, Mouse.getY());
            lastMouseX = 0;
        }
        else if(lastMouseX == 0){
            Mouse.setCursorPosition(1279, Mouse.getY());
            lastMouseX = 1279;
        }
    }
    //Adds to velocity
    private void addVelocity(Vector3f v){
        if(Game.cm.isColliding(transform)){
            Vector3f predictionV = velocity = Helpers.addVectors(velocity, Helpers.multiplyVector(v, RUN_SPEED));
            Vector3f predictedPos = Helpers.addVectors(transform.getPosition(), predictionV);
            Entity obj = Game.cm.getCollidingObject(transform);
            if(Helpers.checkDistance(transform.getPosition(), obj.getPosition()) < Helpers.checkDistance(predictedPos, obj.getPosition())){
                
                velocity = predictionV;
            }
            else{
                if(Math.abs(predictedPos.getX() - obj.getPosition().getX()) < Math.abs(transform.getPosition().getX() - obj.getPosition().getX())){
                    predictionV.setX(0);
                }
                if(Math.abs(predictedPos.getZ() - obj.getPosition().getZ()) < Math.abs(transform.getPosition().getZ() - obj.getPosition().getZ())){
                    predictionV.setZ(0);
                }
            }
        }
        else{
            velocity = Helpers.addVectors(velocity, Helpers.multiplyVector(v, RUN_SPEED));
        }
        
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(float x, float y, float z) {
        this.velocity.setX(x);
        this.velocity.setY(y);
        this.velocity.setZ(z);
    }
    
    
    
    
}
