package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.request.SanitationRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import edu.wpi.teamo.database.Database;

public class SanitationRequestTest {

    @Test
    public void testInitTable() throws SQLException, ClassNotFoundException {
        Database.setTesting ("testInitTable");
        SanitationRequest.initTable();
        BaseRequest.initTable();
        String[] loc = {"loc"};
        BaseRequest b = new BaseRequest("id", "det", Stream.of(loc), "me", false);
        SanitationRequest s = new SanitationRequest(false, b);
        s.update();
    }

    @Test
    public void testGetByID() throws Exception {
        Database.setTesting ("testGetByID");
        Database db = Database.getInstance ();
        SanitationRequest.initTable();
        BaseRequest.initTable();
        String[] loc = {"loc"};
        LocalDateTime t = LocalDateTime.now();
        BaseRequest b = new BaseRequest("id", "det", Stream.of(loc), "me", false, t, t,"user");
        SanitationRequest s = new SanitationRequest(false, b);
        s.update();
        assertEquals("id", SanitationRequest.getByID("id").getID());
        assertEquals("loc", SanitationRequest.getByID("id").getLocations().findFirst().orElseThrow(Exception::new));
        assertEquals(t, SanitationRequest.getByID("id").getTimestamp());
        assertEquals(t, SanitationRequest.getByID("id").getDue());
    }

    @Test
    public void testGetAll() throws SQLException, ClassNotFoundException {
        Database.setTesting("testGetAll");
        Database db = Database.getInstance();
        SanitationRequest.initTable();
        BaseRequest.initTable();
        String[] loc = { "loc" };
        BaseRequest b = new BaseRequest("id", "det", Stream.of(loc), "me", false);
        SanitationRequest s = new SanitationRequest(false, b);
        s.update();
        assertEquals(1, SanitationRequest.getAll().count());
    }

    @Test
    public void testUpdate() throws SQLException, ClassNotFoundException {
        Database.setTesting ("testUpdate");
        Database db = Database.getInstance ();
        SanitationRequest.initTable();
        BaseRequest.initTable();
        String[] loc = { "loc" };
        BaseRequest b = new BaseRequest("id", "det", Stream.of(loc), "me", false);
        SanitationRequest s = new SanitationRequest(false, b);
        s.update();
        assertEquals(false, SanitationRequest.getByID("id").isComplete());
        s.setComplete(true);
        s.update();
        assertEquals(true, SanitationRequest.getByID("id").isComplete());
    }

    @Test
    public void testDelete() throws SQLException, ClassNotFoundException {
        Database.setTesting ("testDelete");
        Database db = Database.getInstance ();
        SanitationRequest.initTable();
        BaseRequest.initTable();
        String[] loc = {"loc"};
        BaseRequest b = new BaseRequest("id", "det", Stream.of(loc), "me", false);
        SanitationRequest s = new SanitationRequest(false, b);
        s.update();
        assertEquals("id", SanitationRequest.getByID("id").getID());
        s.delete();
        assertThrows(SQLException.class, () -> SanitationRequest.getByID("id").getID());
    }
}
