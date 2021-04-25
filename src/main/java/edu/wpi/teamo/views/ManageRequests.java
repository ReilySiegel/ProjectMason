package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.App;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.MedicineRequest;
import edu.wpi.teamo.database.request.SanitationRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManageRequests extends ServiceRequestPage implements Initializable {

    @FXML
    private JFXTreeTableView<MedicineRequest> medRequestTable;

    @FXML
    private JFXTreeTableView<SanitationRequest> sanRequestTable;

    @FXML
    private JFXCheckBox medShowCompleted;

    @FXML
    private JFXCheckBox sanShowCompleted;

    @FXML
    private Text medErrorText;

    @FXML
    private Text sanErrorText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateMedicineTable();
        updateSanitationTable();

    }

    @FXML
    private void updateMedicineTable() {
        JFXTreeTableColumn<MedicineRequest, String> medIDs = new JFXTreeTableColumn<>("ID");
        medIDs.setPrefWidth(200);
        medIDs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getID());
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> medType = new JFXTreeTableColumn<>("Medicine Type");
        medType.setPrefWidth(200);
        medType.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getType());
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> medAmount = new JFXTreeTableColumn<>("Medicine Amount");
        medAmount.setPrefWidth(200);
        medAmount.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getAmount());
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> rooms = new JFXTreeTableColumn<>("Room/Node(s)");
        rooms.setPrefWidth(200);
        rooms.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLocations().collect(Collectors.joining(", ")));
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> assignees = new JFXTreeTableColumn<>("Assigned Person");
        assignees.setPrefWidth(200);
        assignees.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getAssigned());
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> completed = new JFXTreeTableColumn<>("Status");
        completed.setPrefWidth(200);
        completed.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp;
                if (param.getValue().getValue().isComplete()) strProp = new SimpleStringProperty("Complete");
                else strProp = new SimpleStringProperty("In progress");
                return strProp;
            }
        });

        ObservableList<MedicineRequest> medRequests = FXCollections.observableArrayList();

        try {
            Stream<MedicineRequest> medReqStream = MedicineRequest.getAll();
            if (medShowCompleted.isSelected()) {
                medReqStream.forEach(m -> medRequests.add(m));
            }
            else {
                medReqStream.forEach(m -> {
                    if (!m.isComplete())
                        medRequests.add(m);
                });
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        final TreeItem<MedicineRequest> root = new RecursiveTreeItem<MedicineRequest>(medRequests, RecursiveTreeObject::getChildren);
        medRequestTable.getColumns().setAll(medIDs, medType, medAmount, rooms, assignees, completed);
        medRequestTable.setRoot(root);
        medRequestTable.setShowRoot(false);



    }

    @FXML
    private void updateSanitationTable() {
        JFXTreeTableColumn<SanitationRequest, String> sanIDs = new JFXTreeTableColumn<>("ID");
        sanIDs.setPrefWidth(200);
        sanIDs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getID());
                return strProp;
            }
        });

        JFXTreeTableColumn<SanitationRequest, String> sanLocs = new JFXTreeTableColumn<>("Room/Node(s)");
        sanLocs.setPrefWidth(200);
        sanLocs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLocations().collect(Collectors.joining(", ")));
                return strProp;
            }
        });

        JFXTreeTableColumn<SanitationRequest, String> sanAssigned = new JFXTreeTableColumn<>("Assigned Person");
        sanAssigned.setPrefWidth(200);
        sanAssigned.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getAssigned());
                return strProp;
            }
        });

        JFXTreeTableColumn<SanitationRequest, String> sanDetails = new JFXTreeTableColumn<>("Details");
        sanDetails.setPrefWidth(200);
        sanDetails.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDetails());
                return strProp;
            }
        });

        JFXTreeTableColumn<SanitationRequest, String> sanCompleted = new JFXTreeTableColumn<>("Status");
        sanDetails.setPrefWidth(200);
        sanDetails.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationRequest, String> param) {
                StringProperty strProp;
                if (param.getValue().getValue().isComplete()) strProp = new SimpleStringProperty("Complete");
                else strProp = new SimpleStringProperty("In progress");
                return strProp;
            }
        });

        ObservableList<SanitationRequest> sanRequests = FXCollections.observableArrayList();

        try {
            Stream<SanitationRequest> sanReqStream = SanitationRequest.getAll();
            if (sanShowCompleted.isSelected()) {
                sanReqStream.forEach(m -> sanRequests.add(m));
            } else {
                sanReqStream.forEach(m -> {
                    if (!m.isComplete()) sanRequests.add(m);
                });
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        final TreeItem<SanitationRequest> sanRoot = new RecursiveTreeItem<SanitationRequest>(sanRequests, RecursiveTreeObject::getChildren);
        sanRequestTable.getColumns().setAll(sanIDs, sanLocs, sanAssigned, sanDetails, sanCompleted);
        sanRequestTable.setRoot(sanRoot);
        sanRequestTable.setShowRoot(false);
    }

    @FXML
    private void removeMedRequest() throws SQLException {
        TreeItem<MedicineRequest> selection = medRequestTable.getSelectionModel().getSelectedItem();
        if (selection == null) medErrorText.setText("No medicine requests selected");
        else {
            App.requestService.setMedicineCompleted(medRequestTable.getSelectionModel().getSelectedItem().getValue().getID());
            updateMedicineTable();
        }

    }

    @FXML
    private void removeSanRequest() throws SQLException {
        TreeItem<SanitationRequest> selection = sanRequestTable.getSelectionModel().getSelectedItem();
        if (selection == null) sanErrorText.setText("No sanitation requests selected");
        else {
            App.requestService.setSanitationCompleted(sanRequestTable.getSelectionModel().getSelectedItem().getValue().getID());
            updateSanitationTable();
        }

    }

    @FXML
    private void clearMedError() {
        medErrorText.setText("");
    }

    @FXML
    private void clearSanError() {
        sanErrorText.setText("");
    }


}
