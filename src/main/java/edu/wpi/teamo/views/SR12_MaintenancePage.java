package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.*;

public class SR12_MaintenancePage extends ServiceRequestPage implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField service;

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

    private boolean validRequest;
    private SR12Type type;
    private ResourceBundle resources;
    private LocationSearcher ls;

    private List<NodeInfo> allItems, elevItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.getPrimaryStage().getScene().getStylesheets().add("edu/wpi/teamo/fxml/CSS/MaintenancePage.css");
        this.resources = resources;
        validRequest = true;
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
                    ls.searchResultsList.getItems().removeAll(ls.searchResultsList.getItems());
                    ls.setLocations(allItems);
                    break;
                }
                case ELEVATOR: {
                    ls.searchResultsList.getItems().removeAll(ls.searchResultsList.getItems());
                    ls.setLocations(elevItems);
                    break;
                }
            }
    }

    @FXML
    private void submitBtnOnClick(ActionEvent actionEvent) {
    //TODO
    }

    @FXML
    private void helpBtnOnClick(ActionEvent actionEvent) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Help"));
        content.setBody(new Text(resources.getString("key.maintenance_help")));
        JFXDialog helpWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
        JFXButton closeButton = new JFXButton("Close");
        closeButton.setStyle("-fx-background-color: #f40f19");
        closeButton.setOnAction(event -> helpWindow.close());
        content.setActions(closeButton);
        helpWindow.show();
    }

    private enum SR12Type {
        ELEVATOR,POWER
    }
}
