package edu.wpi.teamo.views.pathfindingpage;

import javafx.scene.layout.BackgroundFill;
import edu.wpi.teamo.algos.TextDirManager;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXButton;
import javafx.scene.layout.Background;
import javafx.scene.image.ImageView;
import edu.wpi.teamo.algos.AlgoNode;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.LinkedList;
import java.util.HashMap;
import edu.wpi.teamo.App;
import java.util.List;

public class TextualDirections {

    private JFXListView<HBox> textualDirView;
    private HBox currentDirectionDisplay;
    private JFXButton textualUnitsBtn;
    private VBox textualWindow;

    private HashMap<String, List<HBox>> directionHBoxesByFloor;
    private List<HBox> allDirectionHBoxes;
    private boolean metric = true;

    public TextualDirections(VBox textualWindow, JFXListView<HBox> textualDirView, JFXButton textualUnitsBtn, HBox currentDirectionDisplay) {
        this.currentDirectionDisplay = currentDirectionDisplay;
        this.textualUnitsBtn = textualUnitsBtn;
        this.textualDirView = textualDirView;
        this.textualWindow = textualWindow;
    }

    public void update(int iterator, String floor) {
        /* update the list of directions corresponding to the floor */
        if (allDirectionHBoxes.size() > iterator) {
            textualDirView.getItems().setAll(directionHBoxesByFloor.get(floor));
            updateCurrentDisplay(allDirectionHBoxes.get(iterator));
            highlightDirection(iterator);
        }
        else {
            clear();
        }


    }

    private void clear() {
        currentDirectionDisplay.getChildren().clear();
        textualDirView.getItems().clear();
    }

    public void loadDirections(List<AlgoNode> path) {
        allDirectionHBoxes = new LinkedList<>();
        directionHBoxesByFloor = new HashMap<>();

        List<String> directionsRaw = TextDirManager.getTextualDirections(path, metric);

        for (int i = 0; i < path.size(); i++) {
            String direction = directionsRaw.get(i);
            AlgoNode node = path.get(i);

            /* if this is a new floor, instantiate a list mapped with the floor */
            directionHBoxesByFloor.computeIfAbsent(node.getFloor(), k -> new LinkedList<>());

            /* create an icon HBox from the direction */
            HBox directionHBox = createHBoxFromDirection(direction);

            /* add the hbox to the set of floor directions, and the set of all directions */
            directionHBoxesByFloor.get(node.getFloor()).add(directionHBox);
            allDirectionHBoxes.add(directionHBox);
        }
    }

    public void toggleUnit(List<AlgoNode> calculatedPath, int directionIterator, String floor) {
        metric = !metric;
        if (metric) textualUnitsBtn.setText(App.resourceBundle.getString("key.units_metric"));
        else textualUnitsBtn.setText(App.resourceBundle.getString("key.units_us"));
        if(textualDirView.getItems().size() > 0 && calculatedPath != null) {
            textualDirView.getItems().remove(0, textualDirView.getItems().size() - 1);
            loadDirections(calculatedPath);
        }
        update(directionIterator, floor);
    }

    public void highlightDirection(int directionIterator) {
        for(HBox h: textualDirView.getItems())
        {
            h.setBackground(null);
        }
        BackgroundFill fill  = new BackgroundFill(new Color(0,0,0,.10),null,null);
        if (allDirectionHBoxes.size() > directionIterator) {
            allDirectionHBoxes.get(directionIterator).setBackground(new Background(fill,null));
        }
    }

    private void updateCurrentDisplay(HBox currentHBOX) {
        ImageView icon = new ImageView(((ImageView) currentHBOX.getChildren().get(0)).getImage());
        icon.setFitWidth(40);
        icon.setFitHeight(40);
        Text s = new Text(((Text) currentHBOX.getChildren().get(1)).getText());
        if(s.getText().toLowerCase().contains(App.resourceBundle.getString("key.back_right_turn"))) icon.setRotate(90);
        else if(s.getText().toLowerCase().contains(App.resourceBundle.getString("key.back_left_turn"))) icon.setRotate(270);
        else if(s.getText().toLowerCase().contains(App.resourceBundle.getString("key.backwards"))) icon.setRotate(180);
        currentDirectionDisplay.getChildren().setAll(icon,s);
    }

    private HBox createHBoxFromDirection(String direction) {
        ImageView icon;
        if(direction.toLowerCase().contains(App.resourceBundle.getString("key.slight_left_turn"))) {
            icon = new ImageView(new Image("edu/wpi/teamo/images/Icons/icons8-up-left-96.png"));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
        }
        else if(direction.toLowerCase().contains(App.resourceBundle.getString("key.slight_right_turn"))) {
            icon = new ImageView(new Image("edu/wpi/teamo/images/Icons/icons8-up-right-96.png"));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
        }
        else if(direction.toLowerCase().contains(App.resourceBundle.getString("key.back_left_turn"))) {
            icon = new ImageView(new Image("edu/wpi/teamo/images/Icons/icons8-up-left-96.png"));
            icon.setRotate(270);
            icon.setFitWidth(20);
            icon.setFitHeight(20);
        }
        else if(direction.toLowerCase().contains(App.resourceBundle.getString("key.back_right_turn"))) {
            icon = new ImageView(new Image("edu/wpi/teamo/images/Icons/icons8-up-right-96.png"));
            icon.setRotate(90);
            icon.setFitWidth(20);
            icon.setFitHeight(20);
        }
        else if(direction.toLowerCase().contains(App.resourceBundle.getString("key.left_turn"))) {
            icon = new ImageView(new Image("edu/wpi/teamo/images/Icons/icons8-up-sharp-left-96.png"));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
        }
        else if(direction.toLowerCase().contains(App.resourceBundle.getString("key.right_turn"))) {
            icon = new ImageView(new Image("edu/wpi/teamo/images/Icons/icons8-up-sharp-right-96.png"));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
        }
        else if(direction.toLowerCase().contains(App.resourceBundle.getString("key.backwards"))) {
            icon = new ImageView(new Image("edu/wpi/teamo/images/Icons/icons8-up-arrow-96.png"));
            icon.setRotate(180);
            icon.setFitWidth(20);
            icon.setFitHeight(20);
        }
        else{
            icon = new ImageView(new Image("edu/wpi/teamo/images/Icons/icons8-up-arrow-96.png"));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
        }
        return new HBox(icon, new Text(direction));
    }

    public void show() {
        textualWindow.setVisible(true);
    }

    public void hide() {
        textualWindow.setVisible(false);
    }

    public JFXButton getTextualUnitsBtn() {
        return textualUnitsBtn;
    }
}
