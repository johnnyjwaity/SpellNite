/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellniteserver;

import DataSaving.Coordinate;
import DataSaving.SaveData;
import Network.NetworkObject;
import Network.SendData;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author johnn
 */
public class Game implements Runnable {
    
    private final List<Client> clients;
    private int objAmt = 0;
    private final Map<Integer, NetworkObject> objs = new HashMap<>();
    private final static float MS_DELAY = 2;
    private long lastMs = 0;
    private int currentFrame = 0;
    private List<Integer> incpacitatedFireballs = new ArrayList<>();
    private List<Integer> deadClients = new ArrayList<>();
    private List<Coordinate> hitSpots = new ArrayList<>();
    

    public Game(List<Client> clients) {
        this.clients = clients;
        for(Client c : clients){
            c.setGame(this);
            c.sendMessage(new SendData("STRT"));
        }
        lastMs = System.currentTimeMillis();
    }
    //Creates id fr object
    public int createObject(){
        //objs.put(objAmt, null);
        objAmt++;
        return objAmt-1;
    }
    //Add objects to the map for syncing
    public void addObjects(List<NetworkObject> o, List<NetworkObject> destroy){
        for(NetworkObject n : o){
            objs.put(n.getObjID(), n);
        }
        for(NetworkObject n : destroy){
            objs.remove(n.getObjID(), n);
        }
    }
    
    private void gameLoop(){
        //Game loop to always run
        while(true){
            //System.out.println("Game Loop Running");
            //Checks to see if certain time passed
            if(System.currentTimeMillis() - lastMs > MS_DELAY){
                lastMs = System.currentTimeMillis();
            }
            else{
                continue;
            }
            //checks win and collsion
            checkCollisions();
            checkWin();
            try{
                //Removes stuff
                List<Integer> keysToRem = new ArrayList<>();
                for(NetworkObject n : objs.values()){
                    if(deadClients.contains(n.getClientID())){
                        keysToRem.add(n.getObjID());
                    }
                }
                for(Integer key : keysToRem){
                    objs.remove(key);
                }
//Sends over server
                SendData d = new SendData("OBJ");
                List<NetworkObject> sendList = new ArrayList<>();
                sendList.addAll(objs.values());
                d.setObjs(sendList);
                for(Client c : clients){
                    if(!deadClients.contains(c.getId()) && c.isDead()){
                        deadClients.add(c.getId());
                    }
                    c.sendMessage(d);
                }
            }catch(ConcurrentModificationException ex){
                System.out.println("Concurrent Exception");
            }
            
        }
    }
    //Checks collsions betwween player and fireball
    private void checkCollisions(){
        List<NetworkObject> players = new ArrayList<>();
        List<NetworkObject> fireballs = new ArrayList<>();
        for(NetworkObject o : objs.values()){
            switch(o.getTexModel()){
                case "Player":
                    players.add(o);
                    break;
                case "Fireball":
                    fireballs.add(o);
                    break;
            }
        }
        //System.out.println(players.size() + " " + fireballs.size());
        for(NetworkObject p : players){
            for(NetworkObject f : fireballs){
                if(p.getClientID() == f.getClientID()){
                    continue;
                }
                if(!incpacitatedFireballs.contains(f.getObjID()) && isColliding(p, f)){
                    incpacitatedFireballs.add(f.getObjID());
                    //System.out.println("Collsion");
                    hitSpots.add(new Coordinate(p.getPosX(), p.getPosZ()));
                    for(Client c : clients){
                        if(c.getId() == p.getClientID()){
                            c.sendMessage(new SendData("HIT"));
                        }
                        else if(c.getId() == f.getClientID()){
                            SendData r = new SendData("REM");
                            r.setId(f.getObjID());
                            c.sendMessage(r);
                        }
                    }
                }
            }
        }
    }
    //Checks collsions
    private boolean isColliding(NetworkObject n1, NetworkObject n2){
        float distance = 7;
        if(Math.abs(n1.getPosX() - n2.getPosX()) < distance && Math.abs(n1.getPosY() - n2.getPosY()) < distance && Math.abs(n1.getPosZ() - n2.getPosZ()) < distance){
            return true;
        }
        return false;
    }
    private boolean savedData = false;
    //Checks to see if a client has won
    private void checkWin(){
        int liveCount = 0;
        for(Client c : clients){
            if(!c.isDead()){
                liveCount++;
            }
        }
        if(liveCount == 1){
            for(Client c : clients){
                if(!c.isDead()){
                    c.sendMessage(new SendData("WIN"));
                    if(!savedData){
                        SaveData.appendToFile(hitSpots);
                        savedData = true;
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        gameLoop();
    }
    
}
