package edu.wpi.teamo;

import edu.wpi.teamo.map.database.IMapService;
import edu.wpi.teamo.map.database.MapDB;
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
  public static IMapService dbService = null;
  private static Stage primaryStage;


  @Override
  public void init() {
    pagePaths.put(Pages.MEDICINE, "/edu/wpi/teamo/fxml/SR07_Medicine.fxml");
    pagePaths.put(Pages.NODE, "/edu/wpi/teamo/fxml/NodePage.fxml");
    pagePaths.put(Pages.EDGE, "/edu/wpi/teamo/fxml/EdgePage.fxml");
    pagePaths.put(Pages.PATHFINDING, "/edu/wpi/teamo/fxml/PathfindingPage.fxml");
    pagePaths.put(Pages.MAIN, "/edu/wpi/teamo/fxml/MainPage.fxml");
    System.out.println("Starting Up");

    /* instantiate the map service, set to a static variable that can be accessed from the handlers */
    try {
      dbService = new MapDB();
      System.out.println("Database Initialized");
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("ERROR: FAILED TO INIT DATABASE");
      e.printStackTrace();
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
