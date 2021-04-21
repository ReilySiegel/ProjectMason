package edu.wpi.teamo.views;

import static org.testfx.api.FxAssert.verifyThat;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import edu.wpi.teamo.views.LocaleType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

public class Scene1Test extends ApplicationTest {

  //Internationalization
  private static ResourceBundle resourceBundle;
  public static LocaleType selectedLocale;
  private static final String localesPath = "edu.wpi.teamo.locales.";
//
//  /** Setup test suite. */
//  @BeforeAll
//  public static void setup() throws Exception {
//    FxToolkit.registerPrimaryStage();
//    FxToolkit.setupApplication(App.class);
//    System.getProperties().put("testfx.robot", "glass");
//  }
//
//  @Override
//  public void start(Stage primaryStage) throws IOException {
//    Locale defaultLocale = new Locale("en","US");
//    resourceBundle = ResourceBundle.getBundle(localesPath + "en_US", defaultLocale);
//    selectedLocale = LocaleType.en_US;
//    App.setPrimaryStage(primaryStage);
//    Parent root = FXMLLoader.load(getClass().getResource("fxml/MainPage.fxml"),resourceBundle);
//    Scene scene = new Scene(root);
//    primaryStage.setScene(scene);
//    primaryStage.show();
//  }
//
//  @Test
//  public void testButton() {
//    verifyThat("Home Page", Node::isVisible);
//    clickOn("Map Editor");
//    //clickOn("Service Requests");
//  }
}
