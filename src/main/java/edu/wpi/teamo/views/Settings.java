package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.Database;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Theme;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Settings implements Initializable {

    JFXComboBox<Label> themeSelect;

    @FXML
    HBox themeBox;

    @FXML
    private JFXComboBox<String> langBox;

    @FXML
    private JFXTextField hostName;

    @FXML
    private JFXTextField databaseName;

    @FXML
    private JFXButton linkButton;

    @FXML
    private JFXButton embeddedButton;

    @FXML
    private Label connectrDBLabel;

    @FXML
    private Label hostnameLabel;

    @FXML
    private Label dbLabel;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        linkButton.setOnAction(event -> linkServerDatabase());
        embeddedButton.setOnAction(event -> switchToEmbedded());
        fillLangBox();
        if (Session.getAccount() == null || !Session.getAccount().isAdmin()) {
            linkButton.setVisible(false);
            linkButton.setManaged(false);
            embeddedButton.setVisible(false);
            embeddedButton.setManaged(false);
            databaseName.setVisible(false);
            databaseName.setManaged(false);
            hostName.setVisible(false);
            hostName.setManaged(false);
            connectrDBLabel.setVisible(false);
            connectrDBLabel.setManaged(false);
            hostnameLabel.setVisible(false);
            hostnameLabel.setManaged(false);
            dbLabel.setVisible(false);
            dbLabel.setManaged(false);

        }

        themeSelect = new JFXComboBox<Label>();
        themeBox.getChildren().add(themeSelect);

        themeSelect.getItems().add(new Label("Theme 1"));
        themeSelect.getItems().add(new Label("Theme 2"));
        themeSelect.getItems().add(new Label("Dark"));
        themeSelect.getItems().add(new Label("Holiday"));
        themeSelect.getSelectionModel().selectFirst();
    }

    private void fillLangBox() {
        langBox.getItems().add(App.resourceBundle.getString("key.english"));
        langBox.getItems().add(App.resourceBundle.getString("key.spanish"));
        langBox.getSelectionModel().selectFirst();
    }

    private void linkServerDatabase(){
        String host = hostName.getText();
        String database = databaseName.getText();
        try{
            Database.setRemoteDB(host, database);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        App.switchPage(Pages.MAIN);
    }

    private void switchToEmbedded(){
        try{
            Database.setEmbeddedDB();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        App.switchPage(Pages.MAIN);
    }


    @FXML
    private void handleConfirm(ActionEvent e) {

        switch (themeSelect.getValue().getText()) {
            case "Theme 1":
                Session.getAccount().setTheme(Theme.BLUE_SKY);
                break;
            case "Theme 2":
                Session.getAccount().setTheme(Theme.CLOUDS);
                break;
            case "Holiday":
                Session.getAccount().setTheme(Theme.HOLIDAY);
                break;
            case "Dark":
                Session.getAccount().setTheme(Theme.DARK);
                break;
        }

        if(langBox.getValue().equals(App.resourceBundle.getString("key.english"))) {
            App.switchLocale("en", "US", LocaleType.en_US, false);
        }
        else if (langBox.getValue().equals(App.resourceBundle.getString("key.spanish"))) {
            App.switchLocale("es", "ES", LocaleType.es_ES, false);
        }

        App.switchPage(Pages.MAIN);

    }

}
