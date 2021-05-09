package edu.wpi.teamo.views.mapeditor;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.teamo.database.map.IMapService;
import edu.wpi.teamo.database.map.NodeInfo;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXListView;
import edu.wpi.teamo.views.TableSearcher;
import edu.wpi.teamo.views.mapeditor.MapEditorPage;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.List;


public class NodeTable extends TableSearcher<NodeInfo, HBox> {

    private final IMapService mapService;

    private JFXComboBox<String> validityFilter = null;
    private JFXComboBox<String> floorFilter = null;

    double columnWidth = 200;

    public static interface OnUpdate {
        void onUpdate();
    }
    OnUpdate onUpdate = null;

    public NodeTable(JFXTextField searchBar, JFXListView<HBox> table, OnUpdate onUpdate, IMapService mapService) {
        super(searchBar, table, null, null, null);
        setHeaderCellCreator(this::createHeader);
        setCellCreator(this::createRow);
        setMatcher(this::nodeMatches);
        this.mapService = mapService;
        this.onUpdate = onUpdate;
    }

    private boolean nodeMatches(NodeInfo node, String text) {
        boolean matching;

        text = text.toLowerCase(Locale.ROOT);

        matching  = String.valueOf(node.getXPos()).toLowerCase(Locale.ROOT).contains(text);
        matching |= String.valueOf(node.getYPos()).toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getShortName().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getNodeType().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getBuilding().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getLongName().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getNodeID().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getFloor().toLowerCase(Locale.ROOT).contains(text);

        if (!floorFilter.getValue().equals(MapEditorPage.allFloorsKey)) {
            matching &= node.getFloor().equals(floorFilter.getValue());
        }

        if (!validityFilter.getValue().equals(MapEditorPage.allValidityKey)) {
            matching &= ( node.isValid() && validityFilter.getValue().equals(MapEditorPage.validKey  )) ||
                        (!node.isValid() && validityFilter.getValue().equals(MapEditorPage.invalidKey));
        }

        return matching;
    }

    private HBox createHeader() {
        List<Label> columns = new LinkedList<>();

        columns.add(new Label("ID"));
        columns.add(new Label("Long Name"));
        columns.add(new Label("Short Name"));
        columns.add(new Label("Type"));
        columns.add(new Label("Building"));
        columns.add(new Label("Floor"));
        columns.add(new Label("X"));
        columns.add(new Label("Y"));

        columns.forEach(column -> column.setMinWidth(columnWidth));
        HBox row = new HBox();
        row.getChildren().addAll(columns);

        return row;
    }

    private HBox createRow(NodeInfo node, boolean isSelected) {

        JFXTextField idField = new JFXTextField();
        idField.setText(node.getNodeID());
        idField.setOnAction(event -> handleIDChange(idField, node));
        idField.setMinWidth(columnWidth);

        JFXTextField longNameField = new JFXTextField();
        longNameField.setText(node.getLongName());
        longNameField.setOnAction(event -> handleLNChange(longNameField, node));
        longNameField.setMinWidth(columnWidth);

        JFXTextField shortNameField = new JFXTextField();
        shortNameField.setText(node.getShortName());
        shortNameField.setOnAction(event -> handleSNChange(shortNameField, node));
        shortNameField.setMinWidth(columnWidth);

        JFXTextField typeField = new JFXTextField();
        typeField.setText(node.getNodeType());
        typeField.setOnAction(event -> handleTypeChange(typeField, node));
        typeField.setMinWidth(columnWidth);

        JFXTextField buildingField = new JFXTextField();
        buildingField.setText(node.getBuilding());
        buildingField.setOnAction(event -> handleBuildingChange(buildingField, node));
        buildingField.setMinWidth(columnWidth);

        JFXTextField floorField = new JFXTextField();
        floorField.setText(node.getFloor());
        floorField.setOnAction(event -> handleFloorChange(floorField, node));
        floorField.setMinWidth(columnWidth);

        JFXTextField xField = new JFXTextField();
        xField.setText(String.valueOf(node.getXPos()));
        xField.setOnAction(event -> handleXChange(xField, node));
        xField.setMinWidth(columnWidth);

        JFXTextField yField = new JFXTextField();
        yField.setText(String.valueOf(node.getYPos()));
        yField.setOnAction(event -> handleYChange(yField, node));
        yField.setMinWidth(columnWidth);

        HBox row = new HBox(idField, longNameField, shortNameField, typeField, buildingField, floorField, xField, yField);

        if (node.isValid()) {
            row.setStyle("-fx-background-color: rgba(219,219,219,0.8)");
        }
        else {
            row.setStyle("-fx-background-color: rgba(252,132,132,0.8)");
        }

        return row;
    }

    private void handleYChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                int newY = Integer.parseInt(text);
                mapService.setNodePosition(node.getNodeID(), node.getXPos(), newY);
                onUpdate.onUpdate();
            } catch (NumberFormatException e) {
                textField.setText(String.valueOf(node.getYPos()));
            } catch (SQLException throwables) {
                textField.setText(String.valueOf(node.getYPos()));
                throwables.printStackTrace();
            }
        }
    }

    private void handleXChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                int newX = Integer.parseInt(text);
                mapService.setNodePosition(node.getNodeID(), newX, node.getYPos());
                onUpdate.onUpdate();
            } catch (NumberFormatException e) {
                textField.setText(String.valueOf(node.getXPos()));
            } catch (SQLException throwables) {
                textField.setText(String.valueOf(node.getXPos()));
                throwables.printStackTrace();
            }
        }
    }

    private void handleFloorChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setNodeFloor(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getFloor());
            }
        }
    }

    private void handleBuildingChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setNodeBuilding(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getBuilding());
            }
        }
    }

    private void handleTypeChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setNodeType(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getNodeType());
            }
        }
    }

    private void handleSNChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setNodeShortName(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getShortName());
            }
        }
    }

    private void handleIDChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                if (mapService.nodeExists(text)) throw new SQLException();
                mapService.setNodeID(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getNodeID());
            }
        }
    }

    private void handleLNChange(JFXTextField textField, NodeInfo node) {
        String text = textField.getText();
        if (mapService != null) {
            try {
                mapService.setNodeLongName(node.getNodeID(), text);
                onUpdate.onUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                textField.setText(node.getLongName());
            }
        }
    }

    public void setFloorFilter(JFXComboBox floorFilter) {
        this.floorFilter = floorFilter;
    }

    public void setValidityFilter(JFXComboBox validityFilter) {
        this.validityFilter = validityFilter;
    }
}
