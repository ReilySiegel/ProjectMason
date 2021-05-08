package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.teamo.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Settings implements Initializable {


    @FXML
    private JFXComboBox<String> langBox;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        fillLangBox();
    }

    private void fillLangBox() {
        langBox.getItems().add(App.resourceBundle.getString("key.english"));
        langBox.getItems().add(App.resourceBundle.getString("key.spanish"));
        langBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleConfirm(ActionEvent e) {
        if(langBox.getValue().equals(App.resourceBundle.getString("key.english"))) {
            App.switchLocale("en", "US", LocaleType.en_US, false);
        }
        else if (langBox.getValue().equals(App.resourceBundle.getString("key.spanish"))) {
            App.switchLocale("es", "ES", LocaleType.es_ES, false);
        }
    }
}