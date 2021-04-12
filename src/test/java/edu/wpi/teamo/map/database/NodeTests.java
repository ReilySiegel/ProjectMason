package edu.wpi.teamo.map.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NodeTests {

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
        String building = "building";
        String shortName = "short";
        String longName = "long";
        String nodeID = "testID";
        String floor = "floor";
        String type = "type";
        int xPos = 1;
        int yPos = 2;

        try {
            Database db = new Database(generateURIFromName("GetNodeByID"));
            Node.initTable(db);

            /* store an edge */
            Node nodeToStore = new Node(nodeID, xPos, yPos, floor, building, type, longName, shortName);
            nodeToStore.update(db);

            /* fetch it */
            Node nodeToCheck = Node.getByID(db, nodeID);

            /* check if its the same */
            assertEquals(shortName, nodeToCheck.getShortName());
            assertEquals(longName, nodeToCheck.getLongName());
            assertEquals(building, nodeToCheck.getBuilding());
            assertEquals(type, nodeToCheck.getNodeType());
            assertEquals(nodeID, nodeToCheck.getNodeID());
            assertEquals(floor, nodeToCheck.getFloor());
            assertEquals(xPos, nodeToCheck.getXPos());
            assertEquals(yPos, nodeToCheck.getYPos());

            db.close();
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testGetAll() {
        boolean pass = true;
        //TODO: Finish
        assertTrue(pass);
    }

    @Test
    public void testUpdate() {
        boolean pass = true;
        //TODO: Finish
        assertTrue(pass);
    }
}
