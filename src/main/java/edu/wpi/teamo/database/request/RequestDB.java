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
    public String requestMedicine(String type, String amount, String location, String assigned, int requestNumber) throws SQLException {
        //TODO
        return null;
    }

    @Override
    public String requestSanitation(String location, String assigned, String details, int number) throws SQLException {
        //TODO
        return null;
    }

    @Override
    public Stream<ISanitationRequestInfo> getAllSanitationRequests() throws SQLException {
        //TODO
        return null;
    }

    @Override
    public Stream<IMedicineRequestInfo> getAllMedicineRequests() throws SQLException {
        //TODO
        return null;
    }

    @Override
    public ISanitationRequestInfo getSanitationRequest(String id) throws SQLException {
        return null;
    }

    @Override
    public IMedicineRequestInfo getMedicineRequest(String id) throws SQLException {
        return null;
    }

    @Override
    public void removeRequest(String requestID) throws SQLException {
        //TODO
    }

    @Override
    public void setCompleted(String requestID) throws SQLException {
        //TODO
    }

    @Override
    public void closeConnection() throws SQLException {
        //TODO
    }

    @Override
    public boolean requestExists(String requestID) {
        /* if all tables throw exceptions, there is no request */
        boolean exists = false;
        try {
            getMedicineRequest(requestID);
            exists = true;
        } catch (SQLException ignored) { }
        try {
            getSanitationRequest(requestID);
            exists = true;
        } catch (SQLException ignored) { }
        return exists;
    }
}
