package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.map.database.NodeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PathfindingPage implements Initializable {

    @FXML
    private JFXButton getDirections;

    @FXML
    private JFXButton goBack;

    @FXML
    private JFXComboBox<String> startLocation;

    @FXML
    private JFXComboBox<String> endLocation;

    @FXML
    private JFXTextArea locationAvailable;

    /**
     *
     * @param event
     */
    @FXML
    private JFXTextArea directions;

    /**
     *
     * @param event
     */
    @FXML
    void handleGetDirections(ActionEvent event) {

        String endNodeId = endLocation.getValue();
        String startNodeId = startLocation.getValue();
        LinkedList<AlgoNode> path = new LinkedList<>();
        try {
            path = App.aStarService.getPath(endNodeId, startNodeId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Display all Node ID, X, Y, Floor, Building, LongName, and ShortName
        String displayString = "";
        for(AlgoNode node : path){
            displayString = displayString + node.getShortName() + "\n";
        }
        directions.setText(displayString);

    }

    /**
     *
     * @param event
     */
    @FXML
    void handleGoBack(ActionEvent event) {
        App.switchPage(Pages.MAIN);
    }


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        try {
            LinkedList<String> nodeShortNames = new LinkedList<>();
            LinkedList<NodeInfo> nodes = App.dbService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));
            for (NodeInfo i: nodes ){
                nodeShortNames.add(i.getShortName());
            }

            for (String nodeName: nodeShortNames){
                startLocation.getItems().add(nodeName);
            }
        } catch (Exception SQLException) {
            return;
        }



        
    }

    /**
     *
     * @param event
     */
    @FXML
    void chooseStartLocation(ActionEvent event) {
        try {
            String startNodeId = startLocation.getValue();
        } catch (Exception SQLException) {
            return;
        }
    }

    /**
     *
     * @param event
     */
    @FXML
    void chooseEndLocation(ActionEvent event) {
        try {

            String endNodeId = endLocation.getValue();

        } catch (Exception SQLException) {
            return;
        }
    }

}