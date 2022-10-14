package com.dissertation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.dissertation.server_side.Message;
import com.dissertation.server_side.Protocol;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SelectItemController implements Initializable {

  @FXML
  private Label blankMessage;
  @FXML
  private Pane                       masterPane;
  @FXML
  private Button search;

  @FXML
  private GridPane choiceTable;
  @FXML
  private GridPane bakery;
  @FXML
  private GridPane freshFood;
  @FXML
  private GridPane drinks;
  @FXML
  private GridPane foodCupboard;
  @FXML
  private GridPane frozenReady;
  @FXML
  private GridPane chilledFood;

  @FXML
  private GridPane pane1;
  @FXML
  private GridPane pane2;
  @FXML
  private GridPane pane3;
  @FXML
  private GridPane pane4;
  @FXML
  private GridPane pane5;
  @FXML
  private GridPane pane6;
  @FXML
  private GridPane pane7;

  @FXML
  private Label choice1;
  @FXML
  private Label choice2;
  @FXML
  private Label choice3;
  @FXML
  private Label choice4;
  @FXML
  private Label choice5;
  @FXML
  private Label choice6;
  @FXML
  private Label choice7;

  @FXML
  private TextField       searchBar;

  private Protocol                            p        =new Protocol();

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    search.setOnMouseClicked(MouseEvent->{
      search(searchBar.getText());
    });

    //if we leave the choiceTable, the column collapses
    choiceTable.setOnMouseExited(mouseEvent -> {
      defaultView();
    });
    /*////////////////////////////////////////////////////////////////////////////////////*/
    bakery.setOnMouseEntered(mouseEvent -> {
      showChoices();
      choice1.setText("Bread");
      choice1.setVisible(true);
      choice2.setText("Cakes");
      choice2.setVisible(true);
    });
    freshFood.setOnMouseEntered(mouseEvent -> {
      showChoices();
      choice1.setText("Fruit, Veg and Pulses");
      choice1.setVisible(true);
      choice2.setText("\"Meat\"");
      choice2.setVisible(true);
      choice3.setText("Nuts");
      choice3.setVisible(true);
    });
    drinks.setOnMouseEntered(MouseEvent->{
      showChoices();
      choice1.setText("Non-Alcoholic");
      choice1.setVisible(true);
      choice2.setText("Alcoholic");
      choice2.setVisible(true);
    });
    foodCupboard.setOnMouseEntered(MouseEvent->{
      showChoices();
      choice1.setText("Cereal");
      choice1.setVisible(true);
      choice2.setText("Chocolate, Crisps and Biscuits");
      choice2.setVisible(true);
      choice3.setText("Sauces, Oil and Dressings");
      choice3.setVisible(true);
      choice4.setText("Pasta");
      choice4.setVisible(true);
      choice5.setText("Sugar and Preserves");
      choice5.setVisible(true);
      choice6.setText("Seasoning");
      choice6.setVisible(true);
      choice7.setText("Snacks");
      choice7.setVisible(true);
    });
    frozenReady.setOnMouseEntered(MouseEvent->{
      showChoices();
      choice1.setText("Frozen Food");
      choice1.setVisible(true);
      choice2.setText("Ready Meals");
      choice2.setVisible(true);
    });
    chilledFood.setOnMouseEntered(MouseEvent->{
      showChoices();
      choice1.setText("Cheese");
      choice1.setVisible(true);
      choice2.setText("Milk and Eggs");
      choice2.setVisible(true);
      choice3.setText("Spread");
      choice3.setVisible(true);
    });
    /*////////////////////////////////////////////////////////////////////////////////////*/
    //if we press the panes, we go to the item selector screen
    pane1.setOnMouseClicked(MouseEvent-> {
      loadItemChooser(choice1.getText().toLowerCase());
    });
    pane2.setOnMouseClicked(MouseEvent-> {
      loadItemChooser(choice2.getText().toLowerCase());
    });
    pane3.setOnMouseClicked(MouseEvent-> {
      loadItemChooser(choice3.getText().toLowerCase());
    });
    pane4.setOnMouseClicked(MouseEvent-> {
      loadItemChooser(choice4.getText().toLowerCase());
    });
    pane5.setOnMouseClicked(MouseEvent-> {
      loadItemChooser(choice5.getText().toLowerCase());
    });
    pane6.setOnMouseClicked(MouseEvent-> {
      loadItemChooser(choice6.getText().toLowerCase());
    });
    pane7.setOnMouseClicked(MouseEvent-> {
      loadItemChooser(choice7.getText().toLowerCase());
    });
  }

  //resets the table clear of columns
  private void defaultView() {
    pane1.setVisible(false);
    pane2.setVisible(false);
    pane3.setVisible(false);
    pane4.setVisible(false);
    pane5.setVisible(false);
    pane6.setVisible(false);
    pane7.setVisible(false);
  }

  public void goBack(){
    openNewWindow("/dissertation/fxml//addDish.fxml");
  }

  //shows the columns of the choices
  public void showChoices(){
    choice1.setText("");
    pane1.setVisible(true);
    pane1.setGridLinesVisible(true);
    choice2.setText("");
    pane2.setVisible(true);
    pane2.setGridLinesVisible(true);
    choice3.setText("");
    pane3.setVisible(true);
    pane3.setGridLinesVisible(true);
    choice4.setText("");
    pane4.setVisible(true);
    pane4.setGridLinesVisible(true);
    choice5.setText("");
    pane5.setVisible(true);
    pane5.setGridLinesVisible(true);
    choice6.setText("");
    pane6.setVisible(true);
    pane6.setGridLinesVisible(true);
    choice7.setText("");
    pane7.setVisible(true);
    pane7.setGridLinesVisible(true);
  }

  //sets the type in ItemChooser to be the type extracted from the box
  public void loadItemChooser(String type){
    if (type.isEmpty()){
      return;
    }
    Message message = new Message("itemChooser-table" + " " + type);
    p.protocolToUse(message);
    Message messageIn = p.getM();
    URL location =
        getClass().getResource("/dissertation/fxml/ItemChooser.fxml");
    FXMLLoader            loader = new FXMLLoader(location);
    ItemChooserController icc    = new ItemChooserController(messageIn.getItemData());
    loader.setController(icc);
    try {
      Parent root = loader.load();
      Stage  stage;
      stage = (Stage) masterPane.getScene().getWindow();
      stage.hide();
      Main.openNewWindow(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //opens up the icc page with results filtered by our keyword
  public void search(String keyword){
    if (keyword.isBlank()){
      return;
    }
    Message message=new Message("itemChooser-table-keyword"+" "+keyword);
    p.protocolToUse(message);
    Message messageIn=p.getM();
    if (messageIn.getMessage().equals("no-item-list-returned")) {
      blankMessage.setVisible(true);
      return;
    }
    URL location =
        getClass().getResource("/dissertation/fxml/ItemChooser.fxml");
    FXMLLoader            loader = new FXMLLoader(location);
    ItemChooserController icc    = new ItemChooserController(messageIn.getItemData());
    loader.setController(icc);

    try {
      Parent root = loader.load();
      Stage  stage;
      stage = (Stage) masterPane.getScene().getWindow();
      stage.hide();
      Main.openNewWindow(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
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
