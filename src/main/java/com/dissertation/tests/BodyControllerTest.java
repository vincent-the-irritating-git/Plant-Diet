package com.dissertation.tests;

import com.dissertation.BodyController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class BodyControllerTest {

  BodyController bc;

  @BeforeEach
  void setUp() throws IOException {
    bc=new BodyController();
  }

  @Test
  @DisplayName("checks if we should display a value for the calorie goal")
  void isCalorieGoalSet() {
    assertFalse(bc.isCalorieGoalSet("0"));
    assertTrue(bc.isCalorieGoalSet("2000"));
  }


}