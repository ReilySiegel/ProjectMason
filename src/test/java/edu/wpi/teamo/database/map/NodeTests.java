package edu.wpi.teamo.database.map;

import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Collectors;

import edu.wpi.teamo.database.Database;
import edu.wpi.teamo.database.map.Node;
import org.junit.jupiter.api.Test;
import java.util.stream.Stream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NodeTests {

    private static String generateURIFromName(String name) {
        return "jdbc:derby:memory:" + name + ";create=true";
    }

    @Test
    public void testInitTable() {
        Database db = null;

        /* init database */
        try {
            db = new Database(generateURIFromName("InitNodeTable"));
        } catch (SQLException | ClassNotFoundException e) {
            fail(e.getMessage());
        }

        /* init node table */
        try {
            Node.initTable(db);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        /* verify table exists */
        try {
            Node.getAll(db);
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        /* close db */
        try {
            db.close();
        } catch (SQLException e) {
            fail(e.getMessage());
        }
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
        String node1ID = "testIDOne";
        String node2ID = "testIDTwo";
        String node3ID = "testIDTre";

        try {
            Database db = new Database(generateURIFromName("GetAllNodes"));
            Node.initTable(db);

            /* store three edges */
            Node nStoredOne = new Node(node1ID, 1, 1, "xd", "xd", "xd", "xd", "xd");
            Node nStoredTwo = new Node(node2ID, 1, 1, "xd", "xd", "xd", "xd", "xd");
            Node nStoredTre = new Node(node3ID, 1, 1, "xd", "xd", "xd", "xd", "xd");
            nStoredOne.update(db);
            nStoredTwo.update(db);
            nStoredTre.update(db);

            /* fetch all - into list */
            Stream<Node> nodeStream = Node.getAll(db);
            List<Node> nodeList   = nodeStream.collect(Collectors.toList());

            /* list must be length 3 */
            int size = nodeList.size();
            assertEquals(3, size);
            if (size == 3) {
                /* make list of the IDs */
                ArrayList<String> ids = new ArrayList<>();
                for (int i = 0; i < 3; i++) ids.add(nodeList.get(i).getNodeID());

                /* check if ids are the same */
                assertTrue(ids.contains(node1ID));
                assertTrue(ids.contains(node2ID));
                assertTrue(ids.contains(node3ID));
            }

            db.close();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        String initialName = "initialName";
        String modifiedName = "newName";
        String nodeID = "nodeID1";

        try {
            Database db = new Database(generateURIFromName("UpdateNode"));
            Node.initTable(db);

            /* store an node */
            Node nodeToStore = new Node(nodeID, 1, 1, "xd", "xd", "xd", initialName, "xd");
            nodeToStore.update(db);

            /* fetch it */
            Node nodeToModify = Node.getByID(db, nodeID);

            /* modify it */
            nodeToModify.setLongName(modifiedName);

            /* store it */
            nodeToModify.update(db);

            /* fetch again */
            Node nodeToCheck = Node.getByID(db, nodeID);

            /* check if it has been modified */
            assertEquals(modifiedName, nodeToCheck.getLongName());
            assertEquals(nodeID, nodeToCheck.getNodeID());

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testDelete() {

        String nodeID = "nodeID1";

        try {
            Database db = new Database(generateURIFromName("DeleteNode"));
            Node.initTable(db);

            /* store an node */
            Node nodeToStore = new Node(nodeID, 1, 1, "xd", "xd", "xd", "initialName", "xd");
            nodeToStore.update(db);

            /* fetch it */
            Node nodeToModify = Node.getByID(db, nodeID);

            /* delete it */
            nodeToModify.delete(db);

            /* Check that node no longer exists */
            assertThrows(SQLException.class, () -> Node.getByID(db, nodeID));

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
