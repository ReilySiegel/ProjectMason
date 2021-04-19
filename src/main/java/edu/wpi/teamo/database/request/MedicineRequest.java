package edu.wpi.teamo.database.request;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.database.Database;

import java.util.ArrayList;
import java.util.stream.Stream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicineRequest extends RecursiveTreeObject<MedicineRequest> implements IMedicineRequestInfo {
    private String locationID;
    private boolean complete;
    private final String id;
    private String assigned;
    private String amount;
    private String type;

    @Deprecated
    public MedicineRequest(String id, String type, String amount, String locationID, String assigned) {
        this.locationID = locationID;
        this.assigned = assigned;
        this.complete = false;
        this.amount = amount;
        this.type = type;
        this.id = id;
    }

    public MedicineRequest(String id, String type, String amount, boolean complete, String locationID, String assigned) {
        this.locationID = locationID;
        this.assigned = assigned;
        this.complete = complete;
        this.amount = amount;
        this.type = type;
        this.id = id;
    }

    public static MedicineRequest getByID(Database db, String id) throws SQLException {
        ResultSet rs = db.processQuery("SELECT * FROM MedicineRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new MedicineRequest(rs.getString("ID"),
                                   rs.getString("type"),
                                   rs.getString("amount"),
                                   rs.getBoolean("complete"),
                                   rs.getString("locationID"),
                                   rs.getString("assigned"));
    }

    public static Stream<MedicineRequest> getAll(Database db) throws SQLException {
        ResultSet rs = db.processQuery("SELECT * FROM MedicineRequest");
        ArrayList<MedicineRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new MedicineRequest(rs.getString("ID"),
                                         rs.getString("type"),
                                                rs.getString("amount"),
                                                rs.getBoolean("complete"),
                                                rs.getString("locationID"),
                                                rs.getString("assigned")));
        return reqs.stream();
    }

    public static void initTable(Database db) throws SQLException {
        db.processUpdate("CREATE TABLE MedicineRequest (" +
                         "id varchar(255) primary key, " +
                         "type varchar(255), " +
                         "amount varchar(255), " +
                         "complete boolean, " +
                         "assigned varchar(255), " +
                         "locationID varchar(255))");
    }

    public void update(Database db) throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            db.processUpdate(String.join(" ",
                                         "INSERT INTO MedicineRequest",
                                         "(id, type, amount, complete, assigned, locationID)",
                                         "VALUES",
                                         String.format("('%s', '%s', '%s', %s, '%s', '%s')",
                                                       this.id,
                                                       this.type,
                                                       this.amount,
                                                       this.complete,
                                                       this.assigned,
                                                       this.locationID)));
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            db.processUpdate(String.join(" ",
                                         "UPDATE MedicineRequest SET",
                                         String.format(String.join(", ",
                                                                   "id = '%s'",
                                                                   "type = '%s'",
                                                                   "amount = '%s'",
                                                                   "complete = %s",
                                                                   "assigned = '%s'",
                                                                   "locationID = '%s'"),
                                                       this.id, this.type, this.amount, this.complete,
                                                       this.assigned, this.locationID),
                                         "WHERE id = '" + this.id + "'"));
         }
    }

    public void delete(Database db) throws SQLException {
        db.processUpdate(String.format("DELETE FROM MedicineRequest WHERE id = '%s'", this.id));
    }

    @Override
    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    @Override
    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    @Override
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String getID() {
        return id;
    }

}
