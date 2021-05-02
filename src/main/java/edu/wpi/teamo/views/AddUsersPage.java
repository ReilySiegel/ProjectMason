package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.account.Account;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddUsersPage extends SubPageController{

    @FXML
    JFXTextField username;
    @FXML
    JFXTextField firstName;
    @FXML
    JFXTextField lastName;
    @FXML
    JFXPasswordField password;
    @FXML
    StackPane stackPane;

    @FXML
    Text fNameError;

    @FXML
    Text lNameError;

    @FXML
    Text userError;

    @FXML
    Text pwError;

    @FXML
    Text roleError;


    @FXML
    void backToMainMenu(ActionEvent e){
        App.switchPage(Pages.MAIN);
    }

    @FXML
    void addUser(ActionEvent e){
        try {
            String requiredField = "This field is required";
            boolean validAccount = true;
            if (username.getText().isEmpty()) {
                validAccount = false;
                userError.setText(requiredField);
            } else userError.setText("");
            if (firstName.getText().isEmpty()) {
                validAccount = false;
                fNameError.setText(requiredField);
            } else fNameError.setText("");
            if (lastName.getText().isEmpty()) {
                validAccount = false;
                lNameError.setText(requiredField);
            } else lNameError.setText("");
            if (password.getText().isEmpty()) {
                validAccount = false;
                pwError.setText(requiredField);
            } else pwError.setText("");


            if (validAccount) {
                new Account(username.getText(), password.getText(), false, firstName.getText(), lastName.getText(), "patient").update();
                App.switchPage(Pages.MAIN);
            }

        }catch (SQLException err){
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(App.resourceBundle.getString("key.error")));
            content.setBody(new Text(App.resourceBundle.getString("key.Invalid_Combo")));
            JFXDialog errorWindow = new JFXDialog(stackPane,
                    content,
                    JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("key.close");
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

}
