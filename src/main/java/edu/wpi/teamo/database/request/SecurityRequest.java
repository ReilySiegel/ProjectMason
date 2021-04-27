package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class SecurityRequest extends ExtendedBaseRequest<SecurityRequest> {

    private boolean emergency;

    public SecurityRequest(boolean emergency, BaseRequest base) {
        super(base);
        this.emergency = emergency;
    }

    public static void initTable() throws SQLException {
        Database.processUpdate("CREATE TABLE SecurityRequest (id varchar(255) primary key, emergency boolean)");
    }

    public static SecurityRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM SecurityRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new SecurityRequest(rs.getBoolean("emergency"),
                                   BaseRequest.getByID(rs.getString("ID")));
    }

    public static Stream<SecurityRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM SecurityRequest");
        ArrayList<SecurityRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new SecurityRequest(rs.getBoolean("emergency"),
                                         BaseRequest.getByID(rs.getString("id"))));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            PreparedStatement pstmt = Database.prepareStatement("INSERT INTO SecurityRequest (id, emergency) VALUES (?, ?)");
            pstmt.setString(1, this.base.getId());
            pstmt.setBoolean (2, this.emergency);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            PreparedStatement pstmt =
                    Database.prepareStatement("UPDATE SecurityRequest SET id = ?, emergency = ? WHERE id = ?");
            pstmt.setString(1, this.base.getId());
            pstmt.setBoolean(2, this.emergency);
            pstmt.setString(2, this.base.getId());
            pstmt.execute();
        }
        this.base.update();
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM SecurityRequest WHERE id = '%s'", this.base.getId()));
        this.base.delete();
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) throws SQLException {
        this.emergency = emergency;
        this.update();
    }


}
