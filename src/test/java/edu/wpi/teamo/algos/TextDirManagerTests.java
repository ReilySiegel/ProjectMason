package edu.wpi.teamo.algos;

import edu.wpi.teamo.App;
import edu.wpi.teamo.database.map.MapDB;
import edu.wpi.teamo.views.LocaleType;
import javafx.util.Pair;
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
        App.switchLocale("en", "US", LocaleType.en_US, true);
        List<String> textualPath = TextDirManager.getTextualDirections(pathP10_P5, true);
        assertEquals("Proceed 19.0 meter(s) towards Floor1RightParking8.",textualPath.get(0));
        assertEquals("Proceed 3.0 meter(s) rightwards to Floor1RightParking7.",textualPath.get(1));
        assertEquals("Proceed 20.0 meter(s) backwards and to the left to Floor1RightParking4.",textualPath.get(2));
        assertEquals("Proceed 6.0 meter(s) leftwards to Floor1RightParking6.",textualPath.get(3));
        assertEquals("Proceed 3.0 meter(s) backwards to Floor1RightParking5.",textualPath.get(4));
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
        App.switchLocale("en", "US", LocaleType.en_US, true);
        List<String> textualPath = TextDirManager.getTextualDirections(pathP1_P6, true);
        assertEquals("Proceed 3.0 meter(s) towards Floor1RightParking2.",textualPath.get(0));
        assertEquals("Proceed 1.0 meter(s) leftwards to Floor1RightParking3.",textualPath.get(1));
        assertEquals("Proceed 7.0 meter(s) rightwards to Floor1RightParking4.",textualPath.get(2));
        assertEquals("Proceed 1.0 meter(s) rightwards to Floor1RightParking5.",textualPath.get(3));
        assertEquals("Proceed 1.0 meter(s) forward to Floor1RightParking6.",textualPath.get(4));
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
        App.switchLocale("en", "US", LocaleType.en_US, true);
        List<String> textualPath = TextDirManager.getTextualDirections(pathP1_P1,true);
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
        App.switchLocale("en", "US", LocaleType.en_US, true);
        List<String> textualPath = TextDirManager.getTextualDirections(pathP1_P6,true);
        assertEquals("Proceed 2.0 meter(s) towards Floor1RightParking2.",textualPath.get(0));
        assertEquals("Proceed 1.0 meter(s) backwards to Floor1RightParking3.",textualPath.get(1));
        assertEquals("Proceed 4.0 meter(s) leftwards to Floor1RightParking4.",textualPath.get(2));
        assertEquals("Proceed 2.0 meter(s) rightwards to Floor1RightParking5.",textualPath.get(3));
        assertEquals("Proceed 2.0 meter(s) leftwards to Floor1RightParking6.",textualPath.get(4));
        assertEquals("You have arrived at your destination.",textualPath.get(5));
    }

    /**
     * Test for paths whose edges are at near boundaries (very close to x/y axes)
     * @throws SQLException If there is a database error
     * @throws ClassNotFoundException If there are any missing classes
     */
    @Test
    public void nearBoundaryTest() throws SQLException, ClassNotFoundException
    {
        MapDB mdb = new MapDB("nearBoundaryTest");
        mdb.addNode("oPARK00101", 3116,1131,"F1", "b","PARK","Floor1RightParking1","F1RightP1");
        mdb.addNode("oPARK00201", 3117,1141,"F1", "b","PARK","Floor1RightParking2","F1RightP2");
        mdb.addNode("oPARK00301", 3127,1142,"F1", "b","PARK","Floor1RightParking3","F1RightP3");
        mdb.addNode("oPARK00401", 3126,1152,"F1", "b","PARK","Floor1RightParking4","F1RightP4");
        mdb.addNode("oPARK00501", 3116,1154,"F1", "b","PARK","Floor1RightParking5","F1RightP5");
        mdb.addNode("oPARK00601", 3115,1164,"F1", "b","PARK","Floor1RightParking6","F1RightP6");

        mdb.addEdge("oPARK00101_oPARK00201","oPARK00101","oPARK00201");
        mdb.addEdge("oPARK00201_oPARK00301","oPARK00201","oPARK00301");
        mdb.addEdge("oPARK00301_oPARK00401","oPARK00301","oPARK00401");
        mdb.addEdge("oPARK00401_oPARK00501","oPARK00401","oPARK00501");
        mdb.addEdge("oPARK00501_oPARK00601","oPARK00501","oPARK00601");
        AStarManager asm = new AStarManager(mdb);
        LinkedList<AlgoNode> pathP1_P6 = asm.getPath("oPARK00101","oPARK00601");
        App.switchLocale("en", "US", LocaleType.en_US, true);
        List<String> textualPath = TextDirManager.getTextualDirections(pathP1_P6, true);
        assertEquals("Proceed 1.0 meter(s) towards Floor1RightParking2.",textualPath.get(0));
        assertEquals("Proceed 1.0 meter(s) leftwards to Floor1RightParking3.",textualPath.get(1));
        assertEquals("Proceed 1.0 meter(s) rightwards to Floor1RightParking4.",textualPath.get(2));
        assertEquals("Proceed 1.0 meter(s) rightwards to Floor1RightParking5.",textualPath.get(3));
        assertEquals("Proceed 1.0 meter(s) leftwards to Floor1RightParking6.",textualPath.get(4));
        assertEquals("You have arrived at your destination.",textualPath.get(5));
    }

    /**
     * Test for testing the textual direction manager's ability to properly assign quadrants to each node-edge-node segment
     * @throws SQLException If there is a database error
     * @throws ClassNotFoundException If there are any missing classes
     */
    @Test
    public void quadrantComputationTest() throws SQLException, ClassNotFoundException {
        MapDB mdb = new MapDB("quadrantFormation");
        mdb.addNode("oPARK00101", 3116,1131,"F1", "b","PARK","Floor1RightParking1","F1RightP1");
        mdb.addNode("oPARK00201", 3116,1121,"F1", "b","PARK","Floor1RightParking2","F1RightP2");
        mdb.addNode("oPARK00301", 3126,1121,"F1", "b","PARK","Floor1RightParking3","F1RightP3");
        mdb.addNode("oPARK00401", 3136,1122,"F1", "b","PARK","Floor1RightParking4","F1RightP4");
        mdb.addNode("oPARK00501", 3126,1129,"F1", "b","PARK","Floor1RightParking5","F1RightP5");
        mdb.addNode("oPARK00601", 3115,1150,"F1", "b","PARK","Floor1RightParking6","F1RightP6");
        mdb.addNode("oPARK00701", 3116,1150,"F1", "b","PARK","Floor1RightParking7","F1RightP7");
        mdb.addNode("oPARK00801", 3114,1150,"F1", "b","PARK","Floor1RightParking8","F1RightP8");
        mdb.addNode("oPARK00901", 3106,1126,"F1", "b","PARK","Floor1RightParking9","F1RightP9");
        mdb.addNode("oPARK001001", 3096,1122,"F1", "b","PARK","Floor1RightParking10","F1RightP10");
        mdb.addNode("oPARK001101", 3096,1121,"F1", "b","PARK","Floor1RightParking11","F1RightP11");
        mdb.addNode("oPARK001201", 3096,1120,"F1", "b","PARK","Floor1RightParking12","F1RightP12");
        mdb.addNode("oPARK001301", 3090,1116,"F1", "b","PARK","Floor1RightParking13","F1RightP13");
        mdb.addNode("oPARK001401", 3115,1011,"F1", "b","PARK","Floor1RightParking14","F1RightP14");
        mdb.addNode("oPARK001501", 3116,1011,"F1", "b","PARK","Floor1RightParking15","F1RightP15");
        mdb.addNode("oPARK001601", 3117,1011,"F1", "b","PARK","Floor1RightParking16","F1RightP16");
        mdb.addNode("oPARK001701", 3120,1116,"F1", "b","PARK","Floor1RightParking17","F1RightP17");
        mdb.addNode("oPARK001801", 3126,1120,"F1", "b","PARK","Floor1RightParking18","F1RightP18");


        mdb.addEdge("oPARK00101_oPARK00201","oPARK00101","oPARK00201");
        mdb.addEdge("oPARK00201_oPARK00301","oPARK00201","oPARK00301");
        mdb.addEdge("oPARK00201_oPARK00401","oPARK00201","oPARK00401");
        mdb.addEdge("oPARK00201_oPARK00501","oPARK00201","oPARK00501");
        mdb.addEdge("oPARK00201_oPARK00601","oPARK00201","oPARK00601");
        mdb.addEdge("oPARK00201_oPARK00701","oPARK00201","oPARK00701");
        mdb.addEdge("oPARK00201_oPARK00801","oPARK00201","oPARK00801");
        mdb.addEdge("oPARK00201_oPARK00901","oPARK00201","oPARK00901");
        mdb.addEdge("oPARK00201_oPARK001001","oPARK00201","oPARK001001");
        mdb.addEdge("oPARK00201_oPARK001101","oPARK00201","oPARK001101");
        mdb.addEdge("oPARK00201_oPARK001201","oPARK00201","oPARK001201");
        mdb.addEdge("oPARK00201_oPARK001301","oPARK00201","oPARK001301");
        mdb.addEdge("oPARK00201_oPARK001401","oPARK00201","oPARK001401");
        mdb.addEdge("oPARK00201_oPARK001501","oPARK00201","oPARK001501");
        mdb.addEdge("oPARK00201_oPARK001601","oPARK00201","oPARK001601");
        mdb.addEdge("oPARK00201_oPARK001701","oPARK00201","oPARK001701");
        mdb.addEdge("oPARK00201_oPARK001801","oPARK00201","oPARK001801");

        AStarManager asm = new AStarManager(mdb);
        LinkedList<AlgoNode> pathP1_P3 = asm.getPath("oPARK00101","oPARK00301");
        LinkedList<AlgoNode> pathP1_P4 = asm.getPath("oPARK00101","oPARK00401");
        LinkedList<AlgoNode> pathP1_P5 = asm.getPath("oPARK00101","oPARK00501");
        LinkedList<AlgoNode> pathP1_P6 = asm.getPath("oPARK00101","oPARK00601");
        LinkedList<AlgoNode> pathP1_P7 = asm.getPath("oPARK00101","oPARK00701");
        LinkedList<AlgoNode> pathP1_P8 = asm.getPath("oPARK00101","oPARK00801");
        LinkedList<AlgoNode> pathP1_P9 = asm.getPath("oPARK00101","oPARK00901");
        LinkedList<AlgoNode> pathP1_P10 = asm.getPath("oPARK00101","oPARK001001");
        LinkedList<AlgoNode> pathP1_P11 = asm.getPath("oPARK00101","oPARK001101");
        LinkedList<AlgoNode> pathP1_P12 = asm.getPath("oPARK00101","oPARK001201");
        LinkedList<AlgoNode> pathP1_P13 = asm.getPath("oPARK00101","oPARK001301");
        LinkedList<AlgoNode> pathP1_P14 = asm.getPath("oPARK00101","oPARK001401");
        LinkedList<AlgoNode> pathP1_P15 = asm.getPath("oPARK00101","oPARK001501");
        LinkedList<AlgoNode> pathP1_P16 = asm.getPath("oPARK00101","oPARK001601");
        LinkedList<AlgoNode> pathP1_P17 = asm.getPath("oPARK00101","oPARK001701");
        LinkedList<AlgoNode> pathP1_P18 = asm.getPath("oPARK00101","oPARK001801");
        
        Pair<Integer, Double> q1 = TextDirManager.getQuadrant(pathP1_P3.get(1),pathP1_P3.get(2));
        Pair<Integer, Double> q2 = TextDirManager.getQuadrant(pathP1_P4.get(1),pathP1_P4.get(2));
        Pair<Integer, Double> q3 = TextDirManager.getQuadrant(pathP1_P5.get(1),pathP1_P5.get(2));
        Pair<Integer, Double> q4 = TextDirManager.getQuadrant(pathP1_P6.get(1),pathP1_P6.get(2));
        Pair<Integer, Double> q5 = TextDirManager.getQuadrant(pathP1_P7.get(1),pathP1_P7.get(2));
        Pair<Integer, Double> q6 = TextDirManager.getQuadrant(pathP1_P8.get(1),pathP1_P8.get(2));
        Pair<Integer, Double> q7 = TextDirManager.getQuadrant(pathP1_P9.get(1),pathP1_P9.get(2));
        Pair<Integer, Double> q8 = TextDirManager.getQuadrant(pathP1_P10.get(1),pathP1_P10.get(2));
        Pair<Integer, Double> q9 = TextDirManager.getQuadrant(pathP1_P11.get(1),pathP1_P11.get(2));
        Pair<Integer, Double> q10 = TextDirManager.getQuadrant(pathP1_P12.get(1),pathP1_P12.get(2));
        Pair<Integer, Double> q11 = TextDirManager.getQuadrant(pathP1_P13.get(1),pathP1_P13.get(2));
        Pair<Integer, Double> q12 = TextDirManager.getQuadrant(pathP1_P14.get(1),pathP1_P14.get(2));
        Pair<Integer, Double> q13 = TextDirManager.getQuadrant(pathP1_P15.get(1),pathP1_P15.get(2));
        Pair<Integer, Double> q14 = TextDirManager.getQuadrant(pathP1_P16.get(1),pathP1_P16.get(2));
        Pair<Integer, Double> q15 = TextDirManager.getQuadrant(pathP1_P17.get(1),pathP1_P17.get(2));
        Pair<Integer, Double> q16 = TextDirManager.getQuadrant(pathP1_P18.get(1),pathP1_P18.get(2));

        assertEquals(q1.getKey(), 5);
        assertEquals(q2.getKey(), 5);
        assertEquals(q3.getKey(), 1);
        assertEquals(q4.getKey(), 6);
        assertEquals(q5.getKey(), 6);
        assertEquals(q6.getKey(), 6);
        assertEquals(q7.getKey(), 2);
        assertEquals(q8.getKey(), 7);
        assertEquals(q9.getKey(), 7);
        assertEquals(q10.getKey(), 7);
        assertEquals(q11.getKey(), 3);
        assertEquals(q12.getKey(), 8);
        assertEquals(q13.getKey(), 8);
        assertEquals(q14.getKey(), 8);
        assertEquals(q15.getKey(), 4);
        assertEquals(q16.getKey(), 5);
    }

    /**
     * Test for switching floors
     * @throws SQLException If there is a database error
     * @throws ClassNotFoundException If there are any missing classes
     */
    @Test
    public void floorSwitchTest() throws SQLException, ClassNotFoundException {
        MapDB mdb = new MapDB("floorSwitch");
        mdb.addNode("oPARK00101", 3116,1131,"F1", "b","PARK","Floor1RightParking1","F1RightP1");
        mdb.addNode("oPARK00201", 3116,1121,"F1", "b","PARK","Floor1RightParking2","F1RightP2");
        mdb.addNode("oPARK00301", 3126,1121,"F2", "b","PARK","Floor1RightParking3","F1RightP3");
        mdb.addNode("oPARK00401", 3136,1122,"F2", "b","PARK","Floor1RightParking4","F1RightP4");

        mdb.addEdge("oPARK00101_oPARK00201","oPARK00101","oPARK00201");
        mdb.addEdge("oPARK00201_oPARK00301","oPARK00201","oPARK00301");
        mdb.addEdge("oPARK00301_oPARK00401","oPARK00301","oPARK00401");

        AStarManager asm = new AStarManager(mdb);
        LinkedList<AlgoNode> pathP1_P4 = asm.getPath("oPARK00101","oPARK00401");
        App.switchLocale("en", "US", LocaleType.en_US, true);
        List<String> textualDirections = TextDirManager.getTextualDirections(pathP1_P4, true);
        assertEquals("Proceed 1.0 meter(s) towards Floor1RightParking2.",textualDirections.get(0));
        assertEquals("Proceed to floor F2 and head to Floor1RightParking3.",textualDirections.get(1));
        assertEquals("Proceed 1.0 meter(s) forward to Floor1RightParking4.",textualDirections.get(2));
        assertEquals("You have arrived at your destination.",textualDirections.get(3));
    }

    /**
     * Test for handling null input for textual directions
     */
    @Test
    public void nullTest() {
        App.switchLocale("en", "US", LocaleType.en_US, true);
        List<String> textualDirections = TextDirManager.getTextualDirections(null, true);
        assertEquals(textualDirections.get(0), "No path exists/provided.");
    }
    /**
     * Test for handling empty list for textual directions
     */
    @Test
    public void emptyTest() {
        App.switchLocale("en", "US", LocaleType.en_US, true);
        List<String> textualDirections = TextDirManager.getTextualDirections(new LinkedList<>(), true);
        assertEquals(textualDirections.get(0), "No path exists.");
    }
}
