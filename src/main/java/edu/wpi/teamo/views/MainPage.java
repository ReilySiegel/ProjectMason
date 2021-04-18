package edu.wpi.teamo.views;

import edu.wpi.teamo.App;
import java.io.IOException;

import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainPage {

    @FXML
    private javafx.scene.control.Button closeButton;

    /**
     * Event handler
     * @param e
     */
    @FXML
    private void closeWindow(ActionEvent e){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }


    /**
     * event handler for switching to service request page
     * @param e Action Event parameter
     */
    @FXML
    private void handleServiceRequest(ActionEvent e) {
        App.switchPage(Pages.SERVICEREQUEST);
    }


    /**
     * event handler for switching to map editor page
     * @param e Action Event parameter
     */
    @FXML
    private void handleMapEditor(ActionEvent e) {
        App.switchPage(Pages.MAPEDITOR);
    }


    /**
     * event handler for switching to pathfinding page
     * @param e Action Event parameter
     */
    @FXML
    private void handlePathfinding(ActionEvent e) {
        App.switchPage(Pages.PATHFINDING);
    }

    @FXML
    private void handleHelp(ActionEvent e){
        Stage helpWindow = new Stage();

        helpWindow.initModality(Modality.APPLICATION_MODAL);
        helpWindow.setTitle("Help");
        helpWindow.setMinWidth(950);
        helpWindow.setMinHeight(250);

        Label label = new Label();
        label.setText("Help:\n" +
                "Select a button to go to the corresponding page.\n" +
                "Medicine Request Page: Order an available non-prescription drug for delivery straight to any hospital room.\n" +
                "AlgoNode Editing Page: Load/Save a list of map nodes from a CSV file. Add, edit, or remove nodes for later use in pathfinding.\n" +
                "Edge Editing Page: Load/Save a list of map edges from a CSV file. Add, edit, or remove edges for later use in pathfinding.\n" +
                "Pathfinding Page: Select a starting and ending location and have the program find the fastest route between them.\n");
        Button close = new Button("Return");
        close.setOnAction(g -> helpWindow.close());

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, close);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        helpWindow.setScene(scene);
        helpWindow.showAndWait();
    }

}