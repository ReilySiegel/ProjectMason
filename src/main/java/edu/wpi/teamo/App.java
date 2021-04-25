package edu.wpi.teamo;

import edu.wpi.teamo.database.request.IRequestService;
import edu.wpi.teamo.database.request.RequestDB;
import edu.wpi.teamo.database.map.IMapService;
import edu.wpi.teamo.algos.AStarManager;
import edu.wpi.teamo.algos.AStarService;
import edu.wpi.teamo.database.map.MapDB;
import edu.wpi.teamo.database.Database;
import edu.wpi.teamo.views.LocaleType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import java.sql.SQLException;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.*;

import static edu.wpi.teamo.views.Map.loadImages;


public class App extends Application {

  private static final EnumMap<Pages, String> pagePaths = new EnumMap<>(Pages.class);
  public static IRequestService requestService = null;
  public static AStarService aStarService = null;
  public static IMapService mapService = null;
  private static Stage primaryStage;

  //Internationalization
  private static ResourceBundle resourceBundle;
  public static LocaleType selectedLocale;
  private static final String localesPath = "edu.wpi.teamo.locales.";

  @Override
  public void init() {
    pagePaths.put(Pages.SERVICEREQUEST,"/edu/wpi/teamo/fxml/ServiceRequestPage.fxml");
    pagePaths.put(Pages.MAPEDITOR,"/edu/wpi/teamo/fxml/MapEditorPage.fxml");
    pagePaths.put(Pages.PATHFINDING, "/edu/wpi/teamo/fxml/PathfindingPage.fxml");
    pagePaths.put(Pages.MEDICINE, "/edu/wpi/teamo/fxml/SR07_Medicine.fxml");
    pagePaths.put(Pages.SANITATION, "/edu/wpi/teamo/fxml/Sanitation.fxml");
    pagePaths.put(Pages.MANAGEREQUESTS, "/edu/wpi/teamo/fxml/ManageRequests.fxml");
    pagePaths.put(Pages.MAIN, "/edu/wpi/teamo/fxml/MainPage.fxml");
    pagePaths.put(Pages.LOADINGSCREEN,"edu/wpi/teamo/fxml/LoadingScreen.fxml");
    pagePaths.put(Pages.LOGIN, "/edu/wpi/teamo/fxml/LoginPage.fxml");
    pagePaths.put(Pages.LANGUAGEINTERPRETER, "/edu/wpi/teamo/fxml/SR02_LanguageInterpreter.fxml");
    System.out.println("Starting Up");

    /* instantiate the database services, set to static variables that can be accessed from the handlers */
    try {
      Database db = new Database();
      requestService = new RequestDB(db);
      mapService = new MapDB(db);
      System.out.println("Database Services Initialized");
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("ERROR: FAILED TO INIT DATABASE SERVICES");
      e.printStackTrace();
    }

    /* instantiate the aStar service, set to a static variable that can be accessed from the handlers */
    if (mapService != null) {
      aStarService = new AStarManager(mapService);
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
      Parent root = FXMLLoader.load(getClass().getResource("fxml/MainPage.fxml"),resourceBundle);
      Scene scene = new Scene(root);
      scene.getStylesheets().add("edu/wpi/teamo/fxml/CSS/HelpButton.css");
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
    }
    /*
      display the loading screeen here
     */
    loadImages();// is called from the map class
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
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Switch between different languages, falls back to en_US if lang or country parameters are malformed
   * @param lang language code (lower case)
   * @param country country code (upper case)
   * @param type Selected locale type
   * @param isDebug extra parameter for determining if the function call is for a test
   */
  public static void switchLocale(String lang, String country, LocaleType type, boolean isDebug) {
    Locale locale = new Locale(lang, country);
    try {
      resourceBundle = ResourceBundle.getBundle(localesPath + lang + "_" + country, locale);
      selectedLocale = type;
    }
    catch(Exception e) {
      resourceBundle = ResourceBundle.getBundle(localesPath + "en_US", locale);
      selectedLocale = LocaleType.en_US;
    }
    //Update main page
    if(!isDebug) switchPage(Pages.MAIN);
  }

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}
