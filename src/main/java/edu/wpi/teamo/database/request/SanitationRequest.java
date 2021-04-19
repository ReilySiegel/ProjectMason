package edu.wpi.teamo.database.request;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class SanitationRequest extends RecursiveTreeObject<SanitationRequest> implements ISanitationRequestInfo {
    private String locationID;
    private boolean complete;
    private String assigned;
    private final String id;
    private String details;

    @Deprecated
    public SanitationRequest(String id, String locationID, String assigned, String details) {
        this.locationID = locationID;
        this.assigned = assigned;
        this.details = details;
        this.complete = false;
        this.id = id;
    }

    public SanitationRequest(String id, String locationID, String assigned, String details, boolean complete) {
        this.locationID = locationID;
        this.assigned = assigned;
        this.details = details;
        this.complete = complete;
        this.id = id;
    }

    public static SanitationRequest getByID(Database db, String id) throws SQLException {
        ResultSet rs = db.processQuery("SELECT * FROM SanitationRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new SanitationRequest(rs.getString("id"),
                                     rs.getString("locationID"),
                                     rs.getString("assigned"),
                                     rs.getString("details"),
                                     rs.getBoolean("complete"));
    }

    public static Stream<SanitationRequest> getAll(Database db) throws SQLException {
        ResultSet rs = db.processQuery("SELECT * FROM SanitationRequest");
        ArrayList<SanitationRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new SanitationRequest(rs.getString("id"),
                                           rs.getString("locationID"),
                                           rs.getString("assigned"),
                                           rs.getString("details"),
                                           rs.getBoolean("complete")));
        return reqs.stream();
    }

    public static void initTable(Database db) throws SQLException {
        db.processUpdate("CREATE TABLE SanitationRequest (" +
                         "id varchar(255) primary key, " +
                         "details varchar(255), " +
                         "complete boolean, " +
                         "assigned varchar(255), " +
                         "locationID varchar(255))");
    }

    public void update(Database db) throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            db.processUpdate(String.join(" ",
                                         "INSERT INTO SanitationRequest",
                                         "(id, details, complete, assigned, locationID)",
                                         "VALUES",
                                         String.format("('%s', '%s', %s, '%s', '%s')",
                                                       this.id,
                                                       this.details,
                                                       this.complete,
                                                       this.assigned,
                                                       this.locationID)));
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            db.processUpdate(String.join(" ",
                                         "UPDATE SanitationRequest SET",
                                         String.format(String.join(", ",
                                                                   "id = '%s'",
                                                                   "details = '%s'",
                                                                   "complete = %s",
                                                                   "assigned = '%s'",
                                                                   "locationID = '%s'"),
                                                       this.id,
                                                       this.details,
                                                       this.complete,
                                                       this.assigned,
                                                       this.locationID),
                                         "WHERE id = '" + this.id + "'"));
        }
    }

    public void delete(Database db) throws SQLException {
        db.processUpdate(String.format("DELETE FROM SanitationRequest WHERE id = '%s'", this.id));
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
