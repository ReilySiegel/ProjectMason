package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.MapDB;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextDirManagerTests {

    /**
     * Basic test for textual directions
     * @throws SQLException If there is a database error
     * @throws ClassNotFoundException If there are any missing classes
     */
    @Test
    public void testArbitraryTextPath() throws SQLException, ClassNotFoundException {

        MapDB mdb = new MapDB("testArbitraryTextPath");
        mdb.addNode("oPARK00101", 3116,1131,"F1", "b","PARK","Floor1RightParking1","F1RightP1");
        mdb.addNode("oPARK00201", 3116,1155,"F1", "b","PARK","Floor1RightParking2","F1RightP2");
        mdb.addNode("oPARK00301", 3116,1181,"F1", "b","PARK","Floor1RightParking3","F1RightP3");
        mdb.addNode("oPARK00401", 3116,1207,"F1", "b","PARK","Floor1RightParking4","F1RightP4");
        mdb.addNode("oPARK00501", 3116,1233,"F1", "b","PARK","Floor1RightParking5","F1RightP5");
        mdb.addNode("oPARK00601", 3116,1253,"F1", "b","PARK","Floor1RightParking6","F1RightP6");
        mdb.addNode("oPARK00701", 3242,1111,"F1", "b","PARK","Floor1RightParking7","F1RightP7");
        mdb.addNode("oPARK00801", 3242,1133,"F1", "b","PARK","Floor1RightParking8","F1RightP8");
        mdb.addNode("oPARK00901", 3242,1157,"F1", "b","PARK","Floor1RightParking9","F1RightP9");
        mdb.addNode("oPARK01001",  3284,1279,"F1", "b","PARK","Floor1RightParkingA","F1RightPA");
        mdb.addEdge("oPARK00101_oPARK00301","oPARK00101","oPARK00301");
        mdb.addEdge("oPARK00101_oPARK00601","oPARK00101","oPARK00601");
        mdb.addEdge("oPARK00101_oPARK00201","oPARK00101","oPARK00201");
        mdb.addEdge("oPARK00201_oPARK00701","oPARK00201","oPARK00701");
        mdb.addEdge("oPARK00601_oPARK00401","oPARK00601","oPARK00401");
        mdb.addEdge("oPARK00601_oPARK00501","oPARK00601","oPARK00501");
        mdb.addEdge("oPARK00701_oPARK00401","oPARK00701","oPARK00401");
        mdb.addEdge("oPARK00701_oPARK00801","oPARK00701","oPARK00801");
        mdb.addEdge("oPARK00801_oPARK00901","oPARK00801","oPARK00901");
        mdb.addEdge("oPARK00801_oPARK01001","oPARK00801","oPARK01001");

        AStarManager asm = new AStarManager(mdb);
        LinkedList<AlgoNode> pathP10_P5 = asm.getPath("oPARK01001","oPARK00501");
        List<String> textualPath = TextDirManager.getTextualDirections(pathP10_P5);
        assertEquals("Proceed towards Floor1RightParking8",textualPath.get(0));
        assertEquals("Proceed rightwards to Floor1RightParking7",textualPath.get(1));
        assertEquals("Proceed backwards to Floor1RightParking4",textualPath.get(2));
        assertEquals("Proceed leftwards to Floor1RightParking6",textualPath.get(3));
        assertEquals("Proceed backwards to Floor1RightParking5",textualPath.get(4));
        assertEquals("You have arrived at your destination.",textualPath.get(5));
    }

    /**
     * Test for textual directions for nodes forming 90 degree angles
     * @throws SQLException If there is a database error
     * @throws ClassNotFoundException If there are any missing classes
     */
    @Test
    public void testBoundaryTextPath() throws SQLException, ClassNotFoundException {

        MapDB mdb = new MapDB("testBoundaryTextPath");
        mdb.addNode("oPARK00101", 3116,1131,"F1", "b","PARK","Floor1RightParking1","F1RightP1");
        mdb.addNode("oPARK00201", 3116,1155,"F1", "b","PARK","Floor1RightParking2","F1RightP2");
        mdb.addNode("oPARK00301", 3123,1155,"F1", "b","PARK","Floor1RightParking3","F1RightP3");
        mdb.addNode("oPARK00401", 3123,1207,"F1", "b","PARK","Floor1RightParking4","F1RightP4");
        mdb.addNode("oPARK00501", 3116,1207,"F1", "b","PARK","Floor1RightParking5","F1RightP5");
        mdb.addNode("oPARK00601", 3110,1207,"F1", "b","PARK","Floor1RightParking6","F1RightP6");

        mdb.addEdge("oPARK00101_oPARK00201","oPARK00101","oPARK00201");
        mdb.addEdge("oPARK00201_oPARK00301","oPARK00201","oPARK00301");
        mdb.addEdge("oPARK00301_oPARK00401","oPARK00301","oPARK00401");
        mdb.addEdge("oPARK00401_oPARK00501","oPARK00401","oPARK00501");
        mdb.addEdge("oPARK00501_oPARK00601","oPARK00501","oPARK00601");

        AStarManager asm = new AStarManager(mdb);
        LinkedList<AlgoNode> pathP1_P6 = asm.getPath("oPARK00101","oPARK00601");
        List<String> textualPath = TextDirManager.getTextualDirections(pathP1_P6);
        assertEquals("Proceed towards Floor1RightParking2",textualPath.get(0));
        assertEquals("Proceed leftwards to Floor1RightParking3",textualPath.get(1));
        assertEquals("Proceed rightwards to Floor1RightParking4",textualPath.get(2));
        assertEquals("Proceed rightwards to Floor1RightParking5",textualPath.get(3));
        assertEquals("Proceed forward to Floor1RightParking6",textualPath.get(4));
        assertEquals("You have arrived at your destination.",textualPath.get(5));
    }

    /**
     * Trivial test case where start and end node are the same
     * @throws SQLException If there is a database error
     * @throws ClassNotFoundException If there are any missing classes
     */
    @Test
    public void trivialTextTest() throws SQLException, ClassNotFoundException {
        MapDB mdb = new MapDB("testTrivialTextPath");
        mdb.addNode("oPARK00101", 3116,1131,"F1", "b","PARK","Floor1RightParking1","F1RightP1");
        mdb.addEdge("oPARK00101_oPARK00101","oPARK00101","oPARK00101");
        AStarManager asm = new AStarManager(mdb);
        LinkedList<AlgoNode> pathP1_P1 = asm.getPath("oPARK00101","oPARK00101");
        List<String> textualPath = TextDirManager.getTextualDirections(pathP1_P1);
        assertEquals("You have arrived at your destination.",textualPath.get(0));
    }

    /**
     * Test for paths at 45 degree angles
     * @throws SQLException If there is a database error
     * @throws ClassNotFoundException If there are any missing classes
     */
    @Test
    public void fortyFiveDegreeDiagonals() throws SQLException, ClassNotFoundException {
        MapDB mdb = new MapDB("fortyFiveDegreeDiagonals");
        mdb.addNode("oPARK00101", 3116,1131,"F1", "b","PARK","Floor1RightParking1","F1RightP1");
        mdb.addNode("oPARK00201", 3126,1141,"F1", "b","PARK","Floor1RightParking2","F1RightP2");
        mdb.addNode("oPARK00301", 3121,1136,"F1", "b","PARK","Floor1RightParking3","F1RightP3");
        mdb.addNode("oPARK00401", 3101,1156,"F1", "b","PARK","Floor1RightParking4","F1RightP4");
        mdb.addNode("oPARK00501", 3091,1146,"F1", "b","PARK","Floor1RightParking5","F1RightP5");
        mdb.addNode("oPARK00601", 3081,1156,"F1", "b","PARK","Floor1RightParking6","F1RightP6");

        mdb.addEdge("oPARK00101_oPARK00201","oPARK00101","oPARK00201");
        mdb.addEdge("oPARK00201_oPARK00301","oPARK00201","oPARK00301");
        mdb.addEdge("oPARK00301_oPARK00401","oPARK00301","oPARK00401");
        mdb.addEdge("oPARK00401_oPARK00501","oPARK00401","oPARK00501");
        mdb.addEdge("oPARK00501_oPARK00601","oPARK00501","oPARK00601");

        AStarManager asm = new AStarManager(mdb);
        LinkedList<AlgoNode> pathP1_P6 = asm.getPath("oPARK00101","oPARK00601");
        List<String> textualPath = TextDirManager.getTextualDirections(pathP1_P6);
        assertEquals("Proceed towards Floor1RightParking2",textualPath.get(0));
        assertEquals("Proceed backwards to Floor1RightParking3",textualPath.get(1));
        assertEquals("Proceed leftwards to Floor1RightParking4",textualPath.get(2));
        assertEquals("Proceed rightwards to Floor1RightParking5",textualPath.get(3));
        assertEquals("Proceed leftwards to Floor1RightParking6",textualPath.get(4));
        assertEquals("You have arrived at your destination.",textualPath.get(5));
    }
}
