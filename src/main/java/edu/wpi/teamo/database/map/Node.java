package edu.wpi.teamo.database.map;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Node extends RecursiveTreeObject<Node> implements NodeInfo {

    private int xPos;
    private int yPos;
    private String nodeID;
    private String floor;
    private String building;
    private String nodeType;
    private String longName;
    private String shortName;
    boolean valid;

    public Node(String nodeID, int xPos, int yPos, String floor,
                String building, String nodeType, String longName, String shortName, boolean valid){
        this.shortName = shortName;
        this.nodeType = nodeType;
        this.building = building;
        this.longName = longName;
        this.nodeID = nodeID;
        this.floor = floor;
        this.yPos = yPos;
        this.xPos = xPos;
        this.valid = valid;
    }

    public Node(String nodeID, int xPos, int yPos, String floor,
                String building, String nodeType, String longName, String shortName){
        this.shortName = shortName;
        this.nodeType = nodeType;
        this.building = building;
        this.longName = longName;
        this.nodeID = nodeID;
        this.floor = floor;
        this.yPos = yPos;
        this.xPos = xPos;
        this.valid = true;
    }

    /**
     * Create an AlgoNode table in a database.
     * @param db The database to create the table in.
     * @return True if the table was created successfully.
     */
    public static void initTable(Database db) throws SQLException {
        db.processUpdate("CREATE TABLE Node ("
                + "nodeID VARCHAR(255) PRIMARY KEY, "
                + "xcoord INT, "
                + "ycoord INT, "
                + "floor VARCHAR(255), "
                + "building VARCHAR(255), "
                + "nodeType VARCHAR(255), "
                + "longName VARCHAR(255), "
                + "shortName VARCHAR(255), "
                + "valid boolean)"
        );
    }

    /**
     * Create an AlgoNode object from an existing edge in a database.
     * @param db The database object to fetch the node from.
     * @param id The id of the node to be fetched.
     * @return The node object.
     * @throws SQLException When the id is incorrect, or database is not properly initialized.
     */
    public static Node getByID (Database db, String id) throws SQLException {
        ResultSet rs = db.processQuery("SELECT * FROM Node WHERE Node.nodeID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();

        return new Node(
            rs.getString("nodeID"),
            rs.getInt("xcoord"),
            rs.getInt("ycoord"),
            rs.getString("floor"),
            rs.getString("building"),
            rs.getString("nodeType"),
            rs.getString("longName"),
            rs.getString("shortName"),
            rs.getBoolean("valid")
        );
    }

    /**
     * Retrieve all nodes from a database.
     * @param db The database to retrieve the nodes from.
     * @return A stream of AlgoNode objects.
     * @throws SQLException When the database is not properly initialized.
     */
    public static Stream<Node> getAll(Database db) throws SQLException {
        ArrayList<Node> nodes = new ArrayList<>();
        ResultSet rs = db.processQuery("SELECT * FROM Node");
        while (rs.next())
            nodes.add(new Node(
                rs.getString("nodeID"),
                rs.getInt("xcoord"),
                rs.getInt("ycoord"),
                rs.getString("floor"),
                rs.getString("building"),
                rs.getString("nodeType"),
                rs.getString("longName"),
                rs.getString("shortName"),
                rs.getBoolean("valid")
            ));
        return nodes.stream();
    }

    /**
     * Update a database with the information stored in the AlgoNode object.
     * @param db The database to update.
     * @throws SQLException When the database is not properly initialized,
     * or the node object's attributes are invalid.
     */
    public void update(Database db) throws SQLException, IllegalArgumentException {

        if (this.shortName == null || this.shortName.isEmpty()) throw new IllegalArgumentException("invalid shortName");
        if (this.building == null  || this.building.isEmpty())  throw new IllegalArgumentException("invalid building");
        if (this.longName == null  || this.longName.isEmpty())  throw new IllegalArgumentException("invalid longName");
        if (this.nodeType == null  || this.nodeType.isEmpty())  throw new IllegalArgumentException("invalid nodeType");
        if (this.floor == null     || this.floor.isEmpty())     throw new IllegalArgumentException("invalid floor");
        if (this.nodeID == null    || this.nodeID.isEmpty())    throw new IllegalArgumentException("invalid ID");

        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            String sql = String.join (" ",
                    "INSERT INTO Node ",
                    "(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName, valid)",
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.nodeID);
            pstmt.setInt(2, this.xPos);
            pstmt.setInt(3, this.yPos);
            pstmt.setString(4, this.floor);
            pstmt.setString(5, this.building);
            pstmt.setString(6, this.nodeType);
            pstmt.setString(7, this.longName);
            pstmt.setString(8, this.shortName);
            pstmt.setBoolean(9, this.valid);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            String sql = String.join (" ",
                    "UPDATE Node SET",
                    "nodeID = ?, xcoord = ?, ycoord = ?, floor = ?, building = ?, nodeType = ?,",
                    "longName = ?, shortName = ?, valid = ? WHERE nodeID = ?");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.nodeID);
            pstmt.setInt(2, this.xPos);
            pstmt.setInt(3, this.yPos);
            pstmt.setString(4, this.floor);
            pstmt.setString(5, this.building);
            pstmt.setString(6, this.nodeType);
            pstmt.setString(7, this.longName);
            pstmt.setString(8, this.shortName);
            pstmt.setBoolean(9, this.valid);
            pstmt.setString(10, this.nodeID);
            pstmt.execute();
        }
    }

    public void delete(Database db) throws SQLException {
        db.processUpdate(String.format("DELETE FROM Node WHERE nodeID = '%s'", this.nodeID));
    }

    /**
     * This function updates x and y coordinate of a node object
     *
     * @param xcoord
     * @param ycoord
     */
    public void changeXY(int xcoord, int ycoord) {
        this.xPos = xcoord;
        this.yPos = ycoord;
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public int compareTo(NodeInfo otherNode) {
        return longName.compareToIgnoreCase(otherNode.getLongName());
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean isValid() {
        return valid;
    }
}

