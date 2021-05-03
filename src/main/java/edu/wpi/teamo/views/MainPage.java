package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPage implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private VBox mainMenuButtonBox;

    @FXML
    private JFXButton pathfinderButton;

    @FXML
    private JFXButton serviceRequestButton;

    @FXML
    private JFXButton aboutButton;

    @FXML
    private JFXButton mapEditorButton;

    @FXML
    private JFXButton requestManagerButton;

    @FXML
    private JFXButton accountManagerButton;

    @FXML
    private AnchorPane containerPane;

    @FXML
    private AnchorPane backgroundPane;

    @FXML
    private VBox containerVBox;

    @FXML
    private GridPane mainGrid;

    @FXML
    private JFXButton loginButton;

    @FXML
    private Text usernameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SubPageContainer.newInstance(containerPane, containerVBox);

        boolean isAdmin = Session.isLoggedIn() && Session.getAccount().isAdmin();
        setAdminButtonVisibility(isAdmin);
        updateAccountWindow();

        //Enable clicking through the gridpane overlaying the whole page
        mainGrid.setFillHeight(backgroundPane, true);
        mainGrid.setFillWidth(backgroundPane, true);
        mainGrid.setPickOnBounds(false);

        //Set dimensions of background image to the pane so it expands properly
        imageView.fitHeightProperty().bind(mainGrid.heightProperty());
        imageView.fitWidthProperty().bind(mainGrid.widthProperty());

        serviceRequestButton.setOnAction(this::handleServiceRequestButton);
        requestManagerButton.setOnAction(this::handleRequestManagerButton);
        accountManagerButton.setOnAction(this::handleAccountManagerButton);
        pathfinderButton.setOnAction(this::handlePathfinderButton);
        mapEditorButton.setOnAction(this::handleMapEditorButton);
        aboutButton.setOnAction(this::handleAboutButton);

        //commented out because right now these guys need the primary scene which is not yet set if this is the first page being loaded
//        try {
//            mapEditorPage = FXMLLoader.load(getClass().getResource("/edu/wpi/teamo/fxml/MapEditorPage.fxml"),
//                    App.resourceBundle);
//            pathfindingPage = FXMLLoader.load(getClass().getResource("/edu/wpi/teamo/fxml/PathfindingPage.fxml"),
//                    App.resourceBundle);
//            backgroundPane.getChildren().addAll(mapEditorPage,pathfindingPage);
//
//            pathfindingPage.setVisible(false);
//            pathfindingPage.setMouseTransparent(true);
//            mapEditorPage.setVisible(false);
//            mapEditorPage.setMouseTransparent(true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void handleAccountManagerButton(ActionEvent actionEvent) {
        setManageAccounts();
    }

    private void handleRequestManagerButton(ActionEvent actionEvent) {
        setManageRequests();
    }

    private void handleMapEditorButton(ActionEvent actionEvent) {
        setMapEditor();
    }

    private void handleAboutButton(ActionEvent actionEvent) {
        setAbout();
    }

    private void handlePathfinderButton(ActionEvent actionEvent) {
        setPathfinding();
    }

    private void handleServiceRequestButton(ActionEvent actionEvent) {
        setServiceRequest();
    }

    public void setMapEditor() {
        hidePages();
        backgroundPane.setVisible(true);
        try {
            Node mapEditorPage = FXMLLoader.load(getClass().getResource("/edu/wpi/teamo/fxml/MapEditorPage.fxml"),
                    App.resourceBundle);
            backgroundPane.getChildren().setAll(mapEditorPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPathfinding() {
        hidePages();
        backgroundPane.setVisible(true);
        try {
            Node pathfindingPage = FXMLLoader.load(getClass().getResource("/edu/wpi/teamo/fxml/PathfindingPage.fxml"),
                    App.resourceBundle);
            backgroundPane.getChildren().setAll(pathfindingPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAbout() {
        hidePages();
        SubPageContainer.switchPage(Pages.ABOUT);
    }

    public void setServiceRequest() {
        hidePages();
        SubPageContainer.switchPage(Pages.SERVICEREQUEST);
    }

    public void setManageRequests() {
        hidePages();
        SubPageContainer.switchPage(Pages.MANAGEREQUESTS);
    }

    public void setManageAccounts() {
        hidePages();
        SubPageContainer.switchPage(Pages.MANAGEACCOUNTS);
    }
  
    public void setCovidSurvey() {
        hidePages();
        SubPageContainer.switchPage(Pages.SURVEY);
    }

    private void updateAccountWindow() {
        if(Session.isLoggedIn()) {
            loginButton.setText(App.resourceBundle.getString("key.logout"));
            usernameLabel.setText(Session.getAccount().getUsername());
            loginButton.setOnAction(this::handleLogout);
        }
        else {
            loginButton.setText(App.resourceBundle.getString("key.login"));
            loginButton.setOnAction(this::handleLogin);
            usernameLabel.setText("Guest");
        }
    }

    private void setAdminButtonVisibility(boolean visible) {
        accountManagerButton.setVisible(visible);
        accountManagerButton.setManaged(visible);
        requestManagerButton.setVisible(visible);
        requestManagerButton.setManaged(visible);
        mapEditorButton.setVisible(visible);
        mapEditorButton.setManaged(visible);
    }

    private void handleLogin(ActionEvent e) {
        hidePages();
        SubPageContainer.switchPage(Pages.LOGIN);
    }

    private void handleLogout(ActionEvent e){
        Session.logout();

        setAdminButtonVisibility(false);
        updateAccountWindow();
        hidePages();
    }

    private void hidePages() {
        SubPageContainer.getInstance().hide();
        backgroundPane.getChildren().clear();
        backgroundPane.setVisible(false);
    }

    @FXML
    private void langOnClick(ActionEvent e) {
        switch(App.selectedLocale) {
            case en_US: {
                App.switchLocale("es", "ES", LocaleType.es_ES, false);
                break;
            }
            case es_ES: {
                App.switchLocale("en", "US", LocaleType.en_US, false);
                break;
            }
        }
    }

}
