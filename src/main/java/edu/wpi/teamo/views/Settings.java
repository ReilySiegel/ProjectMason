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
        langBox.getItems().add("English");
        langBox.getItems().add("Español");
        langBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleConfirm(ActionEvent e) {
        if(langBox.getValue().equals("English")) {
            App.switchLocale("en", "US", LocaleType.es_ES, false);
        }
        else if (langBox.getValue().equals("Español")) {
            App.switchLocale("es", "ES", LocaleType.en_US, false);
        }
    }
}