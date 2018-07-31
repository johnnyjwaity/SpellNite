/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellnite;

import Entities.Entity;

/**
 *
 * @author JohnnyWaity
 */
//Interfaxes for Scripts that are attactched to entites
public interface AttachableScript {
    void start(Entity t);
    void update();
}
