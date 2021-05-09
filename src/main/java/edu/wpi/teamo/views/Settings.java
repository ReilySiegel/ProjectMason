package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
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
import java.util.ResourceBundle;

public class Settings implements Initializable {

    JFXComboBox<Label> themeSelect;

    @FXML
    HBox themeBox;

    @FXML
    private JFXComboBox<String> langBox;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        fillLangBox();

        themeSelect = new JFXComboBox<Label>();
        themeBox.getChildren().add(themeSelect);

        themeSelect.getItems().add(new Label("Theme 1"));
        themeSelect.getItems().add(new Label("Theme 2"));
    }

    private void fillLangBox() {
        langBox.getItems().add(App.resourceBundle.getString("key.english"));
        langBox.getItems().add(App.resourceBundle.getString("key.spanish"));
        langBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleConfirm(ActionEvent e) {
        String theme1 = App.class.getResource("/edu/wpi/teamo/fxml/css/MainPage.css").toExternalForm();
        String theme2 = App.class.getResource("/edu/wpi/teamo/fxml/css/MainPage2.css").toExternalForm();

        switch (themeSelect.getValue().getText()) {
            case "Theme 1":
                Session.getAccount().setTheme(Theme.BLUE_SKY);
                break;
            case "Theme 2":
                Session.getAccount().setTheme(Theme.CLOUDS);
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