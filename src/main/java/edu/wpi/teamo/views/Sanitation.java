package edu.wpi.teamo.views;

import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Sanitation {

    @FXML
    private void backToMain(ActionEvent e) {
        App.switchPage(Pages.MAIN);
    }

}
