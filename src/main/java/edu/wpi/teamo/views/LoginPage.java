package edu.wpi.teamo.views;
import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class LoginPage extends SubPageController{
    @FXML
    JFXTextField username;
    @FXML
    JFXPasswordField password;
    @FXML
    StackPane loginStack;

    @FXML
    void checkPasswordUsernameCombo(ActionEvent e){
        if(username.getText().equals("admin") && password.getText().equals("password")){
            App.switchPage(Pages.MAPEDITOR);
        }
        else{
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Error"));
            content.setBody(new Text("Incorrect Username or Password"));
            JFXDialog errorWindow = new JFXDialog(loginStack, content, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("Close");
            closeButton.setStyle("-fx-background-color: #f40f19");
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
}
