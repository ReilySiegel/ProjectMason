package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.request.MedicineRequest;
import edu.wpi.teamo.database.request.BaseRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.SQLException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import edu.wpi.teamo.database.Database;

public class MedicineRequestTest {

    @Test
    public void testInitTable() throws SQLException, ClassNotFoundException {
        Database.setTesting ("testInitTable");
        MedicineRequest.initTable();
        BaseRequest.initTable();
        String[] loc = {"loc"};
        BaseRequest b = new BaseRequest ("id", "", Stream.of(loc), "assigned", false);
        MedicineRequest s = new MedicineRequest("type", "10", b);
        s.update();
    }

    @Test
    public void testGetByID() throws Exception {
        Database.setTesting ("testMedGetByID");
        MedicineRequest.initTable();
        BaseRequest.initTable();
        String[] loc = { "loc" };
        BaseRequest b = new BaseRequest ("id", "", Stream.of(loc), "assigned", false);
        MedicineRequest s = new MedicineRequest("type", "10", b);
        s.update();
        assertEquals("id", MedicineRequest.getByID("id").getID());
        assertEquals("loc", MedicineRequest.getByID("id").getLocations().findFirst().orElseThrow(Exception::new));
    }

    @Test
    public void testGetAll() throws SQLException, ClassNotFoundException {
        Database.setTesting ("testGetAll");
        MedicineRequest.initTable();
        BaseRequest.initTable();
        String[] loc = { "loc" };
        BaseRequest b = new BaseRequest ("id", "", Stream.of(loc), "assigned", false);
        MedicineRequest s = new MedicineRequest("type", "10", b);
        s.update();
        assertEquals(1, MedicineRequest.getAll().count());
    }

    @Test
    public void testUpdate() throws SQLException, ClassNotFoundException {
        Database.setTesting ("testUpdate");
        MedicineRequest.initTable();
        BaseRequest.initTable();
        String[] loc = { "loc" };
        BaseRequest b = new BaseRequest ("id", "", Stream.of(loc), "assigned", false);
        MedicineRequest s = new MedicineRequest("type", "10", b);
        s.update();
        assertEquals(false, MedicineRequest.getByID("id").isComplete());
        s.setComplete(true);
        assertEquals(true, MedicineRequest.getByID("id").isComplete());
    }

    @Test
    public void testDelete() throws SQLException, ClassNotFoundException {
        Database.setTesting ("testDelete");
        MedicineRequest.initTable();
        BaseRequest.initTable();
        String[] loc = { "loc" };
        BaseRequest b = new BaseRequest ("id", "", Stream.of(loc), "assigned", false);
        MedicineRequest s = new MedicineRequest("type", "10", b);
        s.update();
        assertEquals("id", MedicineRequest.getByID("id").getID());
        s.delete();
        assertThrows(SQLException.class, () -> MedicineRequest.getByID("id").getID());
    }
}
