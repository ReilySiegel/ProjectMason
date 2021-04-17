package edu.wpi.teamo.database.map;

import edu.wpi.teamo.database.Database;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    @Test
    public void testConnectDatabase() {
        boolean pass = true;
        try {
            Database db = new Database();
            db.close();
        } catch (Exception e) {
            System.out.println("CANNOT CONNECT TO DATABASE");
            e.printStackTrace();
            pass = false;
        }
        assertTrue(pass);
    }
}
