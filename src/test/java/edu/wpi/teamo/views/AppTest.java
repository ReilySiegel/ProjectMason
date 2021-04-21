package edu.wpi.teamo.views;

import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.teamo.App;
import edu.wpi.teamo.views.LocaleType;
import javafx.scene.Node;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import static org.junit.jupiter.api.Assertions.*;
/**
 * This is an integration test for the entire application. Rather than running a single scene
 * individually, it will run the entire application as if you were running it.
 */
@ExtendWith(ApplicationExtension.class)
public class AppTest extends FxRobot {

  /** Setup test suite. */
  @BeforeAll
  public static void setup() throws Exception {
    //Toolkit.registerPrimaryStage();
    //FxToolkit.setupApplication(App.class);
  }

  @AfterAll
  public static void cleanup() {}

  @Test
  public void testButton() {
    //verifyThat("Scene 1", AlgoNode::isVisible);
    //clickOn("Advance Scene");
    //verifyThat("Scene 2", AlgoNode::isVisible);
    //clickOn("Advance Scene");
    //verifyThat("Scene 3", AlgoNode::isVisible);
    //clickOn("Advance Scene");
    //verifyThat("Scene 4", AlgoNode::isVisible);
    //clickOn("Advance Scene");
    //verifyThat("Scene 1", AlgoNode::isVisible);
  }

  /**
   * Backend test for switching languages to another language
   */
  @Test
  public void testArbitraryLanguageSwitch() {
    App.switchLocale("es", "ES", LocaleType.es_ES, true);
    assertEquals(App.selectedLocale, LocaleType.es_ES);
  }

  /**
   * Backend test for switching back to English
   */
  @Test
  public void testLanguageSwitchBack() {
    App.switchLocale("en", "US", LocaleType.en_US, true);
    assertEquals(App.selectedLocale, LocaleType.en_US);
  }
  /**
   * Test for malformed lang and country codes
   */
  @Test
  public void testMalformedLanguageSwitch() {
    App.switchLocale("null", "null", LocaleType.es_ES, true);
    assertEquals(App.selectedLocale, LocaleType.en_US);
  }

}
