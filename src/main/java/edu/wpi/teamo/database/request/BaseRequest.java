package edu.wpi.teamo.database.request;

import java.time.LocalDateTime;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.wpi.teamo.database.Database;

public class BaseRequest {
    private String id;
    private ArrayList<String> locations;
    private String assigned;
    private boolean complete;
    private String details;
    private LocalDateTime due;
    private LocalDateTime timestamp;

    public BaseRequest(String id, String details, Stream<String> locations, String assigned, boolean complete) {
        this (id, details, locations, assigned, complete, LocalDateTime.MAX);
    }

    public BaseRequest(String id, String details, Stream<String> locations, String assigned, boolean complete, LocalDateTime due) {
        this (id, details, locations, assigned, complete, due, LocalDateTime.now());
    }

    public BaseRequest(String id, String details, Stream<String> locations, String assigned, boolean complete, LocalDateTime due, LocalDateTime timestamp) {
        this.id = id;
        this.details = details;
        this.locations = locations.collect(Collectors.toCollection(ArrayList::new));
        this.assigned = assigned;
        this.complete = complete;
        this.due = due;
        this.timestamp = timestamp;
    }

    public static void initTable() throws SQLException {
        Database.processUpdate(String.join(", ", 
                                           "CREATE TABLE BaseRequest (id varchar(255) primary key",
                                           "details varchar(255)",
                                           "complete boolean",
                                           "timestamp varchar(35)",
                                         "due varchar(35)",
                                           "assigned varchar(255)",
                                           "locations varchar(255))"));
    }

    public static BaseRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM BaseRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new BaseRequest(rs.getString("id"),
                               rs.getString("details"),
                               Stream.of(rs.getString("locations").split(",")),
                               rs.getString("assigned"),
                               rs.getBoolean("complete"),
                               LocalDateTime.parse(rs.getString("due")),
                               LocalDateTime.parse(rs.getString("timestamp")));
    }

    void update() throws SQLException {
        try {
            String sql = String.join(" ",
                                     "INSERT INTO BaseRequest",
                                     "(id, details, locations, assigned, complete, due, timestamp)",
                                     "VALUES",
                                     "(?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.id);
            pstmt.setString(2, this.details);
            pstmt.setString(3, this.locations.stream().collect(Collectors.joining(",")));
            pstmt.setString(4, this.assigned);
            pstmt.setBoolean(5, this.complete);
            pstmt.setString(6, this.due.toString());
            pstmt.setString(7, this.due.toString());
            pstmt.execute();
        } catch (SQLException e) {
            String sql = String.join(" ",
                                     "UPDATE BaseRequest SET",
                                     "id = ?, details = ?, locations = ?, assigned = ?, complete = ?, due = ?, timestamp = ?",
                                     "WHERE id = ?");

            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.id);
            pstmt.setString(2, this.details);
            pstmt.setString(3, this.locations.stream().collect(Collectors.joining(",")));
            pstmt.setString(4, this.assigned);
            pstmt.setBoolean(5, this.complete);
            pstmt.setString(6, this.due.toString());
            pstmt.setString(7, this.due.toString());
            pstmt.setString(8, this.id);
            pstmt.execute();
        }
    }

    void delete() throws SQLException {
        PreparedStatement pstmt =
            Database.prepareStatement(String.join(" ",
                                                  "DELETE FROM BaseRequest",
                                                  "WHERE id = ?"));
        pstmt.setString(1, this.id);
        pstmt.execute();
    }

    public String getId() {
        return id;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) throws SQLException {
        this.assigned = assigned;
        this.update();
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) throws SQLException {
        this.complete = complete;
        this.update();
    }

    public Stream<String> getLocations() {
        return locations.stream();
    }

    public void setLocations(Stream<String> locations) throws SQLException {
        this.locations = locations.collect(Collectors.toCollection(ArrayList::new));
        this.update();
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) throws SQLException {
        this.details = details;
        this.update();
    }

    public LocalDateTime getDue() {
        return due;
    }

    public void setDue(LocalDateTime due) throws SQLException {
        this.due = due;
        this.update();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
