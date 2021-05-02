package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CovidSurveyPage extends ServiceRequestPage implements Initializable {

    @FXML
    StackPane stackPane;

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

    private boolean Q1Check = false;
    private boolean Q2Check = false;
    private boolean Q3Check = false;
    private boolean Q4Check = false;
    private boolean Q5Check = false;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        topHbox.getStyleClass().add("root");
        bottomHbox.getStyleClass().add("root");
        middleVbox.getStyleClass().add("vbox");

        Q1.getStyleClass().add("check-box");
        Q2.getStyleClass().add("check-box");
        Q3.getStyleClass().add("check-box");
        Q4.getStyleClass().add("check-box");
        Q5.getStyleClass().add("check-box");

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

    public void handleSubmit(javafx.event.ActionEvent actionEvent) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Thank you for submitting your response."));

        if(Q1Check || Q2Check || Q3Check || Q4Check || Q5Check){
            content.setBody(new Text("Please enter the hospital through the back entrance. \n" +
                    "Please wear a mask and maintain a 6-foot distance between all hospital staff \n" +
                    "and patients unless instructed otherwise"));
        }
        else{
            content.setBody(new Text("Please enter the hospital through the front entrance \n" +
                    "Please wear a mask and maintain a 6-foot distance between all hospital staff \n" +
                    "and patients unless instructed otherwise"));
        }

        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
        content.setStyle("-fx-background-color: #d8dee9");

        JFXButton closeButton = new JFXButton("Close");
        closeButton.setStyle("-fx-background-color: #434c5e; -fx-text-fill: #d8dee9");
        closeButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                //errorWindow.close();
                App.switchPage(Pages.OLDMAIN);
            }


        });

        content.setActions(closeButton);
        errorWindow.show();
    }
}
