/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import spellnite.Game;

/**
 *
 * @author johnn
 */
public class Listener implements Runnable{
    private final Game game;
    private Socket s;
    private Communication c;

    public Listener(Game game, Socket s, Communication c) {
        this.game = game;
        this.s = s;
        this.c = c;
    }
    //Listencs for information form the server and runs readMsg with the result
    private void listen(){
        ObjectInputStream in = null;
        while(true){
            try{
                if(in == null){
                    in = new ObjectInputStream(s.getInputStream());
                }
                //ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                SendData d = (SendData)in.readObject();
                readMsg(d);
                //readMsg(d);
            }catch (IOException ex) {
                System.out.println(ex);
            } catch (ClassNotFoundException ex) {
                System.out.println(ex);
            }
        }
    }
    //Interprets SendData and does different things based on waht it is
    private void readMsg(SendData d){
        switch(d.getMsg()){
            case "CID":
            //A client id was returned
                System.out.println("My Id id: " + d.getId());
                c.setId(d.getId());
                break;
            case "OID":
            //An object id was returned
                System.out.println("Object ID: " + d.getId());
                game.addObjId(d.getId());
                break;
            case "OBJ":
            //A list of NetObj was returned and needs to be updated
                //System.out.println("Size: "+d.getObjs().size());
                game.updateNetworkObj(d.getObjs());
                break;
            case "STRT":
            //Game started
                game.toggleNetGame(true);
                break;
            case "HIT":
            //The player was hit
                game.damage();
                break;
            case "REM":
            //Call to destroy one of their objects
                game.destroy(d.getId());
                break;
            case "WIN":
            //Player Won Game
                game.win();
                break;
        }
    }


    @Override
    public void run() {
        listen();
    }

}
