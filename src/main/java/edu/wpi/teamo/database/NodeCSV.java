package edu.wpi.teamo.database;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class NodeCSV {

    public static HashMap<String, Node> read(String filepath) throws FileNotFoundException {
        HashMap<String, Node> nodeMap = new HashMap<>();

        try {
            CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
            FileReader fr = new FileReader(filepath);
            CSVParser parser = new CSVParser(fr, format);
            for (CSVRecord record : parser) {
                Node node = new Node(
                        record.get("nodeID"),
                        Integer.parseInt(record.get("xcoord")),
                        Integer.parseInt(record.get("ycoord")),
                        record.get("floor"),
                        record.get("building"),
                        record.get("nodeType"),
                        record.get("longName"),
                        record.get("shortName")
                );
                nodeMap.put(record.get("nodeID"), node);
            }
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodeMap;
    }

    public static void write(String filepath, Stream<Node> nodeStream) throws IOException {
        FileWriter csvWriter = new FileWriter(filepath);
        csvWriter.append("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName\n");
        for (Node node : nodeStream.collect(Collectors.toList())) {
            csvWriter.append(
                    String.format("%s,%d,%d,%s,%s,%s,%s,%s\n",
                            node.getNodeID(),
                            node.getXPos(),
                            node.getYPos(),
                            node.getFloor(),
                            node.getBuilding(),
                            node.getNodeType(),
                            node.getLongName(),
                            node.getShortName()
            ));
        }
        csvWriter.flush();
        csvWriter.close();
    }

}
