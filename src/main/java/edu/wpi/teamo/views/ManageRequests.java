package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.App;
import edu.wpi.teamo.database.request.IMedicineRequestInfo;
import edu.wpi.teamo.database.request.ISanitationRequestInfo;
import edu.wpi.teamo.database.request.MedicineRequest;
import edu.wpi.teamo.database.request.SanitationRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class ManageRequests extends ServiceRequestPage implements Initializable {

    @FXML
    private JFXTreeTableView<MedicineRequest> medRequestTable;

    @FXML
    private JFXTreeTableView<SanitationRequest> sanRequestTable;

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

        JFXTreeTableColumn<MedicineRequest, String> rooms = new JFXTreeTableColumn<>("Room (Node)");
        rooms.setPrefWidth(200);
        rooms.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLocationID());
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

        ObservableList<MedicineRequest> medRequests = FXCollections.observableArrayList();

        try {
            Stream<IMedicineRequestInfo> medReqStream = App.requestService.getAllMedicineRequests();
            medReqStream.forEach(m -> medRequests.add(new MedicineRequest(m.getID(), m.getType(), m.getAmount(), m.getLocationID(), m.getAssigned())));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        final TreeItem<MedicineRequest> root = new RecursiveTreeItem<MedicineRequest>(medRequests, RecursiveTreeObject::getChildren);
        medRequestTable.getColumns().setAll(medIDs, medType, medAmount, rooms, assignees);
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

        JFXTreeTableColumn<SanitationRequest, String> sanLocs = new JFXTreeTableColumn<>("Room (Node)");
        sanLocs.setPrefWidth(200);
        sanLocs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLocationID());
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

        ObservableList<SanitationRequest> sanRequests = FXCollections.observableArrayList();

        try {
            Stream<ISanitationRequestInfo> sanReqStream = App.requestService.getAllSanitationRequests();
            sanReqStream.forEach(m -> sanRequests.add(new SanitationRequest(m.getID(), m.getLocationID(), m.getAssigned(), m.getDetails())));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        final TreeItem<SanitationRequest> sanRoot = new RecursiveTreeItem<SanitationRequest>(sanRequests, RecursiveTreeObject::getChildren);
        sanRequestTable.getColumns().setAll(sanIDs, sanLocs, sanAssigned, sanDetails);
        sanRequestTable.setRoot(sanRoot);
        sanRequestTable.setShowRoot(false);
    }

    @FXML
    private void removeMedRequest() throws SQLException {
        TreeItem<MedicineRequest> selection = medRequestTable.getSelectionModel().getSelectedItem();
        if (selection == null) medErrorText.setText("No medicine requests selected");
        else {
            App.requestService.removeMedicineRequest(medRequestTable.getSelectionModel().getSelectedItem().getValue().getID());
            updateMedicineTable();
        }

    }

    @FXML
    private void removeSanRequest() throws SQLException {
        TreeItem<SanitationRequest> selection = sanRequestTable.getSelectionModel().getSelectedItem();
        if (selection == null) sanErrorText.setText("No sanitation requests selected");
        else {
            App.requestService.removeSanitationRequest(sanRequestTable.getSelectionModel().getSelectedItem().getValue().getID());
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
