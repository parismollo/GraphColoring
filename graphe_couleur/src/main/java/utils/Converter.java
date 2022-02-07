package utils;

import java.io.File;

/**
 * The Converter class, transforms a csv file into a graph. 
 */
public class Converter {

    public static void main(String[] args) throws Exception {
        if(args.length !=2) {
            throw new Exception("Please insert the file path. Only one argument is accepted, e.g. java Converter USA.txt USA");
        }else {
            loadFile(args[0], args[1]);
        }
    }

    public static void loadFile(String filepath, String filename) throws Exception{
        /**
         * 1. Check if CSV file.
         * 2. Read and Load File into resources folder.
         * 3. Raise Error otherwise.
         */
        int extension_len = 3; // csv
        String extension = filepath.substring(filepath.length()-extension_len);
        if (!extension.equals("csv")) {
            throw new Exception("Extension \""+extension+"\" not supported. Please use a .csv file");
        }

        File file = new File(filepath);
        boolean can_use_file = file.exists() && file.canRead();
        if(can_use_file) {
            System.out.println(file.renameTo(new File("graphe_couleur/src/resources/"+filename+".csv")));
        } else {
            throw new Exception("File not found or not readable. Please insert a valid file");
        }
    }
}
