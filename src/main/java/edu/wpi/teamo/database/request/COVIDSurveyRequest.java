package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;
import edu.wpi.teamo.database.account.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class COVIDSurveyRequest extends ExtendedBaseRequest<COVIDSurveyRequest>{
    String username;
    boolean useEmergencyEntrance;

    public COVIDSurveyRequest(String username, boolean useEmergencyEntrance, BaseRequest base) {
        super(base);
        this.username = username;
        this.useEmergencyEntrance = useEmergencyEntrance;
    }

    public static void initTable() throws SQLException {
        String sql = String.join (", ",
                "CREATE TABLE EntranceRequest (id varchar(255) primary key",
                "username varchar(255)",
                "emergencyEntrance boolean)");
        Database.processUpdate(sql);
    }

    public static InterpreterRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM EntranceRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new InterpreterRequest(rs.getString("username"),
                rs.getString("emergencyEntrance"),
                BaseRequest.getByID(rs.getString("ID")));
    }

    public static Stream<InterpreterRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM EntranceRequest");
        ArrayList<InterpreterRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new InterpreterRequest(rs.getString("username"),
                    rs.getString("emergencyEntrance"),
                    BaseRequest.getByID(rs.getString("id"))));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            String sql = String.join (" ",
                    "INSERT INTO EntranceRequest",
                    "(id, username, emergencyEntrance)",
                    "VALUES (?, ?, ?)");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.username);
            pstmt.setString(3, String.valueOf(this.useEmergencyEntrance));
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            String sql = String.join(" ",
                    "UPDATE EntranceRequest SET",
                    "id = ?, language = ?, type = ?",
                    "WHERE id = ?");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.username);
            pstmt.setString(3, String.valueOf(this.useEmergencyEntrance));
            pstmt.setString(4, this.base.getId());
            pstmt.execute();
        }
        this.base.update();
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM EntranceRequest WHERE id = '%s'", this.base.getId()));
        this.base.delete();
    }

    public boolean getUseEmergencyEntrance(){
        return this.useEmergencyEntrance;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUseEmergencyEntrance(boolean useEmergencyEntrance){
        this.useEmergencyEntrance = useEmergencyEntrance;
    }

    public void setAccount(String username){
        this.username = username;
    }


}
