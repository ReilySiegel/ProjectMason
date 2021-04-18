package edu.wpi.teamo.database.map;

import javafx.util.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapCSV {
    static final String separator = "-------";

    /* readers */

    public static Pair<Stream<Node>, Stream<Edge>> readMapFile(String filepath) throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(filepath));

        Stream<Node> nodeStream = readNodesUntilDelimiter(csvReader);
        Stream<Edge> edgeStream = readEdges(csvReader);

        csvReader.close();

        return new Pair<>(nodeStream, edgeStream);
    }

    public static Stream<Node> readNodeFile(String filepath) throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(filepath));
        Stream<Node> nodeStream = readNodesUntilDelimiter(csvReader);
        csvReader.close();
        return nodeStream;
    }

    public static Stream<Edge> readEdgeFile(String filepath) throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(filepath));
        Stream<Edge> edgeStream = readEdges(csvReader);
        csvReader.close();
        return edgeStream;
    }

    public static Stream<Node> readNodesUntilDelimiter(BufferedReader csvReader) throws IOException {
        List<Node> nodes = new ArrayList<>();
        assert csvReader.readLine().equals("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName");
        for (String row = csvReader.readLine(); row != null && !row.equals(separator); row = csvReader.readLine()) {
            String[] data = row.split(",");
            assert data.length == 8;
            Node node = new Node(
                data[0],
                Integer.parseInt(data[1]),
                Integer.parseInt(data[2]),
                data[3],
                data[4],
                data[5],
                data[6],
                data[7]
            );
            nodes.add(node);
        }
        return nodes.stream();
    }

    public static Stream<Edge> readEdges(BufferedReader csvReader) throws IOException {
        List<Edge> edges = new ArrayList<>();
        assert csvReader.readLine().equals("edgeID,startNode,endNode");
        for (String row = csvReader.readLine(); row != null && !row.equals(separator); row = csvReader.readLine()) {
            String[] data = row.split(",");
            assert data.length == 3;
            Edge edge = new Edge(
                    data[0],
                    data[1],
                    data[2]
            );
            edges.add(edge);
        }
        return edges.stream();
    }

    /* writers */

    public static void writeMapFile(String filepath, Stream<Node> nodeStream, Stream<Edge> edgeStream) throws IOException {
        FileWriter csvWriter = new FileWriter(filepath);
        writeMap(csvWriter, nodeStream, edgeStream);
        csvWriter.close();
    }

    public static void writeNodeFile(String filepath, Stream<Node> nodeStream) throws IOException {
        FileWriter csvWriter = new FileWriter(filepath);
        writeNodes(csvWriter, nodeStream);
        csvWriter.close();
    }

    public static void writeEdgeFile(String filepath, Stream<Edge> edgeStream) throws IOException {
        FileWriter csvWriter = new FileWriter(filepath);
        writeEdges(csvWriter, edgeStream);
        csvWriter.close();
    }

    private static void writeMap(FileWriter csvWriter, Stream<Node> nodeStream, Stream<Edge> edgeStream) throws IOException {
        writeNodes(csvWriter, nodeStream);
        csvWriter.append(separator);
        csvWriter.append("\n");
        writeEdges(csvWriter, edgeStream);
    }

    private static void writeNodes(FileWriter csvWriter, Stream<Node> nodeStream) throws IOException {
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
    }

    private static void writeEdges(FileWriter csvWriter, Stream<Edge> edgeStream) throws IOException {
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
    }


}

