package edu.wpi.teamo.map.database;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeCSVTests {
    static final String testCSVPath = "src/test/resources/edu/wpi/teamo/map/database/testEdges.csv";

    @Test
    public void testRead() {
        try {

            HashMap<String, Edge> edges = EdgeCSV.read(testCSVPath);
            assertEquals(3, edges.size());
            if (edges.size() == 3) {

                assertEquals("edge1E", edges.get("edgeID1").getEndNodeID());
                assertEquals("edge2E", edges.get("edgeID2").getEndNodeID());
                assertEquals("edge3E", edges.get("edgeID3").getEndNodeID());

            }

        } catch (FileNotFoundException e) {
            fail("Cannot test EdgeCSV.read. Test file not found.");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
