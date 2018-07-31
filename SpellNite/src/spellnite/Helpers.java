/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellnite;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author JohnnyWaity
 */
public class Helpers {
//Basic vector math fuctions
    public static Vector3f Vector3(Vector3f vec){
        return new Vector3f(vec.x, vec.y, vec.z);
    }
    public static double checkDistance(Vector3f pos1, Vector3f pos2){
        return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2) + Math.pow(pos1.z - pos2.z, 2));
    }
    
    public static Vector3f circlePoint(Vector3f orgin, float radius, float angle){
        float radius1 = radius +0.1f;
        float x = (float) (radius1 * Math.cos(angle) + orgin.x);
        float z = (float) (radius1 * Math.sin(angle) + orgin.z);
        return new Vector3f(x, orgin.y, z);
    }
    
    public static Vector3f addVectors(Vector3f v1, Vector3f v2){
        Vector3f vector = Vector3(v1);
        vector.x += v2.x;
        vector.y += v2.y;
        vector.z += v2.z;
        return vector;
    }
    public static Vector3f divideVector(Vector3f vec, float div){
        Vector3f vector = Vector3(vec);
        vector.x /= div;
        vector.y /=div;
        vector.z /= div;
        return vector;
    }
    
    public static Vector3f multiplyVector(Vector3f vec, float m){
        Vector3f vector = Vector3(vec);
        vector.x *= m;
        vector.y *=m;
        vector.z *= m;
        return vector;
    }
    
}
