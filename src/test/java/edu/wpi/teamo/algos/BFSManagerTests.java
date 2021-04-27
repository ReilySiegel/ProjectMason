package edu.wpi.teamo.algos;

import edu.wpi.teamo.database.map.MapDB;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BFSManagerTests {


    @Test
    public void testGetPath() throws SQLException, ClassNotFoundException {

        MapDB mdb = new MapDB("testBFSFindPath");
        mdb.addNode("oPARK00101", 3116, 1131, "F1", "b", "PARK", "Floor1RightParking1", "F1RightP1");
        mdb.addNode("oPARK00201", 3116, 1155, "F1", "b", "PARK", "Floor1RightParking2", "F1RightP2");
        mdb.addNode("oPARK00301", 3116, 1181, "F1", "b", "PARK", "Floor1RightParking3", "F1RightP3");
        mdb.addNode("oPARK00401", 3116, 1207, "F1", "b", "PARK", "Floor1RightParking4", "F1RightP4");
        mdb.addNode("oPARK00501", 3116, 1233, "F1", "b", "PARK", "Floor1RightParking5", "F1RightP5");
        mdb.addNode("oPARK00601", 3116, 1253, "F1", "b", "PARK", "Floor1RightParking6", "F1RightP6");
        mdb.addNode("oPARK00701", 3242, 1111, "F1", "b", "PARK", "Floor1RightParking7", "F1RightP7");
        mdb.addNode("oPARK00801", 3242, 1133, "F1", "b", "PARK", "Floor1RightParking8", "F1RightP8");
        mdb.addNode("oPARK00901", 3242, 1157, "F1", "b", "PARK", "Floor1RightParking9", "F1RightP9");
        mdb.addNode("oPARK01001", 3284, 1279, "F1", "b", "PARK", "Floor1RightParkingA", "F1RightPA");
        mdb.addEdge("oPARK00101_oPARK00301", "oPARK00101", "oPARK00301");
        mdb.addEdge("oPARK00101_oPARK00601", "oPARK00101", "oPARK00601");
        mdb.addEdge("oPARK00101_oPARK00201", "oPARK00101", "oPARK00201");
        mdb.addEdge("oPARK00201_oPARK00701", "oPARK00201", "oPARK00701");
        mdb.addEdge("oPARK00601_oPARK00401", "oPARK00601", "oPARK00401");
        mdb.addEdge("oPARK00601_oPARK00501", "oPARK00601", "oPARK00501");
        mdb.addEdge("oPARK00701_oPARK00401", "oPARK00701", "oPARK00401");
        mdb.addEdge("oPARK00701_oPARK00801", "oPARK00701", "oPARK00801");
        mdb.addEdge("oPARK00801_oPARK00901", "oPARK00801", "oPARK00901");
        mdb.addEdge("oPARK00801_oPARK01001", "oPARK00801", "oPARK01001");

        BFSManager bfsm = new BFSManager(mdb);
        LinkedList<AlgoNode> pathP10_P5 = bfsm.getPath("oPARK01001","oPARK00501");

        assertEquals(6, pathP10_P5.size());
        assertEquals("oPARK01001", pathP10_P5.get(0).getID());
        assertEquals("oPARK00801", pathP10_P5.get(1).getID());
        assertEquals("oPARK00701", pathP10_P5.get(2).getID());
        assertEquals("oPARK00401", pathP10_P5.get(3).getID());
        assertEquals("oPARK00601", pathP10_P5.get(4).getID());
        assertEquals("oPARK00501", pathP10_P5.get(5).getID());
        System.out.println("If reach here, tests for AstarManager.getPath() are passed!");


    }


}
