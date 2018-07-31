/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RenderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 *
 * @author johnn
 */
public class DisplayManager {
    
    
    
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS_CAP = 120;
    
    private static long lastFrameTime;
    private static float delta;
    
    
    public static void createDisplay(){
        //Attributes For The Display
        ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
        
        
        try {
            //Sets The Display Up By Screen Size
            
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setFullscreen(true);
            //Creates The Display
            Display.create(new PixelFormat(), attribs);
            //Sets Dislpay Title
            Display.setTitle("SpellNite");
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
        
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
        
    }
    //Closes Display
    public static void closeDisplay(){
        Display.destroy();

    }
    //Syncs the display so it stays at the proper FPS and then updates the disply with the new information
    public static void updateDisplay(){
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;
        
    }
    
    public static float getFrameTimeSeconds(){
        return delta;
    }
    
    private static long getCurrentTime(){
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }
}
