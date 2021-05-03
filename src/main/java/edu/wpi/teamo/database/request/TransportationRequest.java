package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class TransportationRequest extends ExtendedBaseRequest<TransportationRequest> {

    private String destination;

    public TransportationRequest(String destination, BaseRequest base) {
        super(base);
        this.destination = destination;
    }

    public static void initTable() throws SQLException {
        Database.processUpdate("CREATE TABLE TransportationRequest (id varchar(255) primary key, destination varchar(255))");
    }

    public static TransportationRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM TransportationRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new TransportationRequest(rs.getString("destination"),
                                   BaseRequest.getByID(rs.getString("ID")));
    }

    public static Stream<TransportationRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM TransportationRequest");
        ArrayList<TransportationRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new TransportationRequest(rs.getString("destination"),
                                         BaseRequest.getByID(rs.getString("id"))));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            PreparedStatement pstmt = Database.prepareStatement("INSERT INTO TransportationRequest (id, destination) VALUES (?, ?)");
            pstmt.setString(1, this.base.getId());
            pstmt.setString (2, this.destination);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            PreparedStatement pstmt =
                    Database.prepareStatement("UPDATE TransportationRequest SET id = ?, destination = ? WHERE id = ?");
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.destination);
            pstmt.setString(3, this.base.getId());
            pstmt.execute();
        }
        this.base.update();
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM TransportationRequest WHERE id = '%s'", this.base.getId()));
        this.base.delete();
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) throws SQLException {
        this.destination = destination;
        this.update();
    }


}
