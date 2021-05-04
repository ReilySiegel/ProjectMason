package edu.wpi.teamo.views;

import animatefx.animation.Bounce;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import animatefx.animation.FadeOutDownBig;
import edu.wpi.teamo.Pages;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import edu.wpi.teamo.App;


public class SubPageContainer {

    private static SubPageContainer instance = null;
    public static SubPageContainer getInstance() {
        return instance;
    }

    public static void newInstance(AnchorPane containerPane, VBox containerVBox) {
        instance = new SubPageContainer(containerPane, containerVBox);
    }

    private final AnchorPane containerPane;
    private final VBox containerVBox;

    private SubPageContainer(AnchorPane containerPane, VBox containerVBox) {
        this.containerVBox = containerVBox;
        this.containerPane = containerPane;
    }

    public static void switchPage(Pages page) { getInstance().loadPageAndShow(App.getPagePath(page)); }

    public void loadPage(String fxmlPath) {
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

    public void loadPageAndShow(String fxmlPath) {
        loadPage(fxmlPath);
        show();
    }

    public void show() {
        containerVBox.setMouseTransparent(false);
        containerVBox.setVisible(true);
        new FadeIn(containerVBox).play();
    }

    public void hide() {
        containerVBox.setMouseTransparent(true);
        containerVBox.setVisible(false);
    }

}
