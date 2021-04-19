package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;
import edu.wpi.teamo.database.map.Edge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RequestDBTests {

    @Test
    public void requestMedicine() throws SQLException, ClassNotFoundException {
        String assigned = "as";
        String location = "l";
        String amount = "a";
        String type = "t";

        Database db = new Database(Database.getMemoryURIFromName("requestMedicine"));
        RequestDB rDB = new RequestDB(db);
        String id = rDB.requestMedicine(type, amount, location, assigned);

        try {
            IMedicineRequestInfo medReqToCheck = rDB.getMedicineRequest(id);
            assertEquals(location, medReqToCheck.getLocationID());
            assertEquals(assigned, medReqToCheck.getAssigned());
            assertEquals(amount, medReqToCheck.getAmount());
            assertEquals(type, medReqToCheck.getType());
            assertEquals(id, medReqToCheck.getID());
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        db.close();
    }

    @Test
    public void requestSanitation() throws SQLException, ClassNotFoundException {
        String locationID = "l";
        String assigned = "a";
        String details = "d";

        Database db = new Database(Database.getMemoryURIFromName("requestSanitation"));
        RequestDB rDB = new RequestDB(db);
        String id = rDB.requestSanitation(locationID, assigned, details);

        try {
            ISanitationRequestInfo sanReqToCheck = rDB.getSanitationRequest(id);
            assertEquals(locationID, sanReqToCheck.getLocationID());
            assertEquals(assigned, sanReqToCheck.getAssigned());
            assertEquals(id, sanReqToCheck.getID());
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        db.close();
    }

    @Test
    public void getAllSanitationRequests() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("getAllSanitationRequests"));
        RequestDB rDB = new RequestDB(db);
        String id1 = rDB.requestSanitation("loc", "assigned1", "det");
        String id2 = rDB.requestSanitation("loc", "assigned2", "det");
        String id3 = rDB.requestSanitation("loc", "assigned3", "det");

        /* get all, convert to stream of the ID's, put into list */
        Stream<ISanitationRequestInfo> requestStream = rDB.getAllSanitationRequests();
        Stream<String> idStream = requestStream.map(ISanitationRequestInfo::getID);
        List<String> idListToCheck = idStream.collect(Collectors.toList());

        /* list must be length 3 */
        assertEquals(3, idListToCheck.size());

        /* check if ids are the same */
        assertTrue(idListToCheck.contains(id1));
        assertTrue(idListToCheck.contains(id2));
        assertTrue(idListToCheck.contains(id3));

        db.close();
    }

    @Test
    public void getAllMedicineRequests() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("getAllMedicineRequests"));
        RequestDB rDB = new RequestDB(db);
        String id1 = rDB.requestMedicine("t", "a", "loc", "assigned1");
        String id2 = rDB.requestMedicine("t", "a", "loc", "assigned2");
        String id3 = rDB.requestMedicine("t", "a", "loc", "assigned3");

        /* get all, convert to stream of the ID's, put into list */
        Stream<IMedicineRequestInfo> requestStream = rDB.getAllMedicineRequests();
        Stream<String> idStream = requestStream.map(IMedicineRequestInfo::getID);
        List<String> idListToCheck = idStream.collect(Collectors.toList());

        /* list must be length 3 */
        assertEquals(3, idListToCheck.size());

        /* check if ids are the same */
        assertTrue(idListToCheck.contains(id1));
        assertTrue(idListToCheck.contains(id2));
        assertTrue(idListToCheck.contains(id3));

        db.close();
    }

    @Test
    public void getSanitationRequest() throws SQLException, ClassNotFoundException {
        String locationID = "l";
        String assigned = "a";
        String details = "d";

        Database db = new Database(Database.getMemoryURIFromName("getSanitationRequest"));
        RequestDB rDB = new RequestDB(db);

        assertThrows(SQLException.class, () -> rDB.getSanitationRequest("idontexist"));

        String id = rDB.requestSanitation(locationID, assigned, details);

        try {
            ISanitationRequestInfo sanReqToCheck = rDB.getSanitationRequest(id);
            assertEquals(locationID, sanReqToCheck.getLocationID());
            assertEquals(assigned, sanReqToCheck.getAssigned());
            assertEquals(id, sanReqToCheck.getID());
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        db.close();
    }

    @Test
    public void getMedicineRequest() throws SQLException, ClassNotFoundException {
        String assigned = "as";
        String location = "l";
        String amount = "a";
        String type = "t";

        Database db = new Database(Database.getMemoryURIFromName("getMedicineRequest"));
        RequestDB rDB = new RequestDB(db);

        assertThrows(SQLException.class, () -> rDB.getMedicineRequest("idontexist"));

        String id = rDB.requestMedicine(type, amount, location, assigned);

        try {
            IMedicineRequestInfo medReqToCheck = rDB.getMedicineRequest(id);
            assertEquals(location, medReqToCheck.getLocationID());
            assertEquals(assigned, medReqToCheck.getAssigned());
            assertEquals(amount, medReqToCheck.getAmount());
            assertEquals(type, medReqToCheck.getType());
            assertEquals(id, medReqToCheck.getID());
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        db.close();
    }

    @Test
    public void removeSanitationRequest() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("removeSanitationRequest"));
        RequestDB rDB = new RequestDB(db);

        /* store request */
        String sanID = rDB.requestSanitation("locationID", "assigned", "details");

        /* make sure it was stored */
        assertTrue(rDB.sanitationRequestExists(sanID));

        /* remove it */
        rDB.removeSanitationRequest(sanID);

        /* make sure its gone */
        assertThrows(SQLException.class, () -> rDB.getSanitationRequest(sanID));

        db.close();
    }

    @Test
    public void removeMedicineRequest() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("removeMedicineRequest"));
        RequestDB rDB = new RequestDB(db);

        /* store request */
        String medID = rDB.requestMedicine("type", "amount", "locationID", "assigned");

        /* make sure it was stored */
        assertTrue(rDB.medicineRequestExists(medID));

        /* remove it */
        rDB.removeMedicineRequest(medID);

        /* make sure its gone */
        assertThrows(SQLException.class, () -> rDB.getMedicineRequest(medID));

        db.close();
    }

    @Test
    public void setSanitationCompleted() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("setSanitationCompleted"));
        RequestDB rDB = new RequestDB(db);

        /* store request */
        String sanID = rDB.requestSanitation("locationID", "assigned", "details");

        /* make sure it starts false */
        assertFalse(rDB.getSanitationRequest(sanID).isComplete());

        /* set it to true */
        rDB.setSanitationCompleted(sanID);

        /* check that it's been set */
        assertTrue(rDB.getSanitationRequest(sanID).isComplete());

        db.close();
    }

    @Test
    public void setMedicineCompleted() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("setMedicineCompleted"));
        RequestDB rDB = new RequestDB(db);

        /* store request */
        String medID = rDB.requestMedicine("type", "amount", "locationID", "assigned");

        /* make sure it starts false */
        assertFalse(rDB.getMedicineRequest(medID).isComplete());

        /* set it to true */
        rDB.setMedicineCompleted(medID);

        /* check that it's been set */
        assertTrue(rDB.getMedicineRequest(medID).isComplete());

        db.close();
    }

    @Test
    public void sanitationRequestExists() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("sanitationRequestExists"));
        RequestDB rDB = new RequestDB(db);

        /* store request */
        String sanID = rDB.requestSanitation("locationID", "assigned", "details");

        /* check existence */
        assertTrue(rDB.sanitationRequestExists(sanID));

        /* remove it */
        rDB.removeSanitationRequest(sanID);

        /* check that it doesnt exist */
        assertFalse(rDB.sanitationRequestExists(sanID));

        db.close();
    }

    @Test
    public void medicineRequestExists() throws SQLException, ClassNotFoundException {
        Database db = new Database(Database.getMemoryURIFromName("medicineRequestExists"));
        RequestDB rDB = new RequestDB(db);

        /* store request */
        String medID = rDB.requestMedicine("type", "amount", "locationID", "assigned");

        /* check existence */
        assertTrue(rDB.medicineRequestExists(medID));

        /* remove it */
        rDB.removeMedicineRequest(medID);

        /* check that it doesnt exist */
        assertFalse(rDB.medicineRequestExists(medID));

        db.close();
    }
}
