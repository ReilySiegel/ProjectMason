package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.LaundryRequest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LaundryRequestPage extends SubPageController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField service;

    @FXML
    private Text checkError;

    @FXML
    private JFXCheckBox gown;

    @FXML
    private JFXCheckBox sheets;

    @FXML
    private JFXTextField assignee;

    @FXML
    private JFXTextField notes;

    @FXML
    private Text assigneeErrorText;

    @FXML
    private Text roomErrorText;



    @FXML
    private MenuButton locationBox;

    @FXML
    private JFXTextField locationSearchBox;



    private boolean validRequest;

    LocationSearcher locationSearcher;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        App.getPrimaryStage().getScene().getStylesheets().add(LocationSearcher.getStylePath());
        try {

            this.resetLocationBox();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        //locationSearcher = new LocationSearcher(locationSearchBox, roomList);
       // updateLocations();

        validRequest = true;
    }
    private void resetLocationBox() throws SQLException {
        LinkedList<NodeInfo> nodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));
        locationBox.getItems().removeAll(locationBox.getItems());

        for (NodeInfo node : nodes) {
            CheckMenuItem menuItem = new CheckMenuItem();
            menuItem.setText(node.getNodeID());

            menuItem.setOnAction(event -> {
                locationBox.setText(locationBox.getItems().stream()
                        .map((MenuItem mI) -> (CheckMenuItem) mI)
                        .filter(CheckMenuItem::isSelected)
                        .map(CheckMenuItem::getText)
                        .collect(Collectors.joining(", ")));
            });

            locationBox.getItems().add(menuItem);
        }

    }





//    @FXML
//    private void clearRoomError(ActionEvent e) {
//        roomErrorText.setText("");
//    }

    @FXML
    private void clearAssigneeError(KeyEvent e) {
        assigneeErrorText.setText("");
    }

    @FXML
    private void handleSubmission(ActionEvent e) throws SQLException {
        boolean gownCheck = gown.isSelected();
        boolean sheetCheck = sheets.isSelected();
        String assignName = assignee.getText();
        String note = notes.getText();
        String laundryChecked = "";

        //List<NodeInfo> locations = locationSearcher.getSelectedLocations();
        //List<String> locationIDs = locationSearcher.getSelectedLocationIDs();
        List<MenuItem> mItems    = locationBox.getItems();
        Stream<CheckMenuItem> cMItems   = mItems.stream().map((MenuItem mI) -> (CheckMenuItem) mI);
        Stream<CheckMenuItem> checked   = cMItems.filter(CheckMenuItem::isSelected);
        List<String>          locations = checked.map(CheckMenuItem::getText).collect(Collectors.toList());
        validRequest = true;
        if(!gownCheck && !sheetCheck)
        {
            validRequest = false;
            checkError.setText(App.resourceBundle.getString("key.laundry_check_error"));
        }
        else{
            if (gownCheck){
                laundryChecked= " Gowns ";
                if(sheetCheck)
                {laundryChecked+="and Sheets";
                }
            }
            else{
                laundryChecked = "Sheets";
            }
        }


        if (locations.size() == 0) {
            roomErrorText.setText(App.resourceBundle.getString("key.no_room_specified"));
            validRequest = false;
        }
        if (assignName.equals("")) {
           assigneeErrorText.setText(App.resourceBundle.getString("key.no_laundry_checked"));
            validRequest = false;
        }

        if (validRequest) {
            LocalDateTime now = LocalDateTime.now();
            BaseRequest br = new BaseRequest(UUID.randomUUID().toString(), note, locations.stream(), assignName, false, now);
            new LaundryRequest(gownCheck,sheetCheck,br);


            assigneeErrorText.setText("");
            roomErrorText.setText("");
            checkError.setText("");
            System.out.println("request successful");

            assignee.setText("");
            this.notes.setText("");

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(App.resourceBundle.getString("key.laundry_request_submitted")));
            content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                    App.resourceBundle.getString("key.laundry_checked")  + laundryChecked+ "\n" +
                    App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locations) + "\n" +
                    App.resourceBundle.getString("key.persons_assigned_semicolon")  + assignName + "\n" +
                    App.resourceBundle.getString("key.notes") + ": " + note));
            JFXDialog popup = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
            JFXButton backButton = new JFXButton(App.resourceBundle.getString("key.back_to_main"));

            closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
            backButton.setStyle("-fx-background-color: #333333; -fx-text-fill: #fff");

            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    popup.close();
                }
            });

            backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    App.switchPage(Pages.SERVICEREQUEST);
                }
            });

            content.setActions(closeButton, backButton);
            popup.show();
        }
    }

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help_laundry_request")));
        content.setBody(new Text( App.resourceBundle.getString("key.room_help") +
                App.resourceBundle.getString("key.assignee_help")+
                App.resourceBundle.getString("key.laundry_check_help")));

        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorWindow.close();
            }
        });

        content.setActions(closeButton);
        errorWindow.show();
    }








}
