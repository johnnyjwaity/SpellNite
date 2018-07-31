/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import ToolBox.Maths;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import spellnite.Helpers;

/**
 *
 * @author johnn
 */
public class Camera {

    private static final float SENSITIVITY = 0.2f;

    private float distanceFromPlayer = 10;
    private float yOffset = 10;
    private float angleAroundPlayer = 0;



    private Vector3f position = new Vector3f(25, 5, 0);
    private float pitch = 20f;
    private float pitchRadien;
    private float yaw;
    private float roll;
    private int lastMoseY;

    private Entity player;

    public Camera(Entity player) {
        this.player = player;
        lastMoseY = Mouse.getY();

    }

    public Entity getPlayer() {
        return player;
    }




    //Calculates different variables in order to determine camera position
    public void move(){
      //Changes Zoom
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getPitchRadien() {
        return pitchRadien;
    }



    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance){

//        position = Helpers.addVectors(player.getPosition(), Helpers.multiplyVector(player.getBackwardVector(), distanceFromPlayer));
//        position.y += 5;

        //Calculates posiiton by making  cirle around the player usingvariables
        float theta = player.getRotY() + angleAroundPlayer;
        float xOffset = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float zOffset = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - xOffset;
        position.z = player.getPosition().z - zOffset;
        position.y = player.getPosition().y + verticalDistance + yOffset;
    }

    private float calculateHorizontalDistance(){
        //return 1;
        //Distcance it has to be on circle
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        //return 4;
        //return 1;
        //Distance on circle
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom(){
      //Changes zoom based on mouse level
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
        distanceFromPlayer = -2;
    }

    private void calculatePitch(){

        //Calculates pitch based on Y mouse movement
        float mouseYDistance = Mouse.getY() - lastMoseY;
        mouseYDistance *= SENSITIVITY;
        pitch -= mouseYDistance;
        //System.out.println(mouseYDistance);
        lastMoseY = Mouse.getY();
        //System.out.println(Mouse.getY());

        //System.out.println(pitch);
//Makes the mouse loop
        if(Mouse.getY() == 719){
            Mouse.setCursorPosition(Mouse.getX(), 0);
            lastMoseY = 0;
        }
        else if(Mouse.getY() == 0){
            Mouse.setCursorPosition(Mouse.getX(), 719);
            lastMoseY = 719;
        }


        if(pitch > 90){
            pitch = 90;
        }
        else if(pitch < -90){
            pitch = -90;
        }

        //System.out.println(yaw);
        //pitch = 0;
        //System.out.println(pitch);

    }

    private void calculateAngleAroundPlayer(){
//        if(Mouse.isButtonDown(0)){
//            float angleChange = Mouse.getDX() * 0.3f;
//            angleAroundPlayer -= angleChange;
//        }
    }

    public Vector3f getForwardVector(){
        Matrix4f view = Maths.createViewMatrix(this);
        Vector3f vec = new Vector3f(view.m20, -2*(((pitch+90)/180)-0.5f), -view.m22);

        System.out.println(yaw);
        vec.normalise();
        return vec;
    }


}
