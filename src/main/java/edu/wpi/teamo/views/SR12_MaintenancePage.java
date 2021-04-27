package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.algos.NodeType;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private MenuButton locationBox;

    @FXML
    private JFXComboBox<String> MainTypeComboBox;

    private boolean validRequest;
    private SR12Type type;
    private ResourceBundle resources;

    private List<CheckMenuItem> allItems, elevItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        validRequest = true;
        String elevatorM = resources.getString("key.maintenance_elevator");
        String powerM = resources.getString("key.maintenance_power");
        MainTypeComboBox.getItems().add(elevatorM);
        MainTypeComboBox.getItems().add(powerM);
        MainTypeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals(elevatorM)){
                    type = SR12Type.ELEVATOR;
                    updateLocationBox();
                }
                else if(newValue.equals(powerM)){
                    type = SR12Type.POWER;
                    updateLocationBox();
                }
            }
        });

        try {
            resetLocationBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetLocationBox() throws SQLException {
        List<NodeInfo> savedNodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));
        allItems = FXCollections.observableArrayList();
        elevItems = FXCollections.observableArrayList();
        locationBox.getItems().removeAll(locationBox.getItems());
        for (NodeInfo node : savedNodes) addNode(node);
    }

    private void updateLocationBox() {
        locationBox.getItems().removeAll(locationBox.getItems());
            switch(type) {
                case POWER: {
                    locationBox.getItems().setAll(allItems);
                    break;
                }
                case ELEVATOR: {
                    locationBox.getItems().setAll(elevItems);
                    break;
                }
            }
    }

    private void addNode(NodeInfo node) {
        CheckMenuItem menuItem = new CheckMenuItem();
        menuItem.setText(node.getNodeID());
        menuItem.setOnAction(event -> locationBox.setText(locationBox.getItems().stream()
                .map((MenuItem mI) -> (CheckMenuItem) mI)
                .filter(CheckMenuItem::isSelected)
                .map(CheckMenuItem::getText)
                .collect(Collectors.joining(", "))));
        locationBox.getItems().add(menuItem);
        if(node.getNodeType().equals("ELEV")) elevItems.add(menuItem);
        allItems.add(menuItem);
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
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                helpWindow.close();
            }
        });
        content.setActions(closeButton);
        helpWindow.show();
    }

    private enum SR12Type {
        ELEVATOR,POWER
    }
}
