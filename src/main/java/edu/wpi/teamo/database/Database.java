package edu.wpi.teamo.database;

import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.account.Account;
import edu.wpi.teamo.database.map.Edge;
import edu.wpi.teamo.database.map.Node;
import edu.wpi.teamo.database.request.*;

import java.nio.file.Paths;
import java.sql.*;

public class Database {
    Connection conn;
    private static Database db;

    static {
        try {
            db = new Database();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String defaultFilePath () {
        return Paths.get(System.getProperty("user.home"), ".oxblood", "db").toString();
    }

    private Database () throws Exception {
        this("jdbc:derby:" + defaultFilePath() +  ";create=true");
    }

    private Database (String uri) throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        conn = DriverManager.getConnection(uri);
    }

    public static void init() throws SQLException {
        Node.initTable(getInstance());
        Edge.initTable(getInstance());
        Account.initTable();
        BaseRequest.initTable();
        SanitationRequest.initTable();
        MedicineRequest.initTable();
        SecurityRequest.initTable();
        InterpreterRequest.initTable();
        GiftRequest.initTable();
        LaundryRequest.initTable();
        MaintenanceRequest.initTable();
        ReligiousRequest.initTable();
        FoodRequest.initTable();
        COVIDSurveyRequest.initTable();
        TransportationRequest.initTable();
        new Account("admin", "admin", true, "Wilson", "Wong", "admin", "example@example.com", false, true ,true).update();
        new Account("patient", "patient", false, "Nestor", "Lopez", "patient", "example@example.com").update();
        new Account("staff", "staff", false, "Reily", "Siegel", "employee", "example@example.com", false, true, true).update();
        new Account("guest", "guest", false, "guest", "guest", "guest", "example@example.com").update();
        try {
            Session.login("guest", "guest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        return db;
    }

    public static synchronized void setTesting (String dbname) {
        setDB(getMemoryURIFromName(dbname));
    }

    public static synchronized  void setEmbeddedDB () {
        setDB("jdbc:derby:" + defaultFilePath() +  ";create=true");
    }

    public static synchronized  void setRemoteDB (String host, String dbname) {
        setDB("jdbc:derby://" + host + "/" + dbname + ";create=true");
    }

    public static synchronized void setDB (String uri) {
        try {
            Database newDB = new Database(uri);
            db.close();
            db = newDB;
            init();
        } catch (Exception e) {
        }
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
