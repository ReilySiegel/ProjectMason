package edu.wpi.teamo.database.map;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MapCSVTests {
    static final String testNodeCSVPath = "src/test/resources/edu/wpi/teamo/database/testNodes.csv";
    static final String testEdgeCSVPath = "src/test/resources/edu/wpi/teamo/database/testEdges.csv";
    static final String testMapFilepath = "src/test/resources/edu/wpi/teamo/database/testMap.csv";
    static final String writtenCSVPath = "src/test/resources/edu/wpi/teamo/database/writtenMap.csv";

    @Test
    public void writeMapFile() {

        try {
            Stream<Node> nodeStreamToWrite = MapCSV.readNodeFile(testNodeCSVPath);
            Stream<Edge> edgeStreamToWrite = MapCSV.readEdgeFile(testEdgeCSVPath);

            MapCSV.writeMapFile(writtenCSVPath, nodeStreamToWrite, edgeStreamToWrite);

            /* read it and check that it is sound */
            Pair<Stream<Node>, Stream<Edge>> mapStreamPair = MapCSV.readMapFile(testMapFilepath);

            List<Node> nodeList = mapStreamPair.getKey().collect(Collectors.toList());
            List<String> nodeIDList = nodeList.stream().map(Node::getNodeID).collect(Collectors.toList());

            List<Edge> edgeList = mapStreamPair.getValue().collect(Collectors.toList());
            List<String> edgeIDList = edgeList.stream().map(Edge::getEdgeID).collect(Collectors.toList());

            assertEquals(3, nodeList.size());
            assertTrue(nodeIDList.contains("testID1"));
            assertTrue(nodeIDList.contains("testID2"));
            assertTrue(nodeIDList.contains("testID3"));
            for (Node node : nodeList) {
                if (node.getNodeID().equals("testID1")) {
                    assertEquals(1, node.getXPos());
                    assertEquals(1, node.getYPos());
                    assertEquals("1", node.getFloor());
                    assertEquals("Parking", node.getBuilding());
                    assertEquals("PARK", node.getNodeType());
                    assertEquals("Floor1RightParking1", node.getLongName());
                    assertEquals("F1RightP1", node.getShortName());
                }
                if (node.getNodeID().equals("testID2")) {
                    assertEquals(2, node.getXPos());
                    assertEquals(2, node.getYPos());
                    assertEquals("2", node.getFloor());
                    assertEquals("Parking", node.getBuilding());
                    assertEquals("PARK", node.getNodeType());
                    assertEquals("Floor2RightParking2", node.getLongName());
                    assertEquals("F2RightP2", node.getShortName());
                }
                if (node.getNodeID().equals("testID3")) {
                    assertEquals(3, node.getXPos());
                    assertEquals(3, node.getYPos());
                    assertEquals("3", node.getFloor());
                    assertEquals("Parking", node.getBuilding());
                    assertEquals("PARK", node.getNodeType());
                    assertEquals("Floor3RightParking3", node.getLongName());
                    assertEquals("F3RightP3", node.getShortName());
                }
            }

            assertEquals(3, edgeList.size());
            assertTrue(edgeIDList.contains("edgeID1"));
            assertTrue(edgeIDList.contains("edgeID2"));
            assertTrue(edgeIDList.contains("edgeID3"));
            for (Edge edge : edgeList) {
                if (edge.getEdgeID().equals("edgeID1")) {
                    assertEquals("edge1S", edge.getStartNodeID());
                    assertEquals("edge1E", edge.getEndNodeID());
                }
                if (edge.getEdgeID().equals("edgeID2")) {
                    assertEquals("edge2S", edge.getStartNodeID());
                    assertEquals("edge2E", edge.getEndNodeID());
                }
                if (edge.getEdgeID().equals("edgeID3")) {
                    assertEquals("edge3S", edge.getStartNodeID());
                    assertEquals("edge3E", edge.getEndNodeID());
                }
            }

            new File(writtenCSVPath).delete();

        } catch (FileNotFoundException e) {
            fail("Cannot test MapCSV.write. Test file not found.");
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testReadMapFile() throws IOException {
        Pair<Stream<Node>, Stream<Edge>> mapStreamPair = MapCSV.readMapFile(testMapFilepath);

        List<Node> nodeList = mapStreamPair.getKey().collect(Collectors.toList());
        List<String> nodeIDList = nodeList.stream().map(Node::getNodeID).collect(Collectors.toList());

        List<Edge> edgeList = mapStreamPair.getValue().collect(Collectors.toList());
        List<String> edgeIDList = edgeList.stream().map(Edge::getEdgeID).collect(Collectors.toList());

        assertEquals(3, nodeList.size());
        assertTrue(nodeIDList.contains("testID1"));
        assertTrue(nodeIDList.contains("testID2"));
        assertTrue(nodeIDList.contains("testID3"));
        for (Node node : nodeList) {
            if (node.getNodeID().equals("testID1")) {
                assertEquals(1, node.getXPos());
                assertEquals(1, node.getYPos());
                assertEquals("1", node.getFloor());
                assertEquals("Parking", node.getBuilding());
                assertEquals("PARK", node.getNodeType());
                assertEquals("Floor1RightParking1", node.getLongName());
                assertEquals("F1RightP1", node.getShortName());
            }
            if (node.getNodeID().equals("testID2")) {
                assertEquals(2, node.getXPos());
                assertEquals(2, node.getYPos());
                assertEquals("2", node.getFloor());
                assertEquals("Parking", node.getBuilding());
                assertEquals("PARK", node.getNodeType());
                assertEquals("Floor2RightParking2", node.getLongName());
                assertEquals("F2RightP2", node.getShortName());
            }
            if (node.getNodeID().equals("testID3")) {
                assertEquals(3, node.getXPos());
                assertEquals(3, node.getYPos());
                assertEquals("3", node.getFloor());
                assertEquals("Parking", node.getBuilding());
                assertEquals("PARK", node.getNodeType());
                assertEquals("Floor3RightParking3", node.getLongName());
                assertEquals("F3RightP3", node.getShortName());
            }
        }

        assertEquals(3, edgeList.size());
        assertTrue(edgeIDList.contains("edgeID1"));
        assertTrue(edgeIDList.contains("edgeID2"));
        assertTrue(edgeIDList.contains("edgeID3"));
        for (Edge edge : edgeList) {
            if (edge.getEdgeID().equals("edgeID1")) {
                assertEquals("edge1S", edge.getStartNodeID());
                assertEquals("edge1E", edge.getEndNodeID());
            }
            if (edge.getEdgeID().equals("edgeID2")) {
                assertEquals("edge2S", edge.getStartNodeID());
                assertEquals("edge2E", edge.getEndNodeID());
            }
            if (edge.getEdgeID().equals("edgeID3")) {
                assertEquals("edge3S", edge.getStartNodeID());
                assertEquals("edge3E", edge.getEndNodeID());
            }
        }
    }
}
