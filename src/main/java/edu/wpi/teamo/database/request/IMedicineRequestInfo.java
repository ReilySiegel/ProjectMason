package edu.wpi.teamo.database.request;

public interface IMedicineRequestInfo {
    String getLocationID();
    boolean isComplete();
    String getAssigned();
    String getAmount();
    String getType();
    int getNumber();
}
