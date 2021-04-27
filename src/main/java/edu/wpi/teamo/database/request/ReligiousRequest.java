package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ReligiousRequest extends ExtendedBaseRequest<ReligiousRequest> {

    private String service;
    private String figure;
    private boolean isLastRites;

    public ReligiousRequest(String service, String figure, boolean isLastRites, BaseRequest base) {
        super(base);
        this.service = service;
        this.figure = figure;
        this.isLastRites = isLastRites;
    }

    public static void initTable() throws SQLException {
        String sql = String.join (", ",
                                  "CREATE TABLE ReligiousRequest (id varchar(255) primary key",
                                  "service varchar(255)",
                                  "figure varchar(255)",
                                  "isLastRites boolean)");
        Database.processUpdate(sql);
    }

    public static ReligiousRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM ReligiousRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new ReligiousRequest(rs.getString("service"),
                                    rs.getString("figure"),
                                    rs.getBoolean("isLastRites"),
                                    BaseRequest.getByID(rs.getString("ID")));
    }

    public static Stream<ReligiousRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM ReligiousRequest");
        ArrayList<ReligiousRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new ReligiousRequest(rs.getString("service"),
                                          rs.getString("figure"),
                                          rs.getBoolean("isLastRites"),
                                          BaseRequest.getByID(rs.getString("id"))));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            String sql = String.join (" ",
                                      "INSERT INTO ReligiousRequest",
                                      "(id, service, figure, isLastRites)",
                                      "VALUES (?, ?, ?, ?)");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.service);
            pstmt.setString(3, this.figure);
            pstmt.setBoolean(4, this.isLastRites);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            String sql = String.join(" ",
                                     "UPDATE ReligiousRequest SET",
                                     "id = ?, service = ?, figure = ?, isLastRites = ?",
                                     "WHERE id = ?");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.service);
            pstmt.setString(3, this.figure);
            pstmt.setBoolean(4, this.isLastRites);
            pstmt.setString(5, this.base.getId());
            pstmt.execute();
        }
        this.base.update();
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM ReligiousRequest WHERE id = '%s'", this.base.getId()));
        this.base.delete();
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) throws SQLException {
        this.figure = figure;
        this.update ();
    }

    public String getService() {
        return service;
    }

    public void setService(String service) throws SQLException {
        this.service = service;
        this.update ();
    }

    public boolean isLastRites() {
        return isLastRites;
    }

    public void setLastRites(boolean isLastRites) throws SQLException {
        this.isLastRites = isLastRites;
        this.update();
    }


}
