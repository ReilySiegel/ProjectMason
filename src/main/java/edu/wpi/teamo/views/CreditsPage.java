package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXScrollPane;
import edu.wpi.teamo.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;

public class CreditsPage implements Initializable {

    @FXML
    private JFXScrollPane scrollPane;

    @FXML
    private VBox vbox;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Font font = Font.font("System", FontWeight.NORMAL, 45);
        scrollPane.setContent(vbox);
        Label label1 = new Label(App.resourceBundle.getString("key.credits"));
        label1.setFont(font);
        JFXScrollPane.smoothScrolling((ScrollPane) scrollPane.getChildren().get(0));
        scrollPane.getTopBar().setPickOnBounds(true);
        scrollPane.getMainHeader().getChildren().add(label1);
    }
}