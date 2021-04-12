package edu.wpi.teamo.map.database;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class EdgeCSV {

    public static HashMap<String, Edge> read(String filepath) throws FileNotFoundException {
        HashMap<String, Edge> edgeMap = new HashMap<>();

        try {
            CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
            FileReader fr = new FileReader(filepath);
            CSVParser parser = new CSVParser(fr, format);
            for (CSVRecord record : parser) {
                Edge edge = new Edge(
                        record.get("edgeID"),
                        record.get("startNode"),
                        record.get("endNode")
                );
                edgeMap.put(record.get("edgeID"), edge);
            }
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return edgeMap;
    }

    public static boolean write(String filename, HashMap<String, Edge> edgeMap) {
        boolean written = true;
        //TODO: finish
        return written;
    }

}

