/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataSaving;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author johnn
 */
public class SaveData {
    
    
    //REads csv file and the updates them with new coordinates
    public static void appendToFile(List<Coordinate> hits){
        String FILE = "";
        String rDir = "";
        String rScriptDir = "";
        if(System.getProperty("os.name").charAt(0) == 'W'){
            rDir = "C:/Program Files/R/R-3.5.0/bin/x64/Rscript.exe";
            rScriptDir = "C:/Users/johnn/Desktop/SpellNiteServerAlt/";
            FILE = rScriptDir + "data.csv";
        }
        else{
            rDir = "Rscript";
            rScriptDir = "/var/www/html/SpellNite/";
            FILE = rScriptDir + "data.csv";
        }
        List<String> currentLines = new ArrayList<>();
        try{
            FileReader reader = new FileReader(FILE);
            Scanner scan = new Scanner(reader);
            while(scan.hasNext()){
                currentLines.add(scan.nextLine());
            }
            scan.close();
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            Logger.getLogger(SaveData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Coordinate c : hits){
            currentLines.add(c.getX() + "," + c.getZ());
        }
        
        try{
            PrintWriter out = new PrintWriter(FILE);
            for(String s : currentLines){
                out.println(s);
            }
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaveData.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            
            Runtime.getRuntime().exec(rDir + " " + rScriptDir + "rTest.R " + rScriptDir);
        } catch (IOException ex) {
            Logger.getLogger(SaveData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
