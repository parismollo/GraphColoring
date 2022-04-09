package utils;

import java.io.File;
import java.util.Scanner;

import gui.GUI;

public class GraphUpdater {
    /**
 * This class will be used to update csv files from the console.
 *  run GraphUpdate.java
 * (1) Select file you want to update 
 * (2) Update coordinates
 * (3) Close process and file will be updated
 */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        File res_folder = new File("src/resources/");
        String[] files;
        files = res_folder.list();
        for (String file : files) {
            if(Converter.getExtension(file).get().equals("csv")) {
                System.out.println("File name: "+file);
            }
        }
        System.out.println("[LOG]: Write the name of the file you wish to update: ");
        String selected = sc.nextLine();
        // selected = selected.toUpperCase();
        boolean valid = false;
        for (String file : files) {
            if(selected.equals(file)) {
                GUI gui =  new GUI(1200, 800);
                System.out.println("[LOG]: Map Selected: "+selected.substring(0, selected.length()-4));
                gui.setMapPage(selected.substring(0, selected.length()-4), true);
                valid = true;
            }
        }
        if (!valid) {
            System.out.println("[LOG]: Please indicate a valid file name. Run this program again and select a valid file.");
        }
    }
}
