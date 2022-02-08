package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import graphs.Graph;
import graphs.Vertex;

/**
 * The Converter class, transforms a csv file into a graph. 
 */
public class Converter {
    static String saveFolder = "src/resources/";
    static String graphName;

    public static void main(String[] args) throws Exception {
        /*if(args.length !=2) {
            throw new Exception("\n[LOG]: Please insert the file path. Only two arguments are accepted [filepath] [filename], e.g. java Converter USA.txt USA\n");
        }else {
            String path = args[0];
            String fileName = args[1];
            graphName = fileName;
            loadFile(path, fileName);
            System.out.println(mapToGraph(saveFolder+fileName+".csv"));
        }*/
        Vertex v = new Vertex();
        v.setPosition(22, 220);
        write(v, "src/resources/USA.csv");
    }

    public static Graph mapToGraph(String filepath) throws FileNotFoundException {
        Graph graph = new Graph(graphName);

        Scanner scanner = new Scanner(new File(filepath));
        scanner.useDelimiter("\n");
        scanner.nextLine();

        while (scanner.hasNext()) {
            // System.out.println(scanner.next());
            String line = scanner.next();
            String[] vertices = line.split(",\"");

            // System.out.println("vertices[0]:" + vertices[0]);
            // System.out.println(vertices[1]);

            String main = vertices[0].strip();
            String[] borders = vertices[1].replace("\"", "").split(",");

            Vertex v = graph.getVertex(main);
            if(v == null) {
                v = graph.addVertex(main);
            }
            for(String s : borders) {
                s = s.strip();
                Vertex border = graph.getVertex(s);
                if (border == null) {
                    border = graph.addVertex(s);
                }
                graph.addEdge(v, border);
            }         
        }
        scanner.close();

        return graph;
    }

    private static void loadFile(String filepath, String filename) throws Exception{
        /**
         * 1. Check if CSV file.
         * 2. Read and Load File into resources folder.
         * 3. Raise Error otherwise.
         */
        int extension_len = 3; // csv
        String extension = filepath.substring(filepath.length()-extension_len);
        if (!extension.equals("csv")) {
            throw new Exception("\n[LOG]: Extension \""+extension+"\" not supported. Please use a .csv file\n");
        }

        File file = new File(filepath);
        boolean can_use_file = file.exists() && file.canRead();
        boolean saved = false;

        saveFile(filename, file, can_use_file, saved);
    }

    private static void saveFile(String filename, File file, boolean can_use_file, boolean saved) throws Exception {
        if(can_use_file) {
            saved = file.renameTo(new File(saveFolder+filename+".csv"));
        }
         else {
            throw new Exception("\n[LOG]: File not found or not readable. Please insert a valid file\n");
        }

        if(saved) {
            System.out.println("\n[LOG]: File saved at resources folder resources/"+filename+".csv!\n");

        } else {
            throw new Exception("\n[LOG]: File could not be saved.\n");
        }
    }

    public static void write(Vertex v, String filepath) throws IOException {
        String str = new String(Files.readAllBytes(Paths.get(filepath)));
        Scanner sc = new Scanner(str);
        String newStr = sc.nextLine()+"\n";
        while(sc.hasNext()) {
            String line = sc.nextLine();
            String[] split = line.split(",\"");
            if(split != null && split[0].equals(v.getTitle())) {
                String main = split[0].strip()+",";
                main += "\""+split[1].strip();
                main = main.substring(0, main.lastIndexOf("\"")+1)+",";
                main += v.getX()+","+v.getY();
                newStr += main+"\n";
            }
            else {
                newStr += line+"\n";
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write(newStr);
        
        writer.close();
    }

}
