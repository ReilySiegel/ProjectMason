package edu.wpi.teamo.database;

import java.nio.file.Paths;
import java.sql.*;

public class Database {
    Connection conn;
    private static Database db = new Database();

    private static String defaultFilePath () {
        return Paths.get(System.getProperty("user.home"), ".oxblood", "db").toString();
    }

    private Database () {
        this("jdbc:derby:" + defaultFilePath() +  ";create=true");
    }

    private Database (String uri) {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(uri);
        } catch (Exception e) {
            System.out.println("Failed to create DB");
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        return db;
    }

    public static synchronized void setTesting (String dbname) {
        try {
            db.close();
        } catch (Exception e) {
        }
        db = new Database(getMemoryURIFromName(dbname));
    }

    public static ResultSet processQuery (String s) throws SQLException {
        Statement stmt = getInstance().conn.createStatement();
        ResultSet rs = stmt.executeQuery(s);
        return rs;
    }

    public static int processUpdate (String s) throws SQLException {
        try {
            Statement stmt = getInstance().conn.createStatement();
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

    public static PreparedStatement prepareStatement(String s) throws SQLException {
        return getInstance().conn.prepareStatement(s);
    }

    public void close() throws SQLException {
        conn.close();
    }

    public static String getMemoryURIFromName(String name) {
        return String.format("jdbc:derby:memory:%s;create=true", name);
    }
}
