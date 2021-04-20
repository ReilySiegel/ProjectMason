package edu.wpi.teamo.database.request;

import java.util.stream.*;

public interface IMedicineRequestInfo {
    Stream<String> getLocationIDs();
    boolean isComplete();
    String getAssigned();
    String getAmount();
    String getType();
    String getID();
}
