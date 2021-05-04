package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.request.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestDisplay {

    JFXListView<VBox> reqList;

    HashMap<String, Boolean> types;

    Boolean showComplete;

    LocalDateTime latestTime;


    public RequestDisplay(JFXListView<VBox> reqList, Boolean showComplete, LocalDateTime latestTime) {
        this.reqList = reqList;
        this.showComplete = showComplete;
        this.latestTime = latestTime;

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
        this.types.put("COVID Survey", true);
    }

    public void setShowComplete(Boolean showComplete) {
        this.showComplete = showComplete;
    }

    public void update(HashMap<String, Boolean> selectedTypes, LocalDateTime dueBy) throws SQLException {
        reqList.getItems().clear();
        latestTime = dueBy;

        Stream<MedicineRequest> medRequests = MedicineRequest.getAll();
        Stream<SanitationRequest> sanRequests = SanitationRequest.getAll();
        Stream<SecurityRequest> secRequests = SecurityRequest.getAll();
        Stream<FoodRequest> foodRequests = FoodRequest.getAll();
        Stream<GiftRequest> giftRequests = GiftRequest.getAll();
        Stream<InterpreterRequest> interpRequests = InterpreterRequest.getAll();
        Stream<LaundryRequest> laundryRequests = LaundryRequest.getAll();
        Stream<MaintenanceRequest> maintRequests = MaintenanceRequest.getAll();
        Stream<ReligiousRequest> religRequests = ReligiousRequest.getAll();
        //Stream<COVIDSurveyRequest> covidRequests = COVIDSurveyRequest.getAll();

        if (selectedTypes.get("Medicine")) medRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.med_sr")));
        if (selectedTypes.get("Sanitation")) sanRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.san_sr")));
        if (selectedTypes.get("Security")) secRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.sec_sr")));
        if (selectedTypes.get("Food")) foodRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.food_sr")));
        if (selectedTypes.get("Gift")) giftRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.gift_sr")));
        if (selectedTypes.get("Interpreter")) interpRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.int_sr")));
        if (selectedTypes.get("Laundry")) laundryRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.laun_sr")));
        if (selectedTypes.get("Maintenance")) maintRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.main_sr")));
        if (selectedTypes.get("Religious")) religRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.rel_sr")));
    }

    public void makeSRBox(ExtendedBaseRequest m, String type) {

        boolean passFilter = true;

        if (!showComplete) {
            if (m.isComplete()) passFilter = false;
        }

        if (latestTime.isBefore(m.getDue())) passFilter = false;

        if (passFilter) {
            JFXButton expand = new JFXButton("+");
            expand.setMinWidth(30);
            expand.setPrefWidth(30);
            expand.setMinHeight(30);
            expand.setPrefHeight(30);

            //text displaying type of request
            Text typeText = new Text(type);
            typeText.setFont(new Font(20));

            HBox typeBox = new HBox(typeText);
            typeBox.setMinWidth(200);

            //room/nodes display
            Text locText = new Text("At " + m.getLocations().collect(Collectors.joining(", ")).toString());
            locText.setWrappingWidth(100);

            //assigned person display
            Text assignedText = new Text("Assigned to: " + m.getAssigned());
            HBox assignedBox = new HBox(assignedText);
            assignedBox.setMinWidth(100);
            assignedBox.setPrefWidth(100);
            assignedBox.setMaxWidth(100);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, " + "H:m:s");
            Text dueText = new Text("Due: " + m.getDue().format(formatter));
            dueText.setWrappingWidth(130);
            HBox dateBox = new HBox(dueText);
            dateBox.setMinWidth(130);
            dateBox.setPrefWidth(130);
            dateBox.setMaxWidth(130);


            //status (is complete)
            Text statusText;

            if (m.isComplete()) statusText = new Text("Complete");
            else statusText = new Text("In progress");

            JFXButton viewContextMenu = new JFXButton("...");
            viewContextMenu.setStyle("-fx-border-radius: 5; -fx-background-radius: 5");
            expand.setStyle("-fx-border-radius: 5; -fx-background-radius: 5");

            HBox mbox = new HBox(expand, typeBox, locText, assignedBox, dateBox, statusText, viewContextMenu);
            mbox.setSpacing(10);

            VBox mContainer = new VBox(mbox);
            mContainer.setStyle("-fx-padding: 10px; -fx-background-color: #e5e5e5; -fx-effect: dropshadow(gaussian, rgba(170, 170, 170, 0.3), 10, 0.5, 0.0, 0.0)");
            mContainer.setSpacing(7);

            MenuItem edit = new MenuItem("Reassign");
            MenuItem markComplete = new MenuItem("Mark as Complete");

            ContextMenu srContextMenu = new ContextMenu();
            srContextMenu.getItems().add(edit);
            srContextMenu.getItems().add(markComplete);

            markComplete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    srContextMenu.hide();
                    try {
                        BaseRequest.getByID(m.getID()).setComplete(true);
                        update(types, latestTime);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });

            edit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    srContextMenu.hide();
                    assignedBox.getChildren().clear();
                    JFXTextField editAssignee = new JFXTextField(m.getAssigned());
                    editAssignee.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if (event.getCode().equals(KeyCode.ENTER)) {
                                try {
                                    m.setAssigned(editAssignee.getText());
                                    update(types, latestTime);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            }
                        }
                    });
                    assignedBox.getChildren().add(editAssignee);
                }
            });

            expand.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mContainer.getChildren().clear();
                    mContainer.getChildren().add(mbox);

                    if (expand.getText().equals("+")) {
                        expand.setText("-");

                        switch (type) {
                            case "Medicine Request":
                                try {
                                    mContainer.getChildren().add(new Text("Type: " + MedicineRequest.getByID(m.getID()).getType()));
                                    mContainer.getChildren().add(new Text("Amount: " + MedicineRequest.getByID(m.getID()).getAmount()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Sanitation Request":
                                try {
                                    mContainer.getChildren().add(new Text("Recurring: " + SanitationRequest.getByID(m.getID()).isRecurring()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Security Request":
                                try {
                                    mContainer.getChildren().add(new Text("Emergency: " + SecurityRequest.getByID(m.getID()).isEmergency()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Food Request":
                                try {
                                    mContainer.getChildren().add(new Text("Dietary Restrictions: " + FoodRequest.getByID(m.getID()).getdR()));
                                    mContainer.getChildren().add(new Text("Appetizer: " + FoodRequest.getByID(m.getID()).getAppetizer()));
                                    mContainer.getChildren().add(new Text("Entree: " + FoodRequest.getByID(m.getID()).getEntre()));
                                    mContainer.getChildren().add(new Text("Dessert: " + FoodRequest.getByID(m.getID()).getDessert()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Gift Request":
                                if (type.equals("Gift Request")) {
                                    try {
                                        mContainer.getChildren().add(new Text("Recipient: " + GiftRequest.getByID(m.getID()).getRecipient()));
                                        mContainer.getChildren().add(new Text("Tracking ID: " + GiftRequest.getByID(m.getID()).getTrackingID()));
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
                                }
                                break;
                            case "Interpreter Request":
                                try {
                                    mContainer.getChildren().add(new Text("Language: " + InterpreterRequest.getByID(m.getID()).getLanguage()));
                                    mContainer.getChildren().add(new Text("Type: " + InterpreterRequest.getByID(m.getID()).getType()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Laundry Request":
                                try {
                                    mContainer.getChildren().add(new Text("Gown: " + LaundryRequest.getByID(m.getID()).getGown()));
                                    mContainer.getChildren().add(new Text("Bedding: " + LaundryRequest.getByID(m.getID()).getBedding()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Maintenance Request":
                                try {
                                    mContainer.getChildren().add(new Text("Type: " + MaintenanceRequest.getByID(m.getID()).getType()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Religious Request":
                                try {
                                    mContainer.getChildren().add(new Text("Service: " + ReligiousRequest.getByID(m.getID()).getService()));
                                    mContainer.getChildren().add(new Text("Figure: " + ReligiousRequest.getByID(m.getID()).getFigure()));
                                    mContainer.getChildren().add(new Text("Last Rites: " + ReligiousRequest.getByID(m.getID()).isLastRites()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                        }

                        mContainer.getChildren().add(new Text("Details: " + m.getDetails()));
                    } else {
                        expand.setText("+");
                    }
                }
            });

//            mbox.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    if (event.isSecondaryButtonDown())
//                        srContextMenu.show(mbox, event.getScreenX(), event.getScreenY());
//                }
//            });
            reqList.getItems().add(mContainer);

            viewContextMenu.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    srContextMenu.show(viewContextMenu, event.getScreenX(), event.getScreenY());
                }
            });
        }
    }

    public void makeSurveyBox(COVIDSurveyRequest c) {

    }

}
