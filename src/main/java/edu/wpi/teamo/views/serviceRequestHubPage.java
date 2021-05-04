package edu.wpi.teamo.views;


import animatefx.animation.FadeOut;
import edu.wpi.teamo.Session;
import javafx.scene.text.FontWeight;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import com.jfoenix.controls.*;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.App;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.*;

public class serviceRequestHubPage implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton backButton;

    @FXML
    private JFXButton helpButton;

    @FXML
    private JFXTextField searchBar;

    @FXML
    private JFXListView<JFXButton> listView;

    @FXML
    private HBox hboxSize;

    private SearchSelect<Pages, JFXButton> searcher;

    private EnumMap<Pages, String> descriptionMap;
    private EnumMap<Pages, String> titleMap;
    private EnumMap<Pages, Image> iconMap;

    private boolean empAccess;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        empAccess = Session.isLoggedIn() && Session.getAccount().hasEmployeeAccess();

        backButton.setOnAction(this::handleBacktoMain);
        helpButton.setOnAction(this::handleHelp);

        List<Pages> serviceRequestPages = new LinkedList<>();
        serviceRequestPages.add(Pages.LANGUAGEINTERPRETER);
        if (empAccess) serviceRequestPages.add(Pages.TRANSPORTATION);
        if (empAccess) serviceRequestPages.add(Pages.MAINTENANCE);
        serviceRequestPages.add(Pages.SANITATION);
        serviceRequestPages.add(Pages.RELIGIOUS);
        serviceRequestPages.add(Pages.SECURITY);
        if (empAccess) serviceRequestPages.add(Pages.MEDICINE);
        serviceRequestPages.add(Pages.LAUNDRY);
        serviceRequestPages.add(Pages.GIFTS);
        serviceRequestPages.add(Pages.FOOD);

        descriptionMap = new EnumMap<>(Pages.class);
        titleMap = new EnumMap<>(Pages.class);
        iconMap = new EnumMap<>(Pages.class);
        populateMaps(serviceRequestPages);

        SearchSelect.Matcher<Pages> matcher = (page, text) ->
                titleMap.get(page).toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));

        searcher = new SearchSelect<>(searchBar, listView, matcher, this::makeButton);

        if (verifyMaps(serviceRequestPages)) {
            searcher.setItems(serviceRequestPages);
        }
        else {
            System.out.println("Request page button tables are incomplete.");
        }

    }

    private void populateMaps(List<Pages> serviceRequestPages) {
        //TODO make this more reliable by looping through the given pages and setting wit a switch case
        iconMap.put(Pages.LANGUAGEINTERPRETER, new Image("edu/wpi/teamo/images/Icons/icons8-collaboration-96.png"));
        if (empAccess) iconMap.put(Pages.TRANSPORTATION, new Image("edu/wpi/teamo/images/Icons/icons8-ambulance-100.png"));
        if (empAccess) iconMap.put(Pages.MAINTENANCE, new Image("edu/wpi/teamo/images/Icons/icons8-maintenance-96.png"));
        iconMap.put(Pages.SANITATION, new Image("edu/wpi/teamo/images/Icons/icons8-housekeeping-96.png"));
        iconMap.put(Pages.SECURITY, new Image("edu/wpi/teamo/images/Icons/icons8-police-badge-96.png"));
        iconMap.put(Pages.RELIGIOUS, new Image("edu/wpi/teamo/images/Icons/icons8-holy-bible-96.png"));
        if (empAccess) iconMap.put(Pages.MEDICINE, new Image("edu/wpi/teamo/images/Icons/icons8-pill-100.png"));
        iconMap.put(Pages.LAUNDRY, new Image("edu/wpi/teamo/images/Icons/icons8-hanger-96.png"));
        iconMap.put(Pages.FOOD, new Image("edu/wpi/teamo/images/Icons/icons8-hamburger-96.png"));
        iconMap.put(Pages.GIFTS, new Image("edu/wpi/teamo/images/Icons/icons8-gift-100.png"));

        descriptionMap.put(Pages.LANGUAGEINTERPRETER, App.resourceBundle.getString("key.language_description"));
        if (empAccess) descriptionMap.put(Pages.TRANSPORTATION, App.resourceBundle.getString("key.transportation_description"));
        if (empAccess) descriptionMap.put(Pages.MAINTENANCE, App.resourceBundle.getString("key.maintenance_description"));
        descriptionMap.put(Pages.SANITATION, App.resourceBundle.getString("key.sanitation_description"));
        descriptionMap.put(Pages.RELIGIOUS, App.resourceBundle.getString("key.religious_description"));
        descriptionMap.put(Pages.SECURITY, App.resourceBundle.getString("key.security_description"));
        if (empAccess) descriptionMap.put(Pages.MEDICINE, App.resourceBundle.getString("key.medicine_description"));
        descriptionMap.put(Pages.LAUNDRY, App.resourceBundle.getString("key.laundry_description"));
        descriptionMap.put(Pages.GIFTS, App.resourceBundle.getString("key.gift_description"));
        descriptionMap.put(Pages.FOOD, App.resourceBundle.getString("key.food_description"));

        titleMap.put(Pages.LANGUAGEINTERPRETER, App.resourceBundle.getString("key.language_interpreter"));
        if (empAccess) titleMap.put(Pages.TRANSPORTATION, App.resourceBundle.getString("key.patient_transportation"));
        titleMap.put(Pages.RELIGIOUS, App.resourceBundle.getString("key.religious_requests"));
        if (empAccess) titleMap.put(Pages.MAINTENANCE, App.resourceBundle.getString("key.maintenance"));
        titleMap.put(Pages.SANITATION, App.resourceBundle.getString("key.sanitation"));
        titleMap.put(Pages.GIFTS, App.resourceBundle.getString("key.gift_delivery"));
        titleMap.put(Pages.FOOD, App.resourceBundle.getString("key.food_delivery"));
        titleMap.put(Pages.SECURITY, App.resourceBundle.getString("key.security"));
        if (empAccess) titleMap.put(Pages.MEDICINE, App.resourceBundle.getString("key.medicine"));
        titleMap.put(Pages.LAUNDRY, App.resourceBundle.getString("key.laundry"));
    }

    private boolean verifyMaps(List<Pages> serviceRequestPages) {
        for (Pages page : serviceRequestPages) {
            if (
                descriptionMap.get(page) == null ||
                titleMap.get(page) == null ||
                iconMap.get(page) == null
               ) {
                return false;
            }
        }
        return true;
    }

    private JFXButton makeButton(Pages page, boolean isSelected) {

        Label description = new Label(descriptionMap.get(page));
        Label title = new Label(titleMap.get(page));

        Font font2 = Font.font("System", FontWeight.NORMAL, 10);
        Font font = Font.font("System", FontWeight.NORMAL, 20);
        description.setFont(font2);
        title.setFont(font);

        VBox vbox = new VBox(title, description);
        vbox.setPadding(new Insets(0,0,0,5));

        ImageView icon = new ImageView();
        icon.setImage(iconMap.get(page));

        HBox hbox = new HBox(icon, vbox);
        hboxSize.setPrefWidth(768);

        JFXButton button = new JFXButton("", hbox);
        button.setOnAction(actionEvent -> SubPageContainer.switchPage(page));
        //s.setMinWidth(hboxSize.getPrefWidth());
        button.setMaxWidth(Double.MAX_VALUE);
        button.setFont(font);

        button.getStyleClass().clear();
        button.getStyleClass().add("pane");

        return button;
    }

    void handleBacktoMain(ActionEvent event) {
        stackPane.setMouseTransparent(true);
        new FadeOut(stackPane).play();
    }


    private void handleHelp(ActionEvent e) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help_service_requests")));
        content.setBody(new Text(App.resourceBundle.getString("key.help_finding_a_service") +
                App.resourceBundle.getString("key.help_selecting_a_service") +
                App.resourceBundle.getString("key.help_service_back")));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
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