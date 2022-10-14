package com.dissertation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.dissertation.server_side.Message;
import com.dissertation.server_side.Protocol;

public class NewRegisterController {

  private       Protocol  p     = new Protocol();
  private final String    SPACE = " ";
  @FXML
  private       VBox      masterPane;
  @FXML
  private       Label     status;
  @FXML
  private       TextField height;
  @FXML
  private       TextField weight;
  @FXML
  private     TextField calorieGoal;

  public void register() {

    //set defaults for teh other values if they aren't used
    if (calorieGoal.getText().isBlank() ||
        weight.getText().isBlank() ||
        height.getText().isBlank()) {
      //check the values are numeric
      status.setText("Values must not be blank!");
      return;
    }

    try {
      int x = Integer.parseInt(calorieGoal.getText());
    } catch (NumberFormatException e) {
      status.setText("Values must be numeric!");
      return;
    }
    try {
      int x = Integer.parseInt(height.getText());
    } catch (NumberFormatException e) {
      status.setText("Values must be numeric!");
      return;
    }
    if (!AddDishController.isNumeric(weight.getText())) {
      status.setText("Values must be numeric!");
      return;
    }

    //now we make the message
    Message message =
        new Message("register" +
                    SPACE +
                    calorieGoal.getText() +
                    SPACE +
                    height.getText() +
                    SPACE +
                    weight.getText());
    try {
      p.protocolToUse(message);
    } catch (Exception e) {
      e.printStackTrace();
    }

    //change scene
    openNewWindow("/dissertation/fxml/body.fxml");

  }

  public void openNewWindow(String window) {
    //to hide
    Stage stage;
    stage = (Stage) masterPane.getScene().getWindow();
//    stage.hide();
    FXMLLoader loader = new FXMLLoader();
    try {
      Pane root =
          loader.load(getClass().getResource(window).openStream());
      Scene scene = new Scene(root);
//      scene.getStylesheets()
//           .add(getClass().getResource("application.css").toExternalForm());
      stage.setScene(scene);
    } catch (Exception e) {
      e.printStackTrace();
    }
    stage.show();
  }

}
