package edu.wpi.teamo.database.request;

public interface ISanitationRequestInfo {
    String getLocationID();
    boolean isComplete();
    String getAssigned();
    String getDetails();
    int getNumber();
    String getID();
}
