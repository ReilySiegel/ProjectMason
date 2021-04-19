package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;

import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.MedicineRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SR07_Medicine extends ServiceRequestPage implements Initializable {

    @FXML
    private JFXTextField medName;

    @FXML
    private JFXTextField medAmount;

    @FXML
    private JFXComboBox<String> loc;

    @FXML
    private JFXTextField assignee;

    @FXML
    private Text medErrorText;

    @FXML
    private Text amountErrorText;

    @FXML
    private Text roomErrorText;

    @FXML
    private Text assigneeErrorText;

    private boolean validRequest;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        try {
            validRequest = true;
            LinkedList<String> nodeShortNames = new LinkedList<String>();
            LinkedList<NodeInfo> nodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));

            for (NodeInfo i : nodes) {
                nodeShortNames.add(i.getNodeID());
            }

            for (String nodeName : nodeShortNames) {
                loc.getItems().add(nodeName);
            }

        } catch (Exception SQLException) {
            return;
        }

    }

    @FXML
    private void clearMedError(KeyEvent e) {
        medErrorText.setText("");
    }

    @FXML
    private void clearAmountError(KeyEvent e) {
        amountErrorText.setText("");
    }

//    @FXML
//    private void clearRoomError(ActionEvent e) {
//        roomErrorText.setText("");
//    }

    @FXML
    private void clearAssigneeError(KeyEvent e) {
        assigneeErrorText.setText("");
    }

    @FXML
    private void handleSubmission(ActionEvent e) throws SQLException {
        String medicine = medName.getText();
        String amount = medAmount.getText();
        String room = loc.getValue();
        String assignName = assignee.getText();

        validRequest = true;

        if (medicine.equals("")) {
            medErrorText.setText("No medicine specified");
            validRequest = false;
        }

        if (amount.equals("")) {
            amountErrorText.setText("No amount specified");
            validRequest = false;
        }
        if (room == null) {
            roomErrorText.setText("No room specified");
            validRequest = false;
        }
        if (assignName.equals("")) {
            assigneeErrorText.setText("No assignee specified");
            validRequest = false;
        }

        if (validRequest) {
            App.requestService.requestMedicine(medicine, amount, room, assignName);
            medErrorText.setText("");
            amountErrorText.setText("");
            roomErrorText.setText("");
            assigneeErrorText.setText("");
            System.out.println("request successful");
        }
    }

    @FXML
    private void handleHelp(ActionEvent e) {

        Stage helpWindow = new Stage();

        helpWindow.initModality(Modality.APPLICATION_MODAL);
        helpWindow.setTitle("Help - Medicine Defedgdfghdfghlivery");
        helpWindow.setMinWidth(400);
        helpWindow.setMinHeight(200);

        Label label = new Label();
        label.setText("Fill out each field as follows: \n" +
                "Medicine Name: Exact name of medicine to be delivered \n" +
                "Room: Select the node/room for it to be delivered to \n" +
                "Your Name: Your full name \n" +
                "Asignee Name: Full name of the person you want to fulfill the request");
        Button close = new Button("Close");

        close.setOnAction(g -> helpWindow.close());

        VBox layout = new VBox(13);
        layout.getChildren().addAll(label, close);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        helpWindow.setScene(scene);
        helpWindow.showAndWait();
    }




}