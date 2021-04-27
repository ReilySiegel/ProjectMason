package edu.wpi.teamo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import edu.wpi.teamo.database.Database;
import edu.wpi.teamo.database.account.Account;

public class SessionTest {

    @Test
    public void testSession() throws Exception, SQLException {
        Database.setTesting("testSession");
        Account.initTable();
        new Account("test", "pass", false, "Test", "User", "test").update();
        Session.login("test", "pass");
        assertEquals(true, Session.isLoggedIn());
        assertThrows(Exception.class, () -> Session.login("test", "notPass"));
    }
}
