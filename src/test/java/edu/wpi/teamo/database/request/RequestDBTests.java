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
        fail();
//        String assigned = "as";
//        int requestNumber = 1;
//        String location = "l";
//        String amount = "a";
//        String type = "t";
//
//        Database db = new Database(Database.getURIFromName("requestMedicine"));
//        RequestDB rDB = new RequestDB(db);
//        String id = rDB.requestMedicine(type, amount, location, assigned, requestNumber);
//
//        try {
//            IMedicineRequestInfo medReqToCheck = rDB.getMedicineRequest(id);
//            assertEquals(requestNumber, medReqToCheck.getNumber());
//            assertEquals(location, medReqToCheck.getLocationID());
//            assertEquals(assigned, medReqToCheck.getAssigned());
//            assertEquals(amount, medReqToCheck.getAmount());
//            assertEquals(type, medReqToCheck.getType());
//            assertEquals(id, medReqToCheck.getID());
//        }
//        catch (SQLException e) {
//            fail(e.getMessage());
//        }
//
//        db.close();
    }

    @Test
    public void requestSanitation() throws SQLException, ClassNotFoundException {
        fail();
//        String locationID = "l";
//        String assigned = "a";
//        String details = "d";
//        int number = 1;
//
//        Database db = new Database(Database.getURIFromName("requestSanitation"));
//        RequestDB rDB = new RequestDB(db);
//        String id = rDB.requestSanitation(locationID, assigned, details, number);
//
//        try {
//            ISanitationRequestInfo sanReqToCheck = rDB.getSanitationRequest(id);
//            assertEquals(locationID, sanReqToCheck.getLocationID());
//            assertEquals(assigned, sanReqToCheck.getAssigned());
//            assertEquals(number, sanReqToCheck.getNumber());
//            assertEquals(id, sanReqToCheck.getID());
//        }
//        catch (SQLException e) {
//            fail(e.getMessage());
//        }
//
//        db.close();
    }

    @Test
    public void getAllSanitationRequests() throws SQLException, ClassNotFoundException {
        fail();
//        Database db = new Database(Database.getURIFromName("getAllSanitationRequests"));
//        RequestDB rDB = new RequestDB(db);
//        String id1 = rDB.requestSanitation("loc", "assigned1", "det", 1);
//        String id2 = rDB.requestSanitation("loc", "assigned2", "det", 2);
//        String id3 = rDB.requestSanitation("loc", "assigned3", "det", 3);
//
//        /* get all, convert to stream of the ID's, put into list */
//        Stream<ISanitationRequestInfo> requestStream = rDB.getAllSanitationRequests();
//        Stream<String> idStream = requestStream.map(ISanitationRequestInfo::getID);
//        List<String> idListToCheck = idStream.collect(Collectors.toList());
//
//        /* list must be length 3 */
//        assertEquals(3, idListToCheck.size());
//
//        /* check if ids are the same */
//        assertTrue(idListToCheck.contains(id1));
//        assertTrue(idListToCheck.contains(id2));
//        assertTrue(idListToCheck.contains(id3));
//
//        db.close();
    }

    @Test
    public void getAllMedicineRequests() throws SQLException, ClassNotFoundException {
        fail();
//        Database db = new Database(Database.getURIFromName("getAllMedicineRequests"));
//        RequestDB rDB = new RequestDB(db);
//        String id1 = rDB.requestMedicine("t", "a", "loc", "assigned1", 1);
//        String id2 = rDB.requestMedicine("t", "a", "loc", "assigned2", 2);
//        String id3 = rDB.requestMedicine("t", "a", "loc", "assigned3", 3);
//
//        /* get all, convert to stream of the ID's, put into list */
//        Stream<IMedicineRequestInfo> requestStream = rDB.getAllMedicineRequests();
//        Stream<String> idStream = requestStream.map(IMedicineRequestInfo::getID);
//        List<String> idListToCheck = idStream.collect(Collectors.toList());
//
//        /* list must be length 3 */
//        assertEquals(3, idListToCheck.size());
//
//        /* check if ids are the same */
//        assertTrue(idListToCheck.contains(id1));
//        assertTrue(idListToCheck.contains(id2));
//        assertTrue(idListToCheck.contains(id3));
//
//        db.close();
    }

    @Test
    public void getSanitationRequest() throws SQLException, ClassNotFoundException {
        fail();
//        String locationID = "l";
//        String assigned = "a";
//        String details = "d";
//        int number = 1;
//
//        Database db = new Database(Database.getURIFromName("getSanitationRequest"));
//        RequestDB rDB = new RequestDB(db);
//
//        assertThrows(SQLException.class, () -> rDB.getSanitationRequest("idontexist"));
//
//        String id = rDB.requestSanitation(locationID, assigned, details, number);
//
//        try {
//            ISanitationRequestInfo sanReqToCheck = rDB.getSanitationRequest(id);
//            assertEquals(locationID, sanReqToCheck.getLocationID());
//            assertEquals(assigned, sanReqToCheck.getAssigned());
//            assertEquals(number, sanReqToCheck.getNumber());
//            assertEquals(id, sanReqToCheck.getID());
//        }
//        catch (SQLException e) {
//            fail(e.getMessage());
//        }
//
//        db.close();
    }

    @Test
    public void getMedicineRequest() throws SQLException, ClassNotFoundException {
        fail();
//        String assigned = "as";
//        int requestNumber = 1;
//        String location = "l";
//        String amount = "a";
//        String type = "t";
//
//        Database db = new Database(Database.getURIFromName("getMedicineRequest"));
//        RequestDB rDB = new RequestDB(db);
//
//        assertThrows(SQLException.class, () -> rDB.getMedicineRequest("idontexist"));
//
//        String id = rDB.requestMedicine(type, amount, location, assigned, requestNumber);
//
//        try {
//            IMedicineRequestInfo medReqToCheck = rDB.getMedicineRequest(id);
//            assertEquals(requestNumber, medReqToCheck.getNumber());
//            assertEquals(location, medReqToCheck.getLocationID());
//            assertEquals(assigned, medReqToCheck.getAssigned());
//            assertEquals(amount, medReqToCheck.getAmount());
//            assertEquals(type, medReqToCheck.getType());
//            assertEquals(id, medReqToCheck.getID());
//        }
//        catch (SQLException e) {
//            fail(e.getMessage());
//        }
//
//        db.close();
    }

    @Test
    public void removeRequest() throws SQLException, ClassNotFoundException {
        fail();
//        Database db = new Database(Database.getURIFromName("removeRequest"));
//        RequestDB rDB = new RequestDB(db);
//
//        /* store requests */
//        String medID = rDB.requestMedicine("type", "amount", "location", "assigned", 1);
//        String sanID = rDB.requestSanitation("locationID", "assigned", "details", 1);
//
//        /* make sure they were stored */
//        try { rDB.getMedicineRequest(medID);   } catch(SQLException e) { fail(); }
//        try { rDB.getSanitationRequest(sanID); } catch(SQLException e) { fail(); }
//
//        /* remove them */
//        rDB.removeRequest(medID);
//        rDB.removeRequest(sanID);
//
//        /* make sure they gone */
//        assertThrows(SQLException.class, () -> rDB.getSanitationRequest(sanID));
//        assertThrows(SQLException.class, () -> rDB.getMedicineRequest(medID));
//
//        db.close();
    }

}
