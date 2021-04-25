package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXML;
import javafx.scene.text.Text;


public class SR08_Religious extends SubPageController{
    @FXML
    private StackPane stackPane;

    public void handleBackClick(ActionEvent e){
        App.switchPage(Pages.SERVICEREQUEST);
    }
    public void handleHelpPress(ActionEvent e){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Help"));
        content.setBody(new Text(" Service: request service\n Assignee name: enter your name\n Notes: write any additional notes\n Religious figure: request a religious figure to come\n Last Rites: Please check this box for your last rites"));
        JFXDialog helpWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton("Close");
        closeButton.setStyle("-fx-background-color: #f40f19");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                helpWindow.close();
            }
        });

        content.setActions(closeButton);
        helpWindow.show();
    }

}
