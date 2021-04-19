package edu.wpi.teamo.database.request;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.database.Database;

import java.sql.SQLException;
import java.util.stream.Stream;

public class SanitationRequest extends RecursiveTreeObject<SanitationRequest> implements ISanitationRequestInfo {
    private String locationID;
    private boolean complete;
    private String assigned;
    private final String id;
    private String details;

    public SanitationRequest(String id, String locationID, String assigned, String details) {
        this.locationID = locationID;
        this.assigned = assigned;
        this.details = details;
        this.complete = false;
        this.id = id;
    }

    public static SanitationRequest getByID(Database db, String id) throws SQLException {
        //TODO
        return null;
    }

    public static Stream<SanitationRequest> getAll(Database db) throws SQLException {
        //TODO
        return null;
    }

    public static void initTable(Database db) throws SQLException {
        //TODO
    }

    public void update() throws SQLException {
        //TODO
    }

    public void delete(Database db) throws SQLException {
        //TODO
    }

    @Override
    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    @Override
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String getID() {
        return id;
    }
}
