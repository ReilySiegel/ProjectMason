package edu.wpi.teamo.views.requests;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.FoodRequest;
import edu.wpi.teamo.views.LocaleType;
import edu.wpi.teamo.views.SubPageContainer;
import edu.wpi.teamo.utils.itemsifters.LocationSearcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class SR01_FoodRequestPage extends ServiceRequestPage implements Initializable {

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
    private Text timeErrorText;

    @FXML
    private Text dateErrorText;

    @FXML
    private Text roomErrorText;

    @FXML
    private Text foodErrorText;

    @FXML
    private JFXTextField patientNameBox;

    public static LocaleType selectedLocale;
    Locale locale;

    private boolean validRequest;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));

        locationSearcher = new LocationSearcher(locationLine, locationSearch);
        updateLocations();
        updateLocale();
    }

    @FXML
    private void handleSubmit(ActionEvent e){
        List<NodeInfo> locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();



        String appetizer = appetizerBox.getText();

        String dessert = dessertBox.getText();

        String entree = entreeBox.getText();

        String dR = dRBox.getText();

        String patientName = patientNameBox.getText();

        validRequest = true;

        if ((appetizer.equals("")) && (dessert.equals("")) && (entree.equals(""))) {
            foodErrorText.setText(App.resourceBundle.getString("key.no_meal_specified"));
            validRequest = false;
        }

        if(deliveryTime.getValue() == null){
            timeErrorText.setText(App.resourceBundle.getString("key.no_time_specified"));
            validRequest = false;
        }

        if(deliveryDate.getValue() == null){
            dateErrorText.setText(App.resourceBundle.getString("key.no_date_specified"));
            validRequest = false;
        }


        if (locations.size() == 0) {
            roomErrorText.setText(App.resourceBundle.getString("key.no_room_specified"));
            validRequest = false;
        }

        if(deliveryTime.getValue() == null){
            validRequest = false;
        }

        if(deliveryDate.getValue() == null){
            validRequest = false;
        }

        if(validRequest) {

            LocalTime curTime = deliveryTime.getValue();
            LocalDateTime curDate = deliveryDate.getValue().atTime(curTime);
            BaseRequest br = new BaseRequest(UUID.randomUUID().toString(), "", locationIDs.stream(), "", false, curDate);
            FoodRequest fr = new FoodRequest(appetizer, entree, dessert, dR, br);

            try {
                fr.update();
                System.out.println("Request Successful");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            timeErrorText.setText("");
            dateErrorText.setText("");
            foodErrorText.setText("");
            roomErrorText.setText("");

            System.out.println("request successful");
            appetizerBox.setText("");
            entreeBox.setText("");
            dessertBox.setText("");
            dRBox.setText("");
            patientNameBox.setText("");


            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(App.resourceBundle.getString("key.food_request_submitted")));
            content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                    App.resourceBundle.getString("key.patient_name_semicolon") + patientName + "\n" +
                    App.resourceBundle.getString("key.appetizer_semicolon")  + appetizer + "\n" +
                    App.resourceBundle.getString("key.entree_semicolon")  + entree + "\n" +
                    App.resourceBundle.getString("key.dessert_semicolon")  + dessert + "\n" +
                    App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                    App.resourceBundle.getString("key.time") + ": " +  curDate.toString()));
            JFXDialog popup = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
            JFXButton backButton = new JFXButton(App.resourceBundle.getString("key.back_to_menu"));

            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    popup.close();
                }
            });

            backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    SubPageContainer.switchPage(Pages.SERVICEREQUEST);
                }
            });

            content.setActions(closeButton, backButton);
            popup.show();
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
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorWindow.close();
            }
        });

        content.setActions(closeButton);
        errorWindow.show();

    }
    public void updateLocale() {
        if (App.selectedLocale == LocaleType.en_US) {
            Locale.setDefault(new Locale("en", "US"));
        }
        else if (App.selectedLocale == LocaleType.es_ES) {
            Locale.setDefault(new Locale("es", "US"));
        }
    }

}