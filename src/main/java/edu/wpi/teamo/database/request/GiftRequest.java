package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class GiftRequest extends ExtendedBaseRequest<GiftRequest> {

    private String recipient;
    private String trackingID;

    public GiftRequest(String recipient, String trackingID, BaseRequest base) {
        super(base);
        this.recipient = recipient;
        this.trackingID = trackingID;
    }

    public static void initTable() throws SQLException {
        String sql = String.join (", ",
                                  "CREATE TABLE GiftRequest (id varchar(255) primary key",
                                  "recipient varchar(255)",
                                  "trackingID varchar(255))");
        Database.processUpdate(sql);
    }

    public static GiftRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM GiftRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new GiftRequest(rs.getString("recipient"),
                               rs.getString("trackingID"),
                                      BaseRequest.getByID(rs.getString("ID")));
    }

    public static Stream<GiftRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM GiftRequest");
        ArrayList<GiftRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new GiftRequest(rs.getString("recipient"),
                                     rs.getString("trackingID"),
                                            BaseRequest.getByID(rs.getString("id"))));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            String sql = String.join (" ",
                                      "INSERT INTO GiftRequest",
                                      "(id, recipient, trackingID)",
                                      "VALUES (?, ?, ?)");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.recipient);
            pstmt.setString(3, this.trackingID);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            String sql = String.join(" ",
                                     "UPDATE GiftRequest SET",
                                     "id = ?, recipient = ?, trackingID = ?",
                                     "WHERE id = ?");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.recipient);
            pstmt.setString(3, this.trackingID);
            pstmt.setString(4, this.base.getId());
            pstmt.execute();
        }
        this.base.update();
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM GiftRequest WHERE id = '%s'", this.base.getId()));
        this.base.delete();
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) throws SQLException {
        this.trackingID = trackingID;
        this.update ();
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) throws SQLException {
        this.recipient = recipient;
        this.update ();
    }

}
