package edu.wpi.teamo;

import java.io.IOException;
import java.util.EnumMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class App extends Application {

  private static Stage primaryStage;
  private static final EnumMap<Pages, String> pagePaths = new EnumMap<>(Pages.class);



  @Override
  public void init() {
    pagePaths.put(Pages.MEDICINE, "/edu/wpi/teamname/fxml/SR03_MedicinePage.fxml");
    pagePaths.put(Pages.NODE, "/edu/wpi/teamname/fxml/NodePage.fxml");
    pagePaths.put(Pages.EDGE, "/edu/wpi/teamname/fxml/EdgePage.fxml");
    pagePaths.put(Pages.PATHFINDING, "/edu/wpi/teamname/fxml/PathfindingPage.fxml");
    pagePaths.put(Pages.MAIN, "/edu/wpi/teamname/fxml/MainPage.fxml");
    System.out.println("Starting Up");
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

  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}
