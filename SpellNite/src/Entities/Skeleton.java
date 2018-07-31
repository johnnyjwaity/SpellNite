/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Models.TexturedModel;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.util.vector.Vector3f;
import spellnite.AttachableScript;
import spellnite.Game;
import spellnite.Helpers;

/**
 *
 * @author johnn
 */

 //CLASS NOT USED
public class Skeleton extends Entity {

    private Entity head;


    public Skeleton(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Game game, Entity head) {
        super(model, position, rotX, rotY, rotZ, scale, game);
        this.head = head;
    }


    @Override
    public void updateScripts(){
        for(AttachableScript s : myScripts){
            s.update();
        }
        update();

    }

    private void update(){
        head.increaseRotation(1, 1, 1);
        head.setPosition(Helpers.addVectors(getPosition(), new Vector3f(0, 10, 0)));
    }


}
