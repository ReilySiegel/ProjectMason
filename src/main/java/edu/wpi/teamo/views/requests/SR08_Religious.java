package edu.wpi.teamo.views.requests;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.account.Account;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.ReligiousRequest;
import edu.wpi.teamo.utils.itemsifters.LocationSearcher;
import edu.wpi.teamo.views.SubPageContainer;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SR08_Religious implements Initializable {
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField service;

    @FXML
    private JFXComboBox<String> assigneeBox;

    @FXML
    private JFXTextField notes;

    @FXML
    private JFXTextField religiousFigure;

    @FXML
    private JFXCheckBox lastRites;

    @FXML
    private JFXListView<JFXCheckBox> roomList;

    @FXML
    private JFXTextField room;

    @FXML
    private VBox topVbox;

    @FXML
    private VBox midVbox;

    @FXML
    private HBox bottomHbox;

    @FXML
    private Text typeErrorText;

    private boolean needLastRites;

    private boolean validRequest;

    LocationSearcher locationSearcher;

    @FXML
    private Text roomErrorText;

    @FXML
    private Text assignedErrorText;

    @FXML
    private JFXButton backButton;

    @FXML
    private HBox assignedBox;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        initAsigneeBox();

        if (Session.getAccount() == null || !Session.getAccount().hasEmployeeAccess()) {
            assignedBox.setVisible(false);
            assignedBox.setManaged(false);
        }

        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));

        topVbox.getStyleClass().add("vbox");
        bottomHbox.getStyleClass().add("vbox");
        midVbox.getStyleClass().add("text-area");
        lastRites.getStyleClass().add("check-box");

        locationSearcher = new LocationSearcher(room, roomList);
        updateLocations();

    }

    private void updateLocations() {
        try {
            locationSearcher.setLocations(App.mapService.getAllNodes().collect(Collectors.toList()));
        } catch (SQLException throwables) {
            locationSearcher.setLocations(new LinkedList<>());
            throwables.printStackTrace();
        }
    }

    private void initAsigneeBox() {
        Stream<Account> employees = Stream.empty();
        try {
            employees = Account.getAll().filter(Account::hasEmployeeAccess);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        employees.forEach(employee -> {
            assigneeBox.getItems().add(employee.getUsername());
        });
    }

    @FXML
    private void handleSubmission(ActionEvent e) throws SQLException {
        String serviceName = service.getText();
        String assigned = assigneeBox.getValue();
        String details = notes.getText();
        String figure = religiousFigure.getText();

        List<NodeInfo> locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();

        if(assigned == null){
            assigned = "Unassigned";
        }

        if (validateRequest()) {

            BaseRequest baseRequest = new BaseRequest(UUID.randomUUID().toString(),
                                                      serviceName + ", " + details,
                                                      locationIDs.stream(),
                                                      assigned,
                                                      false, LocalDateTime.now(), Session.getAccount().getUsername()
                                                      );

            new ReligiousRequest(service.getText(),
                                 religiousFigure.getText(),
                                 lastRites.isSelected(),
                                 baseRequest).update();

            System.out.println("Religious request submitted");

            resetFields();

            receiptPopup(serviceName, locationIDs, assigned, details, figure);
        }
    }

    private void receiptPopup(String serviceName, List<String> locationIDs, String assigned, String details, String figure) {

        if(figure.equals("")){
            figure = "None";
        }

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.religious_request_submitted")));

        if(!Session.getAccount().hasEmployeeAccess()){
            content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                    App.resourceBundle.getString("key.type_of_service") + serviceName + "\n" +
                    App.resourceBundle.getString("key.additional_notes")+ details + "\n" +
                    App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                    App.resourceBundle.getString("key.religious_figure_semicolon") + figure + "\n" +
                    App.resourceBundle.getString("key.last_rights_semicolon") + lastRites.isSelected()));
        }
        else{
            content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                    App.resourceBundle.getString("key.type_of_service") + serviceName + "\n" +
                    App.resourceBundle.getString("key.persons_assigned_semicolon") + assigned + "\n" +
                    App.resourceBundle.getString("key.additional_notes")+ details + "\n" +
                    App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                    App.resourceBundle.getString("key.religious_figure_semicolon") + figure + "\n" +
                    App.resourceBundle.getString("key.last_rights_semicolon") + lastRites.isSelected()));
        }

        JFXDialog popup = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        JFXButton backButton = new JFXButton(App.resourceBundle.getString("key.back_to_menu"));

        closeButton.setOnAction(event -> popup.close());

        backButton.setOnAction(event -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));

        content.setActions(closeButton, backButton);
        popup.show();
    }

    private boolean validateRequest() {
        validRequest = true;

        if (service.getText().equals("")) {
            typeErrorText.setText(App.resourceBundle.getString("key.no_religion_type_specified"));
            validRequest = false;
        }

        if (locationSearcher.getSelectedLocationIDs().size() == 0) {
            roomErrorText.setText(App.resourceBundle.getString("key.no_room_specified"));
            validRequest = false;
        }

        /*
        if(assigneeName.getText().equals("")){
            assignedErrorText.setText(App.resourceBundle.getString("key.no_assignee_specified"));
            validRequest = false;
        } */

        return validRequest;
    }


    public void handleHelpPress(ActionEvent e){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help")));
        content.setBody(new Text(App.resourceBundle.getString("key.religion_help")));
        JFXDialog helpWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setOnAction(event -> helpWindow.close());

        content.setActions(closeButton);
        helpWindow.show();
    }

    public void resetFields() {
        locationSearcher.clearSelectedLocations();
        service.setText("");
        religiousFigure.setText("");
        notes.setText("");
        typeErrorText.setText("");
        roomErrorText.setText("");
    }

}
