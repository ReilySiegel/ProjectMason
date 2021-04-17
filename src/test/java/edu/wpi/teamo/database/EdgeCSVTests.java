package edu.wpi.teamo.database;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.HashMap;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeCSVTests {
    static final String testCSVPath = "src/test/resources/edu/wpi/teamo/database/testEdges.csv";

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

    @Test
    public void testWrite() {
        final String writtenFilepath = "src/test/resources/edu/wpi/teamo/database/writtenEdges.csv";
        try {
            Stream<Edge> edgeStream = EdgeCSV.read(testCSVPath).values().stream();
            EdgeCSV.write(writtenFilepath, edgeStream);

            HashMap<String, Edge> edges = EdgeCSV.read(writtenFilepath);
            assertEquals(3, edges.size());
            if (edges.size() == 3) {

                assertEquals("edge1E", edges.get("edgeID1").getEndNodeID());
                assertEquals("edge2E", edges.get("edgeID2").getEndNodeID());
                assertEquals("edge3E", edges.get("edgeID3").getEndNodeID());

            }

            new File(writtenFilepath).delete();

        } catch (FileNotFoundException e) {
            fail("Cannot test EdgeCSV.write. Test file not found.");
        } catch (IOException e) {
            fail(e.getMessage());
        }


    }
}
