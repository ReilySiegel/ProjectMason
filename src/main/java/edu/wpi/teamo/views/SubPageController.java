package edu.wpi.teamo.views;

import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SubPageController {

    /**
     * event handler for switching to main page
     * @param e Action Event parameter
     */
    @FXML
    public void backToMain(ActionEvent e){
        App.switchPage(Pages.MAIN);
    }
}
