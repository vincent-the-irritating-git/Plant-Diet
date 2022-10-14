package com.dissertation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.dissertation.server_side.Message;
import com.dissertation.server_side.Protocol;

import java.net.URL;
import java.util.ResourceBundle;

public class DirectDishController implements Initializable {

  private final String SPACE=" ";
  Protocol p=new Protocol();

  @FXML
  private GridPane  masterPane;
  @FXML
  private TextField dishName;
  @FXML
  private TextField dishCalories;
  @FXML
  private ChoiceBox<String> veganChoice;
  @FXML
  private Label             label;

  public void saveDish(){
    if(!checkValues()){
      return;
    }
    Message message=
        new Message("custom-dish"+SPACE+AddDishController.despacer(dishName.getText())+SPACE+dishCalories.getText()+SPACE+
                    veganChoice.getValue());
    p.protocolToUse(message);
    openNewWindow("dissertation/fxml//body.fxml");
  }

  public boolean checkValues() {
    if ((dishName.getText().isBlank()) ||
        (dishCalories.getText().isBlank()) ||
        (veganChoice.getValue().isBlank())) {
      label.setText("Invalid input, fields cannot be blank!");
      return false;
    }
    if (!AddDishController.isNumeric(dishCalories.getText())){
      label.setText("Invalid input, calories must be numeric!");
      return false;
    }
    return true;
  }

  //for Junit
  public boolean checkValues(String a,String b, String c) {
    if ((a.isBlank()) ||
        (b.isBlank()) ||
        (c.isBlank())) {
      return false;
    }
    if (!AddDishController.isNumeric(b)){
      return false;
    }
    return true;
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
    ObservableList<String>list= FXCollections.observableArrayList();
    list.add("Vegan");
    list.add("Vegetarian");
    veganChoice.setItems(list);
  }
}
