package edu.wpi.teamo.map.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeTests {

    @Test
    public void testInitTable() {
        boolean pass = true;
        //TODO: Finish
        assertTrue(pass);
    }

    @Test
    public void testGetByID() {
        boolean pass = true;
        //TODO: Finish
        assertTrue(pass);
    }

    @Test
    public void testGetAll() {
        boolean pass = true;
        //TODO: Finish
        assertTrue(pass);
    }

    @Test
    public void testUpdate() {
        String modifiedEndNodeID = "newEndNode";
        String startNodeID = "testStartNodeID";
        String endNodeID = "testEndNodeID";
        String edgeID = "testID";

        try {
            Database db = new Database();
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
