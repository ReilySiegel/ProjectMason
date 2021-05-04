package edu.wpi.teamo.views;

import animatefx.animation.FadeOut;
import com.jfoenix.controls.*;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.request.COVIDSurveyRequest;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CovidSurveyPage extends ServiceRequestPage implements Initializable {

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

    private boolean Q1Check = false;
    private boolean Q2Check = false;
    private boolean Q3Check = false;
    private boolean Q4Check = false;
    private boolean Q5Check = false;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        backButton.setOnAction(this::handleBack);

        Q1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Q1Check = true;
                } else {
                    Q1Check = false;
                }
            }
        });

        Q2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Q2Check = true;
                } else {
                    Q2Check = false;
                }
            }
        });

        Q3.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Q3Check = true;
                } else {
                    Q3Check = false;
                }
            }
        });

        Q4.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Q4Check = true;
                } else {
                    Q4Check = false;
                }
            }
        });

        Q5.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Q5Check = true;
                } else {
                    Q5Check = false;
                }
            }
        });



    }

    public void handleSubmit(javafx.event.ActionEvent actionEvent) throws SQLException {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.thanks_for_submitting_response")));



        if(Q1Check || Q2Check || Q3Check || Q4Check || Q5Check){
            if(Session.isLoggedIn()){
                Session.getAccount().setUseEmergencyEntrance(true);
                new COVIDSurveyRequest(Session.getAccount().getUsername(), true).update();
            }
            content.setBody(new Text(App.resourceBundle.getString("key.survey_back_entrance")));

            if(Session.isLoggedIn()){
                Session.getAccount().setUseEmergencyEntrance(true);
                new COVIDSurveyRequest(Session.getAccount().getUsername(), true).update();
            }
            content.setBody(new Text(App.resourceBundle.getString("key.survey_await_response")));
        }
        else{
            if(Session.isLoggedIn()){
                Session.getAccount().setUseEmergencyEntrance(false);
                new COVIDSurveyRequest(Session.getAccount().getUsername(), false).update();
            }
            content.setBody(new Text(App.resourceBundle.getString("key.survey_await_response")));
        }

        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);
        content.setStyle("-fx-background-color: #d8dee9");

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setStyle("-fx-background-color: #434c5e; -fx-text-fill: #d8dee9");
        closeButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                App.switchPage(Pages.MAIN);
            }
        });

        content.setActions(closeButton);
        errorWindow.show();
    }

    void handleBack(ActionEvent actionEvent){
        parentStackPane.setMouseTransparent(true);
        new FadeOut(parentStackPane).play();
    }
}
