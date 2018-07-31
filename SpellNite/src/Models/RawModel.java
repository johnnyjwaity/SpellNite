/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author johnn
 */
public class RawModel {
    //Holds info for each model
    private int vaoID;
    private int vertexCount;
    
    public RawModel(int vaoID, int vertexCount){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }
    
    public int getVaoID(){
        return vaoID;
    }
    public int getVertexCount(){
        return vertexCount;
    }
}
