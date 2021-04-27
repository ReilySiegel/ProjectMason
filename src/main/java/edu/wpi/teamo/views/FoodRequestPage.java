package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.NodeInfo;
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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FoodRequestPage extends SubPageController implements Initializable {

    @FXML
    private JFXComboBox<String> appetizer_list;

    @FXML
    private JFXComboBox<String> dessert_list;

    @FXML
    private JFXComboBox<String> entree_list;

    @FXML
    private JFXTextArea dietary_restrictions;



    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }




}