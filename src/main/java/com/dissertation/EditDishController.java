package com.dissertation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.dissertation.misc.Quantities;
import com.dissertation.server_side.Dish;
import com.dissertation.server_side.Item;
import com.dissertation.server_side.Message;
import com.dissertation.server_side.Protocol;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class EditDishController implements Initializable {

  //====================================================================
  @FXML
  private Button                    goBack;
  @FXML
  private Pane                      masterPane;
  @FXML
  private TextField                 searchBar;
  @FXML
  private TableView<Dish>           results;
  @FXML
  private TableColumn<Dish, String> name     = new TableColumn<>();
  @FXML
  private TableColumn<Dish, String> isVegan  = new TableColumn<>();
  @FXML
  private TableColumn<Dish, String> calories = new TableColumn<>();

  ////////////////////////////NEW DISH////////////////////////////////
  //==================================================================
  private ObservableList<Item>      itemDishList =
      FXCollections.observableArrayList();
  @FXML
  private TableView<Item>           newDishTable;
  @FXML
  private TableColumn<Item, String> itemName     =
      new TableColumn<>();
  @FXML
  private TableColumn<Item, String> itemAmount   = new TableColumn<>();
  @FXML
  private TableColumn<Item, Number> itemCalories =
      new TableColumn<>();
  @FXML
  private TableColumn<Item, String> itemVegan    =
      new TableColumn<>();
  //===================================================================

  private final  String   SPACE      = " ";
  private        Protocol p          = new Protocol();
  private static int      dishNumber = 999;

  private FilteredList<Dish> filteredData =
      new FilteredList<>(FXCollections.observableList(prepareListForFiltering()));

  private Predicate<Dish> createPredicate(String searchText) {
    return dish -> {
      if (searchText == null || searchText.isEmpty()) return true;
      return isSearchFindsDish(dish.getName(), searchText);
    };
  }

  //=======================================================================================================================

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    ///////////////////////////////NEW DISH////////////////////////////////
    //===================================================================//
    itemDishList.addAll(getTempItems());
    try {
      newDishTable.setItems(itemDishList);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }

    itemName.setCellValueFactory(new PropertyValueFactory<>("Name"));
    itemCalories.setCellValueFactory(new PropertyValueFactory<>(
        "caloriesBound"));
    //TODO still need to be able to set custom values
    itemAmount.setCellFactory(ComboBoxTableCell -> {
      //set up the combobox
      ComboBox<String> combo = new ComboBox<>();
      //load the combobox content
      Quantities q = new Quantities() {
        @Override
        public ArrayList<String> addValuesString(String container) {
          return super.addValuesString(container);
        }

        @Override
        public quantity getQuantityFromString(String quantity) {
          return super.getQuantityFromString(quantity);
        }
      };
      //set up the tablecell
      TableCell<Item, String> cell =
          new TableCell<>() {
            @Override
            //this actually *displays* the cell on screen
            protected void updateItem(String string, boolean empty) {
              try {
                int row = this.getTableRow().getIndex();
                Item item =
                    this.getTableRow().getTableView().getItems().get(row);
//                System.out.println(item.getAmount());
                ObservableList<String> list =
                    FXCollections.observableArrayList(q.addValuesString((item.getAmount())));
                combo.setItems(list);
                //if it's a loose item, we have it editable
                if (item.getAmount().equals("loose")) {
                  combo.setEditable(true);
                }
                //this checks if we have pre-existing data
                Message message =
                    new Message("get-calories-and-amounts" +
                                " " +
                                item.getItemID());
                p.protocolToUse(message);
                String amount = p.getM().getData().get(0).get(0);
                //if there is no data
                if (amount == null) {
                  //placeholder for combobox
                  Label loader = new Label("Select an item");
                  combo.setPromptText("Please enter a value");
//                  auto picks the first item int the column
                  combo.getSelectionModel().selectFirst();
                }
//                  System.out.println("the value amount is "+amount);
                //try and set the preset
                else {
                  combo.getSelectionModel().select(amount);
                }
              } catch (IndexOutOfBoundsException ignore) {
//                e.printStackTrace();
              }
              //the part that does the actual setting on screen
              super.updateItem(string, empty);
              if (empty) {
                setGraphic(null);
              } else {
                combo.setValue(string);
                setGraphic(combo);
              }
            }
          };

      //adding the listener
      combo.valueProperty().addListener((observableValue, o, n) -> {
//        attempts to load in existing values (if they exist)
        int row = cell.getTableRow().getIndex();
        ObservableList<Item> list =
            cell.getTableRow().tableViewProperty().get().getItems();
        //this is the current item
        Item item = list.get(row);
//        defaults the firstmost value if none are chosen
        if (combo.valueProperty().getValue() == null) {
          //this checks if we have pre-existing data
          Message message =
              new Message("get-calories-and-amounts" + " " + item.getItemID());
          p.protocolToUse(message);
          String amount = p.getM().getData().get(0).get(0);
//          System.out.println("combobox amount is "+amount);
          //try and set the preset
          if (!amount.isBlank()) {
            combo.getSelectionModel().select(amount);
          } else {
            combo.getSelectionModel().selectFirst();
          }
        }
//        System.out.println("the new value is "+n);
        //if it's edited*/
        if (isNumeric(n)) {
          double cals = Double.parseDouble(n) * item.getCalories();
          item.setCaloriesBound((int) cals);
          Message message =
              new Message("save-tempDish-values" +
                          " " +
                          item.getItemID() +
                          " " +
                          item.getCaloriesBoundValue() +
                          " " +
                          combo.getValue());
          p.protocolToUse(message);
        }
        //if the user puts in an incorrect value in the editable
        else if (!combo.getItems().contains(n.toUpperCase())) {
          item.setCaloriesBound(0);
        }
        //if it's not edited
        else {
          //now we convert the string to enum
          double percent =
              q.getQuantityFromString(combo.getValue()).getPercent();
          //... this actually does what we wanted and set it
          item.setPerCent(percent);
          int v = (int) (item.getPerCent() * item.getCalories());
//          System.out.println("value should be " + v);
          item.setCaloriesBound(v);
//          System.out.println("the value of the nonsense is "+combo.valueProperty().getValue());
          Message message =
              new Message("save-tempDish-values" +
                          " " +
                          item.getItemID() +
                          " " +
                          item.getCaloriesBoundValue() +
                          " " +
                          combo.getValue());
          p.protocolToUse(message);
        }
      });

      return cell;
    });
    itemVegan.setCellValueFactory(new PropertyValueFactory<>("veganString"));

    //this will allow us to double click to delete an item
    newDishTable.setOnMouseClicked(mouseEvent -> {
      int rowToDelete = newDishTable.getSelectionModel().getSelectedIndex();
      if (mouseEvent.getClickCount() == 2 && rowToDelete >= 0) {
        int itemNo = newDishTable.getItems().get(rowToDelete).getItemID();
        newDishTable.getItems().remove(rowToDelete);
        Message message = new Message("remove-temp-dish-item" + " " + itemNo);
        p.protocolToUse(message);
      }
    });

    //////////////////////////////SEARCH DISH//////////////////////////////
    //===================================================================//
    //setting up the search Dish table
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    isVegan.setCellValueFactory(new PropertyValueFactory<>("veganString"));
    calories.setCellValueFactory(new PropertyValueFactory<>("calories"));
    //setting the filtered list up and making the search bar listener
    results.setItems(filteredData);
    searchBar.textProperty().addListener((observable, oldValue, newValue) ->
                                             filteredData.setPredicate(
                                                 createPredicate(newValue))
                                        );
    //making the table be double clickable
    results.setRowFactory(tv -> {
      TableRow<Dish> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        //and for the editing of a meal
        if (event.getButton() == MouseButton.PRIMARY &&
            event.getClickCount() == 2 &&
            (!row.isEmpty())) {
          Message message = new Message("drop-tempDish");
          p.protocolToUse(message);
          viewDish(row.getItem().getId());
          dishNumber = row.getItem().getId();
        }

        //delete dish
        if (event.getButton() == MouseButton.SECONDARY &&
            event.getClickCount() == 2 &&
            (!row.isEmpty())) {
          dishNumber = row.getItem().getId();
          Message message = new Message("delete-dish-from-editDish"+SPACE+dishNumber);
          p.protocolToUse(message);
          Message message2=new Message("drop-tempDish");
          p.protocolToUse(message2);
          openNewWindow("dissertation/fxml//editDish.fxml");
        }
      });
      return row;
    });


    //bind method to goBack button
    goBack.setOnAction(EventHandler -> {
      Message message = new Message("drop-tempDish");
      p.protocolToUse(message);
      openNewWindow("dissertation/fxml//body.fxml");
    });
  }

  //========================================================================================================================//

  public void viewDish(int dish) {
    //drop tempDish immediately and clear the itemList
    Message message3 = new Message("drop-tempDish");
    p.protocolToUse(message3);
    Message message = new Message("get-dish-components" + SPACE + dish);
    p.protocolToUse(message);
    ArrayList<Item> list = new ArrayList<>();
    for (int x = 0; x < p.getM().getItemData().size(); ++x) {
      list.addAll(p.getM().getItemData().get(x));
    }
    for (Item item : list) {
      Message message1 =
          new Message("add-item-to-new-dish" + SPACE + item.getItemID());
      Message message2 =
          new Message("get-itemDish-set-amount" + SPACE + item.getItemID()+SPACE+String.valueOf(dish));
      p.protocolToUse(message1);
      p.protocolToUse(message2);
      itemDishList.clear();
      itemDishList.addAll(getTempItems());
    }
  }

  //this method tests whether or not the words in the field match
  //TODO make private after testing
  public static boolean isSearchFindsDish(String dish, String searchText) {
    return dish.toLowerCase().contains(searchText.toLowerCase());
  }

  //this gets the list to filter
  public ArrayList<ArrayList<String>> getListOfMeals() {
    ArrayList<ArrayList<String>> nullArray = new ArrayList<>();
    Message message =
        new Message("meals");
    p.protocolToUse(message);
    Message messageIn = p.getM();
    if (messageIn.getMessage().equals("returned-list")) {
      return messageIn.getData();
    } else {
      return nullArray;
    }
  }

  public void createNewDish() {
    openNewWindow("dissertation/fxml//selectItem.fxml");
  }

  public void addDishToMeal(String name, Dish rowData) {
    Message message =
        new Message("add-dish-to-table" +
                    SPACE +
                    name +
                    SPACE +
                    rowData.getId() +
                    SPACE +
                    rowData.getCalories());
    System.out.println(message.getMessage());
    p.protocolToUse(message);
    Message messageIn = p.getM();
    System.out.println(messageIn.getMessage());
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

  public ArrayList<Item> getTempItems() {
    Message message = new Message("get-temp-items");
    p.protocolToUse(message);
    return p.getM().getItemData().get(0);
  }

  //deletes tempDish and resets the window
  public void deleteDish() {
    Message message = new Message("drop-tempDish");
    p.protocolToUse(message);
    String s = p.getM().getMessage();
    itemDishList.clear();
    newDishTable.refresh();
  }

  //saves the tempDish
  public void saveDish() {
    openNewWindow("dissertation/fxml//NewDishPopup.fxml");
  }

  public static boolean isNumeric(String str) {
    return str.matches("-?\\d+(\\.\\d+)?");
  }

  //for making the tempDish names suitable for protocol transfer
  public static String despacer(String name) {
    return name.replace(' ', '¬');
  }

  //for restoring the spaces to tempDish names
  public static String spacer(String name) {
    return name.replace('¬', ' ');
  }

  public List<Dish> prepareListForFiltering() {
    Message message = new Message("meals");
    p.protocolToUse(message);
    List<Dish>                 list = new ArrayList<>();
    ArrayList<ArrayList<Dish>> dish = p.getM().getDishData();
    for (ArrayList<Dish> temp : dish) {
      list.add(temp.get(0));
    }
    return list;
  }

  public void updateDish() {
    //user enters the name and it is added to dish
    //get the calories in
    Message message = new Message("get-tempDish-calories");
    p.protocolToUse(message);
    int totalCals = Integer.parseInt(p.getM().getMessage());
    Message message3 =
        new Message("update-dish" + " " + dishNumber + " " + totalCals);
    p.protocolToUse(message3);

    try {
      Main              main = new Main();
      AddDishController ad   = (AddDishController) main.getLoader("addDish");
      ad.deleteDish();
    } catch (IOException e) {
      e.printStackTrace();
    }
    openNewWindow("dissertation/fxml//editDish.fxml");
  }

}

