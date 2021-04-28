package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FoodRequest extends ExtendedBaseRequest<FoodRequest> {
    String dR;
    String appetizer;
    String dessert;
    String entre;

    public FoodRequest(String appetizer, String entre, String dessert, String dietaryRestrictions, BaseRequest base) {
        super(base);
        this.dR = dietaryRestrictions;
        this.appetizer = appetizer;
        this.dessert = dessert;
        this.entre = entre;
    }

    public static void initTable() throws SQLException {
        Database.processUpdate("CREATE TABLE FoodRequest (id varchar(255) primary key, appetizer varchar(255), entre varchar(255), dessert varchar(255), dR varchar(255))");
    }

    public static FoodRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM FoodRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new FoodRequest(rs.getString("appetizer"),
                               rs.getString("entre"),
                               rs.getString("dessert"),
                               rs.getString("dR"),
                               BaseRequest.getByID(rs.getString("ID")));
    }

    public static Stream<FoodRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM FoodRequest");
        ArrayList<FoodRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new FoodRequest(rs.getString("appetizer"),
                                     rs.getString("entre"),
                                     rs.getString("dessert"),
                                     rs.getString("dR"),
                                     BaseRequest.getByID(rs.getString("ID"))));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            PreparedStatement pstmt = Database.prepareStatement("INSERT INTO FoodRequest (id, appetizer, entre, dessert, dR) VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.appetizer);
            pstmt.setString(3, this.entre);
            pstmt.setString(4, this.dessert);
            pstmt.setString(5, this.dR);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            PreparedStatement pstmt =
                    Database.prepareStatement("UPDATE FoodRequest SET id = ?, appetizer = ?, entre = ?, dessert = ?, dR = ? WHERE id = ?");
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.appetizer);
            pstmt.setString(3, this.entre);
            pstmt.setString(4, this.dessert);
            pstmt.setString(5, this.dR);
            pstmt.setString(6, this.base.getId());
            pstmt.execute();
        }
        this.base.update();
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM FoodRequest WHERE id = '%s'", this.base.getId()));
        this.base.delete();
    }

    public String getdR() {
        return dR;
    }

    public void setdR(String dR) throws SQLException {
        this.dR = dR;
        this.update();
    }

    public String getAppetizer() {
        return appetizer;
    }

    public void setAppetizer(String appetizer) throws SQLException {
        this.appetizer = appetizer;
        this.update();
    }

    public String getDessert() {
        return dessert;
    }

    public void setDessert(String dessert) throws SQLException {
        this.dessert = dessert;
        this.update();
    }

    public String getEntre() {
        return entre;
    }

    public void setEntre(String entre) throws SQLException {
        this.entre = entre;
        this.update();
    }

}

