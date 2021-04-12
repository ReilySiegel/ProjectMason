package edu.wpi.teamo.map.database;

import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

public class NodeCSVTests {

    static final String testCSVPath = "src/test/resources/edu/wpi/teamo/map/database/testNodes.csv";

    @Test
    public void testRead() {
        try {

            HashMap<String, Node> nodes = NodeCSV.read(testCSVPath);
            assertEquals(3, nodes.size());
            if (nodes.size() == 3) {

                assertEquals("Floor1RightParking1", nodes.get("testID1").getLongName());
                assertEquals("Floor2RightParking2", nodes.get("testID2").getLongName());
                assertEquals("Floor3RightParking3", nodes.get("testID3").getLongName());

            }

        } catch (FileNotFoundException e) {
            fail("Cannot test NodeCSV.read. Test file not found.");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
