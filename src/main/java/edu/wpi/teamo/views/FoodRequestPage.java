package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.FoodRequest;
import edu.wpi.teamo.database.request.MedicineRequest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class FoodRequestPage extends ServiceRequestPage implements Initializable {

    @FXML
    private JFXTextField appetizerBox;

    @FXML
    private JFXTextField dessertBox;

    @FXML
    private JFXTextField entreeBox;

    @FXML
    private JFXTextField dRBox;

    @FXML
    private JFXListView<JFXCheckBox> locationSearch;

    @FXML
    private JFXTimePicker deliveryTime;

    @FXML
    private JFXDatePicker deliveryDate;

    @FXML
    private JFXButton help_button;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField locationLine;

    @FXML
    private JFXButton backButton;

    LocationSearcher locationSearcher;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));

        locationSearcher = new LocationSearcher(locationLine, locationSearch);
        updateLocations();
    }

    @FXML
    private void handleSubmit(ActionEvent e){
        List<NodeInfo> locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();

        String appetizer = appetizerBox.getText();

        String dessert = dessertBox.getText();

        String entree = entreeBox.getText();

        String dR = dRBox.getText();

        BaseRequest br = new BaseRequest(UUID.randomUUID().toString(), "", locationIDs.stream(), "", false, deliveryDate.getValue().atTime(deliveryTime.getValue()));
        FoodRequest fr = new FoodRequest(appetizer, entree, dessert, dR, br);

        try {
            fr.update();
            System.out.println("Request Successful");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateLocations() {
        try {
            locationSearcher.setLocations(App.mapService.getAllNodes().collect(Collectors.toList()));
        } catch (SQLException throwables) {
            locationSearcher.setLocations(new LinkedList<>());
            throwables.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent e) {App.switchPage(Pages.MAIN);}

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.FoodDeliveryHelp")));
        content.setBody(new Text(
                App.resourceBundle.getString("key.menuHelp")));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorWindow.close();
            }
        });

        content.setActions(closeButton);
        errorWindow.show();

    }

    @FXML
    private void handleMenu(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.titleMenu")));
        content.setBody(new Text(
                App.resourceBundle.getString("key.menuList")));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
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