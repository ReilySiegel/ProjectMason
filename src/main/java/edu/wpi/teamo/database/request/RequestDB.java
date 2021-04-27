package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;
import java.util.stream.Stream;
import java.sql.SQLException;

public class RequestDB implements IRequestService {
    Database db;

    public RequestDB(Database db) throws SQLException {
        this.db = db;
        BaseRequest.initTable();
        SanitationRequest.initTable();
        MedicineRequest.initTable();
    }

    @Override
    public String requestMedicine(String type, String amount, Stream<String> locationIDs, String assigned) throws SQLException {
        int id = 1; /* set a unique id */
        while (medicineRequestExists(String.valueOf(id)) && id < 9999) { id++; }

        BaseRequest b = new BaseRequest(String.valueOf(id), "", locationIDs, assigned, false);
        MedicineRequest mr = new MedicineRequest(type, amount, b);
        mr.update();

        return mr.getID();
    }

    @Override
    public String requestSanitation(Stream<String> locationIDs, String assigned, String details) throws SQLException {
        int id = 1; /* set a unique id */
        while (sanitationRequestExists(String.valueOf(id)) && id < 9999) { id++; }

        SanitationRequest sr = new SanitationRequest(false, new BaseRequest(String.valueOf(id), details,locationIDs, assigned, false));
        sr.update();

        return sr.getID();
    }

    @Override
    public Stream<SanitationRequest> getAllSanitationRequests() throws SQLException {
        return SanitationRequest.getAll();
    }

    @Override
    public Stream<MedicineRequest> getAllMedicineRequests() throws SQLException {
        return MedicineRequest.getAll().map((MedicineRequest sr) -> (MedicineRequest) sr);
    }

    @Override
    public SanitationRequest getSanitationRequest(String id) throws SQLException {
        return SanitationRequest.getByID(id);
    }

    @Override
    public MedicineRequest getMedicineRequest(String id) throws SQLException {
        return (MedicineRequest) MedicineRequest.getByID(id);
    }

    @Override
    public void removeSanitationRequest(String requestID) throws SQLException {
        SanitationRequest.getByID(requestID).delete();
    }

    @Override
    public void removeMedicineRequest(String requestID) throws SQLException {
        MedicineRequest.getByID(requestID).delete();
    }

    @Override
    public void setSanitationCompleted(String requestID) throws SQLException {
        SanitationRequest sr = SanitationRequest.getByID(requestID);
        sr.setComplete(true);
        sr.update();
    }

    @Override
    public void setMedicineCompleted(String requestID) throws SQLException {
        MedicineRequest mr = MedicineRequest.getByID(requestID);
        mr.setComplete(true);
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
