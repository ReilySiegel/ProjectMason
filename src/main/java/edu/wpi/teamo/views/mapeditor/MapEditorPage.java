package edu.wpi.teamo.views.mapeditor;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.algos.DFSManager;
import edu.wpi.teamo.database.map.EdgeInfo;

import edu.wpi.teamo.views.Map;
import edu.wpi.teamo.views.SubPageController;
import javafx.event.Event;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;

import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;

public class MapEditorPage extends SubPageController implements Initializable{

    @FXML
    private StackPane parentStackPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private Text nodeEditorHeadingText;

    @FXML
    private VBox nodeEditorWindow;

    @FXML
    private JFXTextField nodeEditorInputID;

    @FXML
    private JFXTextField nodeEditorInputX;

    @FXML
    private JFXTextField nodeEditorInputIY;

    @FXML
    private JFXTextField nodeEditorInputBuilding;

    @FXML
    private JFXTextField nodeEditorInputFloor;

    @FXML
    private JFXTextField nodeEditorInputType;

    @FXML
    private JFXTextField nodeEditorInputLN;

    @FXML
    private JFXTextField nodeEditorInputSN;

    @FXML
    private JFXButton nodeEditorSubmitButton;

    @FXML
    private JFXButton nodeEditorDeleteButton;

    @FXML
    private Text edgeEditorHeadingText;

    @FXML
    private VBox edgeEditorWindow;

    @FXML
    private JFXTextField edgeEditorInputID;

    @FXML
    private JFXTextField edgeEditorInputStartID;

    @FXML
    private JFXTextField edgeEditorInputEndID;

    @FXML
    private JFXButton edgeEditorSubmitButton;

    @FXML
    private JFXButton edgeEditorDeleteButton;

    @FXML
    private JFXButton loadEdgeCSVButton;

    @FXML
    private JFXButton saveEdgeCSVButton;

    @FXML
    private JFXButton loadNodeCSVButton;

    @FXML
    private JFXButton saveNodeCSVButton;

    @FXML
    private JFXButton helpButton;

    @FXML
    private JFXButton troubleshootButton;

    @FXML
    private StackPane tableWindow;

    @FXML
    private JFXButton openTableButton;

    @FXML
    private JFXComboBox<String> floorSwitcher;
    String selectedFloor = edu.wpi.teamo.views.Map.floor1Key;

    @FXML
    private AnchorPane nodePane;

    @FXML
    private JFXTextField tableSearchBar;

    @FXML
    private JFXComboBox<String> tableTypeBox;

    @FXML
    private JFXComboBox<String> tableFloorFilter;

    @FXML
    private JFXComboBox<String> tableValidityFilter;

    @FXML
    private JFXListView<HBox> tableView;

    private NodeTable nodeTable;
    private EdgeTable edgeTable;

    private static final String tableEdgeKey = "Edge";
    private static final String tableNodeKey = "Node";
    public static final String allFloorsKey = "All";
    public static final String invalidKey = "Invalid";
    public static final String allValidityKey = "All";
    public static final String validKey   = "Valid";

    edu.wpi.teamo.views.Map map;
    boolean dragging = false;

    NodeInfo addingEdgeStartNode = null;
    Line addingEdgeLine = null;
    boolean addingEdge = false;

    //TODO remove this
    EdgeInfo edgeBeingEdited;
    NodeInfo nodeBeingEdited;

    /* these hash maps are only for moving an associated edge when a node is being dragged */
    HashMap<String, List<Line>> associatedEdgeBeginnings;
    HashMap<String, List<Line>> associatedEdgeEndings;

    AlignmentTool alignmentTool = null;

    /**
     * Set validators to insure that the x and y coordinate fields are numbers
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        gridPane.setPickOnBounds(false);

        floorSwitcher.setOnAction(this::onFloorSwitch);
        floorSwitcher.getItems().add(edu.wpi.teamo.views.Map.floorL2Key);
        floorSwitcher.getItems().add(edu.wpi.teamo.views.Map.floorL1Key);
        floorSwitcher.getItems().add(edu.wpi.teamo.views.Map.floorGKey);
        floorSwitcher.getItems().add(edu.wpi.teamo.views.Map.floor1Key);
        floorSwitcher.getItems().add(edu.wpi.teamo.views.Map.floor2Key);
        floorSwitcher.getItems().add(edu.wpi.teamo.views.Map.floor3Key);
        floorSwitcher.setValue(selectedFloor);

        map = new edu.wpi.teamo.views.Map(nodePane);
        map.setOnMapClicked(this::onMapClicked);
        map.setOnDrawNode(this::onDrawNode);
        map.setOnDrawEdge(this::onDrawEdge);
        map.setOnMouseMoved(this::onMouseMoved);

        helpButton.setOnAction(this::handleHelp);
//        exitButton.setOnAction(this::backToMain);
        nodeEditorSubmitButton.setOnAction(this::handleNodeEditSubmit);
        edgeEditorSubmitButton.setOnAction(this::handleEdgeEditSubmit);
        nodeEditorDeleteButton.setOnAction(this::handleNodeEditDelete);
        edgeEditorDeleteButton.setOnAction(this::handleEdgeEditDelete);
        troubleshootButton.setOnAction(this::handleTroubleshootButton);
        loadNodeCSVButton.setOnAction(this::handleLoadNodeCSV);
        loadEdgeCSVButton.setOnAction(this::handleLoadEdgeCSV);
        saveNodeCSVButton.setOnAction(this::handleSaveNodeCSV);
        saveEdgeCSVButton.setOnAction(this::handleSaveEdgeCSV);
        tableTypeBox.setOnAction(this::handleTableTypeSwitch);
        openTableButton.setOnAction(this::handleToggleTable);

        nodeEditorWindow.setVisible(false);
        edgeEditorWindow.setVisible(false);
        closeTable();

        nodeTable = new NodeTable(tableSearchBar, tableView, this::update, App.mapService);
        edgeTable = new EdgeTable(tableSearchBar, tableView, this::update, App.mapService);
        edgeTable.setValidityFilter(tableValidityFilter);
        nodeTable.setValidityFilter(tableValidityFilter);
        nodeTable.setFloorFilter(tableFloorFilter);
        edgeTable.setFloorFilter(tableFloorFilter);

        /* if we want to use the search bar for both we must set the handler here */
        tableSearchBar.setOnKeyTyped(event -> {
            nodeTable.update();
            edgeTable.update();
        });

        tableTypeBox.getItems().add(tableNodeKey);
        tableTypeBox.getItems().add(tableEdgeKey);
        tableTypeBox.setValue(tableNodeKey);
        edgeTable.setEnabled(false);

        tableValidityFilter.setOnAction(event -> { nodeTable.update(); edgeTable.update(); });
        tableValidityFilter.getItems().add(allValidityKey);
        tableValidityFilter.getItems().add(invalidKey);
        tableValidityFilter.getItems().add(validKey);
        tableValidityFilter.setValue(allValidityKey);

        tableFloorFilter.setOnAction(event -> { nodeTable.update(); edgeTable.update(); });
        tableFloorFilter.getItems().add(edu.wpi.teamo.views.Map.floorL2Key);
        tableFloorFilter.getItems().add(edu.wpi.teamo.views.Map.floorL1Key);
        tableFloorFilter.getItems().add(edu.wpi.teamo.views.Map.floorGKey);
        tableFloorFilter.getItems().add(edu.wpi.teamo.views.Map.floor1Key);
        tableFloorFilter.getItems().add(edu.wpi.teamo.views.Map.floor2Key);
        tableFloorFilter.getItems().add(edu.wpi.teamo.views.Map.floor3Key);
        tableFloorFilter.getItems().add(allFloorsKey);
        tableFloorFilter.setValue(allFloorsKey);

        App.getPrimaryStage().getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                edgeEditorWindow.setVisible(false);
                nodeEditorWindow.setVisible(false);
                closeTable();
            }
        });

        update();
    }

    private void handleTroubleshootButton(ActionEvent actionEvent) {
        List<String> newInvalidNodeIDs = new LinkedList<>();
        List<String> oldInvalidNodeIDs = queryAllNodes().filter(node -> !node.isValid())
                                                            .map(NodeInfo::getNodeID)
                                                            .collect(Collectors.toList());

        List<AlgoNode> isolatedNodes     = findIsolatedNodes();
        List<NodeInfo> invalidFloorNodes = findInvalidFloorNodes().collect(Collectors.toList());
        List<NodeInfo> outOfBoundsNodes  = findOutOfBoundsNodes().collect(Collectors.toList());

        Consumer<String> addIfNotThere = (string) -> {
            if (!newInvalidNodeIDs.contains(string)) newInvalidNodeIDs.add(string);
        };
        isolatedNodes.forEach    (node -> addIfNotThere.accept(node.getID()));
        invalidFloorNodes.forEach(node -> addIfNotThere.accept(node.getNodeID()));
        outOfBoundsNodes.forEach (node -> addIfNotThere.accept(node.getNodeID()));

        newInvalidNodeIDs.forEach(id -> setNodeValidity(id, false));

        for (String id : oldInvalidNodeIDs) {
            if (!newInvalidNodeIDs.contains(id)) {
                setNodeValidity(id, true);
            }
        }

        update();
    }

    private Stream<NodeInfo> findOutOfBoundsNodes() {
        Stream<NodeInfo> nodes = queryAllNodes();
        return nodes.filter(node -> !edu.wpi.teamo.views.Map.isWithinMapBounds(node.getXPos(), node.getYPos()));
    }

    private Stream<NodeInfo> findInvalidFloorNodes() {
        Stream<NodeInfo> nodes = queryAllNodes();
        List<String> validFloors = Arrays.asList(Map.floorKeys);
        return nodes.filter(node -> !validFloors.contains(node.getFloor()));
    }

    private List<AlgoNode> findIsolatedNodes() {
        try {
            return (new DFSManager(App.mapService)).getIsolatedNodes();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new LinkedList<>();
        }
    }

    private void setNodeValidity(String id, boolean valid) {
        try {
            App.mapService.setNodeValid(id, valid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void handleTableTypeSwitch(Event event) {
        String value = tableTypeBox.getValue();
        switch (value) {
            case tableNodeKey:
                edgeTable.setEnabled(false);
                nodeTable.setEnabled(true);
                updateNodeTable();
                break;
            case tableEdgeKey:
                nodeTable.setEnabled(false);
                edgeTable.setEnabled(true);
                updateEdgeTable();
                break;
            default:
                throw new Error("Map editor table combobox has invalid values.");
        }
    }

    private void closeTable() {
        openTableButton.setText(App.resourceBundle.getString("key.open_table"));
        tableWindow.setVisible(false);
    }

    private void openTable() {
        openTableButton.setText(App.resourceBundle.getString("key.close_table"));
        tableWindow.setVisible(true);
    }

    private void handleToggleTable(ActionEvent actionEvent) {
        if (tableWindow.isVisible()) {
            closeTable();
        }
        else {
            openTable();
        }
    }

    private void handleNodeEditDelete(ActionEvent actionEvent) {
        if (nodeBeingEdited != null) {
            confirmDeleteNode(nodeBeingEdited);
            nodeBeingEdited = null;
        }
        nodeEditorWindow.setVisible(false);
    }

    private void handleEdgeEditDelete(ActionEvent actionEvent) {
        if (edgeBeingEdited != null) {
            confirmDeleteEdge(edgeBeingEdited);
            edgeBeingEdited = null;
        }
        edgeEditorWindow.setVisible(false);
    }

    void update() {
        updateNodeTable();
        updateEdgeTable();
        updateMap();
    }

    void updateMap() {
        associatedEdgeBeginnings = new HashMap<>();
        associatedEdgeEndings = new HashMap<>();

        List<NodeInfo> nodeList = queryAllNodes().collect(Collectors.toList());
        List<EdgeInfo> edgeList = queryAllEdges().collect(Collectors.toList());

        map.clearShapes();
        map.drawEdges(nodeList, edgeList, selectedFloor, "map-editor-edge");
        map.drawNodes(nodeList, selectedFloor, "map-editor-node");
    }

    void onFloorSwitch(ActionEvent event) {
        selectedFloor = floorSwitcher.getValue();
        updateMap();
    }

    /* gets cast to a consumer */
    void onMapClicked(MouseEvent e) {
        double paneX = e.getX();
        double paneY = e.getY();

        int mapX = map.paneToMapX(e.getX());
        int mapY = map.paneToMapY(e.getY());

        switch (e.getButton()) {
            case PRIMARY: {
                if (addingEdge) {
                    nodePane.getChildren().remove(addingEdgeLine);
                    addingEdgeStartNode = null;
                    addingEdgeLine = null;
                    addingEdge = false;
                }
                else {
                    edgeEditorWindow.setVisible(false);
                    nodeEditorWindow.setVisible(false);
                }
                break;
            }
            case SECONDARY: {
                mapContextMenu(e, mapX, mapY);
            }
        }
    }

    void handleAlignNode(Circle circle, NodeInfo node) {
        if (alignmentTool == null) {
            alignmentTool = new AlignmentTool(associatedEdgeBeginnings,
                                              associatedEdgeEndings,
                                              circle.getCenterX(),
                                              circle.getCenterY(),
                                              App.mapService,
                                              map);
        }
        alignmentTool.addAlignee(circle, node);
    }

    void handleConfirmAlignment() {
        try {
            alignmentTool.confirmAlignment();
        } catch (Exception throwables) {
            App.showError("Alignment failed: " + throwables.getMessage(), parentStackPane);
        }
        finally {
            clearAlignmentTool();
        }
    }

    void clearAlignmentTool() {
        alignmentTool = null;
        update();
    }

    void onMouseMoved(Pair<Double, Double> p) {
        double x = p.getKey();
        double y = p.getValue();
        if (addingEdgeLine != null) {
            addingEdgeLine.setEndX(x);
            addingEdgeLine.setEndY(y);
        }
        else if (alignmentTool != null) {
            alignmentTool.onMouseMoved(x, y);
        }
    }

    void onDrawNode(Pair<Circle, NodeInfo> p) {
            Circle circle = p.getKey();
            NodeInfo node = p.getValue();

            if (!node.isValid()) {
                circle.setFill(Color.RED);
                circle.setRadius(circle.getRadius() * 2);
            }

            circle.setOnMouseEntered(event -> {
                circle.setRadius(circle.getRadius() * 2);
                event.consume();
            });

            circle.setOnMouseExited(event -> {
                circle.setRadius(circle.getRadius() / 2);
                event.consume();
            });

            circle.setOnMouseClicked((MouseEvent e) -> {
                onClickNode(e, circle, node);
                e.consume();
            });

            circle.setOnMouseDragged((MouseEvent e) -> {
                if (alignmentTool == null) onDraggingNode(node, circle, e);
                e.consume();
            });

            circle.setOnMouseReleased((MouseEvent e) -> {
                if (dragging) {
                    onDraggedNodeDrop(node, e);
                }
                e.consume();
            });
    }

    private void nodeContextMenu(MouseEvent e, Circle circle, NodeInfo node) {
        ContextMenu menu = new ContextMenu();

        MenuItem addEdgeItem = new MenuItem(App.resourceBundle.getString("key.add_edge"));
        addEdgeItem.setOnAction(event -> handleAddingEdge(circle, node));

        String alignmentKey = (alignmentTool == null) ? "key.begin_alignment" : "key.add_to_alignment";
        MenuItem alignmentItem = new MenuItem(App.resourceBundle.getString(alignmentKey));
        alignmentItem.setOnAction(event -> handleAlignNode(circle, node));

        MenuItem deleteItem = new MenuItem(App.resourceBundle.getString("key.delete"));
        deleteItem.setOnAction(event -> confirmDeleteNode(node));

        menu.getItems().add(addEdgeItem);
        menu.getItems().add(alignmentItem);
        menu.getItems().add(deleteItem);
        menu.show(nodePane.getScene().getWindow(), e.getScreenX(), e.getScreenY());
    }

    private void edgeContextMenu(MouseEvent e, EdgeInfo edge) {
        ContextMenu menu = new ContextMenu();

        MenuItem deleteItem = new MenuItem(App.resourceBundle.getString("key.delete"));
        deleteItem.setOnAction(event -> confirmDeleteEdge(edge));

        menu.getItems().add(deleteItem);
        menu.show(nodePane.getScene().getWindow(), e.getScreenX(), e.getScreenY());
    }

    private void mapContextMenu(MouseEvent e, Integer x, Integer y) {
        ContextMenu menu = new ContextMenu();

        MenuItem addNodeItem = new MenuItem(App.resourceBundle.getString("key.add_node"));
        addNodeItem.setOnAction(event -> handleAddingNode(x, y));

        MenuItem confirmAlignmentItem = new MenuItem(App.resourceBundle.getString("key.confirm_alignment"));
        confirmAlignmentItem.setOnAction(event -> handleConfirmAlignment());

        MenuItem cancelAlignmentItem = new MenuItem(App.resourceBundle.getString("key.cancel_alignment"));
        cancelAlignmentItem.setOnAction(event -> clearAlignmentTool());

        if (alignmentTool != null) {
            menu.getItems().add(confirmAlignmentItem);
            menu.getItems().add(cancelAlignmentItem);
        }
        else {
            menu.getItems().add(addNodeItem);
        }

        menu.show(
            nodePane.getScene().getWindow(),
            e.getScreenX(),
            e.getScreenY()
        );
    }

    //    Consumer<NodeInfo> onClickNode = (NodeInfo node) -> System.out.println("Node " + node.getNodeID() + "was clicked");
    void onClickNode(MouseEvent e, Circle circle, NodeInfo node) {
        if (addingEdge && addingEdgeStartNode != null) {
            addEdge(addingEdgeStartNode, node);
            addingEdge = false;
            addingEdgeStartNode = null;
            nodePane.getChildren().remove(addingEdgeLine);
        }
        else if (e.getButton() == MouseButton.SECONDARY) {
            nodeContextMenu(e, circle, node);
        }
        else if (alignmentTool == null) {
            openNodeEditor(node);
        }
    }

    private void openNodeEditor(NodeInfo node) {
        nodeEditorHeadingText.setText(App.resourceBundle.getString("key.editing_node"));
        nodeEditorInputX.setText(Integer.toString(node.getXPos()));
        nodeEditorInputIY.setText(Integer.toString(node.getYPos()));
        nodeEditorInputBuilding.setText(node.getBuilding());
        nodeEditorInputType.setText(node.getNodeType());
        nodeEditorInputSN.setText(node.getShortName());
        nodeEditorInputLN.setText(node.getLongName());
        nodeEditorInputFloor.setText(node.getFloor());
        nodeEditorInputID.setText(node.getNodeID());
        nodeEditorWindow.setVisible(true);
        edgeEditorWindow.setVisible(false);
        nodeBeingEdited = node;
    }

    private void onDrawEdge(Pair<Line, EdgeInfo> p) {
        EdgeInfo edge = p.getValue();
        Line line = p.getKey();

        line.setOnMouseClicked(event -> {
            onClickEdge(event, edge);
            event.consume();
        });

        line.setOnMouseEntered(event -> {
            line.setStrokeWidth(line.getStrokeWidth() * 2);
            event.consume();
        });

        line.setOnMouseExited(event -> {
            line.setStrokeWidth(line.getStrokeWidth() / 2);
            event.consume();
        });

        /* add the line to node id hashmaps so that this line can be referenced when the node is dragged */
        try {
            associatedEdgeBeginnings.computeIfAbsent(edge.getStartNodeID(), k -> new LinkedList<>());
            associatedEdgeBeginnings.get(edge.getStartNodeID()).add(line);

            associatedEdgeEndings.computeIfAbsent(edge.getEndNodeID(), k -> new LinkedList<>());
            associatedEdgeEndings.get(edge.getEndNodeID()).add(line);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    void onDraggingNode(NodeInfo node, Circle circle, MouseEvent e) {
        dragging = true;

        /* move the circle */
        circle.setCenterX(e.getX());
        circle.setCenterY(e.getY());

        /* move connected edges */
        try {
            List<Line> lineBeginnings = associatedEdgeBeginnings.get(node.getNodeID());
            if (lineBeginnings != null) {
                for (Line line : lineBeginnings) {
                    line.setStartX(e.getX());
                    line.setStartY(e.getY());
                }
            }
            List<Line> lineEndings = associatedEdgeEndings.get(node.getNodeID());
            if (lineEndings != null) {
                for (Line line : lineEndings) {
                    line.setEndX(e.getX());
                    line.setEndY(e.getY());
                }
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }

    }

    void onDraggedNodeDrop(NodeInfo node, MouseEvent e) {
        int mapX = map.paneToMapX(e.getX());
        int mapY = map.paneToMapY(e.getY());
        try {
            App.mapService.setNodePosition(node.getNodeID(), mapX, mapY);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        nodeEditorWindow.setVisible(false);
        dragging = false;
        update();
    }

    //    Consumer<EdgeInfo> onClickEdge = (EdgeInfo edge) -> System.out.println("Edge " + node.getEdgeID() + "was clicked");
    void onClickEdge(MouseEvent e, EdgeInfo edge) {
        if (e.getButton() == MouseButton.SECONDARY) {
            edgeContextMenu(e, edge);
        }
        else if (alignmentTool == null) {
            openEdgeEditor(edge);
        }
    }

    private void openEdgeEditor(EdgeInfo edge) {
        edgeEditorHeadingText.setText(App.resourceBundle.getString("key.editing_edge"));
        edgeEditorInputStartID.setText(edge.getStartNodeID());
        edgeEditorInputEndID.setText(edge.getEndNodeID());
        edgeEditorInputID.setText(edge.getEdgeID());
        nodeEditorWindow.setVisible(false);
        edgeEditorWindow.setVisible(true);
        edgeBeingEdited = edge;
    }

    private void addNode(String id, int x, int y, String fl, String bu, String ty, String ln, String sn) throws SQLException {
        App.mapService.addNode(id, x, y, fl, bu, ty, ln, sn);
        update();
    }

    private void confirmDeleteNode(NodeInfo node) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.confirm_node_deletion")));
        content.setBody(new Text(App.resourceBundle.getString("key.deleting_a_node") +
                App.resourceBundle.getString("key.ID_semicolon")+ node.getNodeID() + "\n" +
                App.resourceBundle.getString("key.name_semicolon") + node.getLongName() + "\n"));
        JFXDialog deleteConfirmationWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton cancelButton = new JFXButton(App.resourceBundle.getString("key.cancel"));
        cancelButton.setStyle("-fx-background-color: #004cff; -fx-text-fill: #ffffff");
        cancelButton.setOnAction(event -> deleteConfirmationWindow.close());

        JFXButton deleteButton = new JFXButton(App.resourceBundle.getString("key.delete"));
        deleteButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #ffffff");
        deleteButton.setOnAction(event -> {
            try {
                deleteNode(node.getNodeID());
            } catch (SQLException throwables) {
                App.showError(App.resourceBundle.getString("key.there_was_a_delete_error"), parentStackPane);
                throwables.printStackTrace();
            }
            deleteConfirmationWindow.close();
        });

        content.setActions(cancelButton);
        content.setActions(deleteButton);
        deleteConfirmationWindow.show();
    }

    private void confirmDeleteEdge(EdgeInfo edge) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.confirm_edge_deletion")));
        content.setBody(new Text(App.resourceBundle.getString("key.deleting_an_edge") +
                App.resourceBundle.getString("key.ID_semicolon") + edge.getEdgeID() + "\n"));
        JFXDialog deleteConfirmationWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton cancelButton = new JFXButton(App.resourceBundle.getString("key.cancel"));
        cancelButton.setStyle("-fx-background-color: #004cff; -fx-text-fill: #ffffff");
        cancelButton.setOnAction(event -> deleteConfirmationWindow.close());

        JFXButton deleteButton = new JFXButton(App.resourceBundle.getString("key.delete"));
        deleteButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #ffffff");
        deleteButton.setOnAction(event -> {
            try {
                deleteEdge(edge.getEdgeID());
            } catch (SQLException throwables) {
                App.showError(App.resourceBundle.getString("key.there_was_a_delete_error"), parentStackPane);
                throwables.printStackTrace();
            }
            deleteConfirmationWindow.close();
        });


        content.setActions(cancelButton);
        content.setActions(deleteButton);
        deleteConfirmationWindow.show();
    }

    private void handleAddingNode(Integer x, Integer y) {
        String defaultBulding = "building";
        String defaultType = "DEFAULT";
        String id = selectedFloor + "_" + x + "_" + y + "_" + "default";

        int count = 0;
        String numberedID = id;
        while (App.mapService.nodeExists(numberedID) && count < 1000) {
            numberedID = id + count;
            ++count;
        }
        id = numberedID;

        try {
            addNode(id, x, y, selectedFloor, defaultBulding, defaultType, id, id);
        } catch (SQLException throwables) {
            App.showError(App.resourceBundle.getString("key.error_saving_node"), parentStackPane);
            throwables.printStackTrace();
        }
    }

    void handleAddingEdge(Circle circle, NodeInfo node) {
        addingEdgeLine = new Line(circle.getCenterX(), circle.getCenterY(), circle.getCenterX(), circle.getCenterY());
        addingEdgeLine.setStroke(Color.RED);
        addingEdgeLine.setStrokeWidth(2);
        addingEdgeLine.setMouseTransparent(true);
        addingEdgeStartNode = node;
        addingEdge = true;
        nodePane.getChildren().add(addingEdgeLine);
    }

    void addEdge(NodeInfo startNode, NodeInfo endNode) {
        String startID = startNode.getNodeID();
        String endID = endNode.getNodeID();
        String edgeID = startID + "_" + endID;
        addEdge(edgeID, startID, endID);
    }

    void addEdge(String edgeID, String startNodeID, String endNodeID) {
        try {
            App.mapService.addEdge(edgeID, startNodeID, endNodeID);
        } catch (SQLException | IllegalArgumentException e) {
            App.showError(App.resourceBundle.getString("key.error_saving_edge"), parentStackPane);
        }
        update();
    }

    void deleteNode(String nodeID) throws SQLException {
        App.mapService.deleteNode(nodeID);
        update();
    }

    void deleteEdge(String edgeID) throws SQLException {
        App.mapService.deleteEdge(edgeID);
        update();
    }

    void handleNodeEditSubmit(ActionEvent event) {
        String newNodeBuild = nodeEditorInputBuilding.getText();
        String newNodeFloor = nodeEditorInputFloor.getText();
        String newNodeType = nodeEditorInputType.getText();
        String currentNodeID = nodeEditorInputID.getText();
        String origNodeID = nodeBeingEdited.getNodeID();
        String newNodeLN = nodeEditorInputLN.getText();
        String newNodeSN = nodeEditorInputSN.getText();
        String newNodeY = nodeEditorInputIY.getText();
        String newNodeX = nodeEditorInputX.getText();

        if (!currentNodeID.equals(nodeBeingEdited.getNodeID())) {
            if (!App.mapService.nodeExists(currentNodeID)) {
                try {
                    App.mapService.setNodeID(origNodeID, currentNodeID);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            else {
                App.showError(App.resourceBundle.getString("key.node_id_exists"), parentStackPane);
            }
        }

        try{
            App.mapService.setNodePosition(currentNodeID, Integer.parseInt(newNodeX), Integer.parseInt(newNodeY));
            App.mapService.setNodeBuilding(currentNodeID, newNodeBuild);
            App.mapService.setNodeFloor(currentNodeID, newNodeFloor);
            App.mapService.setNodeType(currentNodeID, newNodeType);
            App.mapService.setNodeLongName(currentNodeID, newNodeLN);
            App.mapService.setNodeShortName(currentNodeID, newNodeSN);
            nodeEditorWindow.setVisible(false);
            nodeBeingEdited = null;
        }
        catch (SQLException | IllegalArgumentException | AssertionError e){
            e.printStackTrace();
            App.showError(App.resourceBundle.getString("key.please_fill_out_all_fields_with_valid_arguments"), parentStackPane);
        }

        update();
    }

    void handleEdgeEditSubmit(ActionEvent event) {
        String newEditEdgeID = edgeEditorInputID.getText();
        String editEdgeNode1 = edgeEditorInputStartID.getText();
        String editEdgeNode2 = edgeEditorInputEndID.getText();

        try {
            if (App.mapService.edgeExists(newEditEdgeID) && !newEditEdgeID.equals(edgeBeingEdited.getEdgeID())) {
                App.showError(App.resourceBundle.getString("key.edge_id_exists"), parentStackPane);
            }
            else {
                App.mapService.setEdgeID(edgeBeingEdited.getEdgeID(), newEditEdgeID);
                App.mapService.setEdgeStartID(newEditEdgeID, editEdgeNode1);
                App.mapService.setEdgeEndID(newEditEdgeID, editEdgeNode2);

                edgeEditorWindow.setVisible(false);
                edgeBeingEdited = null;

            }
        } catch (SQLException | IllegalArgumentException e) {
            App.showError(App.resourceBundle.getString("key.please_fill_out_all_fields_with_valid_arguments"), parentStackPane);
            return;
        }
        update();
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
        catch(IOException | NullPointerException | SQLException | IllegalArgumentException e){
            App.showError(App.resourceBundle.getString("key.select_a_valid_file"), parentStackPane);
            return;
        }

        update();
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
            App.showError(App.resourceBundle.getString("key.save_aborted"), parentStackPane);
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
        } catch (IOException | SQLException | NullPointerException | IllegalArgumentException e)  {
            App.showError(App.resourceBundle.getString("key.select_a_valid_file"), parentStackPane);
            return;
        }
        update();
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
            App.showError(App.resourceBundle.getString("key.save_aborted"), parentStackPane);
        }
    }

    private Stream<NodeInfo> queryAllNodes() {
        try {
            return App.mapService.getAllNodes();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return Stream.empty();
        }
    }

    private Stream<EdgeInfo> queryAllEdges() {
        try {
            return App.mapService.getAllEdges();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return Stream.empty();
        }
    }

    @FXML
    void updateNodeTable() {
        List<NodeInfo> nodes = queryAllNodes().collect(Collectors.toList());
        nodeTable.setItems(nodes);
    }

    @FXML
    void updateEdgeTable() {
        List<EdgeInfo> edges = queryAllEdges().collect(Collectors.toList());
        edgeTable.setItems(edges);
    }

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help_map_editor")));
        content.setBody(new Text(App.resourceBundle.getString("key.help_map_editor_text")));

        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setStyle("-jfx-button-type: RAISED");

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