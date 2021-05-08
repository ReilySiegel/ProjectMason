package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.account.Account;
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
import javafx.util.StringConverter;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
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
    String user;

    Stream<Account> acctStream;


    public RequestDisplay(JFXListView<VBox> reqList, Boolean showComplete, LocalDateTime latestTime, HashMap<String, Boolean> types) {
        this.reqList = reqList;
        this.showComplete = showComplete;
        this.latestTime = latestTime;
        this.types = types;
        this.user = "";
    }

    public RequestDisplay(JFXListView<VBox> reqList, Boolean showComplete, LocalDateTime latestTime, HashMap<String, Boolean> types, String user) {
        this.reqList = reqList;
        this.showComplete = showComplete;
        this.latestTime = latestTime;
        this.types = types;
        this.user = user;
    }

    public void setShowComplete(Boolean showComplete) {
        this.showComplete = showComplete;
    }

    public void update(HashMap<String, Boolean> selectedTypes, LocalDateTime dueBy) throws SQLException {
        reqList.getItems().clear();
        latestTime = dueBy;
        this.acctStream = Account.getAll().filter(a -> a.hasEmployeeAccess());

        Stream<MedicineRequest> medRequests = MedicineRequest.getAll();
        Stream<SanitationRequest> sanRequests = SanitationRequest.getAll();
        Stream<SecurityRequest> secRequests = SecurityRequest.getAll();
        Stream<FoodRequest> foodRequests = FoodRequest.getAll();
        Stream<GiftRequest> giftRequests = GiftRequest.getAll();
        Stream<InterpreterRequest> interpRequests = InterpreterRequest.getAll();
        Stream<LaundryRequest> laundryRequests = LaundryRequest.getAll();
        Stream<MaintenanceRequest> maintRequests = MaintenanceRequest.getAll();
        Stream<ReligiousRequest> religRequests = ReligiousRequest.getAll();
        Stream<TransportationRequest> transRequests = TransportationRequest.getAll();
        Stream<COVIDSurveyRequest> covidRequests = COVIDSurveyRequest.getAll();

        if (selectedTypes.get("Medicine")) medRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.med_sr")));
        if (selectedTypes.get("Sanitation")) sanRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.san_sr")));
        if (selectedTypes.get("Security")) secRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.sec_sr")));
        if (selectedTypes.get("Food")) foodRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.food_sr")));
        if (selectedTypes.get("Gift")) giftRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.gift_sr")));
        if (selectedTypes.get("Interpreter")) interpRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.int_sr")));
        if (selectedTypes.get("Laundry")) laundryRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.laun_sr")));
        if (selectedTypes.get("Maintenance")) maintRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.main_sr")));
        if (selectedTypes.get("Transportation")) transRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.transportation_request")));
        if (selectedTypes.get("Religious")) religRequests.forEach(r -> makeSRBox(r, App.resourceBundle.getString("key.rel_sr")));
        if (selectedTypes.get("COVID Survey")) covidRequests.forEach(r -> makeSurveyBox(r, user));

    }

    public void makeSRBox(ExtendedBaseRequest m, String type) {

        boolean passFilter = true;

        if (!showComplete) {
            if (m.isComplete()) passFilter = false;
        }

        if (latestTime.isBefore(m.getDue())) passFilter = false;

        if (!user.isEmpty()) {
            if (!m.getAssigned().equals(user)) {
                passFilter = false;
            }
        }

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
            Text locText = new Text(App.resourceBundle.getString("key.at_semicolon") + m.getLocations().collect(Collectors.joining(", ")).toString());
            locText.setWrappingWidth(100);

            //assigned person display
            Text assignedText = new Text(App.resourceBundle.getString("key.assigned_semicolon") + m.getAssigned());
            HBox assignedBox = new HBox(assignedText);
            assignedBox.setMinWidth(100);
            assignedBox.setPrefWidth(100);
            assignedBox.setMaxWidth(100);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, " + "H:m:s");
            Text dueText = new Text(App.resourceBundle.getString("key.due_semicolon") + m.getDue().format(formatter));
            dueText.setWrappingWidth(130);
            HBox dateBox = new HBox(dueText);
            dateBox.setMinWidth(130);
            dateBox.setPrefWidth(130);
            dateBox.setMaxWidth(130);


            //status (is complete)
            Text statusText;

            if (m.isComplete()) statusText = new Text(App.resourceBundle.getString("key.complete"));
            else statusText = new Text(App.resourceBundle.getString("key.in_progress"));

            JFXButton viewContextMenu = new JFXButton("...");
            viewContextMenu.setStyle("-fx-border-radius: 5; -fx-background-radius: 5");
            expand.setStyle("-fx-border-radius: 5; -fx-background-radius: 5");

            HBox mbox;

            if (user.isEmpty()) mbox = new HBox(expand, typeBox, locText, assignedBox, dateBox, statusText, viewContextMenu);
            else mbox = new HBox(expand, typeBox, locText, assignedBox, dateBox, statusText);

            mbox.setSpacing(10);

            VBox mContainer = new VBox(mbox);
            mContainer.setStyle("-fx-padding: 10px; -fx-background-color: #e5e5e5; -fx-effect: dropshadow(gaussian, rgba(170, 170, 170, 0.3), 10, 0.5, 0.0, 0.0)");
            mContainer.setSpacing(7);

            MenuItem edit = new MenuItem(App.resourceBundle.getString("key.reassign"));
            MenuItem markComplete = new MenuItem(App.resourceBundle.getString("key.mark_as_complete"));

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
                    JFXComboBox<Label> editAssigneeDrop = new JFXComboBox<Label>();
                    editAssigneeDrop.setStyle("-fx-prompt-text-fill: white");

                    try {
                        Stream<Account> acctStream = Account.getAll();
                        acctStream.forEach(a -> {
                            Label item = new Label(a.getUsername());
                            editAssigneeDrop.getItems().add(new Label(a.getUsername()));
                        });
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    editAssigneeDrop.setConverter(new StringConverter<Label>() {
                        @Override
                        public String toString(Label object) {
                            return object==null? "" : object.getText();
                        }
                        @Override
                        public Label fromString(String string) {
                            return new Label(string);
                        }
                    });

                    JFXButton submitChange = new JFXButton("Apply");
                    submitChange.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (!editAssigneeDrop.getValue().getText().isEmpty()) {

                                try {
                                    m.setAssigned(editAssigneeDrop.getValue().getText());
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }

                                try {
                                    update(types, latestTime);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            }
                        }
                    });

                    assignedBox.getChildren().add(editAssigneeDrop);
                    assignedBox.getChildren().add(submitChange);
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
                            case "Medicamento":
                                try {
                                    mContainer.getChildren().add(new Text("Type: " + MedicineRequest.getByID(m.getID()).getType()));
                                    mContainer.getChildren().add(new Text("Amount: " + MedicineRequest.getByID(m.getID()).getAmount()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Sanitation Request":
                            case "Saneamiento":
                                try {
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.recurring_semicolon") + SanitationRequest.getByID(m.getID()).isRecurring()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Security Request":
                            case "Seguridad":
                                try {
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.emergency_semicolon") + SecurityRequest.getByID(m.getID()).isEmergency()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Food Request":
                            case "Alimento":
                                try {
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.dietary_restrictions_semicolon") + FoodRequest.getByID(m.getID()).getdR()));
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.appetizer_semicolon") + FoodRequest.getByID(m.getID()).getAppetizer()));
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.entree_semicolon") + FoodRequest.getByID(m.getID()).getEntre()));
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.dessert_semicolon") + FoodRequest.getByID(m.getID()).getDessert()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Gift Request":
                            case "Regalo":
                                if (type.equals("Gift Request")) {
                                    try {
                                        mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.recipient_semicolon") + GiftRequest.getByID(m.getID()).getRecipient()));
                                        mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.tracking_id_semicolon") + GiftRequest.getByID(m.getID()).getTrackingID()));
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }
                                }
                                break;
                            case "Interpreter Request":
                            case "Intérprete":
                                try {
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.language_semicolon") + InterpreterRequest.getByID(m.getID()).getLanguage()));
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.type_semicolon")  + InterpreterRequest.getByID(m.getID()).getType()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Laundry Request":
                            case "Lavado de Ropa":
                                try {
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.gown_semicolon") + LaundryRequest.getByID(m.getID()).getGown()));
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.bedding_semicolon") + LaundryRequest.getByID(m.getID()).getBedding()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Maintenance Request":
                            case "Mantenimiento":
                                try {
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.type_semicolon") + MaintenanceRequest.getByID(m.getID()).getType()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Religious Request":
                            case "Religión":
                                try {
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.service_semicolon") + ReligiousRequest.getByID(m.getID()).getService()));
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.figure_semicolon") + ReligiousRequest.getByID(m.getID()).getFigure()));
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.last_rights_semicolon") + ReligiousRequest.getByID(m.getID()).isLastRites()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                break;
                            case "Transportation Request":
                            case "Solicitude de transporte":
                                try {
                                    mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.destination_semicolon") + TransportationRequest.getByID(m.getID()).getDestination()));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                        }
                        mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.details_semicolon") + m.getDetails()));
                        mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.ID_semicolon") + m.getID()));
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

    public void makeSurveyBox(COVIDSurveyRequest c, String user) {
        boolean passFilter = true;

        if (!showComplete && c.getIsComplete()) passFilter = false;
        if (!user.isEmpty()) {
            if (!user.equals(c.getUsername())) passFilter = false;
        }

        if (passFilter) {
            JFXButton expand = new JFXButton("+");
            expand.setMinWidth(30);
            expand.setPrefWidth(30);
            expand.setMinHeight(30);
            expand.setPrefHeight(30);

            Text typeText = new Text(App.resourceBundle.getString("key.COVID_survey_requests"));
            typeText.setFont(new Font(20));

            HBox typeBox = new HBox(typeText);
            typeBox.setMinWidth(200);

            //assigned person display
            Text userText = new Text(App.resourceBundle.getString("key.user_semicolon") + c.getUsername());
            HBox userBox = new HBox(userText);
            userBox.setMinWidth(100);
            userBox.setPrefWidth(100);
            userBox.setMaxWidth(100);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, " + "H:m:s");
            Text timeText = new Text(App.resourceBundle.getString("key.submitted_at_semicolon") + c.getTimestamp().format(formatter));
            timeText.setWrappingWidth(130);
            HBox dateBox = new HBox(timeText);
            dateBox.setMinWidth(100);
            dateBox.setPrefWidth(100);
            dateBox.setMaxWidth(100);

            Text emergencyEntrance = new Text();

            if (c.getUseEmergencyEntrance())
                emergencyEntrance.setText(App.resourceBundle.getString("key.entrance_emergency"));
            else emergencyEntrance.setText(App.resourceBundle.getString("key.entrance_normal"));
            emergencyEntrance.setWrappingWidth(120);

            HBox entranceBox = new HBox(emergencyEntrance);
            entranceBox.setMinWidth(130);
            entranceBox.setPrefWidth(130);
            entranceBox.setMaxWidth(130);

            Text statusText = new Text();
            if (c.getIsComplete()) statusText.setText("Complete");
            else statusText.setText("In progress");

            JFXButton viewContextMenu = new JFXButton("...");
            viewContextMenu.setStyle("-fx-border-radius: 5; -fx-background-radius: 5");
            expand.setStyle("-fx-border-radius: 5; -fx-background-radius: 5");

            ContextMenu contextMenu = new ContextMenu();

            MenuItem approveItem = new MenuItem("Cleared for Entry");
            approveItem.setOnAction(event -> {
                try {
                    COVIDSurveyRequest.getByID(c.getId()).setComplete(true);
                    Account.getByUsername(c.getUsername()).setUseEmergencyEntrance(false);
                    Account.getByUsername(c.getUsername()).setTakenSurvey(true);
                    update(types, latestTime);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });

            MenuItem rejectItem = new MenuItem("Possibly Sick");
            rejectItem.setOnAction(event -> {
                try {
                    COVIDSurveyRequest.getByID(c.getId()).setComplete(true);
                    Account.getByUsername(c.getUsername()).setUseEmergencyEntrance(true);
                    Account.getByUsername(c.getUsername()).setTakenSurvey(true);
                    update(types, latestTime);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });

            contextMenu.getItems().add(approveItem);
            contextMenu.getItems().add(rejectItem);

            viewContextMenu.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    contextMenu.show(viewContextMenu, event.getScreenX(), event.getScreenY());
                }
            });

            HBox mbox;
            if (user.isEmpty()) {
                mbox = new HBox(expand, typeBox, userBox, dateBox, entranceBox, statusText, viewContextMenu);
            } else {
                mbox = new HBox(expand, typeBox, userBox, dateBox, entranceBox, statusText);
            }

            mbox.setSpacing(10);

            VBox mContainer = new VBox(mbox);
            mContainer.setStyle("-fx-padding: 10px; -fx-background-color: #e5e5e5; -fx-effect: dropshadow(gaussian, rgba(170, 170, 170, 0.3), 10, 0.5, 0.0, 0.0)");
            mContainer.setSpacing(7);

            expand.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (expand.getText().equals("+")) {
                        mContainer.getChildren().add(new Text(App.resourceBundle.getString("key.ID_semicolon") + c.getId()));
                        expand.setText("-");
                    } else {
                        mContainer.getChildren().clear();
                        mContainer.getChildren().add(mbox);
                        expand.setText("+");
                    }
                }
            });

            reqList.getItems().add(mContainer);
            System.out.println(c.getTimestamp());
        }
    }

}
