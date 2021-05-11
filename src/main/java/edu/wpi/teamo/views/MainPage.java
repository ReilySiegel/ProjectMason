package edu.wpi.teamo.views;

import animatefx.animation.Bounce;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class MainPage implements Initializable {

    @FXML
    private StackPane parentStackPane;

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

    boolean mapEditorLoaded;
    boolean pathfindingLoaded;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mapEditorLoaded = false;
        pathfindingLoaded = false;
        SubPageContainer.newInstance(containerPane, containerVBox);

        boolean isAdmin = Session.isLoggedIn() && Session.getAccount().isAdmin();
        boolean isStaff = Session.isLoggedIn() && Session.getAccount().hasEmployeeAccess();

        setAdminButtonAnimation(isAdmin);
        setStaffButtonAnimation(isStaff);

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

        final BooleanProperty initialFocus = new SimpleBooleanProperty(true);

        pathfinderButton.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && initialFocus.get()) {
                containerVBox.requestFocus();
                initialFocus.setValue(false);
            }
        });

        imageView.setImage(new Image(App.getImagePath(Session.getAccount().getTheme())));

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

        if (Session.isLoggedIn() && !Session.getAccount().getTakenSurvey() && !Session.getAccount().isAdmin()) {
            App.showError(App.resourceBundle.getString("key.please_take_covid_survey"), parentStackPane);
        }
        else {
            setPathfinding();
        }

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
            backgroundPane.setTopAnchor(mapEditorPage, 0.0);
            backgroundPane.setBottomAnchor(mapEditorPage, 0.0);
            backgroundPane.setLeftAnchor(mapEditorPage, 0.0);
            backgroundPane.setRightAnchor(mapEditorPage, 0.0);
            new FadeIn(backgroundPane).play();
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
            backgroundPane.setTopAnchor(pathfindingPage, 0.0);
            backgroundPane.setBottomAnchor(pathfindingPage, 0.0);
            backgroundPane.setLeftAnchor(pathfindingPage, 0.0);
            backgroundPane.setRightAnchor(pathfindingPage, 0.0);

            new FadeIn(backgroundPane).play();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void setProfile(){
        hidePages();
        SubPageContainer.switchPage(Pages.PROFILE);
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

    public void setSettings() {
        hidePages();
        SubPageContainer.switchPage(Pages.SETTINGS);
    }

    public void setCredits(){
        hidePages();
        SubPageContainer.switchPage(Pages.CREDITS);
    }

    public void setCovidInfo(){
        hidePages();
        SubPageContainer.switchPage(Pages.INFO);
    }


    private void updateAccountWindow() {
        if(Session.isLoggedIn() && !Session.getAccount().getRole().equals("guest")) {
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
        if(visible){
            FadeOut fadeOut = new FadeOut(mainMenuButtonBox);
            fadeOut.setOnFinished(event -> setAdminButtonAnimation(visible));
            mainMenuButtonBox.setMouseTransparent(true);
            fadeOut.play();
        }
        else{
            setAdminButtonAnimation(visible);
        }
    }

    private void setAdminButtonAnimation(boolean visible){
        accountManagerButton.setVisible(visible);
        accountManagerButton.setManaged(visible);
        requestManagerButton.setVisible(visible);
        requestManagerButton.setManaged(visible);
        mapEditorButton.setVisible(visible);
        mapEditorButton.setManaged(visible);


        mainMenuButtonBox.setMouseTransparent(false);
        new FadeIn(mainMenuButtonBox).play();
    }

    private void setStaffButtonVisibility(boolean visible) {
        if(visible){
            FadeOut fadeOut = new FadeOut(mainMenuButtonBox);
            fadeOut.setOnFinished(event -> setStaffButtonAnimation(visible));
            mainMenuButtonBox.setMouseTransparent(true);
            fadeOut.play();
        }
        else{
            setStaffButtonAnimation(visible);
        }
    }

    private void setStaffButtonAnimation(boolean visible) {
        requestManagerButton.setVisible(visible);
        requestManagerButton.setManaged(visible);

        mainMenuButtonBox.setMouseTransparent(false);
        new FadeIn(mainMenuButtonBox).play();
    }

    private void handleLogin(ActionEvent e) {
        hidePages();
        SubPageContainer.switchPage(Pages.LOGIN);
    }

    private void handleLogout(ActionEvent e){
        Session.logout();
        try{
            Session.login("guest", "guest");
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        setAdminButtonVisibility(false);
        setStaffButtonVisibility(false);
        updateAccountWindow();
        hidePages();
    }

    private void hidePages() {
        SubPageContainer.getInstance().hide();
        backgroundPane.getChildren().clear();
        backgroundPane.setVisible(false);
    }

}
