package edu.wpi.teamo.map.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class MapDBTest {

    MapDB db;

    @BeforeAll
    public void setup() {
        try {
            db = new MapDB("src/test/resources/edu/wpi/teamo/map/database/testNodes.csv",
                    "src/test/resources/edu/wpi/teamo/map/database/testEdges.csv");
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

}
