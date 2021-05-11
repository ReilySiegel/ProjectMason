package edu.wpi.teamo.views.profilepage;

import edu.wpi.teamo.views.requestmanager.RequestDisplay;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import edu.wpi.teamo.database.request.*;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import com.jfoenix.controls.*;
import edu.wpi.teamo.Session;
import javafx.geometry.Pos;
import edu.wpi.teamo.App;
import java.util.HashMap;

public class ProfileRequestDisplay extends RequestDisplay {

    private JFXCheckBox showCompletedRequestsCheckbox;

    public ProfileRequestDisplay(JFXListView<VBox> reqDisplayListView, JFXCheckBox showCompletedRequestsCheckbox) {
        super(reqDisplayListView);

        this.showCompletedRequestsCheckbox = showCompletedRequestsCheckbox;
        showCompletedRequestsCheckbox.setOnAction(event -> update());

        typeCheckBoxes = new HashMap<>();
        typeCheckBoxes.put("Medicine", new JFXCheckBox());
        typeCheckBoxes.put("Sanitation", new JFXCheckBox());
        typeCheckBoxes.put("Security", new JFXCheckBox());
        typeCheckBoxes.put("Food", new JFXCheckBox());
        typeCheckBoxes.put("Gift", new JFXCheckBox());
        typeCheckBoxes.put("Interpreter", new JFXCheckBox());
        typeCheckBoxes.put("Laundry", new JFXCheckBox());
        typeCheckBoxes.put("Maintenance", new JFXCheckBox());
        typeCheckBoxes.put("Transportation", new JFXCheckBox());
        typeCheckBoxes.put("Religious", new JFXCheckBox());
        typeCheckBoxes.put("COVID Survey", new JFXCheckBox());
        typeCheckBoxes.values().forEach(c -> c.setSelected(true));

    }


    protected void displaySRIfPassFilter(ExtendedBaseRequest m, String type) {
        boolean passFilter = true;

        boolean thisUsersRequest = m.getRequesterUsername().equals(Session.getAccount().getUsername());
        if (!thisUsersRequest) {
            passFilter = false;
        }

        if (!showCompletedRequestsCheckbox.isSelected() && m.isComplete()) {
            passFilter = false;
        }

        if (passFilter) {
            VBox srBox = makeRequestBox(m.isComplete(), type, m.getDue());
            reqList.getItems().add(srBox);

            Runnable delete = () -> {
                try {
                    m.delete();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            };
            srBox.setOnMouseClicked(event -> boxContextMenu(event, delete));
        }

    }

    protected void addCovidSurveyIfPassFilter(COVIDSurveyRequest c) {
        boolean passFilter = true;

        boolean thisUsersRequest = c.getUsername().equals(Session.getAccount().getUsername());
        if (!thisUsersRequest) {
            passFilter = false;
        }

        if (!showCompletedRequestsCheckbox.isSelected() && c.getIsComplete()) {
            passFilter = false;
        }

        String type = App.resourceBundle.getString("key.covid_survey");

        if (passFilter) {
            VBox srBox = makeRequestBox(c.getIsComplete(), type, c.getTimestamp());
            reqList.getItems().add(srBox);

            Runnable delete = () -> {
                try {
                    c.delete();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            };
            srBox.setOnMouseClicked(event -> boxContextMenu(event, delete));
        }
    }

    private void boxContextMenu(MouseEvent event, Runnable delete) {
        if (event.getButton() != MouseButton.SECONDARY) return;

        ContextMenu menu = new ContextMenu();

        MenuItem deleteItem = new MenuItem(App.resourceBundle.getString("key.delete"));
        deleteItem.setOnAction(e -> {
            delete.run();
            update();
        });

        menu.getItems().add(deleteItem);
        menu.show(reqList.getScene().getWindow(), event.getScreenX(), event.getScreenY());
    }

    protected VBox makeRequestBox(boolean completed, String type, LocalDateTime dueDate) {
        VBox sRBox = new VBox();
        sRBox.setSpacing(10);
        sRBox.setAlignment(Pos.CENTER_LEFT);
        sRBox.setStyle("-fx-padding: 10px; -fx-background-color: #e5e5e5; -fx-effect: dropshadow(gaussian, rgba(170, 170, 170, 0.3), 10, 0.5, 0.0, 0.0)");
        HBox hbox = new HBox();

        //text displaying type of request
        Text typeText = new Text(type);
        typeText.getStyleClass().add("annoyingText");
        typeText.setFont(new Font(20));
        HBox typeBox = new HBox(typeText);
        typeBox.setMinWidth(200);
        hbox.getChildren().add(typeBox);

        //time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, " + "H:m:s");
        Text dueText = new Text(App.resourceBundle.getString("key.due_semicolon") + dueDate.format(formatter));
        HBox.setMargin(dueText, new Insets(9, 10, 0, 0));
        dueText.getStyleClass().add("annoyingText");
        hbox.getChildren().add(dueText);

        HBox statusLabelBox = makeStatusLabelBox(completed);
        hbox.getChildren().add(statusLabelBox);

        sRBox.getChildren().add(hbox);
        sRBox.getStyleClass().add("requestBox");
        return sRBox;
    }

    private HBox makeStatusLabelBox(boolean isComplete) {
        String completeKey = isComplete ? "key.complete"
                : "key.in_progress";
        String color = isComplete ? "#7fff63" : "#ffd748";
        Label statusLabel = new Label(App.resourceBundle.getString(completeKey));
        statusLabel.setMinWidth(150);
        statusLabel.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5");
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setPadding(new Insets(3, 20, 3, 20));
        HBox.setMargin(statusLabel, new Insets(0, 10, 0, 10));
        statusLabel.getStyleClass().add("annoyingText");
        HBox statusLabelHolder = new HBox(statusLabel);
        statusLabelHolder.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(statusLabelHolder, Priority.ALWAYS);
        return statusLabelHolder;
    }

}
