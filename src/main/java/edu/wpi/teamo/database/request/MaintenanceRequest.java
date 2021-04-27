package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class MaintenanceRequest extends ExtendedBaseRequest<MaintenanceRequest> {

    private String type;

    public MaintenanceRequest(String type, BaseRequest base) {
        super(base);
        this.type = type;
    }

    public static void initTable() throws SQLException {
        String sql = String.join (", ",
                                  "CREATE TABLE MaintenanceRequest (id varchar(255) primary key",
                                  "type varchar(255))");
        Database.processUpdate(sql);
    }

    public static MaintenanceRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM MaintenanceRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new MaintenanceRequest(rs.getString("type"),
                                      BaseRequest.getByID(rs.getString("ID")));
    }

    public static Stream<MaintenanceRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM MaintenanceRequest");
        ArrayList<MaintenanceRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new MaintenanceRequest(rs.getString("type"),
                                            BaseRequest.getByID(rs.getString("id"))));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            String sql = String.join (" ",
                                      "INSERT INTO MaintenanceRequest",
                                      "(id, type)",
                                      "VALUES (?, ?)");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.type);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            String sql = String.join(" ",
                                     "UPDATE MaintenanceRequest SET",
                                     "id = ?, type = ?",
                                     "WHERE id = ?");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.type);
            pstmt.setString(3, this.base.getId());
            pstmt.execute();
        }
        this.base.update();
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM MaintenanceRequest WHERE id = '%s'", this.base.getId()));
        this.base.delete();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) throws SQLException {
        this.type = type;
        this.update ();
    }
}
