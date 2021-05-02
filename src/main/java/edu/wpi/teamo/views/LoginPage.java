package edu.wpi.teamo.views;
import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.Objects;

public class LoginPage extends SubPageController{
    @FXML
    JFXTextField username;
    @FXML
    JFXPasswordField password;
    @FXML
    StackPane loginStack;
    @FXML
    AnchorPane submitAnchorPane;

    @FXML
    void checkPasswordUsernameCombo(ActionEvent e){
        String usernameString = username.getText();
        String passwordString = password.getText();

        try{
            Session.login(usernameString,passwordString);
            App.switchPage(Pages.MAIN);

        } catch (Exception exception) {
            username.clear();
            password.clear();

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(App.resourceBundle.getString("key.error")));
            content.setBody(new Text(App.resourceBundle.getString("key.Invalid_Combo")));
            JFXDialog errorWindow = new JFXDialog(loginStack,
                    content,
                    JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("Close");
            closeButton.setStyle("-fx-background-color: #F40F19");
            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    errorWindow.close();
                }
            });
            content.setActions(closeButton);
            errorWindow.show();
        }
    }

    @FXML
    public void onEnter(ActionEvent e) {
        checkPasswordUsernameCombo(e);
    }

    @FXML
    void backToMainMenu(ActionEvent e){
        App.switchPage(Pages.MAIN);
    }

    @FXML
    void guestOnClick(ActionEvent e){
        try{
            Session.login("guest","guest");
            App.switchPage(Pages.MAIN);

        } catch (Exception exception) {
            System.out.println("Error");
        }
    }
}
