/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellniteserver;

import Network.NetworkObject;
import Network.SendData;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johnn
 */
public class Listener implements Runnable {
    
    private Client c;
    private Socket s;
    private Game game;
    private List<NetworkObject> lastSend;
    

    public Listener(Client c) {
        s = c.getS();
        this.c = c;
    }
    
    
    
    private void listen(){
        ObjectInputStream in = null;
        while(true){
            try{
                if(in == null){
                    in = new ObjectInputStream(s.getInputStream());
                }
                SendData d = (SendData)in.readObject();
                readMessage(d);
            }catch (IOException ex) {
                System.out.println(ex);
            } catch (ClassNotFoundException ex) {
                System.out.println(ex);
            }
        }
    }
    
    private void readMessage(SendData d){
        switch(d.getMsg()){
            case "OID":
                SendData nd = new SendData("OID");
                nd.setId(game.createObject());
                System.out.println(nd.getId());
                c.addToDataQuene(nd);
                break;
            case "OBJ":
                List<NetworkObject> destroyList = new ArrayList<>();
                if(lastSend != null){
                    for(NetworkObject o : lastSend){
                        if(!d.getObjs().contains(o)){
                            destroyList.add(o);
                        }
                    }
                }
//                if(dead){
//                    destroyList.clear();
//                    destroyList.addAll(d.getObjs());
//                    d.getObjs().clear();
//                }
                game.addObjects(d.getObjs(), destroyList);
                lastSend = d.getObjs();
                break;
            case "DEAD":
                c.setDead(true);
                break;
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    
    
    
    

    @Override
    public void run() {
        listen();
    }
    
}
