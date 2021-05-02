package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class tempContainerController implements Initializable {

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
    AnchorPane containerPane;

    @FXML
    AnchorPane backgroundPane;

    @FXML
    VBox containerVBox;

    @FXML
    GridPane mainGrid;

    Node pathfindingPage;

    Node mapEditorPage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Enable clicking through the gridpane overlaying the whole page
        mainGrid.setPickOnBounds(false);
        mainGrid.setFillHeight(backgroundPane, true);
        mainGrid.setFillWidth(backgroundPane, true);

        //Set dimensions of background image to the pane so it expands properly
        imageView.fitHeightProperty().bind(mainGrid.heightProperty());
        imageView.fitWidthProperty().bind(mainGrid.widthProperty());


        serviceRequestButton.setOnAction(this::handleServiceRequestButton);
        requestManagerButton.setOnAction(this::handleRequestManagerButton);
        accountManagerButton.setOnAction(this::handleAccountManagerButton);
        pathfinderButton.setOnAction(this::handlePathfinderButton);
        mapEditorButton.setOnAction(this::handleMapEditorButton);
        aboutButton.setOnAction(this::handleAboutButton);

        try {
            mapEditorPage = FXMLLoader.load(getClass().getResource("/edu/wpi/teamo/fxml/MapEditorPage.fxml"),
                    App.resourceBundle);
            pathfindingPage = FXMLLoader.load(getClass().getResource("/edu/wpi/teamo/fxml/PathfindingPage.fxml"),
                    App.resourceBundle);
            backgroundPane.getChildren().addAll(mapEditorPage,pathfindingPage);

            pathfindingPage.setVisible(false);
            pathfindingPage.setMouseTransparent(true);

            mapEditorPage.setVisible(false);
            mapEditorPage.setMouseTransparent(true);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleAccountManagerButton(ActionEvent actionEvent) {
        setManageRequests();
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

    private void showContainer() {
        containerVBox.setMouseTransparent(false);
        containerVBox.setVisible(true);
    }

    private void hidePages() {
        pathfindingPage.setMouseTransparent(true);
        pathfindingPage.setVisible(false);
        mapEditorPage.setMouseTransparent(true);
        mapEditorPage.setVisible(false);
        containerVBox.setMouseTransparent(true);
        containerVBox.setVisible(false);
    }

    public void setMapEditor() {
        hidePages();
        mapEditorPage.setMouseTransparent(false);
        mapEditorPage.setVisible(true);
    }

    public void setPathfinding() {
        hidePages();
        pathfindingPage.setMouseTransparent(false);
        pathfindingPage.setVisible(true);
    }

    public void setAbout() {
        loadPageInContainer("/edu/wpi/teamo/fxml/SR07_Medicine.fxml");
    }

    public void setServiceRequest() {
        loadPageInContainer("/edu/wpi/teamo/fxml/serviceRequestHubPage.fxml");
    }

    public void setManageRequests() {
        loadPageInContainer("/edu/wpi/teamo/fxml/ManageRequestsNew.fxml");
    }

    private void loadPageInContainer(String fxmlPath) {
        hidePages();
        showContainer();

        try {
            Pane newPane = FXMLLoader.load(getClass().getResource(fxmlPath), App.resourceBundle);

            containerPane.setTopAnchor(newPane, 0.0);
            containerPane.setRightAnchor(newPane, 0.0);
            containerPane.setLeftAnchor(newPane, 0.0);
            containerPane.setBottomAnchor(newPane,0.0);

            containerPane.getChildren().setAll(newPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
