package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import com.jfoenix.svg.SVGGlyph;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class serviceRequestHubPage implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXListView<JFXButton> listView;

    @FXML
    private HBox hboxSize;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createAllServiceButtons();
    }
    public void createAllServiceButtons() {
        Image icon = new Image("edu/wpi/teamo/images/Icons/icons8-pill-100.png");
        createServiceButtons(App.resourceBundle.getString("key.medicine"),
                App.resourceBundle.getString("key.medicine_description"), icon, Pages.MEDICINE);
        icon = new Image("edu/wpi/teamo/images/Icons/icons8-ambulance-100.png");
        createServiceButtons(App.resourceBundle.getString("key.patient_transportation"),
                App.resourceBundle.getString("key.transportation_description"), icon, Pages.TRANSPORTATION);
        icon = new Image("edu/wpi/teamo/images/Icons/icons8-collaboration-96.png");
        createServiceButtons(App.resourceBundle.getString("key.language_interpreter"),
                App.resourceBundle.getString("key.language_description"), icon, Pages.LANGUAGEINTERPRETER);
        icon = new Image("edu/wpi/teamo/images/Icons/icons8-gift-100.png");
        createServiceButtons(App.resourceBundle.getString("key.gift_delivery"),
                App.resourceBundle.getString("key.gift_description"), icon, Pages.GIFTS);
        icon = new Image("edu/wpi/teamo/images/Icons/icons8-hanger-96.png");
        createServiceButtons(App.resourceBundle.getString("key.laundry"),
                App.resourceBundle.getString("key.laundry_description"), icon, Pages.LAUNDRY);
        icon = new Image("edu/wpi/teamo/images/Icons/icons8-hamburger-96.png");
        createServiceButtons(App.resourceBundle.getString("key.food_delivery"),
                App.resourceBundle.getString("key.food_description"), icon, Pages.FOOD);
        icon = new Image("edu/wpi/teamo/images/Icons/icons8-holy-bible-96.png");
        createServiceButtons(App.resourceBundle.getString("key.religious_requests"),
                App.resourceBundle.getString("key.religious_description"), icon, Pages.RELIGIOUS);
        icon = new Image("edu/wpi/teamo/images/Icons/icons8-housekeeping-96.png");
        createServiceButtons(App.resourceBundle.getString("key.sanitation"),
                App.resourceBundle.getString("key.sanitation_description"), icon, Pages.SANITATION);
        icon = new Image("edu/wpi/teamo/images/Icons/icons8-maintenance-96.png");
        createServiceButtons(App.resourceBundle.getString("key.maintenance"),
                App.resourceBundle.getString("key.maintenance_description"), icon, Pages.MAINTENANCE);
        icon = new Image("edu/wpi/teamo/images/Icons/icons8-police-badge-96.png");
        createServiceButtons(App.resourceBundle.getString("key.security"),
                App.resourceBundle.getString("key.security_description"), icon, Pages.SECURITY);

    }



    public void createServiceButtons(String key1, String key2, Image image, Pages pages) {
        Font font = Font.font("System", FontWeight.NORMAL, 20);

        Label label1 = new Label(key1);
        label1.setFont(font);
        Label label2 = new Label(key2);
        label2.setFont(font);
        VBox vbox = new VBox(label1, label2);

        ImageView icon = new ImageView();
        icon.setImage(image);
        HBox hbox = new HBox(icon,vbox);

        JFXButton s = new JFXButton("",hbox);
        s.setFont(font);
        hboxSize.setPrefWidth(1000);
        s.setMinWidth(hboxSize.getPrefWidth());
        s.setOnAction(actionEvent ->  {
            App.switchPage(pages);
        });
        listView.getItems().add(s);
    }

    @FXML
    void handleBacktoMain(ActionEvent event) {
        App.switchPage(Pages.MAIN);
    }

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help_service_requests")));
        content.setBody(new Text(App.resourceBundle.getString("key.help_finding_a_service") +
                App.resourceBundle.getString("key.help_selecting_a_service") +
                App.resourceBundle.getString("key.help_service_back")));
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