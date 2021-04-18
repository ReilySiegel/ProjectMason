package edu.wpi.teamo.database.map;

import edu.wpi.teamo.database.map.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import java.util.stream.Stream;
import java.sql.SQLException;
import java.io.IOException;
import java.util.HashMap;
import javafx.util.Pair;
import java.util.List;
import java.io.File;

@TestInstance(Lifecycle.PER_CLASS)
public class MapDBTest {

    static final String testNodeFile = "src/test/resources/edu/wpi/teamo/database/testNodes.csv";
    static final String testEdgeFile = "src/test/resources/edu/wpi/teamo/database/testEdges.csv";
    static final String testMapFile = "src/test/resources/edu/wpi/teamo/database/testMap.csv";

    MapDB db;

    @BeforeAll
    public void setup() {
        try {
            db = new MapDB("MapDBTests");
            db.loadEdgesFromFile(testEdgeFile);
            db.loadNodesFromFile(testNodeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllNodes() {
        /* test get node */
        assert (db.nodeExists("testID1"));
    }

    @Test
    public void setNodePosition() throws SQLException {
        db.setNodePosition("testID1", 1001, 1001);
        assertEquals(1001, db.getNode("testID1").getXPos());
        assertEquals(1001, db.getNode("testID1").getYPos());
    }

    @Test
    public void setNodeLongName() throws SQLException {
        db.setNodeLongName("testID1", "The longest name there ever was");
        assertEquals("The longest name there ever was",
                     db.getNode("testID1").getLongName());
    }

    @Test
    public void addEdge() throws SQLException {
        assertThrows(SQLException.class, () -> db.getEdge("edge1_2"));
        db.addEdge("edge1_2", "testID1", "testID2");
        assertNotNull(db.getEdge("edge1_2"));
    }

    @Test
    public void addNode() throws SQLException {
        assertThrows(SQLException.class, () -> db.getNode("nodeForTest"));
        db.addNode("nodeForTest", 24, 34, "ground", "main", "ROOM", "RandomTest", "RT");
        assertNotNull(db.getNode("nodeForTest"));
    }

    @Test
    public void deleteNode() throws SQLException {
        assertThrows(SQLException.class, () -> db.deleteNode("ID that dont exist"));
        assertNotNull(db.getNode("testID1"));
        db.deleteNode("testID1");
        assertThrows(SQLException.class, () -> db.getNode("testID1"));
    }

    @Test
    public void deleteEdge() throws SQLException {
        assertThrows(SQLException.class, () -> db.deleteEdge("ID that dont exist"));
        assertNotNull(db.getEdge("edgeID1"));
        db.deleteEdge("edgeID1");
        assertThrows(SQLException.class, () -> db.getEdge("edgeID1"));
    }

    @Test
    public void testLoadFromCSVs() throws SQLException, ClassNotFoundException {
        MapDB tMDB = new MapDB("testLoadFromCSVs");
        try {

            tMDB.loadEdgesFromFile(testEdgeFile);
            tMDB.loadNodesFromFile(testNodeFile);

            Stream<NodeInfo> nodeStream = tMDB.getAllNodes();
            assertEquals(3, nodeStream.count());

            Stream<EdgeInfo> edgeStream = tMDB.getAllEdges();
            assertEquals(3, edgeStream.count());

        } catch (FileNotFoundException e) {
            fail("Cannot testLoadFromCSVs, file not found.");
        }
    }

    @Test
    public void testLoadMapCSV() throws SQLException, ClassNotFoundException, IOException {
        MapDB mDB = new MapDB("testLoadMapCSV");
        try {

            mDB.loadMapFromCSV(testMapFile);

            Stream<NodeInfo> nodeStream = mDB.getAllNodes();
            assertEquals(3, nodeStream.count());

            Stream<EdgeInfo> edgeStream = mDB.getAllEdges();
            assertEquals(3, edgeStream.count());

        } catch (FileNotFoundException e) {
            fail("Cannot testLoadFromCSVs, file not found.");
        }
    }

    @Test
    public void testWriteEdgesToCSV() throws IOException, SQLException, ClassNotFoundException {
        final String writtenFilepath = "src/test/resources/edu/wpi/teamo/database/writtenEdges.csv";
        MapDB tMDB = new MapDB("testWriteEdgesToCSVs");
        try {

            tMDB.loadEdgesFromFile(testEdgeFile);
            tMDB.writeEdgesToCSV(writtenFilepath);

            HashMap<String, Edge> edgesToCheck = new HashMap<>();
            Stream<Edge> edgeStream = EdgeCSV.read(writtenFilepath);
            edgeStream.forEach((Edge edge) -> edgesToCheck.put(edge.getEdgeID(), edge));

            assertEquals(3, edgesToCheck.size());
            assertEquals("edge1E", edgesToCheck.get("edgeID1").getEndNodeID());
            assertEquals("edge2E", edgesToCheck.get("edgeID2").getEndNodeID());
            assertEquals("edge3E", edgesToCheck.get("edgeID3").getEndNodeID());

            new File(writtenFilepath).delete();

        } catch (FileNotFoundException e) {
            fail("Cannot testLoadFromCSVs, file not found.");
        }
    }

    @Test
    public void testWriteNodesToCSV() throws IOException, SQLException, ClassNotFoundException {
        final String writtenFilepath = "src/test/resources/edu/wpi/teamo/database/writtenNodes.csv";
        MapDB tMDB = new MapDB("testWriteNodesToCSVs");
        try {

            tMDB.loadNodesFromFile(testNodeFile);
            tMDB.writeNodesToCSV(writtenFilepath);
            HashMap<String, Node> nodesToCheck = new HashMap<>();
            Stream<Node> nodeStream = NodeCSV.read(writtenFilepath);
            nodeStream.forEach((Node node) -> nodesToCheck.put(node.getNodeID(), node));

            assertEquals(3, nodesToCheck.size());
            assertEquals("Floor1RightParking1", nodesToCheck.get("testID1").getLongName());
            assertEquals("Floor2RightParking2", nodesToCheck.get("testID2").getLongName());
            assertEquals("Floor3RightParking3", nodesToCheck.get("testID3").getLongName());

            new File(writtenFilepath).delete();

        } catch (FileNotFoundException e) {
            fail("Cannot testLoadFromCSVs, file not found.");
        }
    }

    @Test
    public void testWriteMapToCSV() throws SQLException, ClassNotFoundException, IOException {
        final String writtenFilepath = "src/test/resources/edu/wpi/teamo/database/writtenMap.csv";
        MapDB tMDB = new MapDB("testWriteMapToCSV");

        tMDB.loadMapFromCSV(testMapFile);

        tMDB.writeMapToCSV(writtenFilepath);

        /* read it and check that it is sound */
        Pair<Stream<Node>, Stream<Edge>> mapStreamPair = MapCSV.readMapFile(writtenFilepath);

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

        new File(writtenFilepath).delete();
    }

    @Test
    public void testSetEdgeID() throws SQLException, ClassNotFoundException, FileNotFoundException {
        MapDB tMDB = new MapDB("testSetEdgeID");
        tMDB.loadEdgesFromFile(testEdgeFile);
        assertFalse(tMDB.edgeExists("newID"));
        assertTrue(tMDB.edgeExists("edgeID1"));

        tMDB.setEdgeID("edgeID1", "newID");

        assertFalse(tMDB.edgeExists("edgeID1"));
        assertTrue(tMDB.edgeExists("newID"));
    }

    @Test
    public void testSetEdgeStartID() throws SQLException, ClassNotFoundException, FileNotFoundException {
        MapDB tMDB = new MapDB("testSetEdgeStartID");
        tMDB.loadEdgesFromFile(testEdgeFile);
        assertEquals("edge1S", tMDB.getEdge("edgeID1").getStartNodeID());

        tMDB.setEdgeStartID("edgeID1", "newStartID");

        assertEquals("newStartID", tMDB.getEdge("edgeID1").getStartNodeID());
    }

    @Test
    public void testSetEdgeEndID() throws SQLException, ClassNotFoundException, FileNotFoundException {
        MapDB tMDB = new MapDB("testSetEdgeEndID");
        tMDB.loadEdgesFromFile(testEdgeFile);
        assertEquals("edge1E", tMDB.getEdge("edgeID1").getEndNodeID());

        tMDB.setEdgeEndID("edgeID1", "newEndID");

        assertEquals("newEndID", tMDB.getEdge("edgeID1").getEndNodeID());
    }
}
