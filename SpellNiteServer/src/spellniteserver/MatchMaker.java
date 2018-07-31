/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellniteserver;

import DataSaving.Coordinate;
import DataSaving.SaveData;
import Network.SendData;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johnn
 */
public class MatchMaker {
    
    private int playerAmount = 2;
    
    private List<Client> waitingClients = new ArrayList<>();
    private ServerSocket s;

    public MatchMaker(int playerAmount) {
//        List<Coordinate> coords = new ArrayList<>();
//        coords.add(new Coordinate(800, -800));
//        SaveData.appendToFile(coords);
        this.playerAmount = playerAmount;
        try {
            s = new ServerSocket(4545);
            System.out.println("Listening on 4545");
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        connect();
    }
    
    
    
    
    private void connect(){
        while(true){
            try {
                Client c = new Client(s.accept());
                System.out.println("Connected");
                SendData cId = new SendData("CID");
                cId.setId(waitingClients.size());
                c.setId(waitingClients.size());
                c.sendMessage(cId);
                waitingClients.add(c);
                Thread client = new Thread(c);
                client.start();
                
                if(waitingClients.size() >= playerAmount){
                    Game game = new Game(waitingClients);
                    Thread g = new Thread(game);
                    g.start();
                    waitingClients = new ArrayList<>();
                }
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
            
        }
    }
    
}
