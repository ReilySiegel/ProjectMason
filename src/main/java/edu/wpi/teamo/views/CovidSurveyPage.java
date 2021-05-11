package edu.wpi.teamo.views;

import animatefx.animation.FadeOut;
import com.jfoenix.controls.*;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.request.COVIDSurveyRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class CovidSurveyPage implements Initializable {

    @FXML
    StackPane parentStackPane;

    @FXML
    HBox topHbox;

    @FXML
    HBox bottomHbox;

    @FXML
    VBox middleVbox;

    @FXML
    JFXCheckBox Q1;

    @FXML
    JFXCheckBox Q2;

    @FXML
    JFXCheckBox Q3;

    @FXML
    JFXCheckBox Q4;

    @FXML
    JFXCheckBox Q5;

    @FXML
    JFXButton backButton;

    public void initialize(URL location, ResourceBundle resources) {
        backButton.setOnAction(this::handleBack);
    }

    public void handleSubmit(javafx.event.ActionEvent actionEvent) throws SQLException, MessagingException {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.thanks_for_submitting_response")));

        boolean mayBeSick = Q1.isSelected() || Q2.isSelected() || Q3.isSelected() || Q4.isSelected() || Q5.isSelected();

        if(Session.isLoggedIn()){
            new COVIDSurveyRequest(Session.getAccount().getUsername(), mayBeSick).update();
        }
        content.setBody(new Text(App.resourceBundle.getString("key.survey_await_response")));

        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setOnAction(event -> App.switchPage(Pages.MAIN));
        if (!Session.getAccount().getRole().equals("guest")) {
            emailSender.sendCovidReceiptMail(Session.getAccount().getEmail(), "submitted");
        }
        content.setActions(closeButton);
        errorWindow.show();
    }

    void handleBack(ActionEvent actionEvent){
        parentStackPane.setMouseTransparent(true);
        new FadeOut(parentStackPane).play();
    }
}
