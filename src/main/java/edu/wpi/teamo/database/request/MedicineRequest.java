package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicineRequest extends ExtendedBaseRequest<MedicineRequest> {
    private String amount;
    private String type;

    public MedicineRequest(String type, String amount, BaseRequest baseRequest) {
        super(baseRequest);
        this.amount = amount;
        this.type = type;
    }

    public static MedicineRequest getByID(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM MedicineRequest WHERE ID = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new MedicineRequest(rs.getString("type"),
                                   rs.getString("amount"),
                                   BaseRequest.getByID(rs.getString("ID")));
    }

    public static Stream<MedicineRequest> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM MedicineRequest");
        ArrayList<MedicineRequest> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new MedicineRequest(rs.getString("type"),
                                         rs.getString("amount"),
                                         BaseRequest.getByID(rs.getString("ID"))));
        return reqs.stream();
    }

    public static void initTable() throws SQLException {
        Database.processUpdate("CREATE TABLE MedicineRequest (" +
                               "id varchar(255) primary key, " +
                               "type varchar(255), " +
                               "amount varchar(255))");
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            PreparedStatement pstmt = Database.prepareStatement(
                    String.join(" ",
                                "INSERT INTO MedicineRequest",
                                "(id, type, amount)",
                                "VALUES",
                                "(?, ?, ?)"));
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.type);
            pstmt.setString(3, this.amount);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            PreparedStatement pstmt =
                Database.prepareStatement(String.join(" ",
                                                      "UPDATE MedicineRequest SET",
                                                      "id = ?, type = ?, amount = ?",
                                                      "WHERE id = ?"));
            pstmt.setString(1, this.base.getId());
            pstmt.setString(2, this.type);
            pstmt.setString(3, this.amount);
            pstmt.setString(4, this.base.getId());
            pstmt.execute();
        }
        this.base.update();
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM MedicineRequest WHERE id = '%s'", this.base.getId()));
        this.base.delete();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
