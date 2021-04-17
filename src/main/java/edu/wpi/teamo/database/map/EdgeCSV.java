package edu.wpi.teamo.database.map;

import edu.wpi.teamo.database.map.Edge;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class EdgeCSV {

    public static Stream<Edge> read(String filepath) throws FileNotFoundException {
        List<Edge> edges = new ArrayList<>();

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
                edges.add(edge);
            }
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return edges.stream();
    }

    public static void write(String filepath, Stream<Edge> edgeStream) throws IOException {
        FileWriter csvWriter = new FileWriter(filepath);
        csvWriter.append("edgeID,startNode,endNode\n");
        for (Edge edge : edgeStream.collect(Collectors.toList())) {
            csvWriter.append(
                    String.format("%s,%s,%s\n",
                            edge.getEdgeID(),
                            edge.getStartNodeID(),
                            edge.getEndNodeID())
            );
        }
        csvWriter.flush();
        csvWriter.close();
    }

}

