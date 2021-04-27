package edu.wpi.teamo.views;

import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddUsersPage {



    @FXML
    void backToMainMenu(ActionEvent e){
        App.switchPage(Pages.MAIN);
    }

}
