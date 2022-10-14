package com.dissertation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.dissertation.server_side.Message;
import com.dissertation.server_side.Protocol;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DetailsController implements Initializable {

  Protocol p=new Protocol();
  private static final String SPACE=" ";

  @FXML
  private GridPane masterPane;
  @FXML
  private Label heightBox;
  @FXML
  private TextField heightText;
  @FXML
  private Label weightBox;
  @FXML
  private TextField weightText;
  @FXML
  private Label calorieBox;
  @FXML
  private TextField calorieText;

  public void changeHeight(ActionEvent event) {
    Message message=new Message("change-height"+SPACE+heightText.getText());
    p.protocolToUse(message);
    heightBox.setText(p.getM().getMessage());
    }

  public void changeWeight(ActionEvent event) {
    Message message=new Message("change-weight"+SPACE+weightText.getText());
    p.protocolToUse(message);
    weightBox.setText(p.getM().getMessage());
  }

  public void changeCalorieGoal(ActionEvent event){
    Message message=
        new Message("change-calorie_goal"+SPACE+calorieText.getText());
    p.protocolToUse(message);
    calorieBox.setText(p.getM().getMessage());
  }

  public void goBack(ActionEvent event){
    openNewWindow("dissertation/fxml//body.fxml");
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

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Message message=new Message("get-details");
    p.protocolToUse(message);
    ArrayList<ArrayList<String>>data=p.getM().getData();
    heightBox.setText(data.get(0).get(0));
    weightBox.setText(data.get(0).get(1));
    calorieBox.setText(data.get(0).get(2));
  }
}
