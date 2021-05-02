package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.teamo.App;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OldMainPage extends SubPageController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private javafx.scene.control.Button closeButton;

    @FXML
    private JFXButton loginButton;

    @FXML
    private Text usernameLabel;

    @FXML
    private JFXButton mapeditorButton;

    @FXML
    private JFXButton addaccountButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapeditorButton.setVisible(false);
        mapeditorButton.setManaged(false);
        addaccountButton.setVisible(false);
        addaccountButton.setManaged(false);
        if(Session.isLoggedIn()) {
            loginButton.setText(App.resourceBundle.getString("key.logout"));
            usernameLabel.setText(Session.getAccount().getUsername());
            loginButton.setOnAction(this::handleLogOut);
        }
        else {
            loginButton.setText(App.resourceBundle.getString("key.login"));
            loginButton.setOnAction(this::handleLogin);
            usernameLabel.setText("Guest");
        }
        if(Session.isLoggedIn() && Session.getAccount().isAdmin()) {
            mapeditorButton.setVisible(true);
            mapeditorButton.setManaged(true);
            addaccountButton.setVisible(true);
            addaccountButton.setManaged(true);
        }
    }

    /**
     * event handler for switching to survey
     * @param e Action Event parameter
     */
    @FXML
    private void handleSurvey(ActionEvent e) {
        App.switchPage(Pages.SURVEY);
    }

    /**
     * Event handler
     * @param e
     */
    @FXML
    private void closeWindow(ActionEvent e) {
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
        if(Session.isLoggedIn() && Session.getAccount().isAdmin())
            App.switchPage(Pages.MAPEDITOR);
        else{
//            add error
        }
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
        content.setHeading(new Text(App.resourceBundle.getString("key.help")));
        content.setBody(new Text(App.resourceBundle.getString("key.main_page_help")));
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
    private void handleAddUsers(ActionEvent e) {
        if(Session.isLoggedIn() && Session.getAccount().isAdmin())
            App.switchPage(Pages.ADDUSERS);
        else{
           //add error
       }
    }

    @FXML
    private void handleLogOut(ActionEvent e){
        loginButton.setText(App.resourceBundle.getString("key.login"));
        loginButton.setOnAction(this::handleLogin);
        usernameLabel.setText("Guest");
        Session.logout();
        mapeditorButton.setVisible(false);
        mapeditorButton.setManaged(false);
        addaccountButton.setVisible(false);
        addaccountButton.setManaged(false);
    }

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