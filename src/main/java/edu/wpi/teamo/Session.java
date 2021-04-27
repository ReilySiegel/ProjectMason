package edu.wpi.teamo;

import java.util.Objects;

import edu.wpi.teamo.database.account.Account;

public class Session {
    private static Session s = new Session ();
    private Account user = null;

    private Session () {

    }
    
    public static Session getSession () {
        return s;
    }

    public static boolean isLoggedIn() {
        return !Objects.isNull(getAccount());
    }

    public static Account getAccount () {
        return getSession().user;
    }

    public static synchronized void login (String username, String password) throws Exception {
        Account a = Account.getByUsername(username);
        if (a.getPasswordHash().equals(password))
            getSession().user = a;
        else
            throw new Exception();
    }
}
