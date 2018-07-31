/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellniteserver;

import Network.SendData;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johnn
 */
public class Client implements Runnable {
    private Socket s;
    private int id;
    private Game game;
    private Listener listener;
    private List<SendData> dataQuene = new ArrayList<>();
    private boolean dead;

    public Client(Socket s) {
        this.s = s;
        listener = new Listener(this);
        Thread t = new Thread(listener);
        t.start();
    }

    
    //Sends message on requent to the client
    ObjectOutputStream out;
    public void sendMessage(SendData d){
        try{
            if(out == null){
                out = new ObjectOutputStream(s.getOutputStream());
            }
            
            out.writeObject(d);
            out.flush();
            List<SendData> da = new ArrayList<>();
            da.addAll(dataQuene);
            dataQuene.clear();
            for(SendData data : da){
                out.writeObject(data);
                out.flush();
            }
            
            //out.reset();
            //out.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addToDataQuene(SendData d){
        dataQuene.add(d);
    }

    public Socket getS() {
        return s;
    }

    public Game getGame() {
        return game;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGame(Game game) {
        this.game = game;
        listener.setGame(game);
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public void run() {
        
    }
    
}
