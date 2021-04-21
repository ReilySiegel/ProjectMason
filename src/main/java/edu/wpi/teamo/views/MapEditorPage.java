package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.NumberValidator;
import edu.wpi.teamo.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import edu.wpi.teamo.database.map.Edge;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.Node;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;

import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;

public class MapEditorPage extends SubPageController implements Initializable{

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTreeTableView<Node> treeView;

    @FXML
    private JFXTreeTableView<Edge> treeViewEdge;

    @FXML
    private JFXTextField addNodeID;

    @FXML
    private JFXTextField addNodeBuilding;

    @FXML
    private JFXTextField addNodeFloor;

    @FXML
    private JFXTextField addNodeType;

    @FXML
    private JFXTextField addNodeLN;

    @FXML
    private JFXTextField addNodeSN;

    @FXML
    private JFXTextField addNodeX;

    @FXML
    private JFXTextField addNodeY;

    @FXML
    private JFXTextField origNodeID;

    @FXML
    private JFXTextField newNodeID;

    @FXML
    private JFXTextField origNodeX;

    @FXML
    private JFXTextField origNodeY;

    @FXML
    private JFXTextField origNodeBuilding;

    @FXML
    private JFXTextField origNodeFloor;

    @FXML
    private JFXTextField origNodeType;

    @FXML
    private JFXTextField origNodeLN;

    @FXML
    private JFXTextField origNodeSN;

    @FXML
    private JFXButton editNodeSubmit;

    @FXML
    private JFXTextField deleteNodeID;

    @FXML
    private JFXTextField addEdgeID;

    @FXML
    private JFXTextField addNode1;

    @FXML
    private JFXTextField addNode2;

    @FXML
    private JFXTextField editEdgeID;

    @FXML
    private JFXTextField editNode1;

    @FXML
    private JFXTextField editNode2;

    @FXML
    private JFXTextField deleteEdgeID;

    @FXML
    private JFXTextField editingEdge;

    @FXML
    private JFXTextField editingStart;

    @FXML
    private JFXTextField editingEnd;

    @FXML
    private JFXTextArea mapText;

    @FXML
    private JFXComboBox<String> floorSwitcher;
    String selectedFloor = "1";

    @FXML
    private ImageView mapImage;

    @FXML
    private AnchorPane nodePane;

    @FXML
    JFXButton chooseStartButton;
    boolean selectingStart;

    @FXML
    JFXButton chooseEndButton;
    boolean selectingEnd;

    Map map;

    boolean treeInit = false;

    boolean treeEdgeInit = false;

    //TODO get rid of - add event listener
    @FXML
    void onFloorSwitch(ActionEvent event) {
        selectedFloor = floorSwitcher.getValue();
        updateMap();
    }

    //    Consumer<Pair<Integer, Integer>> onClickEdge = (Pair<Integer, Integer> coords) -> System.out.println("Edge " + node.getEdgeID() + "was clicked");
    void onClickMap(Pair<Integer, Integer> coords) {
        addNodeX.setText(String.valueOf(coords.getKey()));
        addNodeY.setText(String.valueOf(coords.getValue()));

        String autoGenName = String.format("Floor_%s_%d_%d", selectedFloor, coords.getKey(), coords.getValue());
        addNodeFloor.setText(selectedFloor);
        if (addNodeID.getText().isEmpty()) {
            addNodeID.setText(autoGenName);
        }
        if (addNodeBuilding.getText().isEmpty()) {
            addNodeBuilding.setText(String.format("Floor_%s_default", selectedFloor));
        }
        if (addNodeType.getText().isEmpty()) {
            addNodeType.setText("PARK");
        }
        if (addNodeLN.getText().isEmpty()) {
            addNodeLN.setText(autoGenName);
        }
        if (addNodeSN.getText().isEmpty()) {
            addNodeSN.setText(autoGenName);
        }
    }

//    Consumer<NodeInfo> onClickNode = (NodeInfo node) -> System.out.println("Node " + node.getNodeID() + "was clicked");
    void onClickNode(NodeInfo node) {
        origNodeID.setText(node.getNodeID());
        newNodeID.setText(node.getNodeID());
        origNodeX.setText(Integer.toString(node.getXPos()));
        origNodeY.setText(Integer.toString(node.getYPos()));
        origNodeBuilding.setText(node.getBuilding());
        origNodeFloor.setText(node.getFloor());
        origNodeType.setText(node.getNodeType());
        origNodeLN.setText(node.getLongName());
        origNodeSN.setText(node.getShortName());
        deleteNodeID.setText(node.getNodeID());

        if (selectingStart) {
            chooseStartButton.setDisable(false);
            addNode1.setText(node.getNodeID());
            selectingStart = false;
            addEdgeID.setText(node.getNodeID()+"_"+addNode2.getText());
        }
        if (selectingEnd) {
            chooseEndButton.setDisable(false);
            addNode2.setText(node.getNodeID());
            selectingEnd = false;
            addEdgeID.setText(addNode1.getText()+"_"+node.getNodeID());
        }
    }

    //    Consumer<EdgeInfo> onClickEdge = (EdgeInfo edge) -> System.out.println("Edge " + node.getEdgeID() + "was clicked");
    void onClickEdge(EdgeInfo edge) {
        editingEdge.setText(edge.getEdgeID());
        editEdgeID.setText(edge.getEdgeID());
        editNode1.setText(edge.getStartNodeID());
        editNode2.setText(edge.getEndNodeID());
        deleteEdgeID.setText(edge.getEdgeID());
    }

    private void handleChooseStart() {
        if (selectingStart) {
            selectingStart = false;
        }
        else {
            mapText.setText("Select your start location.");
            selectingStart = true;
            selectingEnd = false;
        }

        chooseStartButton.setDisable(selectingStart);
        chooseEndButton.setDisable(selectingEnd);
    }

    private void handleChoseEnd() {
        if (selectingEnd) {
            selectingEnd = false;
        }
        else {
            mapText.setText("Select your destination.");
            selectingStart = false;
            selectingEnd = true;
        }
        chooseEndButton.setDisable(selectingEnd);
        chooseStartButton.setDisable(selectingStart);
    }

    /**
     * Set validators to insure that the x and y coordinate fields are numbers
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){

        floorSwitcher.getItems().add("L2");
        floorSwitcher.getItems().add("L1");
        floorSwitcher.getItems().add("G");
        floorSwitcher.getItems().add("1");
        floorSwitcher.getItems().add("2");
        floorSwitcher.getItems().add("3");
        floorSwitcher.setValue(selectedFloor);

        chooseStartButton.setOnAction(event -> handleChooseStart());
        chooseEndButton.setOnAction(event -> handleChoseEnd());

        map = new Map(mapImage, nodePane, mapText, this::onClickNode, this::onClickEdge, this::onClickMap);

        NumberValidator numberValidator = new NumberValidator();

        //editNodeSubmit.setDisable(true);

        //Ensure that each X and Y field are numbers
        addNodeX.getValidators().add(numberValidator);
        //numberValidator.setMessage("Please enter a number");
        addNodeX.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    addNodeX.validate();
                }
            }
        });

        addNodeY.getValidators().add(numberValidator);
        addNodeY.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    addNodeY.validate();
                }
            }
        });

        origNodeX.getValidators().add(numberValidator);
        origNodeX.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    origNodeX.validate();
                }
            }
        });

        origNodeY.getValidators().add(numberValidator);
        origNodeY.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    origNodeY.validate();
                }
            }
        });


        //Set original node ID, X, and Y in the Edit and Delete box to selected value.
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Node>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Node>> observable, TreeItem<Node> oldValue, TreeItem<Node> newValue) {
                if(newValue != null){
                    origNodeID.setText(newValue.getValue().getNodeID());
                    newNodeID.setText(newValue.getValue().getNodeID());
                    origNodeX.setText(Integer.toString(newValue.getValue().getXPos()));
                    origNodeY.setText(Integer.toString(newValue.getValue().getYPos()));
                    origNodeBuilding.setText(newValue.getValue().getBuilding());
                    origNodeFloor.setText(newValue.getValue().getFloor());
                    origNodeType.setText(newValue.getValue().getNodeType());
                    origNodeLN.setText(newValue.getValue().getLongName());
                    origNodeSN.setText(newValue.getValue().getShortName());
                    deleteNodeID.setText(newValue.getValue().getNodeID());
                    //editNodeSubmit.setDisable(false);
                }
            }
        });

        //Set original node ID, X, and Y in the Edit and Delete box to selected value.
        treeViewEdge.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Edge>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Edge>> observable, TreeItem<Edge> oldValue, TreeItem<Edge> newValue) {
                if(newValue != null){
                    editingEdge.setText(newValue.getValue().getEdgeID());
                    editEdgeID.setText(newValue.getValue().getEdgeID());
                    editNode1.setText(newValue.getValue().getStartNodeID());
                    editNode2.setText(newValue.getValue().getEndNodeID());
                    deleteEdgeID.setText(newValue.getValue().getEdgeID());
                }
            }
        });


        //Update display at start so loaded database persists after switching pages
        updateNodeTreeDisplay();
        updateEdgeTreeDisplay();
    }

    /**
     * Event handler for adding a Node to the database
     * @param event
     */
    @FXML
    void handleAddSubmitNode(ActionEvent event) {
        String newNodeID = addNodeID.getText();
        String newNodeX = addNodeX.getText();
        String newNodeY = addNodeY.getText();
        String newNodeBuilding = addNodeBuilding.getText();
        String newNodeFloor = addNodeFloor.getText();
        String newNodeType = addNodeType.getText();
        String newNodeLN = addNodeLN.getText();
        String newNodeSN = addNodeSN.getText();


        try{
            App.mapService.addNode(newNodeID, Integer.parseInt(newNodeX), Integer.parseInt(newNodeY),
                    newNodeFloor, newNodeBuilding, newNodeType,
                    newNodeLN, newNodeSN);
            updateNodeTreeDisplay();

            addNodeID.setText("");
            addNodeX.setText("");
            addNodeY.setText("");
            addNodeBuilding.setText("");
            addNodeFloor.setText("");
            addNodeType.setText("");
            addNodeLN.setText("");
            addNodeSN.setText("");

        }
        catch(SQLException | AssertionError | IllegalArgumentException e){
            showError("Please fill out all fields with valid arguments");
        }

    }

    /**
     * Event handler for removing a Node from the database
     * @param event
     */
    @FXML
    void handleDeleteSubmitNode(ActionEvent event) {
        String deleteNode= deleteNodeID.getText();

        try{
            App.mapService.deleteNode(deleteNode);
            updateNodeTreeDisplay();
        }
        catch (SQLException | AssertionError | IllegalArgumentException e){
            showError("Please fill out all fields with valid arguments");
        }
    }

    /**
     * Event handler for writing node edits to the database
     * @param event
     */
    @FXML
    void handleEditSubmitNode(ActionEvent event) {
        String currentNodeID = origNodeID.getText();
        String editNodeID = newNodeID.getText();
        String newNodeX = origNodeX.getText();
        String newNodeY = origNodeY.getText();
        String newNodeBuild = origNodeBuilding.getText();
        String newNodeFloor = origNodeFloor.getText();
        String newNodeType = origNodeType.getText();
        String newNodeLN = origNodeLN.getText();
        String newNodeSN = origNodeSN.getText();


        try{
            if(App.mapService.nodeExists(editNodeID) && !currentNodeID.equals(editNodeID)){
                showError("The Node ID already exists.");
                return;
            }
            App.mapService.setNodePosition(currentNodeID, Integer.parseInt(newNodeX), Integer.parseInt(newNodeY));
            App.mapService.setNodeBuilding(currentNodeID, newNodeBuild);
            App.mapService.setNodeFloor(currentNodeID, newNodeFloor);
            App.mapService.setNodeType(currentNodeID, newNodeType);
            App.mapService.setNodeLongName(currentNodeID, newNodeLN);
            App.mapService.setNodeShortName(currentNodeID, newNodeSN);
            App.mapService.setNodeID(currentNodeID, editNodeID);

            updateNodeTreeDisplay();
        }
        catch (SQLException | IllegalArgumentException | AssertionError e){
            showError("Please fill out all fields with valid arguments");
        }
    }



    /**
     * handles editing an edge
     * @param event clicking submit button
     */
    @FXML
    void handleAddSubmitEdge(ActionEvent event) {
        String newEdgeID = addEdgeID.getText();
        String newEdgeNode1 = addNode1.getText();
        String newEdgeNode2 = addNode2.getText();

        // try/catch for SQL and adding an edge
        try {
            App.mapService.addEdge(newEdgeID, newEdgeNode1, newEdgeNode2);
        } catch (SQLException | IllegalArgumentException e) {
            showError("Please fill out all fields with valid arguments");
            return;
        }
        updateEdgeTreeDisplay();
    }

    /**
     * handles editing a edge
     *
     * @param event clicking submit button
     */
    @FXML
    void handleDeleteSubmitEdge(ActionEvent event) {
        String EdgetoDelete = deleteEdgeID.getText();

        // try/catch for SQL and deleting an edge
        try {
            App.mapService.deleteEdge(EdgetoDelete);
        } catch (SQLException | IllegalArgumentException e) {
            showError("Please fill out all fields with valid arguments");
            return;
        }
        // updating the edge display
        updateEdgeTreeDisplay();
    }

    /**
     * handles editing a edge
     *
     * @param event clicking submit button
     */
    @FXML
    void handleEditSubmitEdge(ActionEvent event) {
        String newEditEdgeID = editEdgeID.getText();
        String editEdgeNode1 = editNode1.getText();
        String editEdgeNode2 = editNode2.getText();

        // try catch for editing edge
        try {

            if(App.mapService.edgeExists(newEditEdgeID) && !newEditEdgeID.equals(editingEdge.getText())){
                showError("This Edge ID already exists.");
                return;
            }
            App.mapService.setEdgeID(editingEdge.getText(), newEditEdgeID);
            App.mapService.setEdgeStartID(newEditEdgeID, editEdgeNode1);
            App.mapService.setEdgeEndID(newEditEdgeID, editEdgeNode2);
        } catch (SQLException | IllegalArgumentException e) {
            showError("Please fill out all fields with valid arguments");
            return;
        }
        updateEdgeTreeDisplay();
    }

    /**
     * Event handler to load in and display a Node csv file
     * @param event
     */
    @FXML
    void handleLoadNodeCSV(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILES", "*.csv"));


        try{
            File f = fc.showOpenDialog(null);
            String path = f.getPath();
            App.mapService.loadNodesFromFile(path);
            //App.mapService.loadNodesFromFile("src/test/resources/edu/wpi/teamo/map/database/testNodes.csv");
        }
        catch(NullPointerException | FileNotFoundException | SQLException | IllegalArgumentException e){
            showError("Please select a valid file");
            return;
        }

        updateNodeTreeDisplay();
    }

    /**
     * Event handler for saving a csv file
     * @param event
     */
    @FXML
    void handleSaveNodeCSV(ActionEvent event){
        FileChooser fc = new FileChooser();
        fc.setTitle("Save CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILE", "*.csv"));

        try{
            File f = fc.showSaveDialog(null);
            String path = f.getPath();
            App.mapService.writeNodesToCSV(path);
        }
        catch(NullPointerException | SQLException | IOException e){
            showError("Save aborted!");
        }
    }

    /**
     * handles reading in CSV from file
     *
     * @param event click load button
     */
    @FXML
    void handleLoadEdgeCSV(ActionEvent event) {
        // reading in file
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILE", "*.csv"));

        // try catch for reading in file
        try {
            File f = fc.showOpenDialog(null);
            String path = f.getPath();
            App.mapService.loadEdgesFromFile(path);
        } catch (FileNotFoundException | SQLException | NullPointerException | IllegalArgumentException e)  {
            showError("Please select a valid file");
            return;
        }
        // updating display of all edges
        //updateDisplay();
        updateEdgeTreeDisplay();
    }

    @FXML
    void handleSaveEdgeCSV(ActionEvent event) {
        // choosing where to save
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILE", "*.csv"));

        // try catch for SQL/IO errors
        try {
            File f = fc.showSaveDialog(null);
            String path = f.getPath();
            App.mapService.writeEdgesToCSV(path);
        } catch (IOException | SQLException | NullPointerException e ) {
            showError("Save aborted!");
        }
    }

    @FXML
    void updateNodeTreeDisplay() {

        JFXTreeTableColumn<Node, String> nID = new JFXTreeTableColumn<>("Node ID");
        nID.setPrefWidth(125);
        nID.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getNodeID());
            return var;
        });

        JFXTreeTableColumn<Node, String> nX = new JFXTreeTableColumn<>("X");
        nX.setPrefWidth(125);
        nX.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(String.valueOf(param.getValue().getValue().getXPos()));
            return var;
        });

        JFXTreeTableColumn<Node, String> nY = new JFXTreeTableColumn<>("Y");
        nY.setPrefWidth(125);
        nY.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(String.valueOf(param.getValue().getValue().getYPos()));
            return var;
        });

        JFXTreeTableColumn<Node, String> nFloor = new JFXTreeTableColumn<>("Floor");
        nFloor.setPrefWidth(125);
        nFloor.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getFloor());
            return var;
        });

        JFXTreeTableColumn<Node, String> nBuilding = new JFXTreeTableColumn<>("Building");
        nBuilding.setPrefWidth(125);
        nBuilding.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getBuilding());
            return var;
        });

        JFXTreeTableColumn<Node, String> nType = new JFXTreeTableColumn<>("Node Type");
        nType.setPrefWidth(125);
        nType.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getNodeType());
            return var;
        });

        JFXTreeTableColumn<Node, String> nLongName = new JFXTreeTableColumn<>("Long Name");
        nLongName.setPrefWidth(125);
        nLongName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getLongName());
            return var;
        });

        JFXTreeTableColumn<Node, String> nShortName = new JFXTreeTableColumn<>("Short Name");
        nShortName.setPrefWidth(125);
        nShortName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getShortName());
            return var;
        });

        List<NodeInfo> nodeList = null;
        try {
            nodeList = App.mapService.getAllNodes().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ObservableList<Node> data = FXCollections.observableArrayList();

        //Display all Node ID, X, Y, Floor, Building, LongName, and ShortName
        for(int i = 0; i < nodeList.size(); i++){
            if(!nodeList.get(i).getNodeID().isEmpty()){
                data.add(new Node(nodeList.get(i).getNodeID(),nodeList.get(i).getXPos(),nodeList.get(i).getYPos(),
                        nodeList.get(i).getFloor(), nodeList.get(i).getBuilding(), nodeList.get(i).getNodeType(), nodeList.get(i).getLongName(),
                        nodeList.get(i).getShortName()));

            }
        }

        // checks to see if tree has been initialized and wont make dupe columns
        TreeItem<Node> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        if(!treeInit){
            treeView.getColumns().addAll(nID,nX,nY,nFloor,nBuilding,nType,nLongName,nShortName);
            treeInit = true;
        }

        treeView.setRoot(root);
        treeView.setShowRoot(false);
        updateMap();
    }

    @FXML
    void updateEdgeTreeDisplay() {

        JFXTreeTableColumn<Edge, String> eID = new JFXTreeTableColumn<>("Edge ID");
        eID.setPrefWidth(125);
        eID.setCellValueFactory((TreeTableColumn.CellDataFeatures<Edge, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getEdgeID());
            return var;
        });

        JFXTreeTableColumn<Edge, String> eStart = new JFXTreeTableColumn<>("Start Node");
        eStart.setPrefWidth(125);
        eStart.setCellValueFactory((TreeTableColumn.CellDataFeatures<Edge, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getStartNodeID());
            return var;
        });

        JFXTreeTableColumn<Edge, String> eEnd = new JFXTreeTableColumn<>("End Node");
        eEnd.setPrefWidth(125);
        eEnd.setCellValueFactory((TreeTableColumn.CellDataFeatures<Edge, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getEndNodeID());
            return var;
        });

        List<EdgeInfo> edgeList = null;

        try {
            edgeList = App.mapService.getAllEdges().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ObservableList<Edge> data = FXCollections.observableArrayList();

        //Display all Node ID, X, Y, Floor, Building, LongName, and ShortName
        for(int i = 0; i < edgeList.size(); i++){
            if(!edgeList.get(i).getEdgeID().isEmpty()){
                data.add(new Edge(edgeList.get(i).getEdgeID(),edgeList.get(i).getStartNodeID(),edgeList.get(i).getEndNodeID()));
            }
        }

        // checks to see if tree has been initialized and wont make dupe columns
        TreeItem<Edge> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        if(!treeEdgeInit){
            treeViewEdge.getColumns().addAll(eID,eStart, eEnd);
            treeEdgeInit = true;
        }
        treeViewEdge.setRoot(root);
        treeViewEdge.setShowRoot(false);
        updateMap();
    }

    @FXML
    void showError(String message) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Error"));
        content.setBody(new Text(message));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton("Close");
        closeButton.setStyle("-fx-background-color: #f40f19");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorWindow.close();
            }
        });

        content.setActions(closeButton);
        errorWindow.show();
    }

    void updateMap() {
        try {
            List<NodeInfo> nodeList = App.mapService.getAllNodes().collect(Collectors.toList());
            List<EdgeInfo> edgeList = App.mapService.getAllEdges().collect(Collectors.toList());
            map.drawFloor(nodeList, edgeList, selectedFloor);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Help - Map Editor"));
        content.setBody(new Text("*ADDING NODES*\n" +
                "To add a node to the map, switch to the \"Add\" tab on the right.\n" +
                "The Node ID field must be unique.\n" +
                "Clicking on an unoccupied area of the map will automatically fill in the necessary inputs.\n" +
                "In order for the node to be displayed, the floor field must be one of: L2, L1, G, 1, 2, 3\n" +
                "The X and Y fields must be numeric, the best way to set them is clicking the map.\n" +
                "Click \"Submit Node\" to add the node to the database.\n" +
                "\n*ADDING EDGES*:\n" +
                "To add a path (edge) between two nodes, first click the \"Choose Start\" button under the edge fields. \n" +
                "The button will become dark.  Click the desired starting node on the map, and it's ID will automatically be written to the start node field. \n" +
                "Repeat this process with the \"Choose End\" button to select the end node. An ID will be automatically generated. The ID can be changed, but must be unique." +
                "Click \"Submit Edge\", and the edge will be created.\n" +
                "\n*EDITING NODES AND EDGES*\n" +
                "To edit a node or edge, switch to the \"edit\" tab on the upper right sidebar.\n" +
                "Clicking a node or edge on the map will fill it's information into the editing fields.\n" +
                "Change the fields you would like to be changed, and click the submit button under those fields.\n" +
                "If you change the ID, it must be unique.\n" +
                "\n*DELETING NODES AND EDGES*\n" +
                "To delete a node or edge, switch to the \"Delete\" tab on the upper right. \n" +
                "Click the node or edge on the map that you wish to delete, and its ID will be filled into the input.\n" +
                "Click the delete button under that field, and it will be permanently deleted from the database."));


        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton("Close");
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