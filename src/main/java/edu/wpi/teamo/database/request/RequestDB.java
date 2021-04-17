package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.util.stream.Stream;

public class RequestDB implements IRequestService {
    Database db;

    public RequestDB(Database db) {
        this.db = db;
        SanitationRequest.initTable(db);
        MedicineRequest.initTable(db);
    }

    @Override
    public String requestMedicine(String type, String amount, String location, String employee, int requestNumber) {
        //TODO
        return null;
    }

    @Override
    public String requestSanitation(String type, String location, String employee, int requestNumber) {
        //TODO
        return null;
    }

    @Override
    public Stream<ISanitationRequestInfo> getAllSanitationRequests() {
        //TODO
        return null;
    }

    @Override
    public Stream<IMedicineRequestInfo> getAllMedicineRequests() {
        //TODO
        return null;
    }

    @Override
    public ISanitationRequestInfo getSanitationRequest(String id) {
        return null;
    }

    @Override
    public IMedicineRequestInfo getMedicineRequest(String id) {
        return null;
    }

    @Override
    public void removeRequest(String requestID) {
        //TODO
    }

    @Override
    public void setCompleted(String requestID) {
        //TODO
    }

    @Override
    public void closeConnection() {
        //TODO
    }
}
