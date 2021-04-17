package edu.wpi.teamo;

import edu.wpi.teamo.database.request.IRequestService;
import edu.wpi.teamo.database.request.RequestDB;
import edu.wpi.teamo.database.map.IMapService;
import edu.wpi.teamo.algos.AStarManager;
import edu.wpi.teamo.algos.AStarService;
import edu.wpi.teamo.database.map.MapDB;
import edu.wpi.teamo.database.Database;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import java.sql.SQLException;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.EnumMap;

public class App extends Application {

  private static final EnumMap<Pages, String> pagePaths = new EnumMap<>(Pages.class);
  public static IRequestService requestService = null;
  public static AStarService aStarService = null;
  public static IMapService mapService = null;
  public static IMapService dbService = null; // <- TODO legacy symbol, remove when no longer used
  private static Stage primaryStage;


  @Override
  public void init() {
    pagePaths.put(Pages.PATHFINDING, "/edu/wpi/teamo/fxml/PathfindingPage.fxml");
    pagePaths.put(Pages.MEDICINE, "/edu/wpi/teamo/fxml/SR07_Medicine.fxml");
    pagePaths.put(Pages.SANITATION, "/edu/wpi/teamo/fxml/Sanitation.fxml");
    pagePaths.put(Pages.NODE, "/edu/wpi/teamo/fxml/MapEditorPage.fxml");
    pagePaths.put(Pages.EDGE, "/edu/wpi/teamo/fxml/EdgePage.fxml");
    pagePaths.put(Pages.MAIN, "/edu/wpi/teamo/fxml/MainPage.fxml");
    System.out.println("Starting Up");

    /* instantiate the database services, set to static variables that can be accessed from the handlers */
    try {
      Database db = new Database();
      requestService = new RequestDB(db);
      mapService = new MapDB(db);
      dbService = mapService; //see declaration
      System.out.println("Database Services Initialized");
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("ERROR: FAILED TO INIT DATABASE SERVICES");
      e.printStackTrace();
      dbService = null;
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
    try {
      Parent root = FXMLLoader.load(getClass().getResource("fxml/MainPage.fxml"));
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
    }
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
      Parent root = FXMLLoader.load(App.class.getResource(pagePath));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}
