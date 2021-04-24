package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.database.map.Edge;
import edu.wpi.teamo.database.map.EdgeInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EdgeTable {
    JFXTreeTableView<Edge> tree;

    public EdgeTable(JFXTreeTableView<Edge> edgeTree) {
        tree = edgeTree;
        initColumns();
    }

    public void initClickListener(
            JFXTextField editingEdge,
            JFXTextField editEdgeID,
            JFXTextField editNode1,
            JFXTextField editNode2,
            JFXTextField deleteEdgeID
    ) {
        //Set original node ID, X, and Y in the Edit and Delete box to selected value.
        tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Edge>>() {
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
    }

    public void update(Stream<EdgeInfo> edgeStream){
        if (edgeStream == null) return;

        ObservableList<Edge> data = FXCollections.observableArrayList();

        //Display all Node ID, X, Y, Floor, Building, LongName, and ShortName
        for(EdgeInfo edge : edgeStream.collect(Collectors.toList())){
            if(!edge.getEdgeID().isEmpty()){
                data.add(new Edge(edge.getEdgeID(),edge.getStartNodeID(),edge.getEndNodeID()));
            }
        }

        // checks to see if tree has been initialized and wont make dupe columns
        TreeItem<Edge> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        tree.setRoot(root);
        tree.setShowRoot(false);
    }

    private void initColumns() {
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

        tree.getColumns().addAll(eID,eStart, eEnd);
    }
}
