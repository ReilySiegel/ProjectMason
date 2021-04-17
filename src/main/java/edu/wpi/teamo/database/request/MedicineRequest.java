package edu.wpi.teamo.database.request;

import edu.wpi.teamo.database.Database;

import java.util.stream.Stream;

public class MedicineRequest implements IMedicineRequestInfo {
    private String locationID;
    private boolean complete;
    private String assigned;
    private String amount;
    private String type;
    private int number;

    public MedicineRequest(String type, String amount, String locationID, String assigned) {
        this.locationID = locationID;
        this.assigned = assigned;
        this.amount = amount;
        this.type = type;
    }

    public static MedicineRequest getByID(Database db, String id) {
        //TODO
        return null;
    }

    public static Stream<MedicineRequest> getAll(Database db) {
        //TODO
        return null;
    }

    public static void initTable(Database db) {
        //TODO
    }

    public void update() {
        //TODO
    }

    @Override
    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    @Override
    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    @Override
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public int getNumber() {
        return number;
    }
}
