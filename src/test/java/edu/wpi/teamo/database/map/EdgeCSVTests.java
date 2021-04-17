package edu.wpi.teamo.database.map;

import edu.wpi.teamo.database.map.Edge;
import edu.wpi.teamo.database.map.EdgeCSV;
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

            HashMap<String, Edge> edges = new HashMap<>();
            Stream<Edge> edgeStream = EdgeCSV.read(testCSVPath);
            edgeStream.forEach((Edge edge) -> edges.put(edge.getEdgeID(), edge));

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
            EdgeCSV.write(writtenFilepath, EdgeCSV.read(testCSVPath));

            HashMap<String, Edge> edges = new HashMap<>();
            Stream<Edge> edgeStream = EdgeCSV.read(writtenFilepath);
            edgeStream.forEach((Edge edge) -> edges.put(edge.getEdgeID(), edge));

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
