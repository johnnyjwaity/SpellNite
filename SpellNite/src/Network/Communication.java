/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import spellnite.Game;

/**
 *
 * @author johnn
 */
public class Communication {
    private Socket s;
    private final Game game;
    private int id;
    private List<SendData> dataQuene = new ArrayList<>();

    public Communication(String server, int port, Game game) {
        this.game = game;
        connect(server, port);
    }

    private void connect(String server, int port){
      //Connects To the socket and puts a listener on the socket on another thread
        try{
            s = new Socket(server, port);
            Listener list = new Listener(game, s, this);
            Thread listThread = new Thread(list);
            listThread.start();
        } catch (IOException ex) {
            System.out.println("Connection Failure");
        }
    }
    //Serializes  Send Data and sends it as bytes over the server
    ObjectOutputStream out;
    public void sendMessage(SendData d){
        try {
            if(out == null){
                out = new ObjectOutputStream(s.getOutputStream());
            }
            out.writeObject(d);
            out.flush();
            for(SendData data : dataQuene){
                out.writeObject(data);
                out.flush();
            }
            //Claears info tp be sent
            dataQuene.clear();
            //out.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void addToSendQuene(SendData d){
        dataQuene.add(d);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




}
