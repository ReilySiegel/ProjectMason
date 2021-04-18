package edu.wpi.teamo.database;

import java.nio.file.Paths;
import java.sql.*;

public class Database {
    Connection conn;

    private static String defaultFilePath () {
        return Paths.get(System.getProperty("user.home"), ".oxblood", "db").toString();
    }

    public Database () throws SQLException, ClassNotFoundException {
        this("jdbc:derby:" + defaultFilePath() +  ";create=true");
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
        try {
            Statement stmt = conn.createStatement();
            int numUpdated = stmt.executeUpdate(s);
            return numUpdated;
        } catch (SQLException e) {
            // Ignore if table already exists in the DB.
            if (e.getSQLState().equals("X0Y32"))
                return 0;
            else
                throw e;
        }
    }

    public void close() throws SQLException {
        conn.close();
    }

    public static String getURIFromName(String name) {
        return String.format("jdbc:derby:memory:%s;create=true", name);
    }
}
