package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;
import java.util.stream.Stream;
import java.sql.SQLException;

public class RequestDB implements IRequestService {
    Database db;

    public RequestDB(Database db) throws SQLException {
        this.db = db;
        SanitationRequest.initTable(db);
        MedicineRequest.initTable(db);
    }

    @Override
    public String requestMedicine(String type, String amount, String locationID, String assigned) throws SQLException {
        int id = 1; /* set a unique id */
        while (medicineRequestExists(String.valueOf(id)) && id < 9999) { id++; }

        MedicineRequest mr = new MedicineRequest(String.valueOf(id), type, amount, locationID, assigned);
        mr.update(db);

        return mr.getID();
    }

    @Override
    public String requestSanitation(String location, String assigned, String details) throws SQLException {
        int id = 1; /* set a unique id */
        while (sanitationRequestExists(String.valueOf(id)) && id < 9999) { id++; }

        SanitationRequest sr = new SanitationRequest(String.valueOf(id), location, assigned, details);
        sr.update(db);

        return sr.getID();
    }

    @Override
    public Stream<ISanitationRequestInfo> getAllSanitationRequests() throws SQLException {
        return SanitationRequest.getAll(db).map((SanitationRequest sr) -> (ISanitationRequestInfo) sr);
    }

    @Override
    public Stream<IMedicineRequestInfo> getAllMedicineRequests() throws SQLException {
        return MedicineRequest.getAll(db).map((MedicineRequest sr) -> (IMedicineRequestInfo) sr);
    }

    @Override
    public ISanitationRequestInfo getSanitationRequest(String id) throws SQLException {
        return (ISanitationRequestInfo) SanitationRequest.getByID(db, id);
    }

    @Override
    public IMedicineRequestInfo getMedicineRequest(String id) throws SQLException {
        return (IMedicineRequestInfo) MedicineRequest.getByID(db, id);
    }

    @Override
    public void removeSanitationRequest(String requestID) throws SQLException {
        SanitationRequest.getByID(db, requestID).delete(db);
    }

    @Override
    public void removeMedicineRequest(String requestID) throws SQLException {
        MedicineRequest.getByID(db, requestID).delete(db);
    }

    @Override
    public void setSanitationCompleted(String requestID) throws SQLException {
        SanitationRequest sr = SanitationRequest.getByID(db, requestID);
        sr.setComplete(true);
        sr.update(db);
    }

    @Override
    public void setMedicineCompleted(String requestID) throws SQLException {
        MedicineRequest mr = MedicineRequest.getByID(db, requestID);
        mr.setComplete(true);
        mr.update(db);
    }

    @Override
    public boolean sanitationRequestExists(String requestID) {
        try {
            getSanitationRequest(requestID);
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    @Override
    public boolean medicineRequestExists(String requestID) {
        try {
            getMedicineRequest(requestID);
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    @Override
    public void closeConnection() throws SQLException {
        db.close();
    }

}
