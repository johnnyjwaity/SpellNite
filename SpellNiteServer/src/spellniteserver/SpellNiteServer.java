/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellniteserver;

import java.util.Scanner;

/**
 *
 * @author johnn
 */
public class SpellNiteServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Number Of Players");
        Scanner scan = new Scanner(System.in);
        String amount = scan.next();
        MatchMaker m = new MatchMaker(Integer.parseInt(amount));
    }
    
}
