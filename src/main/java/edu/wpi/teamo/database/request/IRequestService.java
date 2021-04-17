package edu.wpi.teamo.database.request;

import java.util.stream.Stream;
import java.sql.SQLException;

public interface IRequestService {
    String requestMedicine(String type, String amount, String location, String employee, int requestNumber) throws SQLException;
    String requestSanitation(String type, String location, String employee, int requestNumber) throws SQLException;
    Stream<ISanitationRequestInfo> getAllSanitationRequests() throws SQLException;
    Stream<IMedicineRequestInfo> getAllMedicineRequests() throws SQLException;
    ISanitationRequestInfo getSanitationRequest(String id) throws SQLException;
    IMedicineRequestInfo getMedicineRequest(String id) throws SQLException;
    void removeRequest(String requestID) throws SQLException;
    void setCompleted(String requestID) throws SQLException;
    void closeConnection() throws SQLException;
}
