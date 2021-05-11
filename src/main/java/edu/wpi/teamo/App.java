package edu.wpi.teamo;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.teamo.algos.*;
import edu.wpi.teamo.database.account.Account;
import edu.wpi.teamo.database.request.*;
import edu.wpi.teamo.database.map.IMapService;
import edu.wpi.teamo.database.map.MapDB;
import edu.wpi.teamo.database.Database;
import edu.wpi.teamo.views.LocaleType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import java.sql.SQLException;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

import static edu.wpi.teamo.views.Map.loadImages;


public class App extends Application {

  private static final EnumMap<Pages, String> pagePaths = new EnumMap<>(Pages.class);
  private static final EnumMap<Theme, String> themeMap = new EnumMap<>(Theme.class);
  private static final EnumMap<Theme, String> bgMap = new EnumMap<>(Theme.class);
  public static IRequestService requestService = null;
  public static IStrategyPathfinding IStrategyPathfinding = null;
  public static IMapService mapService = null;
  private static Stage primaryStage;

  public static Context context = null;

  //Internationalization
  public static ResourceBundle resourceBundle;
  public static LocaleType selectedLocale;
  private static final String localesPath = "edu.wpi.teamo.locales.";

  public static final String normalEntrance = "FEXIT00201";
  public static final String emergencyEntrance = "FEXIT00301";

  private static String theme1Url = App.class.getResource("/edu/wpi/teamo/fxml/CSS/MainPage.css").toExternalForm();
  private static String theme2Url = App.class.getResource("/edu/wpi/teamo/fxml/CSS/MainPage2.css").toExternalForm();

  @Override
  public void init() throws SQLException {
    pagePaths.put(Pages.SERVICEREQUEST, "/edu/wpi/teamo/fxml/serviceRequestHubPage.fxml");
    pagePaths.put(Pages.MAPEDITOR, "/edu/wpi/teamo/fxml/MapEditorPage.fxml");
    pagePaths.put(Pages.PATHFINDING, "/edu/wpi/teamo/fxml/PathfindingPage.fxml");
    pagePaths.put(Pages.MEDICINE, "/edu/wpi/teamo/fxml/SR07_Medicine.fxml");
    pagePaths.put(Pages.SANITATION, "/edu/wpi/teamo/fxml/SR03_Sanitation.fxml");
    pagePaths.put(Pages.MANAGEREQUESTS, "/edu/wpi/teamo/fxml/ManageRequests.fxml");
    pagePaths.put(Pages.LOGIN, "/edu/wpi/teamo/fxml/LoginPage.fxml");
    pagePaths.put(Pages.LANGUAGEINTERPRETER, "/edu/wpi/teamo/fxml/SR02_LanguageInterpreter.fxml");
    pagePaths.put(Pages.RELIGIOUS, "/edu/wpi/teamo/fxml/SR08_Religious.fxml");
    pagePaths.put(Pages.MAINTENANCE, "/edu/wpi/teamo/fxml/SR12_MaintenancePage.fxml");
    pagePaths.put(Pages.SURVEY, "/edu/wpi/teamo/fxml/CovidSurveyPage.fxml");
    pagePaths.put(Pages.SECURITY, "/edu/wpi/teamo/fxml/SR11_Security.fxml");
    pagePaths.put(Pages.LAUNDRY, "/edu/wpi/teamo/fxml/SR04_Laundry.fxml");
    pagePaths.put(Pages.TRANSPORTATION, "/edu/wpi/teamo/fxml/SR09_PatientTransportation.fxml");
    pagePaths.put(Pages.GIFTS, "/edu/wpi/teamo/fxml/SR05_Gift.fxml");
    pagePaths.put(Pages.FOOD, "/edu/wpi/teamo/fxml/SR01_FoodRequestPage.fxml");
    pagePaths.put(Pages.MAIN, "/edu/wpi/teamo/fxml/MainPage.fxml");
    pagePaths.put(Pages.MANAGEACCOUNTS, "/edu/wpi/teamo/fxml/ManageAccounts.fxml");
    pagePaths.put(Pages.ABOUT, "/edu/wpi/teamo/fxml/AboutPage.fxml");
    pagePaths.put(Pages.SETTINGS, "/edu/wpi/teamo/fxml/Settings.fxml");
    pagePaths.put(Pages.PROFILE, "/edu/wpi/teamo/fxml/ProfileInformationPage.fxml");
    pagePaths.put(Pages.CREDITS, "/edu/wpi/teamo/fxml/CreditsPage.fxml");
    pagePaths.put(Pages.INFO, "/edu/wpi/teamo/fxml/CovidInfoPage.fxml");

    themeMap.put(Theme.BLUE_SKY, "/edu/wpi/teamo/fxml/CSS/MainPage.css");
    themeMap.put(Theme.CLOUDS, "/edu/wpi/teamo/fxml/CSS/MainPage2.css");
    themeMap.put(Theme.HOLIDAY, "/edu/wpi/teamo/fxml/CSS/holiday.css");
    themeMap.put(Theme.DARK, "/edu/wpi/teamo/fxml/CSS/dark.css");
    themeMap.put(Theme.WONG, "/edu/wpi/teamo/fxml/CSS/wong.css");

    bgMap.put(Theme.BLUE_SKY, "/edu/wpi/teamo/images/sky2.png");
    bgMap.put(Theme.CLOUDS, "/edu/wpi/teamo/images/clouds.jpg");
    bgMap.put(Theme.HOLIDAY, "/edu/wpi/teamo/images/snow.jpg");
    bgMap.put(Theme.DARK, "/edu/wpi/teamo/images/night.jpg");
    bgMap.put(Theme.WONG, "/edu/wpi/teamo/images/hospitalbgfade.png");




    System.out.println("Starting Up");

    /* instantiate the database services, set to static variables that can be accessed from the handlers */
    try {
      Database.init();
      requestService = new RequestDB(Database.getInstance());
      mapService = new MapDB(Database.getInstance());

      System.out.println("Database Services Initialized");
    } catch (SQLException e) {
        System.out.println("ERROR: FAILED TO INIT DATABASE SERVICES");
        e.printStackTrace();
    } catch (Exception exception){
      System.out.println("ERROR: GUEST LOGIN FAILED");
      exception.printStackTrace();
    }


    /* instantiate the aStar service, set to a static variable that can be accessed from the handlers */
    if (mapService != null) {

      context = new Context(new BFSManager(mapService), new DFSManager(mapService), new AStarManager(mapService), new BestFirstManager(mapService), new DijkstraManager(mapService), new GreedyDFSManager(mapService));

      System.out.println("Pathfinder Service Initialized");

    }
  }

  @Override
  public void start(Stage primaryStage) {
    App.primaryStage = primaryStage;
    Locale defaultLocale = new Locale("en","US");
    resourceBundle = ResourceBundle.getBundle(localesPath + "en_US", defaultLocale);
    selectedLocale = LocaleType.en_US;
    try {
      Parent root = FXMLLoader.load(getClass().getResource("fxml/MainPage.fxml"), resourceBundle);
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.setMaximized(true);
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
    }
    loadImages();// is called from the map class
    /*
      display the loading screeen here
     */
  }

  public static Stage getPrimaryStage(){
    return primaryStage;
  }

  public static void setPrimaryStage(Stage newPrimaryStage) {
    primaryStage = newPrimaryStage;
  }

  public static void switchPage(Pages page) {
    String pagePath = pagePaths.get(page);
    try {
      Parent root = FXMLLoader.load(App.class.getResource(pagePath),resourceBundle);
      App.getPrimaryStage().getScene().setRoot(root);
      App.getPrimaryStage().getScene().getStylesheets().clear();
      App.getPrimaryStage().getScene().getStylesheets().add(getCSSPath(Session.getAccount().getTheme()));
      System.out.println(getCSSPath(Session.getAccount().getTheme()));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static void switchTheme(Scene root, String theme) {

    root.getStylesheets().clear();
    root.getStylesheets().add(theme);

  }

  public static String getPagePath(Pages page) {
    return pagePaths.get(page);
  }

  public static String getCSSPath(Theme theme) { return themeMap.get(theme); }

  public static String getImagePath(Theme theme) { return bgMap.get(theme); }

  /**
   * Switch between different languages, falls back to en_US if lang or country parameters are malformed
   * @param lang language code (lower case)
   * @param country country code (upper case)
   * @param type Selected locale type
   */
  public static void switchLocale(String lang, String country, LocaleType type) {
    Locale locale = new Locale(lang, country);
    try {
      resourceBundle = ResourceBundle.getBundle(localesPath + lang + "_" + country, locale);
      selectedLocale = type;
    }
    catch(Exception e) {
      resourceBundle = ResourceBundle.getBundle(localesPath + "en_US", locale);
      selectedLocale = LocaleType.en_US;
    }
  }

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }

  public static void showError(String message, StackPane stackPane) {
    JFXDialogLayout content = new JFXDialogLayout();
    content.setHeading(new Label(App.resourceBundle.getString("key.error")));
    content.setBody(new Label(message));
    JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
    JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
//    closeButton.setStyle("-fx-background-color: rgb(128,232,255)");
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
