package com.dissertation;

import com.dissertation.server_side.Dish;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.dissertation.server_side.Message;
import com.dissertation.server_side.Protocol;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BodyController implements Initializable {

  private final String SPACE = " ";
  private String       mealName;
  private Protocol                             p              = new Protocol();
  private ArrayList<TableView<Dish>>           mealTableArray =new ArrayList<>();
  private ArrayList<TableColumn<Dish,Integer>> colCal         =new ArrayList<>();
  private ArrayList<TableColumn<Dish,Integer>>colAm=new ArrayList<>();
  private final String BREAKFAST="breakfast";
  private final String DINNER="dinner";
  private final String TEA="tea";
  private final String SNACK="snack";
  private final String PUDDING="pudding";

  @FXML
  private Label indication;
  @FXML
  private ProgressBar                progressBar;
  @FXML
  private Pane                       pane;
  @FXML
  private Label                      calorieGoal;
  /*==================BREAKFAST=====================*/
  @FXML
  private Label                      breakfastCaloriesTotal;
  @FXML
  private Label                      breakfastVeganLabel;
  @FXML
  private TableView<Dish>            breakfastTable;
  @FXML
  private TableColumn<Dish, String>  breakfastName;
  @FXML
  private TableColumn<Dish, String> breakfastVegan;
  @FXML
  private TableColumn<Dish, Integer> breakfastAmount;
  @FXML
  private TableColumn<Dish, Integer>  breakfastCalories;
  private ObservableList<Dish>       breakfastList =
      FXCollections.observableArrayList();
  /*===================DINNER========================*/
  @FXML
  private Label                      dinnerCaloriesTotal;
  @FXML
  private Label                      dinnerVeganLabel;
  @FXML
  private TableView<Dish>            dinnerTable;
  @FXML
  private TableColumn<Dish, String>  dinnerName;
  @FXML
  private TableColumn<Dish, String> dinnerVegan;
  @FXML
  private TableColumn<Dish, Integer> dinnerAmount;
  @FXML
  private TableColumn<Dish, Integer>  dinnerCalories;
  private ObservableList<Dish>       dinnerList    =
      FXCollections.observableArrayList();
  /*================TEA============================*/
  @FXML
  private Label                      teaCaloriesTotal;
  @FXML
  private Label                      teaVeganLabel;
  @FXML
  private TableView<Dish>            teaTable;
  @FXML
  private TableColumn<Dish, String>  teaName;
  @FXML
  private TableColumn<Dish, String> teaVegan;
  @FXML
  private TableColumn<Dish, Integer> teaAmount;
  @FXML
  private TableColumn<Dish, Integer>  teaCalories;
  private ObservableList<Dish>       teaList       =
      FXCollections.observableArrayList();
  /*================SNACK==========================*/
  @FXML
  private Label                      snackCaloriesTotal;
  @FXML
  private Label                      snackVeganLabel;
  @FXML
  private TableView<Dish>            snackTable;
  @FXML
  private TableColumn<Dish, String>  snackName;
  @FXML
  private TableColumn<Dish, String> snackVegan;
  @FXML
  private TableColumn<Dish, Integer> snackAmount;
  @FXML
  private TableColumn<Dish, Integer>  snackCalories;
  private ObservableList<Dish>       snackList     =
      FXCollections.observableArrayList();
  /*================PUDDING=======================*/
  @FXML
  private Label                      puddingCaloriesTotal;
  @FXML
  private Label                      puddingVeganLabel;
  @FXML
  private TableView<Dish>            puddingTable;
  @FXML
  private TableColumn<Dish, String>  puddingName;
  @FXML
  private TableColumn<Dish, String> puddingVegan;
  @FXML
  private TableColumn<Dish, Integer> puddingAmount;
  @FXML
  private TableColumn<Dish, Integer>  puddingCalories;
  private ObservableList<Dish>       puddingList   =
      FXCollections.observableArrayList();
  /*==============================================*/

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    //set up the tables
    /*==================BREAKFAST=====================*/
    breakfastName.setCellValueFactory(new PropertyValueFactory<>("name"));
    breakfastVegan.setCellValueFactory(new PropertyValueFactory<>(
        "veganString"));
    /*===================DINNER========================*/
    dinnerName.setCellValueFactory(new PropertyValueFactory<>("name"));
    dinnerVegan.setCellValueFactory(new PropertyValueFactory<>("veganString"));
    /*================TEA============================*/
    teaName.setCellValueFactory(new PropertyValueFactory<>("name"));
    teaVegan.setCellValueFactory(new PropertyValueFactory<>("veganString"));
    /*================PUDDING==========================*/
    puddingName.setCellValueFactory(new PropertyValueFactory<>("name"));
    puddingVegan.setCellValueFactory(new PropertyValueFactory<>("veganString"));
    /*================SNACK==========================*/
    snackName.setCellValueFactory(new PropertyValueFactory<>("name"));
    snackVegan.setCellValueFactory(new PropertyValueFactory<>("veganString"));
    /*===============================================*/

    colCal.add(breakfastCalories);
    colCal.add(dinnerCalories);
    colCal.add(teaCalories);
    colCal.add(puddingCalories);
    colCal.add(snackCalories);
    colCal.forEach((i)->{
      i.setCellFactory(data-> {
        String[] mealtime=i.getId().split("C",2);
        Label label=new Label();
        TableCell<Dish, Integer> cell =
            new TableCell<>() {
              @Override
              //this actually *displays* the cell on screen
              protected void updateItem(Integer num, boolean empty) {
                //this gets our current row and loads in the custom amount from
                // dish
                try {
                  int row = this.getTableRow().getIndex();
                  Dish dish =
                      this.getTableRow().getTableView().getItems().get(row);
                  //if we've given a custom portion that we've changed TODAY,
                  // we use it
                  Message message =
                      new Message("get-dish-custom-portion" +
                                  SPACE +
                                  dish.getId()+SPACE+mealtime[0]);
                  p.protocolToUse(message);
                  //got the portion
                  int portion = Integer.parseInt(p.getM().getMessage());
                  //if it has no value, we use the preset
                  if (portion==-1){
                    Message message3=
                        new Message("get-dish-preset-portion"+SPACE+dish.getId());
                    p.protocolToUse(message3);
                    portion=Integer.parseInt(p.getM().getMessage());
                  }
                  //now we get preset meal calories (the full amount)
                  Message message2 =
                      new Message("get-preset-calories" +
                                  SPACE +
                                  dish.getId());
                  p.protocolToUse(message2);
                  int calories=Integer.parseInt(p.getM().getMessage());
                  num=(portion*calories)/100;
                  label.setText(String.valueOf(num));
                } catch (IndexOutOfBoundsException e) {
//                e.printStackTrace();
                }
                //the part that does the actual setting on screen
                super.updateItem(num, empty);
                if (empty) {
                  setGraphic(null);
                } else {
                  label.setText(String.valueOf(num));
                  setGraphic(label);
                }
              }
            };
        return cell;
      });
    });

    colAm.add(breakfastAmount);
    colAm.add(dinnerAmount);
    colAm.add(teaAmount);
    colAm.add(puddingAmount);
    colAm.add(snackAmount);
    colAm.forEach((i)->{
      i.setCellFactory(TableCell-> {

        String[] mealtime=i.getId().split("A",2);
        TextField label=new TextField("DEBUG");

        TableCell<Dish, Integer> cell =
            new TableCell<>() {
              @Override
              //this actually *displays* the cell on screen
              protected void updateItem(Integer num, boolean empty) {
                try {
                  int row = this.getTableRow().getIndex();
                  Dish dish =
                      this.getTableRow().getTableView().getItems().get(row);
                  //get custom portion
                  Message message =
                      new Message("get-dish-custom-portion" +
                                  SPACE +
                                  dish.getId()+
                      SPACE+mealtime[0]);
                  p.protocolToUse(message);
                  int portion=Integer.parseInt(p.getM().getMessage());
                  //if we returned -1, i.e.no custom portion, we load default
                  if (portion==-1){
                    Message message1=
                        new Message("get-dish-preset-portion"+SPACE+dish.getId());
                    p.protocolToUse(message1);
                    portion=Integer.parseInt(p.getM().getMessage());
                    //set the portion in calories for today to preset portion
                          //sets the calories eaten in calories
                          /*this should be done in calCol, really*/
                    int caloriesEaten=((dish.getCalories()*portion)/100);
                    Message message3 =
                        new Message("set-dish-preset-portion" +
                                    SPACE +
                                    mealtime[0]+
                                    SPACE +
                                    dish.getId()+SPACE+portion+SPACE+caloriesEaten);
                    p.protocolToUse(message3);
                  }

                  num = portion;
                  label.setText(String.valueOf(num));

                    refreshBodyGUI();

                } catch (IndexOutOfBoundsException e) {
//                e.printStackTrace();
                }
                //the part that does the actual setting on screen
                super.updateItem(num, empty);
                if (empty) {
                  setGraphic(null);
                } else {
                  label.setText(String.valueOf(num));
                  setGraphic(label);
                }
              }
            };

        label.setEditable(true);

        //addListener
        label.textProperty().addListener((observableValue, o, n) -> {
          int row=cell.getTableRow().getIndex();
          ObservableList<Dish> list =
              cell.getTableRow().tableViewProperty().get().getItems();
          Dish dish=list.get(row);
          //error checking
          if(!AddDishController.isNumeric(label.getText())){
            label.setText("100");
          }
          //send the changed value
          int eaten =Integer.parseInt(n), calories =dish.getCalories();
          int caloriesEaten=eaten*calories/100;
          label.setOnKeyPressed(keyEvent -> {
            if ( keyEvent.getCode() == KeyCode.ENTER){
              dish.setPortion(caloriesEaten);
              Message message =
                  new Message("set-dish-preset-portion" +
                              SPACE +
                              mealtime[0]+
                              SPACE +
                              dish.getId()+SPACE+eaten+SPACE+caloriesEaten);
              p.protocolToUse(message);
              //refresh the tables
              mealTableArray.forEach(TableView::refresh);
            }
            refreshBodyGUI();
          });
        });
        return cell;
      });
    });

    //Set the tables to be doubleclickable and remove stuff
    mealTableArray.add(breakfastTable);
    mealTableArray.add(dinnerTable);
    mealTableArray.add(teaTable);
    mealTableArray.add(puddingTable);
    mealTableArray.add(snackTable);
//    the remove option
    mealTableArray.forEach((i)->{
      String[] s=i.getId().split("T",2);
      i.setOnMouseClicked(mouseEvent -> {
        int rowToDelete= i.getSelectionModel().getSelectedIndex();
        if (mouseEvent.getClickCount()==2){
          int itemNo=i.getItems().get(rowToDelete).getId();
          i.getItems().remove(rowToDelete);
          Message message=
              new Message("remove-from-today's-food" +
                          SPACE + itemNo +
                          SPACE + s[0]);
          p.protocolToUse(message);
          i.refresh();
          refreshBodyGUI();
        }
      });
    });

    try {
      setTables();
    }catch (NullPointerException e){e.printStackTrace();}

    if (isCalorieGoalSet(getCalories()))
      calorieGoal.setText(getCalories());
    else {
    }

    //the progressBar thresholds
    progressThresholds();

    refreshBodyGUI();
  }

  //  loads all the lists into the relevant tables and sets all the tables in
//  initialise
  /* this won't allow us to set multiples of the same item per meal time*/
  public void setTables() {
    List<Dish>listOfMealData=arrayListToDishConverter(getDataForTables());

    for (Dish dish : listOfMealData) {
      if (dish.getTime().equals("breakfast")) {
        breakfastList.add(dish);
      }
      if (dish.getTime().equals("dinner")) {
        dinnerList.add(dish);
      }
      if (dish.getTime().equals("tea")) {
        teaList.add(dish);
      }
      if (dish.getTime().equals("pudding")) {
        puddingList.add(dish);
      }
      if (dish.getTime().equals("snack")) {
        snackList.add(dish);
      }
      breakfastTable.setItems(breakfastList);
      dinnerTable.setItems(dinnerList);
      teaTable.setItems(teaList);
      puddingTable.setItems(puddingList);
      snackTable.setItems(snackList);
    }

  }

  //gets the data to set the tables in initialise
  public ArrayList<ArrayList<Dish>>getDataForTables(){
    Message message = new Message("set-the-tables");
    p.protocolToUse(message);
    return p.getM().getDishData();
  }

  public void changeDetails(ActionEvent event) {

    ((Node) event.getSource()).getScene().getWindow().hide();
    //so now we load the user page
    //we need a new stage
    Stage stage = new Stage();
    //make an object of it instead of just using FXMLLoader
    FXMLLoader fx = new FXMLLoader();
    //can't have more than one parent, so we use Pane
    try {
      Pane root =
          fx.load(getClass().getResource("dissertation/fxml//details.fxml").openStream());
      //make a new scene and set the stage with it
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void checkLog(ActionEvent event) {
    ((Node) event.getSource()).getScene().getWindow().hide();
    //so now we load the user page
    //we need a new stage
    Stage stage = new Stage();
    //make an object of it instead of just using FXMLLoader
    FXMLLoader fx = new FXMLLoader();
    //can't have more than one parent, so we use Pane
    try {
      Pane root =
          fx.load(getClass().getResource("dissertation/fxml//logBook.fxml").openStream());
      //make a new scene and set the stage with it
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //signs out
  public void signOut() {
    String  body    = "sign-out";
    Message message = new Message(body);
    p.protocolToUse(message);
    Platform.exit();
    System.exit(1);
  }

  //TODO all these change scene methods need to be redone without the static
  // variable
  public void addMeal() {
    openNewWindow("/dissertation/fxml/addDish.fxml");
  }

  public void addBreakfast() {
    try {
      Main main = new Main();
      mealName = "breakfast";
      AddDishController ad= (AddDishController) main.getLoader("addDish");
      ad.setMealTime(mealName);
      addMeal();
    }catch (IOException e){e.printStackTrace();}
  }

  public void addDinner() {
    try {
      Main main = new Main();
      mealName = "dinner";
      AddDishController ad= (AddDishController) main.getLoader("addDish");
      ad.setMealTime(mealName);
      addMeal();
    }catch (IOException e){e.printStackTrace();}
  }

  public void addTea() {
    try {
      Main main = new Main();
      mealName = "tea";
      AddDishController ad= (AddDishController) main.getLoader("addDish");
      ad.setMealTime(mealName);
      addMeal();
    }catch (IOException e){e.printStackTrace();}
  }

  public void addSnack() {
    try {
      Main main = new Main();
      mealName = "snack";
      AddDishController ad= (AddDishController) main.getLoader("addDish");
      ad.setMealTime(mealName);
      addMeal();
    }catch (IOException e){e.printStackTrace();}
  }

  public void addPudding() {
    try {
      Main main = new Main();
      mealName = "pudding";
      AddDishController ad= (AddDishController) main.getLoader("addDish");
      ad.setMealTime(mealName);
      addMeal();
    }catch (IOException e){e.printStackTrace();}
  }

  //gets the calorie goal of the user.
  //returns 0 if empty
  public String getCalories() {

    Message message = new Message("get-calories");
    p.protocolToUse(message);
    Message messageIn = p.getM();
    if (Integer.parseInt(messageIn.getMessage()) > 0) {
      return messageIn.getMessage();
    }
    return "0";
  }

  //returns a boolean if we set the calorie goal or not
  public boolean isCalorieGoalSet(String calories) {
    return !calories.equals("0");
  }

  public void openNewWindow(String window) {
    //to hide
    Stage stage;
    stage = (Stage) pane.getScene().getWindow();
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

  public void setLabelIsVegan(ObservableList<Dish> list, Label label) {
    if (list.isEmpty()) {
      label.setVisible(false);
    }
    label.setText("V");
    label.setStyle("-fx-text-fill: green;");

    for (Dish dish : list) {
      if (dish.isVegan() == 0) {
        label.setText("Ve");
        label.setStyle("-fx-text-fill: blue;");
      }
      label.setFont(Font.font("Courier New", FontWeight.BOLD,
                              FontPosture.REGULAR, 30));
      label.setVisible(true);
    }
  }

  //gets the meal times total calories
  public void getUpCalorieTotals() {
    Message message=new Message("get-calories-for-time"+SPACE+BREAKFAST);
    Message message2=new Message("get-calories-for-time"+SPACE+DINNER);
    Message message3=new Message("get-calories-for-time"+SPACE+TEA);
    Message message4=new Message("get-calories-for-time"+SPACE+SNACK);
    Message message5=new Message("get-calories-for-time"+SPACE+PUDDING);
    p.protocolToUse(message);
    breakfastCaloriesTotal.setText(p.getM().getMessage());
    p.protocolToUse(message2);
    dinnerCaloriesTotal.setText(p.getM().getMessage());
    p.protocolToUse(message3);
    teaCaloriesTotal.setText(p.getM().getMessage());
    p.protocolToUse(message4);
    snackCaloriesTotal.setText(p.getM().getMessage());
    p.protocolToUse(message5);
    puddingCaloriesTotal.setText(p.getM().getMessage());
  }

  //sets whether or not to show the meal time totals
  public void setCalorieTotals() {
    Label[] l = {breakfastCaloriesTotal, dinnerCaloriesTotal, teaCaloriesTotal,
                 snackCaloriesTotal, puddingCaloriesTotal};
    for (Label label : l) {
      if (!label.getText().equals("0")) {
        label.setVisible(true);
      }
      else {
        label.setVisible(false);
      }
    }

  }

  public void setUpProgressBar(){
    Message message=new Message("set-up-progress-bar");
    p.protocolToUse(message);
    progressBar.progressProperty().set(Double.parseDouble(p.getM().getMessage()));
    double x=Double.parseDouble(p.getM().getMessage());
    x=x*100;
    int i=(int)x;
    indication.setText(String.valueOf(i)+"%");
  }

  public List<Dish> arrayListToDishConverter(ArrayList<ArrayList<Dish>>dishes){
    List<Dish>listOfMealData=new ArrayList<>();
    for (ArrayList<Dish> dish:dishes) {
      listOfMealData.add(dish.get(0));
    }
    return listOfMealData;
  }

  public void setVeganLabels(){
    setLabelIsVegan(breakfastList, breakfastVeganLabel);
    setLabelIsVegan(dinnerList, dinnerVeganLabel);
    setLabelIsVegan(teaList, teaVeganLabel);
    setLabelIsVegan(snackList, snackVeganLabel);
    setLabelIsVegan(puddingList, puddingVeganLabel);
  }

  public void refreshBodyGUI(){
    setVeganLabels();
    getUpCalorieTotals();
    setCalorieTotals();
    setUpProgressBar();
    progressThresholds();
  }

  public void progressThresholds(){
    if (progressBar.getProgress()<0.33) {
      progressBar.setStyle("-fx-accent: green");
    }
    else if (progressBar.getProgress()<0.67) {
      progressBar.setStyle("-fx-accent: yellow");
    }
    else if (progressBar.getProgress()<1) {
      progressBar.setStyle("-fx-accent: orange");
    }
    else if (progressBar.getProgress()>=1)
      progressBar.setStyle("-fx-accent: red");
  }

  public void openEditDish(){
    openNewWindow("dissertation/fxml//editDish.fxml");
  }

}


