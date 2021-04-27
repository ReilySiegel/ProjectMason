package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class InterpreterRequest extends ExtendedBaseRequest<InterpreterRequest> {

    private String language;
    private String type;

    public InterpreterRequest(String lang, String type, BaseRequest base) {
        super(base);
        this.language = lang;
        this.type = type;
    }

    public static void initTable() throws SQLException {
        String sql = String.join (", ",
                                  "CREATE TABLE InterpreterRequest (id varchar(255) primary key",
                                  "language varchar(255)",
                                  "type varchar(255))");
        Database.processUpdate(sql);
    }

    public static InterpreterRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM InterpreterRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new InterpreterRequest(rs.getString("language"),
                                      rs.getString("type"),
                                      BaseRequest.getByID(rs.getString("ID")));
    }

    public static Stream<InterpreterRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM InterpreterRequest");
        ArrayList<InterpreterRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new InterpreterRequest(rs.getString("language"),
                                            rs.getString("type"),
                                            BaseRequest.getByID(rs.getString("id"))));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            String sql = String.join (" ",
                                      "INSERT INTO InterpreterRequest",
                                      "(id, language, type)",
                                      "VALUES (?, ?, ?)");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.language);
            pstmt.setString(3, this.type);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            String sql = String.join(" ",
                                     "UPDATE InterpreterRequest SET",
                                     "id = ?, language = ?, type = ?",
                                     "WHERE id = ?");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.language);
            pstmt.setString(3, this.type);
            pstmt.setString(4, this.base.getId());
            pstmt.execute();
        }
        this.base.update();
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM InterpreterRequest WHERE id = '%s'", this.base.getId()));
        this.base.delete();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) throws SQLException {
        this.type = type;
        this.update ();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) throws SQLException {
        this.language = language;
        this.update ();
    }

}
