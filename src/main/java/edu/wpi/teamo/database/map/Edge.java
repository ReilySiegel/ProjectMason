package edu.wpi.teamo.database.map;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Edge extends RecursiveTreeObject<Edge> implements EdgeInfo {

    String startNodeID;
    String endNodeID;
    String edgeID;
    boolean valid;

    public Edge(String edgeID, String startNodeID,  String endNodeID, boolean valid) {
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
        this.edgeID = edgeID;
        this.valid = valid;
    }

    public Edge(String edgeID, String startNodeID,  String endNodeID) {
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
        this.edgeID = edgeID;
        this.valid = true;
    }

    /**
     * Create an Edge table in a database.
     * @param db The database to create the table in.
     * @return True if the table was created successfully.
     */
    public static void initTable(Database db) throws SQLException {
        db.processUpdate("CREATE TABLE Edge ("
                + "edgeID varchar(255) primary key, "
                + "startNode varchar(255), "
                + "endNode varchar(255), "
                + "valid boolean)"
        );
    }

    /**
     * Create an Edge object from an existing edge in a database.
     * @param db The database object to fetch the edge from.
     * @param id The id of the edge to be fetched.
     * @return The edge object.
     * @throws SQLException When the id is incorrect, or database is not properly initialized.
     */
    public static Edge getByID(Database db, String id) throws SQLException {
        ResultSet rs = db.processQuery("SELECT * FROM Edge WHERE Edge.edgeID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new Edge(rs.getString("edgeID"),
                rs.getString("startNode"),
                rs.getString("endNode"),
                rs.getBoolean("valid"));
    }

    /**
     * Retrieve all edges from a database.
     * @param db The database to retrieve the edges from.
     * @return A stream of Edge objects.
     * @throws SQLException When the database is not properly initialized.
     */
    public static Stream<Edge> getAll(Database db) throws SQLException {
        ResultSet rs = db.processQuery("SELECT * FROM Edge");
        ArrayList<Edge> edges = new ArrayList<>();
        while (rs.next())
            edges.add(new Edge(rs.getString("edgeID"),
                    rs.getString("startNode"),
                    rs.getString("endNode"),
                    rs.getBoolean("valid")));
        return edges.stream();
    }

    /**
     * Update a database with the information stored in the Edge object.
     * @param db The database to update.
     * @throws SQLException When the database is not properly initialized,
     * or the edge object's attributes are invalid.
     */
    public void update(Database db) throws SQLException {

        if (this.startNodeID == null || this.startNodeID.isEmpty())
            throw new IllegalArgumentException("invalid startNodeID");
        if (this.endNodeID == null || this.endNodeID.isEmpty())
            throw new IllegalArgumentException("invalid endNodeID");
        if (this.edgeID == null || this.edgeID.isEmpty())
            throw new IllegalArgumentException("invalid edgeID");

        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            String sql = String.join(" ",
                                     "INSERT INTO Edge ",
                                     "(edgeID, startNode, endNode, valid)",
                                     "VALUES (?, ?, ?, ?)");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.edgeID);
            pstmt.setString(2, this.startNodeID);
            pstmt.setString(3, this.endNodeID);
            pstmt.setBoolean(4, this.valid);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            String sql = String.join (" ",
                                      "UPDATE Edge SET ",
                                      "edgeID = ?, startNode = ?, endNode = ?, valid = ? ",
                                      "WHERE edgeID = ?");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.edgeID);
            pstmt.setString(2, this.startNodeID);
            pstmt.setString(3, this.endNodeID);
            pstmt.setBoolean(4, this.valid);
            pstmt.setString(5, this.edgeID);
            pstmt.execute();
        }
    }

    public void delete(Database db) throws SQLException {
        db.processUpdate(String.format("DELETE FROM Edge WHERE edgeID = '%s'",
                                       this.edgeID));
    }

    @Override
    public String getStartNodeID() {
        return startNodeID;
    }

    @Override
    public String getEndNodeID() {
        return endNodeID;
    }

    @Override
    public String getEdgeID() {
        return edgeID;
    }

    public void setEdgeID(String id) {
        edgeID = id;
    }

    public void setStartID(String startNodeID) {
        this.startNodeID = startNodeID;
    }

    public void setEndID(String endNodeID) {
        this.endNodeID = endNodeID;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean isValid() {
        return valid;
    }
}
