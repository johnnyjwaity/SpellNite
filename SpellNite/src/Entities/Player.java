/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Models.TexturedModel;
import RenderEngine.DisplayManager;
import Terrains.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 *@depriciated
 * @author johnn
 */
 //CLASS NOT USED
public class Player extends Entity{

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;

    private static final float TERRAIN_HEIGHT = 0;


    private float currentSpeed = 0f;
    private float currentTurnSpeed = 0f;
    private float upwardsSpeed = 0f;
    private boolean inAir = false;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale, null);
    }

    public void move(Terrain t){
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0, dz);
        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float terrainHeight = t.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
        if(super.getPosition().y < terrainHeight){
            upwardsSpeed = 0;
            inAir = false;
            super.getPosition().y = terrainHeight;
        }
    }

    private void checkInputs(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            currentSpeed = RUN_SPEED;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            currentSpeed = -RUN_SPEED;
        }
        else{
            currentSpeed = 0;
        }


        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            currentTurnSpeed = -TURN_SPEED;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            currentTurnSpeed = TURN_SPEED;
        }
        else{
            currentTurnSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !inAir){
            upwardsSpeed = JUMP_POWER;
            inAir = true;
        }

    }

}
