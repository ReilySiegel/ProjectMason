package edu.wpi.teamo.database.request;

import java.util.stream.*;

public interface ISanitationRequestInfo {
    Stream<String> getLocationIDs();
    boolean isComplete();
    String getAssigned();
    String getDetails();
    String getID();
}
