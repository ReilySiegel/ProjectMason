package edu.wpi.teamo.views;

import edu.wpi.teamo.App;
import java.io.IOException;

import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class NodePage {


    /**
     * event hanlder for switching to main page
     * @param e Action Event parameter
     */
    @FXML
    private void goToMain(ActionEvent e) {
        App.switchPage(Pages.MAIN);
    }
}