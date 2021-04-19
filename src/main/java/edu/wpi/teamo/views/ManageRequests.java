package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.App;
import edu.wpi.teamo.database.request.IMedicineRequestInfo;
import edu.wpi.teamo.database.request.MedicineRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class ManageRequests extends ServiceRequestPage implements Initializable {

    @FXML
    private JFXTreeTableView<MedicineRequest> medRequestTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        JFXTreeTableColumn<MedicineRequest, String> IDs = new JFXTreeTableColumn<>("ID");
        IDs.setPrefWidth(250);
        IDs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
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

//        try {
//            Stream<IMedicineRequestInfo> medReqStream = App.requestService.getAllMedicineRequests();
//            medReqStream.forEach(m -> medRequests.add(new MedicineRequest(m.getID(), m.getType(), m.getAmount(), m.getLocationID(), m.getAssigned())));
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        medRequests.add(new MedicineRequest("123", "Advil", "4", "placeholder loc", "Jimothy"));
        medRequests.add(new MedicineRequest("754", "Ketmaine", "50", "under there", "Samiel"));

        final TreeItem<MedicineRequest> root = new RecursiveTreeItem<MedicineRequest>(medRequests, RecursiveTreeObject::getChildren);
        medRequestTable.getColumns().setAll(IDs, medType, medAmount, rooms, assignees);
        medRequestTable.setRoot(root);
        medRequestTable.setShowRoot(false);





    }


}
