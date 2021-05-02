package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import edu.wpi.teamo.database.request.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestDisplay {

    JFXListView<HBox> reqList;

    HashMap<String, Boolean> types;

    Boolean showComplete;

    public RequestDisplay(JFXListView<HBox> reqList, Boolean showComplete) {
        this.reqList = reqList;
        this.showComplete = showComplete;

        this.types = new HashMap<String, Boolean>();
        this.types.put("Medicine", true);
        this.types.put("Sanitation", true);
        this.types.put("Security", true);
        this.types.put("Food", true);
        this.types.put("Gift", true);
        this.types.put("Laundry", true);
        this.types.put("Maintenance", true);
        this.types.put("Interpreter", true);
        this.types.put("Reilgious", true);
    }

    public void setShowComplete(Boolean showComplete) {
        this.showComplete = showComplete;
    }

    public void update(HashMap<String, Boolean> selectedTypes) throws SQLException {

        reqList.getItems().clear();

        Stream<MedicineRequest> medRequests = MedicineRequest.getAll();
        Stream<SanitationRequest> sanRequests = SanitationRequest.getAll();
        Stream<SecurityRequest> secRequests = SecurityRequest.getAll();
        Stream<FoodRequest> foodRequests = FoodRequest.getAll();
        Stream<GiftRequest> giftRequests = GiftRequest.getAll();
        Stream<InterpreterRequest> interpRequests = InterpreterRequest.getAll();
        Stream<LaundryRequest> laundryRequests = LaundryRequest.getAll();
        Stream<MaintenanceRequest> maintRequests = MaintenanceRequest.getAll();
        Stream<ReligiousRequest> religRequests = ReligiousRequest.getAll();

        if (selectedTypes.get("Medicine")) medRequests.forEach(r -> makeSRBox(r, "Medicine Request"));
        if (selectedTypes.get("Sanitation")) sanRequests.forEach(r -> makeSRBox(r, "Sanitation Request"));
        if (selectedTypes.get("Security")) secRequests.forEach(r -> makeSRBox(r, "Security Request"));
        if (selectedTypes.get("Food")) foodRequests.forEach(r -> makeSRBox(r, "Food Request"));
        if (selectedTypes.get("Gift")) giftRequests.forEach(r -> makeSRBox(r, "Gift Request"));
        if (selectedTypes.get("Interpreter")) interpRequests.forEach(r -> makeSRBox(r, "Interpreter Request"));
        if (selectedTypes.get("Laundry")) laundryRequests.forEach(r -> makeSRBox(r, "Laundry Request"));
        if (selectedTypes.get("Maintenance")) maintRequests.forEach(r -> makeSRBox(r, "Maintenance Request"));
        if (selectedTypes.get("Religious")) religRequests.forEach(r -> makeSRBox(r, "Religious Request"));

    }

    public void makeSRBox(ExtendedBaseRequest m, String type) {

        boolean passFilter = true;

        if (!showComplete) {
            if (m.isComplete()) passFilter = false;
        }

        if (passFilter) {
            Text typeText = new Text(type);
            typeText.setFont(new Font(20));

            HBox typeBox = new HBox(typeText);
            typeBox.setMinWidth(200);

            Stream<String> locs = m.getLocations();

            Text locText = new Text(m.getLocations().collect(Collectors.joining(", ")).toString());
            locText.setWrappingWidth(100);

            Text assignedText = new Text(m.getAssigned());
            HBox assignedBox = new HBox(assignedText);
            assignedBox.setMinWidth(170);
            assignedBox.setPrefWidth(170);
            assignedBox.setMaxWidth(170);

            Text statusText;

            if (m.isComplete()) statusText = new Text("Complete");
            else statusText = new Text("In progress");

            HBox mbox = new HBox(typeBox, locText, assignedBox, statusText);
            mbox.setStyle("-fx-padding: 10px");

            MenuItem edit = new MenuItem("Edit");
            MenuItem markComplete = new MenuItem("Mark as Complete");
            MenuItem delete = new MenuItem("Delete");

            ContextMenu srContextMenu = new ContextMenu();
            srContextMenu.getItems().add(edit);
            srContextMenu.getItems().add(markComplete);
            srContextMenu.getItems().add(delete);

            markComplete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        BaseRequest.getByID(m.getID()).setComplete(true);
                        update(types);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });

            mbox.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.isSecondaryButtonDown())
                        srContextMenu.show(mbox, event.getScreenX(), event.getScreenY());
                }
            });
            reqList.getItems().add(mbox);
        }
    }

}
