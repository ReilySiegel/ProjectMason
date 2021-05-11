package edu.wpi.teamo.views.pathfindingpage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.teamo.App;
import edu.wpi.teamo.algos.AlgoNode;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class PathDisplayControls {
    private final VBox pathDisplayControlWindow;
    private final TextualDirections textualDirections;
    private final JFXButton backwardStepButton;
    private final JFXButton forwardStepButton;
    private final JFXComboBox<String> floorComboBox;
    private final HBox pathStepHBox;

    private List<AlgoNode> displayedPath = null;

    int directionIterator = 0;
    int directionMax = 0;
    int directionMin = 0;
    Runnable onStep;

    public PathDisplayControls(VBox pathDisplayControlWindow,
                               JFXButton forwardStepButton,
                               JFXButton backwardStepButton,
                               TextualDirections textualDirections,
                               HBox pathStepHBox,
                               JFXComboBox<String> floorComboBox,
                               Runnable onStep) {
        this.pathDisplayControlWindow = pathDisplayControlWindow;
        this.backwardStepButton = backwardStepButton;
        this.textualDirections = textualDirections;
        this.forwardStepButton = forwardStepButton;
        this.floorComboBox = floorComboBox;
        this.pathStepHBox = pathStepHBox;
        this.onStep = onStep;

        backwardStepButton.setOnAction(this::handleStepBack);
        forwardStepButton.setOnAction(this::handleStepForward);
        textualDirections.getTextualUnitsBtn().setOnAction(this::handleUnitSwitch);
    }

    public void setDisplayedPath(List<AlgoNode> displayedPath) {
        this.displayedPath = displayedPath;

        textualDirections.loadDirections(displayedPath);
        textualDirections.update(0, floorComboBox.getValue());
        show();

        directionIterator = 0;
        directionMax = displayedPath.size();
    }

    public void handleStepForward(ActionEvent e)
    {
        if(directionIterator<directionMax-1) {
            stepDirection(true);
        }
    }

    public void handleStepBack(ActionEvent e)
    {
        if(directionIterator>directionMin) {
            stepDirection(false);
        }
    }

    private void stepDirection(boolean forward)
    {
        if (displayedPath == null || displayedPath.size() == 0) return;

        if (forward)
        {
            directionIterator++;
        }
        else {
            directionIterator--;
        }

        String floor = displayedPath.get(directionIterator).getFloor();
        floorComboBox.setValue(floor);

        textualDirections.update(directionIterator, floor);

        if (onStep != null) onStep.run();
    }

    public void setPathStepButtonVisibility(boolean visible) {
        forwardStepButton.setVisible(visible);
        backwardStepButton.setManaged(visible);
        forwardStepButton.setVisible(visible);
        backwardStepButton.setManaged(visible);
        pathStepHBox.setVisible(visible);
        pathStepHBox.setManaged(visible);
    }

    public void hide() {
        pathDisplayControlWindow.setVisible(false);
        setPathStepButtonVisibility(false);
    }

    public void show() {
        pathDisplayControlWindow.setVisible(true);
        setPathStepButtonVisibility(true);
    }

    public AlgoNode getNodeInFocus() {
        return displayedPath.get(directionIterator);
    }

    public int getDirectionIterator() {
        return directionIterator;
    }

    private void handleUnitSwitch(ActionEvent e) {
        textualDirections.toggleUnit(displayedPath, directionIterator, floorComboBox.getValue());
    }
}
