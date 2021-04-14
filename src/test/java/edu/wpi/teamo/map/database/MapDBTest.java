package edu.wpi.teamo.map.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
public class MapDBTest {

    static final String testNodeFile = "src/test/resources/edu/wpi/teamo/map/database/testNodes.csv";
    static final String testEdgeFile = "src/test/resources/edu/wpi/teamo/map/database/testEdges.csv";

    MapDB db;

    @BeforeAll
    public void setup() {
        try {
            db = new MapDB(testNodeFile, testEdgeFile);
        } catch (Exception e) {

        }
    }

    @Test
    public void getAllNodes() {
        /* test get node */
        assert (db.nodeExists("testID1"));
    }

    @Test
    public void setNodePosition() {
        db.setNodePosition("testID1", 1001, 1001);
        assertEquals(1001, db.getNode("testID1").getXPos());
        assertEquals(1001, db.getNode("testID1").getYPos());
    }

    @Test
    public void setNodeLongName() {
        db.setNodeLongName("testID1", "The longest name there ever was");
        assertEquals("The longest name there ever was",
                     db.getNode("testID1").getLongName());
    }

    @Test
    public void addEdge() {
        assertNull(db.getEdge("edge1_2"));
        db.addEdge("edge1_2", "testID1", "testID2");
        assertNotNull(db.getEdge("edge1_2"));
    }

    @Test
    public void addNode() {
        assertNull(db.getNode("nodeForTest"));
        db.addNode("nodeForTest", 24, 34, "ground", "main", "ROOM", "RandomTest", "RT");
        assertNotNull(db.getNode("nodeForTest"));
    }

    @Test
    public void deleteNode() {
        assertNotNull(db.getNode("testID1"));
        db.deleteNode("testID1");
        assertNull(db.getNode("testID1"));
    }

    @Test
    public void deleteEdge() {
        assertNotNull(db.getEdge("edgeID1"));
        db.deleteEdge("edgeID1");
        assertNull(db.getEdge("edgeID1"));
    }

    @Test
    public void testLoadFromCSVs() {
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
    public void testWriteEdgesToCSV() {
        final String writtenFilepath = "src/test/resources/edu/wpi/teamo/map/database/writtenEdges.csv";
        MapDB tMDB = new MapDB("testWriteEdgesToCSVs");
        try {

            tMDB.loadEdgesFromFile(testEdgeFile);
            tMDB.writeEdgesToCSV(writtenFilepath);
            HashMap<String, Edge> edgesToCheck = EdgeCSV.read(writtenFilepath);

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
    public void testWriteNodesToCSV() {
        final String writtenFilepath = "src/test/resources/edu/wpi/teamo/map/database/writtenNodes.csv";
        MapDB tMDB = new MapDB("testWriteNodesToCSVs");
        try {

            tMDB.loadNodesFromFile(testNodeFile);
            tMDB.writeNodesToCSV(writtenFilepath);
            HashMap<String, Node> nodesToCheck = NodeCSV.read(writtenFilepath);

            assertEquals(3, nodesToCheck.size());
            assertEquals("Floor1RightParking1", nodesToCheck.get("testID1").getLongName());
            assertEquals("Floor2RightParking2", nodesToCheck.get("testID2").getLongName());
            assertEquals("Floor3RightParking3", nodesToCheck.get("testID3").getLongName());

            new File(writtenFilepath).delete();

        } catch (FileNotFoundException e) {
            fail("Cannot testLoadFromCSVs, file not found.");
        }
    }
}
