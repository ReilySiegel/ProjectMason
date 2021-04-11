package edu.wpi.teamo.views;

import edu.wpi.teamo.App;
import java.io.IOException;

import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MainPage {
    /**
     * event hanlder for switching to medicine page
     * @param e Action Event parameter
     */
    @FXML
    private void handleMedicine(ActionEvent e) {
        App.switchPage(Pages.MEDICINE);
    }

    /**
     * event hanlder for switching to node page
     * @param e Action Event parameter
     */
    @FXML
    private void handleNode(ActionEvent e) {
        App.switchPage(Pages.NODE);
    }

    /**
     * event hanlder for switching to edge page
     * @param e Action Event parameter
     */
    @FXML
    private void handleEdge(ActionEvent e) {
        App.switchPage(Pages.EDGE);
    }

    /**
     * event hanlder for switching to pathfinding page
     * @param e Action Event parameter
     */
    @FXML
    private void handlePathfinding(ActionEvent e) {
        App.switchPage(Pages.PATHFINDING);
    }

}