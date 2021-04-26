package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.teamo.App;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainPage {

    @FXML
    private StackPane stackPane;

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

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Help"));
        content.setBody(new Text("Select a button to go to the corresponding page.\n" +
                "Service Requests Page: Select between a variety of services to be performed at a given location in the hospital.\n" +
                "Map Editor Page: Load/Save a list of map nodes and edges from a CSV file. Add, edit, or remove nodes/edges for later use in pathfinding.\n" +
                "Pathfinding Page: Select a starting and ending location and have the program find the fastest route between them.\n"));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton("Close");
        closeButton.setStyle("-fx-background-color: #F40F19");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorWindow.close();
            }
        });

        content.setActions(closeButton);
        errorWindow.show();
    }

    /**
     * event handler for switching to login page
     * @param e Action Event parameter
     */
    @FXML
    private void handleLogin(ActionEvent e) { App.switchPage(Pages.LOGIN);}

    @FXML
    private void handleAddUsers(ActionEvent e) { App.switchPage(Pages.ADDUSERS);}

    /**
     * Toggles between spanish and english (later on implement dedicated page for multiple languages?)
     * @param e action event
     */
    @FXML
    private void langOnClick(ActionEvent e) {
        switch(App.selectedLocale) {
            case en_US: {
                App.switchLocale("es", "ES", LocaleType.es_ES, false);
                break;
            }
            case es_ES: {
                App.switchLocale("en", "US", LocaleType.en_US, false);
                break;
            }
        }
    }
}