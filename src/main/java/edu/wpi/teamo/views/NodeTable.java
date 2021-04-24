package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.database.map.Node;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NodeTable {

    JFXTreeTableView<Node> tree;

    public NodeTable(JFXTreeTableView<Node> nodeTree) {
        tree = nodeTree;
        initColumns();
    }

    public void initClickListener(
                    JFXTextField origNodeID,
                    JFXTextField newNodeID,
                    JFXTextField origNodeX,
                    JFXTextField origNodeY,
                    JFXTextField origNodeBuilding,
                    JFXTextField origNodeFloor,
                    JFXTextField origNodeType,
                    JFXTextField origNodeLN,
                    JFXTextField origNodeSN,
                    JFXTextField deleteNodeID
    ) {
        //Set original node ID, X, and Y in the Edit and Delete box to selected value.
        tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Node>>() {
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
    }

    public void update(Stream<NodeInfo> nodeStream) {
        if (nodeStream == null) return;

        ObservableList<Node> data = FXCollections.observableArrayList();

        //Display all Node ID, X, Y, Floor, Building, LongName, and ShortName
        for(NodeInfo node : nodeStream.collect(Collectors.toList())){
            if(!node.getNodeID().isEmpty()){
                data.add(new Node(node.getNodeID(),node.getXPos(),node.getYPos(),
                        node.getFloor(), node.getBuilding(), node.getNodeType(), node.getLongName(),
                        node.getShortName()));
            }
        }

        TreeItem<Node> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);

        tree.setRoot(root);
        tree.setShowRoot(false);
    }

    private void initColumns() {
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

        tree.getColumns().addAll(nID,nX,nY,nFloor,nBuilding,nType,nLongName,nShortName);
    }

}
