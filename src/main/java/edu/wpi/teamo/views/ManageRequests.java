package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.App;
import edu.wpi.teamo.database.request.*;
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
import java.security.Security;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManageRequests extends ServiceRequestPage implements Initializable {

    @FXML
    private JFXTabPane tabPane;

    @FXML
    private JFXTreeTableView<MedicineRequest> medRequestTable;

    @FXML
    private JFXTreeTableView<SanitationRequest> sanRequestTable;

    @FXML
    private JFXTreeTableView<MaintenanceRequest> mtnRequestTable;

    @FXML
    private JFXTreeTableView<GiftRequest> giftRequestTable;

    @FXML
    private JFXTreeTableView<InterpreterRequest> langRequestTable;

    @FXML
    private JFXTreeTableView<LaundryRequest> laundryRequestTable;

    @FXML
    private JFXTreeTableView<ReligiousRequest> religRequestTable;

    @FXML
    private JFXTreeTableView<FoodRequest> foodRequestTable;

    @FXML
    private JFXTreeTableView<SecurityRequest> secRequestTable;


    @FXML
    private JFXCheckBox medShowCompleted;

    @FXML
    private JFXCheckBox sanShowCompleted;

    @FXML
    private Text medErrorText;

    @FXML
    private Text sanErrorText;

    @FXML
    private JFXTextField medTypeField;

    @FXML
    private JFXTextField medAmountField;

    @FXML
    private Text currentMedReqID;

    @FXML
    private JFXTextField sanDetailsField;

    @FXML
    private Text currentSanReqID;

    @FXML
    private JFXTextField sanAssigneeField;

    @FXML
    private JFXTextArea sanNoteField;

    @FXML
    private JFXTextField sanTimeField;

    @FXML
    private JFXTextField sanDateField;

    @FXML
    private JFXTextArea sanLocationField;

    @FXML
    private JFXCheckBox sanRecurBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tabPane.getStyleClass().add("jfx-tab-pane");

        sanAssigneeField.getStyleClass().add("jfx-text-field");
        sanDateField.getStyleClass().add("jfx-text-field");
        sanNoteField.getStyleClass().add("jfx-text-field");
        sanTimeField.getStyleClass().add("jfx-text-field");
        sanLocationField.getStyleClass().add("jfx-text-field");
        sanRecurBox.getStyleClass().add("check-box");


        updateMedicineTable();
        updateSanitationTable();
        updateInterpreterTable();
        updateGiftTable();
        updateSecTable();
        updateMtnTable();

    }

    @FXML
    private void updateMedicineTable() {
        JFXTreeTableColumn<MedicineRequest, String> medIDs = new JFXTreeTableColumn<>("ID");
        medIDs.setPrefWidth(100);
        medIDs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getID());
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> medType = new JFXTreeTableColumn<>("Medicine Type");
        medType.setPrefWidth(100);
        medType.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getType());
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> medAmount = new JFXTreeTableColumn<>("Medicine Amount");
        medAmount.setPrefWidth(100);
        medAmount.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getAmount());
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> rooms = new JFXTreeTableColumn<>("Room/Node(s)");
        rooms.setPrefWidth(100);
        rooms.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLocations().collect(Collectors.joining(", ")));
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> assignees = new JFXTreeTableColumn<>("Assigned Person");
        assignees.setPrefWidth(100);
        assignees.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getAssigned());
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> medDetails = new JFXTreeTableColumn<>("Details");
        medDetails.setPrefWidth(100);
        medDetails.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDetails());
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> medDue = new JFXTreeTableColumn<>("Due");
        medDue.setPrefWidth(100);
        medDue.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MedicineRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MedicineRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDue().toString());
                return strProp;
            }
        });

        JFXTreeTableColumn<MedicineRequest, String> completed = new JFXTreeTableColumn<>("Status");
        completed.setPrefWidth(100);
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
            } else {
                medReqStream.forEach(m -> {
                    if (!m.isComplete())
                        medRequests.add(m);
                });
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        final TreeItem<MedicineRequest> root = new RecursiveTreeItem<MedicineRequest>(medRequests, RecursiveTreeObject::getChildren);
        medRequestTable.getColumns().setAll(medIDs, medType, medAmount, rooms, assignees, medDetails, medDue, completed);
        medRequestTable.setRoot(root);
        medRequestTable.setShowRoot(false);


    }

    @FXML
    private void updateSanitationTable() {
        JFXTreeTableColumn<SanitationRequest, String> sanIDs = new JFXTreeTableColumn<>("ID");
        sanIDs.setPrefWidth(150);
        sanIDs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getID());
                return strProp;
            }
        });

        JFXTreeTableColumn<SanitationRequest, String> sanLocs = new JFXTreeTableColumn<>("Room/Node(s)");
        sanLocs.setPrefWidth(150);
        sanLocs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLocations().collect(Collectors.joining(", ")));
                return strProp;
            }
        });

        JFXTreeTableColumn<SanitationRequest, String> sanAssigned = new JFXTreeTableColumn<>("Assigned Person");
        sanAssigned.setPrefWidth(150);
        sanAssigned.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getAssigned());
                return strProp;
            }
        });

        JFXTreeTableColumn<SanitationRequest, String> sanDetails = new JFXTreeTableColumn<>("Details");
        sanDetails.setPrefWidth(150);
        sanDetails.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDetails());
                return strProp;

            }
        });

        JFXTreeTableColumn<SanitationRequest, String> sanDue = new JFXTreeTableColumn<>("Due");
        sanDue.setPrefWidth(150);
        sanDue.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SanitationRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDue().format(DateTimeFormatter.ISO_DATE_TIME));
                return strProp;

            }
        });

        JFXTreeTableColumn<SanitationRequest, String> sanCompleted = new JFXTreeTableColumn<>("Status");
        sanCompleted.setPrefWidth(150);
        sanCompleted.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SanitationRequest, String>, ObservableValue<String>>() {
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
        sanRequestTable.getColumns().setAll(sanIDs, sanDetails, sanLocs, sanAssigned, sanDue, sanCompleted);
        sanRequestTable.setRoot(sanRoot);
        sanRequestTable.setShowRoot(false);
    }

    @FXML
    private void updateInterpreterTable() {
        JFXTreeTableColumn<InterpreterRequest, String> intIDs = new JFXTreeTableColumn<>("ID");
        intIDs.setPrefWidth(150);
        intIDs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<InterpreterRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<InterpreterRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getID());
                return strProp;
            }
        });

        JFXTreeTableColumn<InterpreterRequest, String> intLanguage = new JFXTreeTableColumn<>("Language");
        intLanguage.setPrefWidth(150);
        intLanguage.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<InterpreterRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<InterpreterRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLanguage());
                return strProp;

            }
        });

        JFXTreeTableColumn<InterpreterRequest, String> intType = new JFXTreeTableColumn<>("Job Type");
        intType.setPrefWidth(150);
        intType.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<InterpreterRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<InterpreterRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getType());
                return strProp;

            }
        });


        JFXTreeTableColumn<InterpreterRequest, String> intLocs = new JFXTreeTableColumn<>("Room/Node(s)");
        intLocs.setPrefWidth(150);
        intLocs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<InterpreterRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<InterpreterRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLocations().collect(Collectors.joining(", ")));
                return strProp;
            }
        });

        JFXTreeTableColumn<InterpreterRequest, String> intAssigned = new JFXTreeTableColumn<>("Assigned Person");
        intAssigned.setPrefWidth(150);
        intAssigned.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<InterpreterRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<InterpreterRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getAssigned());
                return strProp;
            }
        });

        JFXTreeTableColumn<InterpreterRequest, String> intDetails = new JFXTreeTableColumn<>("Details");
        intDetails.setPrefWidth(150);
        intDetails.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<InterpreterRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<InterpreterRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDetails());
                return strProp;

            }
        });

        JFXTreeTableColumn<InterpreterRequest, String> intDue = new JFXTreeTableColumn<>("Due");
        intDue.setPrefWidth(150);
        intDue.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<InterpreterRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<InterpreterRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDue().format(DateTimeFormatter.ISO_DATE_TIME));
                return strProp;

            }
        });

        ObservableList<InterpreterRequest> intRequests = FXCollections.observableArrayList();

        try {
            Stream<InterpreterRequest> intReqStream = InterpreterRequest.getAll();
            intReqStream.forEach(m -> intRequests.add(m));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        final TreeItem<InterpreterRequest> intRoot = new RecursiveTreeItem<InterpreterRequest>(intRequests, RecursiveTreeObject::getChildren);
        langRequestTable.getColumns().setAll(intIDs, intLanguage, intType, intDetails, intLocs, intAssigned, intDue);
        langRequestTable.setRoot(intRoot);
        langRequestTable.setShowRoot(false);
    }

    @FXML
    private void updateSecTable() {
        JFXTreeTableColumn<SecurityRequest, String> secIDs = new JFXTreeTableColumn<>("ID");
        secIDs.setPrefWidth(150);
        secIDs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SecurityRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SecurityRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getID());
                return strProp;
            }
        });

        JFXTreeTableColumn<SecurityRequest, String> secLocs = new JFXTreeTableColumn<>("Room/Node(s)");
        secLocs.setPrefWidth(150);
        secLocs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SecurityRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SecurityRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLocations().collect(Collectors.joining(", ")));
                return strProp;
            }
        });

        JFXTreeTableColumn<SecurityRequest, String> secAssigned = new JFXTreeTableColumn<>("Assigned Person");
        secAssigned.setPrefWidth(150);
        secAssigned.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SecurityRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SecurityRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getAssigned());
                return strProp;
            }
        });

        JFXTreeTableColumn<SecurityRequest, String> secDue = new JFXTreeTableColumn<>("Due");
        secDue.setPrefWidth(150);
        secDue.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SecurityRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SecurityRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDue().toString());
                return strProp;
            }
        });

        JFXTreeTableColumn<SecurityRequest, String> secEmergency = new JFXTreeTableColumn<>("Urgency");
        secEmergency.setPrefWidth(150);
        secEmergency.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SecurityRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SecurityRequest, String> param) {
                StringProperty strProp;
                if (param.getValue().getValue().isEmergency())
                    strProp = new SimpleStringProperty("Emergency");
                else
                    strProp = new SimpleStringProperty("Normal");
                return strProp;
            }
        });

        JFXTreeTableColumn<SecurityRequest, String> secCompleted = new JFXTreeTableColumn<>("Status");
        secCompleted.setPrefWidth(150);
        secCompleted.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SecurityRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SecurityRequest, String> param) {
                StringProperty strProp;
                if (param.getValue().getValue().isComplete())
                    strProp = new SimpleStringProperty("Complete");
                else
                    strProp = new SimpleStringProperty("In progress");
                return strProp;
            }
        });

        ObservableList<SecurityRequest> secRequests = FXCollections.observableArrayList();

        try {
            Stream<SecurityRequest> secReqStream = SecurityRequest.getAll();
            secReqStream.forEach(m -> secRequests.add(m));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        final TreeItem<SecurityRequest> root = new RecursiveTreeItem<SecurityRequest>(secRequests, RecursiveTreeObject::getChildren);
        secRequestTable.getColumns().setAll(secIDs, secLocs, secAssigned, secDue, secEmergency, secCompleted);
        secRequestTable.setRoot(root);
        secRequestTable.setShowRoot(false);
    }


    @FXML
    private void updateMtnTable() {
        JFXTreeTableColumn<MaintenanceRequest, String> mtnIDs = new JFXTreeTableColumn<>("ID");
        mtnIDs.setPrefWidth(150);
        mtnIDs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MaintenanceRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MaintenanceRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getID());
                return strProp;
            }
        });

        JFXTreeTableColumn<MaintenanceRequest, String> mtnType = new JFXTreeTableColumn<>("Type");
        mtnType.setPrefWidth(150);
        mtnType.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MaintenanceRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MaintenanceRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getType());
                return strProp;
            }
        });

        JFXTreeTableColumn<MaintenanceRequest, String> mtnDetails = new JFXTreeTableColumn<>("Details");
        mtnDetails.setPrefWidth(150);
        mtnDetails.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MaintenanceRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MaintenanceRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDetails());
                return strProp;
            }
        });


        JFXTreeTableColumn<MaintenanceRequest, String> mtnLocs = new JFXTreeTableColumn<>("Room/Node(s)");
        mtnLocs.setPrefWidth(150);
        mtnLocs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MaintenanceRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MaintenanceRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLocations().collect(Collectors.joining(", ")));
                return strProp;
            }
        });

        JFXTreeTableColumn<MaintenanceRequest, String> mtnAssigned = new JFXTreeTableColumn<>("Assigned Person");
        mtnAssigned.setPrefWidth(150);
        mtnAssigned.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MaintenanceRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MaintenanceRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getAssigned());
                return strProp;
            }
        });

        JFXTreeTableColumn<MaintenanceRequest, String> mtnDue = new JFXTreeTableColumn<>("Due");
        mtnDue.setPrefWidth(150);
        mtnDue.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MaintenanceRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MaintenanceRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDue().toString());
                return strProp;
            }
        });

        JFXTreeTableColumn<MaintenanceRequest, String> mtnComplete = new JFXTreeTableColumn<>("Status");
        mtnComplete.setPrefWidth(150);
        mtnComplete.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<MaintenanceRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<MaintenanceRequest, String> param) {
                StringProperty strProp;
                if (param.getValue().getValue().isComplete()) strProp = new SimpleStringProperty("Complete");
                else strProp = new SimpleStringProperty("In progress");
                return strProp;
            }
        });

        ObservableList<MaintenanceRequest> mtnRequests = FXCollections.observableArrayList();

        try {
            Stream<MaintenanceRequest> mtnReqStream = MaintenanceRequest.getAll();
            mtnReqStream.forEach(m -> mtnRequests.add(m));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        final TreeItem<MaintenanceRequest> root = new RecursiveTreeItem<MaintenanceRequest>(mtnRequests, RecursiveTreeObject::getChildren);
        mtnRequestTable.getColumns().setAll(mtnIDs, mtnType, mtnDetails, mtnAssigned, mtnDue, mtnComplete);
        mtnRequestTable.setRoot(root);
        mtnRequestTable.setShowRoot(false);
    }


    @FXML
    private void updateGiftTable() {
        JFXTreeTableColumn<GiftRequest, String> giftIDs = new JFXTreeTableColumn<>("ID");
        giftIDs.setPrefWidth(150);
        giftIDs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GiftRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GiftRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getID());
                return strProp;
            }
        });

        JFXTreeTableColumn<GiftRequest, String> giftTID = new JFXTreeTableColumn<>("Tracking ID");
        giftTID.setPrefWidth(200);
        giftTID.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GiftRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GiftRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getTrackingID());
                return strProp;
            }
        });

        JFXTreeTableColumn<GiftRequest, String> giftLocs = new JFXTreeTableColumn<>("Room/Node(s)");
        giftLocs.setPrefWidth(150);
        giftLocs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GiftRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GiftRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLocations().collect(Collectors.joining(", ")));
                return strProp;
            }
        });

        JFXTreeTableColumn<GiftRequest, String> giftAssigned = new JFXTreeTableColumn<>("Assigned Person");
        giftAssigned.setPrefWidth(150);
        giftAssigned.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GiftRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GiftRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getAssigned());
                return strProp;
            }
        });

        JFXTreeTableColumn<GiftRequest, String> giftDue = new JFXTreeTableColumn<>("Due");
        giftDue.setPrefWidth(150);
        giftDue.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GiftRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GiftRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDue().toString());
                return strProp;
            }
        });

        JFXTreeTableColumn<GiftRequest, String> giftComplete = new JFXTreeTableColumn<>("Status");
        giftComplete.setPrefWidth(150);
        giftComplete.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GiftRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GiftRequest, String> param) {
                StringProperty strProp;
                if (param.getValue().getValue().isComplete()) strProp = new SimpleStringProperty("Complete");
                else strProp = new SimpleStringProperty("In progress");
                return strProp;
            }
        });

        JFXTreeTableColumn<GiftRequest, String> giftNotes = new JFXTreeTableColumn<>("Notes");
        giftComplete.setPrefWidth(300);
        giftComplete.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<GiftRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<GiftRequest, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getDue().toString());
                return strProp;
            }
        });

        ObservableList<GiftRequest> giftRequests = FXCollections.observableArrayList();

        try {
            Stream<GiftRequest> mtnReqStream = GiftRequest.getAll();
            mtnReqStream.forEach(m -> giftRequests.add(m));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        final TreeItem<GiftRequest> root = new RecursiveTreeItem<GiftRequest>(giftRequests, RecursiveTreeObject::getChildren);
        giftRequestTable.getColumns().setAll(giftIDs, giftTID, giftAssigned, giftDue, giftNotes, giftComplete);
        giftRequestTable.setRoot(root);
        giftRequestTable.setShowRoot(false);
    }

    @FXML
    private void markMedRequestComplete() throws SQLException {
        TreeItem<MedicineRequest> selection = medRequestTable.getSelectionModel().getSelectedItem();
        if (selection == null) medErrorText.setText("No medicine requests selected");
        else {
            String id = medRequestTable.getSelectionModel().getSelectedItem().getValue().getID();
            if (App.requestService.getMedicineRequest(id).isComplete())
                medErrorText.setText("Request already complete");
            else {
                App.requestService.setMedicineCompleted(medRequestTable.getSelectionModel().getSelectedItem().getValue().getID());
                updateMedicineTable();
            }
        }
    }

    @FXML
    private void markSecurityRequestComplete() throws SQLException {
        TreeItem<SecurityRequest> selection = secRequestTable.getSelectionModel().getSelectedItem();
        if (selection == null) System.out.println("No security requests selected");
        else {
            String id = secRequestTable.getSelectionModel().getSelectedItem().getValue().getID();
            if (SecurityRequest.getByID(id).isComplete())
                System.out.println("Request already complete");
            else {
                SecurityRequest.getByID(id).setComplete(true);
                updateSecTable();
            }
        }
    }

    @FXML
    private void markLaundryRequestComplete() throws SQLException {
        TreeItem<LaundryRequest> selection = laundryRequestTable.getSelectionModel().getSelectedItem();
        if (selection == null) System.out.println("No laundry requests selected");
        else {
            String id = laundryRequestTable.getSelectionModel().getSelectedItem().getValue().getID();
            if (LaundryRequest.getByID(id).isComplete())
                medErrorText.setText("Request already complete");
            else {
                LaundryRequest.getByID(id).setComplete(true);
                updateLaundryTable();
            }
        }
    }

    @FXML
    private void markSanRequestComplete() throws SQLException {
        TreeItem<SanitationRequest> selection = sanRequestTable.getSelectionModel().getSelectedItem();
        if (selection == null) sanErrorText.setText("No sanitation requests selected");
        else {
            String id = sanRequestTable.getSelectionModel().getSelectedItem().getValue().getID();
            if (App.requestService.getSanitationRequest(id).isComplete())
                sanErrorText.setText("Request already complete");
            else {
                App.requestService.setSanitationCompleted(sanRequestTable.getSelectionModel().getSelectedItem().getValue().getID());
                updateSanitationTable();
            }
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

    @FXML
    private void updateMedEditor() {
        MedicineRequest currentReq = medRequestTable.getSelectionModel().getSelectedItem().getValue();

        currentMedReqID.setText("ID: " + currentReq.getID());
        medTypeField.setText(currentReq.getType());
        medAmountField.setText(currentReq.getAmount());

        medTypeField.setEditable(true);
        medAmountField.setEditable(true);
    }

    @FXML
    private void updateSanEditor() {
        SanitationRequest currentReq = sanRequestTable.getSelectionModel().getSelectedItem().getValue();
        System.out.println(currentReq.getID());

        currentSanReqID.setText("ID: " + currentReq.getID());
        sanDetailsField.setText(currentReq.getDetails());

        sanDetailsField.setEditable(true);
    }

    @FXML
    private void applyMedUpdate() throws SQLException {
        String id = currentMedReqID.getText().substring(4);
        MedicineRequest currentReq = App.requestService.getMedicineRequest(id);
        App.requestService.removeMedicineRequest(id);

        BaseRequest b = new BaseRequest(id, currentReq.getDetails(), currentReq.getLocations(), currentReq.getAssigned(), currentReq.isComplete());
        MedicineRequest mr = new MedicineRequest(medTypeField.getText(), medAmountField.getText(), b);

        mr.update();
        updateMedicineTable();
    }

    @FXML
    private void applySanUpdate() throws SQLException {
        String id = currentSanReqID.getText().substring(4);
        System.out.println(id);
        SanitationRequest currentReq = App.requestService.getSanitationRequest(id);
        App.requestService.removeSanitationRequest(id);

        BaseRequest b = new BaseRequest(id, sanDetailsField.getText(), currentReq.getLocations(), currentReq.getAssigned(), currentReq.isComplete());
        SanitationRequest sr = new SanitationRequest(false, b);

        sr.update();
        updateSanitationTable();
        System.out.println(App.requestService.getSanitationRequest(id).getDetails());
    }
}

