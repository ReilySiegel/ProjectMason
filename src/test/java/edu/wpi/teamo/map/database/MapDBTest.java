package edu.wpi.teamo.map.database;

import org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;
import java.util.stream.Stream;
import java.sql.SQLException;
import java.io.IOException;
import java.util.HashMap;
import java.io.File;

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
        assertNull(db.getEdge("edge1_2"));
        db.addEdge("edge1_2", "testID1", "testID2");
        assertNotNull(db.getEdge("edge1_2"));
    }

    @Test
    public void addNode() throws SQLException {
        assertNull(db.getNode("nodeForTest"));
        db.addNode("nodeForTest", 24, 34, "ground", "main", "ROOM", "RandomTest", "RT");
        assertNotNull(db.getNode("nodeForTest"));
    }

    @Test
    public void deleteNode() throws SQLException {
        assertNotNull(db.getNode("testID1"));
        db.deleteNode("testID1");
        assertNull(db.getNode("testID1"));
    }

    @Test
    public void deleteEdge() throws SQLException {
        assertNotNull(db.getEdge("edgeID1"));
        db.deleteEdge("edgeID1");
        assertNull(db.getEdge("edgeID1"));
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
    public void testWriteEdgesToCSV() throws IOException, SQLException, ClassNotFoundException {
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
    public void testWriteNodesToCSV() throws IOException, SQLException, ClassNotFoundException {
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
