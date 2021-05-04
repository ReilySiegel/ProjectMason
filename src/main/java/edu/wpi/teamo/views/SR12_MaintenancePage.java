package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.MaintenanceRequest;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

public class SR12_MaintenancePage extends ServiceRequestPage implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField assignee;

    @FXML
    private JFXTextField notes;

    @FXML
    private JFXComboBox<String> MainTypeComboBox;

    @FXML
    private JFXTextField nodeSearchField;

    @FXML
    private JFXListView<JFXCheckBox> listOfSearched;

    @FXML
    private JFXButton backButton;

    private boolean validRequest;
    private SR12Type type;
    private ResourceBundle resources;
    private LocationSearcher ls;

    private List<NodeInfo> allItems, elevItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));
        type = SR12Type.NULL;
        this.resources = resources;
        String elevatorM = resources.getString("key.maintenance_elevator");
        String powerM = resources.getString("key.maintenance_power");
        MainTypeComboBox.getItems().add(elevatorM);
        MainTypeComboBox.getItems().add(powerM);
        MainTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(elevatorM)){
                type = SR12Type.ELEVATOR;
                updateOptions();
            }
            else if(newValue.equals(powerM)){
                type = SR12Type.POWER;
                updateOptions();
            }
        });
        ls = new LocationSearcher(nodeSearchField, listOfSearched);
        initLocationSearcher();
    }

    private void initLocationSearcher() {
        try {
            List<NodeInfo> nodes = App.mapService.getAllNodes().collect(Collectors.toList());
            ls.setLocations(nodes);
            elevItems = new LinkedList<>();
            allItems = new LinkedList<>();
            for(NodeInfo nI : nodes) {
                if(nI.getNodeType().equals("ELEV")) elevItems.add(nI);
                allItems.add(nI);
            }
        } catch (SQLException e) {
            ls.setLocations(new LinkedList<>());
            e.printStackTrace();
        }
    }

    private void updateOptions() {
            switch(type) {
                case POWER: {
                    ls.setLocations(allItems);
                    ls.clearSelectedItems();
                    break;
                }
                case ELEVATOR: {
                    ls.setLocations(elevItems);
                    ls.clearSelectedItems();
                    break;
                }
                case NULL: {
                    break;
                }
            }
    }

    @FXML
    private void submitBtnOnClick(ActionEvent actionEvent) {
        JFXDialogLayout submissionContent = new JFXDialogLayout();
        submissionContent.setHeading(new Text(resources.getString("key.confirm_request_submission")));
        submissionContent.setBody(new Text(resources.getString("key.maintenance_help")));
        JFXDialog confirmWindow = new JFXDialog(stackPane, submissionContent, JFXDialog.DialogTransition.TOP);
        JFXButton closeButton = new JFXButton(resources.getString("key.close"));
        JFXButton submitButton = new JFXButton(resources.getString("key.submit"));
        closeButton.setLayoutX(0);
        closeButton.setStyle("-fx-background-color: #f40f19");
        submitButton.setStyle("-fx-background-color: #5ab04c");
        closeButton.setOnAction(event -> confirmWindow.close());
        submitButton.setOnAction(event -> {
            confirmSubmission();
            confirmWindow.close();
        });
        submissionContent.setActions(closeButton);
        submissionContent.setActions(submitButton);
        confirmWindow.show();

    }

    private void promptSuccess(String sr_type, LocalDateTime time, List<String> selectedNodes) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(resources.getString("key.confirm_success_title")));
        content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                App.resourceBundle.getString("key.type_semicolon") + sr_type + "\n" +
                App.resourceBundle.getString("key.location_generic") + String.join(", ", selectedNodes) + "\n" +
                App.resourceBundle.getString("key.persons_assigned_semicolon")  + assignee.getText() + "\n" +
                App.resourceBundle.getString("key.time") + ": " +  time.toString() + "\n" +
                App.resourceBundle.getString("key.notes") + ": " + notes.getText()));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
        JFXButton closeButton = new JFXButton(resources.getString("key.close"));
        closeButton.setStyle("-fx-background-color: #f40f19");
        closeButton.setOnAction(event -> errorWindow.close());
        content.setActions(closeButton);
        errorWindow.show();
    }

    private void confirmSubmission() {
        validateFields();
        if(validRequest) {
            String str_type = "";
            switch(type){
                case ELEVATOR:{
                    str_type = "Elevator Maintenance";
                    break;
                }
                case POWER:{
                    str_type = "Power Maintenance";
                    break;
                }
            }
            String sub_assignee = assignee.getText();
            String sub_notes = notes.getText();
            List<String> selectedNodes = ls.getSelectedLocationIDs();
            LocalDateTime localDateTime = LocalDateTime.now();
            MaintenanceRequest MR = new MaintenanceRequest(str_type, new BaseRequest(
               UUID.randomUUID().toString(), sub_notes, selectedNodes.stream(), sub_assignee, false, localDateTime));
            try{
                MR.update();
                promptSuccess(str_type,localDateTime,selectedNodes);
                resetFields();
            }
            catch(SQLException e){
                e.printStackTrace();
                promptSQLError();
            }
        }
        else promptError();
    }

    private void promptSQLError() {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(resources.getString("key.sqlerror")));
        content.setBody(new Text(resources.getString("key.sql_error_details")));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
        JFXButton closeButton = new JFXButton(resources.getString("key.close"));
        closeButton.setStyle("-fx-background-color: #f40f19");
        closeButton.setOnAction(event -> errorWindow.close());
        content.setActions(closeButton);
        errorWindow.show();
    }

    private void resetFields() {
        assignee.textProperty().setValue("");
        notes.textProperty().setValue("");
        nodeSearchField.textProperty().setValue("");
    }

    private void validateFields() {
        validRequest = type != SR12Type.NULL && !notes.textProperty().getValue().equals("") &&
                !assignee.textProperty().getValue().equals("") && ls.getSelectedLocations().size() != 0;
    }

    private void promptError() {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(resources.getString("key.error")));
        content.setBody(new Text(resources.getString("key.maintenance_error")));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
        JFXButton closeButton = new JFXButton(resources.getString("key.close"));
        closeButton.setStyle("-fx-background-color: #f40f19");
        closeButton.setOnAction(event -> errorWindow.close());
        content.setActions(closeButton);
        errorWindow.show();
    }

    @FXML
    private void helpBtnOnClick(ActionEvent actionEvent) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(resources.getString("key.help")));
        content.setBody(new Text(resources.getString("key.maintenance_help")));
        JFXDialog helpWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
        JFXButton closeButton = new JFXButton(resources.getString("key.close"));
        closeButton.setOnAction(event -> helpWindow.close());
        content.setActions(closeButton);
        helpWindow.show();
    }

    private enum SR12Type {
        ELEVATOR,POWER,NULL
    }
}
