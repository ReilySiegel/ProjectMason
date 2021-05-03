package edu.wpi.teamo.views;

import edu.wpi.teamo.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Settings {

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