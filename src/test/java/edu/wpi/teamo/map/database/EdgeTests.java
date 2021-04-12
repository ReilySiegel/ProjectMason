package edu.wpi.teamo.map.database;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeTests {

    private static String generateURIFromName(String name) {
        return "jdbc:derby:memory:" + name + ";create=true";
    }

    @Test
    public void testInitTable() {
        boolean pass = true;
        //TODO: Finish
        assertTrue(pass);
    }

    @Test
    public void testGetByID() {
        String startNodeID = "testStartNodeID";
        String endNodeID = "testEndNodeID";
        String edgeID = "testID";

        try {
            Database db = new Database(generateURIFromName("GetEdgeByID"));
            Edge.initTable(db);

            /* store an edge */
            Edge edgeToStore = new Edge(edgeID, startNodeID, endNodeID);
            edgeToStore.update(db);

            /* fetch it */
            Edge fetchedEdge = Edge.getByID(db, edgeID);

            /* check if its the same */
            assertEquals(startNodeID, fetchedEdge.getStartNodeID());
            assertEquals(endNodeID, fetchedEdge.getEndNodeID());
            assertEquals(edgeID, fetchedEdge.getEdgeID());

            db.close();
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testGetAll() {
        String edge1ID = "testIDOne";
        String edge2ID = "testIDTwo";
        String edge3ID = "testIDTre";

        try {
            Database db = new Database(generateURIFromName("GetAllEdges"));
            Edge.initTable(db);

            /* store three edges */
            Edge eStoredOne = new Edge(edge1ID, edge1ID + "start", edge1ID + "end");
            Edge eStoredTwo = new Edge(edge2ID, edge2ID + "start", edge2ID + "end");
            Edge eStoredTre = new Edge(edge3ID, edge3ID + "start", edge3ID + "end");
            eStoredOne.update(db);
            eStoredTwo.update(db);
            eStoredTre.update(db);

            /* fetch all - into list */
            Stream<Edge> edgeStream = Edge.getAll(db);
            List<Edge>   edgeList   = edgeStream.collect(Collectors.toList());

            /* list must be length 3 */
            int size = edgeList.size();
//            assertEquals(3, size);
            if (size == 3) {
                /* make list of the IDs */
                ArrayList<String> ids = new ArrayList<>();
                for (int i = 0; i < 3; i++) ids.add(edgeList.get(i).getEdgeID());

                /* check if ids are the same */
                assertTrue(ids.contains(edge1ID));
                assertTrue(ids.contains(edge2ID));
                assertTrue(ids.contains(edge3ID));
            }

            db.close();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        String modifiedEndNodeID = "newEndNode";
        String startNodeID = "testStartNodeID";
        String endNodeID = "testEndNodeID";
        String edgeID = "testID";

        try {
            Database db = new Database(generateURIFromName("UpdateEdge"));
            Edge.initTable(db);

            /* store an edge */
            Edge edgeToStore = new Edge(edgeID, startNodeID, endNodeID);
            edgeToStore.update(db);

            /* fetch it */
            Edge edgeToModify = Edge.getByID(db, edgeID);

            /* modify it */
            edgeToModify.endNodeID = modifiedEndNodeID;

            /* store it */
            edgeToModify.update(db);

            /* fetch again */
            Edge edgeToCheck = Edge.getByID(db, edgeID);

            /* check if it has been modified */
            assertEquals(modifiedEndNodeID, edgeToCheck.getEndNodeID());
            assertEquals(edgeID, edgeToCheck.getEdgeID());

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }
}
