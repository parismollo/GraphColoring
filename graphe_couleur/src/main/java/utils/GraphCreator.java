package utils;

import gui.GUI;

/**
 * This class will be used to generate complete csv files from the console.
 * It will connect an image with an csv file. And ask the coordinates from the image. E.g. 
 * ./GraphCreator.java [filename].png [filename].csv [Graph Name]
 * (1) Get image path
 * (2) Get csv file
 * (3) Start coordinate process
 * (4) Display results in terminal
 */
public class GraphCreator {

    public static void main(String[] args) throws Exception {
        int num_args = args.length;
        if (num_args!=3) {
            String error_message = "\n[LOG]: Number of arguments not valid. Please insert 3 arguments.\n e.g: ./GraphCreator.java [filename].png [filename].csv [Graph Name]";
            throw new Exception(error_message);
        }else {
            
            String image_path = args[0];
            String csv_path = args[1];
            String graph_name = args[2];

            Converter.checkCSV(csv_path, graph_name);
            Converter.checkImage(image_path, graph_name);
            Converter.graphName = graph_name;

            GUI gui =  new GUI(1200, 800);
            gui.setMapPage(graph_name, true);
        }
    }
}
