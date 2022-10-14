package com.dissertation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.dissertation.server_side.Item;
import com.dissertation.server_side.Message;
import com.dissertation.server_side.Protocol;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ItemChooserController implements Initializable {

  private final String SPACE = " ";


  @FXML
  private Button goBack;
  @FXML
  private Button next;
  @FXML
  private Button previous;
  @FXML
  private VBox   masterPane;
  /////////////////////////
  @FXML
  ImageView item1Pic;
  @FXML
  Label     item1isVegan;
  @FXML
  Label     item1Shop;
  @FXML
  Label     item1Name;

  /////////////////////////
  @FXML
  ImageView item2Pic;
  @FXML
  Label     item2isVegan;
  @FXML
  Label     item2Shop;
  @FXML
  Label     item2Name;
  /////////////////////////
  @FXML
  ImageView item3Pic;
  @FXML
  Label     item3isVegan;
  @FXML
  Label     item3Shop;
  @FXML
  Label     item3Name;
  /////////////////////////
  @FXML
  ImageView item4Pic;
  @FXML
  Label     item4isVegan;
  @FXML
  Label     item4Shop;
  @FXML
  Label     item4Name;
  /////////////////////////
  @FXML
  ImageView item5Pic;
  @FXML
  Label     item5isVegan;
  @FXML
  Label     item5Shop;
  @FXML
  Label     item5Name;
  /////////////////////////
  @FXML
  ImageView item6Pic;
  @FXML
  Label     item6isVegan;
  @FXML
  Label     item6Shop;
  @FXML
  Label     item6Name;
  /////////////////////////
  @FXML
  ImageView item7Pic;
  @FXML
  Label     item7isVegan;
  @FXML
  Label     item7Shop;
  @FXML
  Label     item7Name;
  /////////////////////////
  @FXML
  ImageView item8Pic;
  @FXML
  Label     item8isVegan;
  @FXML
  Label     item8Shop;
  @FXML
  Label     item8Name;
  /////////////////////////
  @FXML
  ImageView item9Pic;
  @FXML
  Label     item9isVegan;
  @FXML
  Label     item9Shop;
  @FXML
  Label     item9Name;
  /////////////////////////
  @FXML
  ImageView item10Pic;
  @FXML
  Label     item10isVegan;
  @FXML
  Label     item10Shop;
  @FXML
  Label     item10Name;
  /////////////////////////
  @FXML
  ImageView item11Pic;
  @FXML
  Label     item11isVegan;
  @FXML
  Label     item11Shop;
  @FXML
  Label     item11Name;
  /////////////////////////
  @FXML
  ImageView item12Pic;
  @FXML
  Label     item12isVegan;
  @FXML
  Label     item12Shop;
  @FXML
  Label     item12Name;
  /////////////////////////
  @FXML
  ImageView item13Pic;
  @FXML
  Label     item13isVegan;
  @FXML
  Label     item13Shop;
  @FXML
  Label     item13Name;
  /////////////////////////
  @FXML
  ImageView item14Pic;
  @FXML
  Label     item14isVegan;
  @FXML
  Label     item14Shop;
  @FXML
  Label     item14Name;
  /////////////////////////
  @FXML
  ImageView item15Pic;
  @FXML
  Label     item15isVegan;
  @FXML
  Label     item15Shop;
  @FXML
  Label     item15Name;
  ////////////////////////
  @FXML
  private Label item1ID;
  @FXML
  private Label item2ID;
  @FXML
  private Label item3ID;
  @FXML
  private Label item4ID;
  @FXML
  private Label item5ID;
  @FXML
  private Label item6ID;
  @FXML
  private Label item7ID;
  @FXML
  private Label item8ID;
  @FXML
  private Label item9ID;
  @FXML
  private Label item10ID;
  @FXML
  private Label item11ID;
  @FXML
  private Label item12ID;
  @FXML
  private Label item13ID;
  @FXML
  private Label item14ID;
  @FXML
  private Label item15ID;
  ///////////////////////////
  private Label[]lb=new Label[15];
  private ArrayList<ArrayList<Item>> tableList;
  private Protocol   p         = new Protocol();
  //this tracks the limit in the loop
  int limit=0;
  //this tracks how much is left in the total list
  private static int remainder =0;
  //this tracks how many pages we have traversed
  private static int pc=0;
  //this is a class variable of tSize
  private static int tSize=0;
  //this tracks where in the list we are
  private static int listPointer =0;
  //the maximum no. of items displayed
  private final int  MAX_GUI     =15;

  //constructor, takes the list of Items in getData format
  public ItemChooserController(ArrayList<ArrayList<Item>> tableList) {
    this.tableList = tableList;
  }

  //default constructor
  public ItemChooserController() {
  }

  //gives us the data to set up the table
  private void setUpTable(int tSize) {
    //see if we should show the previous button
    if (pc>0){
      previous.setVisible(true);
      previous.setDisable(false);
    }
    else{
      previous.setVisible(false);
      previous.setDisable(true);
    }
    //see if we should show the next button
    if (remainder>=0){
      next.setVisible(false);
      next.setDisable(true);
    }
    else{
      next.setVisible(true);
      next.setDisable(false);
    }

    //arrays
    //these are arrays of the blank FMXL fields
    ImageView[] picArray = {
        item1Pic, item2Pic, item3Pic, item4Pic, item5Pic,
        item6Pic, item7Pic, item8Pic, item9Pic, item10Pic
        , item11Pic, item12Pic, item13Pic, item14Pic, item15Pic
    };
    Label[] name =
        {item1Name, item2Name, item3Name, item4Name, item5Name, item6Name,
         item7Name, item8Name, item9Name, item10Name, item11Name,
         item12Name, item13Name, item14Name, item15Name};
    Label[] vegan = {item1isVegan, item2isVegan, item3isVegan, item4isVegan,
                     item5isVegan, item6isVegan, item7isVegan, item8isVegan,
                     item9isVegan, item10isVegan, item11isVegan, item12isVegan,
                     item13isVegan, item14isVegan, item15isVegan};
    Label[] shop =
        {item1Shop, item2Shop, item3Shop, item4Shop, item5Shop, item6Shop,
         item7Shop, item8Shop, item9Shop, item10Shop, item11Shop, item12Shop,
         item13Shop, item14Shop, item15Shop};
    Label[]ID={item1ID,item2ID,item3ID,item4ID,item5ID,item6ID,item7ID,
               item8ID,item9ID,item10ID,item11ID,item12ID,item13ID,item14ID,
               item15ID};

    blanker(name,vegan,shop,picArray);

    ItemChooserController.tSize = tSize;
    remainder                   = ItemChooserController.tSize - MAX_GUI;
    //to keep remainder from going minus
    //instead tSize' value alone is used
    if (remainder<0){
      remainder=0;
    }
    limit=ItemChooserController.tSize-remainder;
    System.out.println("tSize is "+ItemChooserController.tSize);
    System.out.println("remainder is "+remainder);
    System.out.println("listPointer is "+listPointer);
    System.out.println("previous counter is "+pc);
    System.out.println("limit is "+limit);
    //make tablelist.get() point to y instead
    //this is where we populate the blank arrays with the tableList data
    /*
    x points to the arrays
    listPointer points to the tableList
     */
    for (int x = 0; x < limit; ++x, ++listPointer) {

      System.out.println("x is "+x);
      try {
          picArray[x].setImage((new Image("dissertation/pictures/" +
                                          tableList.get(listPointer).get(0).getItemID() +
                                          ".png")));
      }catch (IllegalArgumentException | NullPointerException e){
        picArray[x].setImage((new Image(
            "dissertation/pictures/placeholder.png")));
      }
        name[x].setText(tableList.get(listPointer).get(0).getName());
        shop[x].setText(tableList.get(listPointer).get(0).getShop());
        vegan[x].setText(Main.veganBoolean(tableList.get(listPointer)
                                                 .get(0)
                                                 .getIsVegan()));
        ID[x].setText(String.valueOf(tableList.get(listPointer).get(0).getItemID()));
          makeVisible(name[x],vegan[x],shop[x],picArray[x]);
      }
    lb=ID;

    //next page, recursively calls the method using the remainder
    if (remainder > 0) {
      next.setVisible(true);
      next.setDisable(false);
    }
  }

  private void makeVisible(Label a, Label b, Label c,
                           ImageView d) {
      a.setVisible(true);
      a.setDisable(false);
      b.setVisible(true);
      b.setDisable(false);
      c.setVisible(true);
      c.setDisable(false);
      d.setVisible(true);
      d.setDisable(false);
  }

  //blanks the fields for the page refresh
  public void blanker(Label[]a,Label[]b,Label[]c,ImageView[]d){
    for (int x=0; x<limit; ++x){
        a[x].setVisible(false);
        a[x].setDisable(true);
        b[x].setVisible(false);
        b[x].setDisable(true);
        c[x].setVisible(false);
        c[x].setDisable(true);
        d[x].setVisible(false);
        d[x].setDisable(true);
    }
  }

  //adds the food to the recipe builder
  public void addItemToCreateDish(String s) {
    System.out.println("the thing is "+SPACE+s);
    Message message = new Message("add-item-to-new-dish" + SPACE + s);
    this.p.protocolToUse(message);
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

  public void defaultValues(){
    listPointer=0;
    remainder=0;
    tSize=0;
    pc=0;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    //set up the back button
    goBack.setOnMouseClicked(mouseEvent -> {
      defaultValues();
      openNewWindow("/dissertation/fxml/selectItem.fxml");
    });

    //set up the pic array to do the itemChoose method
    //loads the fields up with data
      setUpTable(tableList.size());
    //sets the double click to add item to tempDish
    try {
      for (Label label : lb) {
        label.setOnMouseClicked(mouseEvent -> {
          if (mouseEvent.getClickCount()==2) {
            System.out.println("text is " + SPACE + label.getText());
            addItemToCreateDish(label.getText());
            defaultValues();
            openNewWindow("/dissertation/fxml//selectItem.fxml");
          }
        });
      }
    }catch (NullPointerException e){}

    next.setOnMouseClicked(mouseEvent -> {
      ++pc;
      setUpTable(remainder);
    });

    previous.setOnMouseClicked(mouseEvent -> {
      //keep the previousCounter in order so we know to display it
      --pc;
      //minus 15, we must have shown 15 elements to go next
      listPointer-=MAX_GUI;
      //minus the remainder that was shown on the current page
      listPointer-=limit;
      //add 15 to the tSize, we must have shown 15 elements to go next
      tSize+=MAX_GUI;
      setUpTable(tSize);
    });
  }
}
