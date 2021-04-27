package edu.wpi.teamo.views;

import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javax.annotation.Resource;
import javax.swing.*;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ServiceRequestPage extends SubPageController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {return;}


    /**
     * event handler for switching to medicine page
     * @param e Action Event parameter
     */
    @FXML
    private void handleMedicine(ActionEvent e) {
        App.switchPage(Pages.MEDICINE);
    }

    /**
     * event handler for switching to medicine page
     * @param e Action Event parameter
     */
    @FXML
    private void handleLanguageInterpreter(ActionEvent e) {
        App.switchPage(Pages.LANGUAGEINTERPRETER);
    }

    /**
     * event handler for switching to service request page
     * @param e Action Event parameter
     */
    @FXML
    private void handleBackToServicePage(ActionEvent e) {
        App.switchPage(Pages.SERVICEREQUEST);
    }

    /**
     * event handler for switching to sanitation page
     * @param e Action Event parameter
     */
    @FXML
    private void handleSanitation(ActionEvent e) {
        App.switchPage(Pages.SANITATION);
    }

    @FXML
    private void handleManageRequestPage(ActionEvent e) { App.switchPage(Pages.MANAGEREQUESTS);
    }

    @FXML
    private void handleReligiousRequest(ActionEvent e) { App.switchPage(Pages.RELIGIOUS);}

    @FXML
    private void handleMaintenanceBtn(ActionEvent e) {
        App.switchPage(Pages.MAINTENANCE);
    }

    @FXML
    private void handleSecurity(ActionEvent e) { App.switchPage(Pages.SECURITY); }

    @FXML
    private void handleTransportation(ActionEvent e){App.switchPage(Pages.TRANSPORTATION);}

}
