package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class LaundryRequest extends ExtendedBaseRequest<LaundryRequest> {

    private boolean gown;
    private boolean bedding;

    public LaundryRequest(boolean gown, boolean bedding, BaseRequest base) {
        super(base);
        this.gown = gown;
        this.bedding = bedding;
    }

    public static void initTable() throws SQLException {
        String sql = String.join (", ",
                                  "CREATE TABLE LaundryRequest (id varchar(255) primary key",
                                  "gown boolean",
                                  "bedding boolean)");
        Database.processUpdate(sql);
    }

    public static LaundryRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM LaundryRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new LaundryRequest(rs.getBoolean("gown"),
                                  rs.getBoolean("bedding"),
                                  BaseRequest.getByID(rs.getString("ID")));
    }

    public static Stream<LaundryRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM LaundryRequest");
        ArrayList<LaundryRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new LaundryRequest(rs.getBoolean("gown"),
                                        rs.getBoolean("bedding"),
                                        BaseRequest.getByID(rs.getString("id"))));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            String sql = String.join (" ",
                                      "INSERT INTO LaundryRequest",
                                      "(id, gown, bedding)",
                                      "VALUES (?, ?, ?)");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setBoolean(2, this.gown);
            pstmt.setBoolean(3, this.bedding);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            String sql = String.join(" ",
                                     "UPDATE LaundryRequest SET",
                                     "id = ?, gown = ?, bedding = ?",
                                     "WHERE id = ?");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setBoolean(2, this.gown);
            pstmt.setBoolean(3, this.bedding);
            pstmt.setString(4, this.base.getId());
            pstmt.execute();
        }
        this.base.update();
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM LaundryRequest WHERE id = '%s'", this.base.getId()));
        this.base.delete();
    }

    public boolean getBedding() {
        return bedding;
    }

    public void setBedding(boolean bedding) throws SQLException {
        this.bedding = bedding;
        this.update ();
    }

    public boolean getGown() {
        return gown;
    }

    public void setGown(boolean gown) throws SQLException {
        this.gown = gown;
        this.update ();
    }

}
