package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;
import edu.wpi.teamo.database.account.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Stream;

public class COVIDSurveyRequest {
    String id;
    String username;
    boolean useEmergencyEntrance;
    LocalDateTime timestamp;
    boolean isComplete;

    public COVIDSurveyRequest(String id, String username, boolean useEmergencyEntrance, LocalDateTime timestamp, boolean isComplete) {
        this.id = id;
        this.timestamp = timestamp;
        this.username = username;
        this.useEmergencyEntrance = useEmergencyEntrance;
        this.isComplete = isComplete;
    }

    public COVIDSurveyRequest(String username, boolean useEmergencyEntrance){
        this(UUID.randomUUID().toString(),username,useEmergencyEntrance,LocalDateTime.now(), false);
    }

    public static void initTable() throws SQLException {
        String sql = String.join (", ",
                "CREATE TABLE EntranceRequest (id varchar(255) primary key",
                "username varchar(255)",
                "useEmergencyEntrance boolean",
                "timestamp varchar(35)",
                "isComplete boolean)");
        Database.processUpdate(sql);
    }

    public static COVIDSurveyRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM EntranceRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new COVIDSurveyRequest(rs.getString("id"), rs.getString("username"),
                rs.getBoolean("useEmergencyEntrance"), LocalDateTime.parse(rs.getString("timestamp")), rs.getBoolean("isComplete"));
    }

    public static Stream<COVIDSurveyRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM EntranceRequest");
        ArrayList<COVIDSurveyRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new COVIDSurveyRequest(rs.getString("ID"), rs.getString("username"),
                    rs.getBoolean("useEmergencyEntrance"), LocalDateTime.parse(rs.getString("timestamp")), rs.getBoolean("isComplete")));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            String sql = String.join (" ",
                    "INSERT INTO EntranceRequest",
                    "(id, username, useEmergencyEntrance, timestamp, isComplete)",
                    "VALUES (?, ?, ?, ?, ?)");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, this.username);
            pstmt.setBoolean(3, this.useEmergencyEntrance);
            pstmt.setString(4,this.timestamp.toString());
            pstmt.setBoolean(5, this.isComplete);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            String sql = String.join(" ",
                    "UPDATE EntranceRequest SET",
                    "id = ?, username = ?, useEmergencyEntrance = ?, timestamp = ?, isComplete = ?",
                    "WHERE id = ?");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.id);
            pstmt.setString(2, this.username);
            pstmt.setBoolean(3, this.useEmergencyEntrance);
            pstmt.setString(4, this.timestamp.toString());
            pstmt.setBoolean(5, this.isComplete);
            pstmt.setString(6, this.id);
            pstmt.execute();
        }

    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM EntranceRequest WHERE id = '%s'", this.id));

    }

    public boolean getUseEmergencyEntrance(){
        return this.useEmergencyEntrance;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUseEmergencyEntrance(boolean useEmergencyEntrance) throws SQLException {
        this.useEmergencyEntrance = useEmergencyEntrance;
        this.update();
    }

    public void setAccount(String username) throws SQLException {
        this.username = username;
        this.update();
    }

    public String getId(){
        return this.id;
    }

    public void setComplete(boolean isComplete) throws SQLException {
        this.isComplete = isComplete;
        this.update();
    }

    public boolean getIsComplete(){
        return this.isComplete;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
