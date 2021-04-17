package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.util.stream.Stream;

public class SanitationRequest implements ISanitationRequestInfo {
    private String locationID;
    private boolean complete;
    private String assigned;
    private String details;
    private int number;

    public static SanitationRequest getByID(Database db, String id) {
        //TODO
        return null;
    }

    public static Stream<SanitationRequest> getAll(Database db) {
        //TODO
        return null;
    }

    public static void initTable(Database db) {
        //TODO
    }

    public void update() {
        //TODO
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
