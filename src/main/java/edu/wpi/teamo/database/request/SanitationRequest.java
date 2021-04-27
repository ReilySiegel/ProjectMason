package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class SanitationRequest extends ExtendedBaseRequest<SanitationRequest> {

    private boolean recurring;

    public SanitationRequest(boolean recurring, BaseRequest base) {
        super(base);
        this.recurring = recurring;
    }

    public static void initTable() throws SQLException {
        Database.processUpdate("CREATE TABLE SanitationRequest (id varchar(255) primary key, recurring boolean)");
    }

    public static SanitationRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM SanitationRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new SanitationRequest(rs.getBoolean("recurring"),
                                   BaseRequest.getByID(rs.getString("ID")));
    }

    public static Stream<SanitationRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM SanitationRequest");
        ArrayList<SanitationRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new SanitationRequest(rs.getBoolean("recurring"),
                                         BaseRequest.getByID(rs.getString("id"))));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            PreparedStatement pstmt = Database.prepareStatement("INSERT INTO SanitationRequest (id, recurring) VALUES (?, ?)");
            pstmt.setString(1, this.base.getId());
            pstmt.setBoolean (2, this.recurring);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            PreparedStatement pstmt =
                    Database.prepareStatement("UPDATE SanitationRequest SET id = ?, recurring = ? WHERE id = ?");
            pstmt.setString(1, this.base.getId());
            pstmt.setBoolean(2, this.recurring);
            pstmt.setString(3, this.base.getId());
            pstmt.execute();
        }
        this.base.update();
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM SanitationRequest WHERE id = '%s'", this.base.getId()));
        this.base.delete();
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) throws SQLException {
        this.recurring = recurring;
        this.update();
    }


}
