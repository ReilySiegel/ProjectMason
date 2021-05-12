package edu.wpi.teamo.views;
import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginPage extends SubPageController implements Initializable {
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
        verifyCredentials();
    }

    private void verifyCredentials() {
        String usernameString = username.getText();
        String passwordString = password.getText();


        try{
            Session.login(usernameString,passwordString);
            if(Session.getAccount().hasEmployeeAccess() || !Session.getAccount().getUseEmergencyEntrance()){
                App.switchPage(Pages.MAIN);
            }
            else{
                App.switchPage(Pages.MAIN);
                SubPageContainer.switchPage(Pages.SURVEY);
            }


        } catch (Exception exception) {
            username.clear();
            password.clear();

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Label(App.resourceBundle.getString("key.error")));
            content.setBody(new Label(App.resourceBundle.getString("key.Invalid_Combo")));
            JFXDialog errorWindow = new JFXDialog(loginStack,
                    content,
                    JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("Close");
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
            App.switchPage(Pages.SURVEY);

        } catch (Exception exception) {
            System.out.println("Error");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.getPrimaryStage().getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                App.getPrimaryStage().getScene().setOnKeyPressed(event -> {});
                verifyCredentials();
            }
        });
    }
}
