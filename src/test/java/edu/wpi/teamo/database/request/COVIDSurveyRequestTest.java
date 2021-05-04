package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class COVIDSurveyRequestTest {

    @Test
    public void testInitTable() throws SQLException, ClassNotFoundException {
        Database.setTesting ("testInitTable");
        COVIDSurveyRequest.initTable();
        COVIDSurveyRequest s = new COVIDSurveyRequest("jacob", false);
        s.update();
    }

    @Test
    public void testGetByID() throws Exception {
        Database.setTesting ("testCovidGetByID");
        COVIDSurveyRequest.initTable();
        COVIDSurveyRequest s = new COVIDSurveyRequest("id","jacob", false, LocalDateTime.now(), false);
        s.update();
        assertEquals("id", COVIDSurveyRequest.getByID("id").getId());
        assertEquals("jacob", COVIDSurveyRequest.getByID("id").getUsername());
    }

    @Test
    public void testGetAll() throws SQLException, ClassNotFoundException {
        Database.setTesting ("testGetAll");
        COVIDSurveyRequest.initTable();
        COVIDSurveyRequest s = new COVIDSurveyRequest("jacob", true);
        s.update();
        assertEquals(1, COVIDSurveyRequest.getAll().count());
    }

    @Test
    public void testUpdate() throws SQLException, ClassNotFoundException {
        Database.setTesting ("testUpdate");
        COVIDSurveyRequest.initTable();
        COVIDSurveyRequest s = new COVIDSurveyRequest("id","jacob", false, LocalDateTime.now(), false);
        s.update();
        assertEquals(false, COVIDSurveyRequest.getByID("id").getIsComplete());
        s.setComplete(true);
        assertEquals(true, COVIDSurveyRequest.getByID("id").getIsComplete());
    }

    @Test
    public void testDelete() throws SQLException, ClassNotFoundException {
        COVIDSurveyRequest.initTable();
        COVIDSurveyRequest s = new COVIDSurveyRequest("id","jacob", false, LocalDateTime.now(), false);
        s.update();
        assertEquals("id", COVIDSurveyRequest.getByID("id").getId());
        s.delete();
        assertThrows(SQLException.class, () -> COVIDSurveyRequest.getByID("id").getId());
    }
}
