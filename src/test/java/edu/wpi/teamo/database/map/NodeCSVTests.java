package edu.wpi.teamo.database.map;

import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;

import edu.wpi.teamo.database.map.Node;
import edu.wpi.teamo.database.map.NodeCSV;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.HashMap;
import java.io.File;

public class NodeCSVTests {

    static final String testCSVPath = "src/test/resources/edu/wpi/teamo/database/testNodes.csv";

    @Test
    public void testRead() {
        try {

            HashMap<String, Node> nodes = new HashMap<>();
            Stream<Node> nodeStream = NodeCSV.read(new FileReader(testCSVPath));
            nodeStream.forEach((Node node) -> nodes.put(node.getNodeID(), node));

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

    @Test
    public void testWrite() {
        final String writtenFilepath = "src/test/resources/edu/wpi/teamo/database/writtenNodes.csv";

        try {
            Stream<Node> nodeStream = NodeCSV.read(new FileReader(testCSVPath));
            NodeCSV.write(writtenFilepath, nodeStream);

            HashMap<String, Node> nodesToCheck = new HashMap<>();
            Stream<Node> nodeCheckStream = NodeCSV.read(new FileReader(writtenFilepath));
            nodeCheckStream.forEach((Node node) -> nodesToCheck.put(node.getNodeID(), node));

            assertEquals(3, nodesToCheck.size());
            if (nodesToCheck.size() == 3) {

                assertEquals("Floor1RightParking1", nodesToCheck.get("testID1").getLongName());
                assertEquals("Floor2RightParking2", nodesToCheck.get("testID2").getLongName());
                assertEquals("Floor3RightParking3", nodesToCheck.get("testID3").getLongName());

            }

            new File(writtenFilepath).delete();

        } catch (FileNotFoundException e) {
            fail("Cannot test EdgeCSV.write. Test file not found.");
        } catch (IOException e) {
            fail(e.getMessage());
        }


    }
}
