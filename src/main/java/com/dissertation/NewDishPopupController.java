package com.dissertation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.dissertation.server_side.Message;
import com.dissertation.server_side.Protocol;

import java.io.IOException;

public class NewDishPopupController {

  @FXML
  private Pane      masterPane;
  @FXML
  private Label     confirmation;
  @FXML
  private TextField newDishName;

  Protocol p=new Protocol();

  //user enters the name and it is added to dish
  public void saveName(){
    //get the calories in
    Message message=new Message("get-tempDish-calories");
    p.protocolToUse(message);
    int totalCals=Integer.parseInt(p.getM().getMessage());

    String dishName=newDishName.getText();

    if (dishName.isBlank()){
      confirmation.setText("Please enter a dish name");
      return;
    }

    String dishNameDespaced=AddDishController.despacer(dishName);
    Message message3=
        new Message("save-new-tempDish"+" "+dishNameDespaced+" "+totalCals);
    p.protocolToUse(message3);

    try{
    Main main=new Main();
    AddDishController ad=(AddDishController) main.getLoader("addDish");
    ad.deleteDish();
    openNewWindow("/dissertation/fxml/addDish.fxml");
    }catch (IOException e){e.printStackTrace();}
  }

  public void openNewWindow(String window) {
    //to hide
    Stage stage;
    stage = (Stage) masterPane.getScene().getWindow();
    stage.hide();
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
