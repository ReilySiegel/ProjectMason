package edu.wpi.teamo.map.database;

import java.sql.*;

public class Database {
    Connection conn;

    public Database () throws SQLException, ClassNotFoundException {
        this("jdbc:derby:memory:DB;create=true");
    }

    public Database (String uri) throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        conn = DriverManager.getConnection(uri);
    }

    public ResultSet processQuery (String s) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(s);
        return rs;
    }

    public int processUpdate (String s) throws SQLException {
        Statement stmt = conn.createStatement();
        int numUpdated = stmt.executeUpdate(s);
        return numUpdated;
    }

    public void close() throws SQLException {
        conn.close();
    }
}
