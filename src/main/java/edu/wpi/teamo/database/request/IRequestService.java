package edu.wpi.teamo.database.request;

import java.util.stream.Stream;

public interface IRequestService {
    String requestMedicine(String type, String amount, String location, String employee, int requestNumber);
    String requestSanitation(String type, String location, String employee, int requestNumber);
    Stream<ISanitationRequestInfo> getAllSanitationRequests();
    Stream<IMedicineRequestInfo> getAllMedicineRequests();
    void removeRequest(String requestID);
    void setCompleted(String requestID);
    void closeConnection();
}
