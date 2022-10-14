package com.dissertation.server_side;

import com.dissertation.AddDishController;

import java.time.LocalDate;
import java.util.ArrayList;

public class Protocol {

  private Message                      m;
  private String                       messageBody;
  private ArrayList<ArrayList<String>> data=new ArrayList<>();
  private ArrayList<ArrayList<Dish>> dataDish=new ArrayList<>();
  private ArrayList<ArrayList<Item>> dataItem=new ArrayList<>();
  private Database                   db    = new Database();

  public Message getM() {
    return m;
  }

  public void protocolToUse(Message message) {
    String[] protocol;
    protocol = message.getMessage().split(" ", 7);
    switch (protocol[0]) {
      default:
        System.err.println("Invalid response!");
        m = new Message("invalid-response");
        return;
      case "register":
        try {
          register(Integer.parseInt(protocol[1]), Integer.parseInt(protocol[2]),
                   Double.parseDouble(protocol[3]));
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
//          e.printStackTrace();
        }
        return;
      case "is-registered?":
        isRegistered();
        return;
      case "get-calories":
        getCalories();
        return;
      case "meals":
        getMealList();
        return;
      case "add-dish-to-table":
        addDishToTable(protocol[1], protocol[2], protocol[3]);
        return;
      case "itemChooser-table":
        String[] rejoin2 = message.getMessage().split(" ", 2);
        System.out.println(rejoin2[1]);
        getItemChooserList(rejoin2[1].toLowerCase());
        return;
      //this is for choosing by name not type
      case "itemChooser-table-keyword":
        String[] rejoin = message.getMessage().split(" ", 2);
        getItemChooserListKeyword(rejoin[1].toLowerCase());
        return;
      case "add-item-to-new-dish":
        addItemToDish(Integer.parseInt(protocol[1]));
        return;
      case "get-temp-items":
        getTempItems();
        return;
      case "save-tempDish-values":
        try {
          saveTempDish(Integer.parseInt(protocol[1]),
                       Integer.parseInt(protocol[2]),
                       protocol[3]);
        } catch (NullPointerException e) {
          e.printStackTrace();
          messageBody = "tempDish-did-not-save";
          m           = new Message(messageBody);
        }
        return;
      case "get-calories-and-amounts":
        getCaloriesAndAmounts(Integer.parseInt(protocol[1]));
        return;
      case "drop-tempDish":
        dropTempDish();
        return;
      case "save-new-tempDish":
        saveNewDish(protocol[1], Integer.parseInt(protocol[2]));
        return;
      case "get-tempDish-calories":
        getTempDishCalories();
        return;
      case "remove-temp-dish-item":
        removeTempDishItem(Integer.parseInt(protocol[1]));
        return;
      case "remove-from-today's-food":
        removeFromCaloriesTable(Integer.parseInt(protocol[1]), protocol[2]);
        return;
      case "get-calories-for-time":
        getCaloriesForTime(protocol[1]);
        return;
      case "set-up-progress-bar":
        setUpProgressBar();
        return;
      case "get-dish-preset-portion":
        getPresetPortion(Integer.parseInt(protocol[1]));
        return;
      case "get-dish-custom-portion":
        getCustomPortion(Integer.parseInt(protocol[1]),protocol[2]);
        return;
      case "set-dish-preset-portion":
        setPresetPortion(protocol[1],
                         Integer.parseInt(protocol[2]),
                         Integer.parseInt(protocol[3]),
                         Integer.parseInt(protocol[4]));
        return;
      case "get-preset-calories":
        getPresetCalories(Integer.parseInt(protocol[1]));
        return;
      case "is-portion-null":
        isPortionNull(Integer.parseInt(protocol[1]));
        return;
      case "get-details":
        getDetails();
        return;
      case "change-height":
        changeHeight(protocol[1]);
        return;
      case "change-weight":
        changeWeight(protocol[1]);
        return;
      case "change-calorie_goal":
        changeCalorieGoal(protocol[1]);
        return;
        case "get-frequency-for":
        getFrequencies(protocol[1]);
        return;
      case "get-dish-details":
        getDishForLabels(Integer.parseInt(protocol[1]));
        return;
      case "get-dish-components":
        getDishComponents(Integer.parseInt(protocol[1]));
        return;
      case "get-itemDish-set-amount":
        getAmountFromItemDish(Integer.parseInt(protocol[1]),
                              Integer.parseInt(protocol[2]));
        return;
      case "update-dish":
        updateDish(Integer.parseInt(protocol[1]),
                   Integer.parseInt(protocol[2]));
        return;
      case "custom-dish":
        addCustomDish(AddDishController.spacer(protocol[1]),
                      Integer.parseInt(protocol[2]),protocol[3]);
        return;
      case "delete-dish-from-editDish":
        removeFromEditDish(Integer.parseInt(protocol[1]));
      case "set-the-tables":
        getTodaysFoodEaten();
    }
  }

  public Message removeFromEditDish(int id){
    db.openDatabase();
    messageBody=String.valueOf(db.removeFromEditDish(id));
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message addCustomDish(String s, int i, String z){
    db.openDatabase();
    messageBody=String.valueOf(db.addCustomDish(s,i,z));
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message getAmountFromItemDish(int id, int dishID){
    db.openDatabase();
    messageBody=String.valueOf(db.getAmountFromItemDish(id, dishID));
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message getDishComponents(int id){
    db.openDatabase();
    dataItem=db.getTempItems(id);
    db.closeDatabase();
    return m=new Message(false,messageBody,dataItem);
  }

  public Message getDishForLabels(int id){
    db.openDatabase();
    data=db.getDishForLabels(id);
    db.closeDatabase();
    messageBody="data-returned";
    return m=new Message(messageBody,data);
  }

  public Message getFrequencies(String mealtime) {
    db.openDatabase();
    data = db.getFrequencies(mealtime);
    if (data == null || data.isEmpty()) {
      messageBody = "no-data";
    } else {
      messageBody = "data-returned";
    }
    db.closeDatabase();
    return m = new Message(messageBody, data);
  }

  public Message changeCalorieGoal(String height){
    db.openDatabase();
    messageBody=db.changeCalorieGoal(height);
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message changeHeight(String height){
    db.openDatabase();
    messageBody=db.changeHeight(height);
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message changeWeight(String height){
    db.openDatabase();
    messageBody=db.changeWeight(height);
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message changCalorieGoal(String height){
    db.openDatabase();
    messageBody=db.changeHeight(height);
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message getDetails(){
    db.openDatabase();
    data=db.getDetails();
    db.closeDatabase();
    messageBody="data-returned";
    return m=new Message(messageBody,data);
  }

  public Message getCustomPortion(int id, String mealtime){
    db.openDatabase();
    messageBody=String.valueOf(db.getCustomPortion(id,mealtime));
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message isRegistered() {
    db.openDatabase();
    if (db.isRegistered()) {
      messageBody = "true";
    } else {
      messageBody = "false";
    }
    db.closeDatabase();
    return m = new Message(messageBody);
  }

  public Message isPortionNull(int id){
    db.openDatabase();
    messageBody=String.valueOf(db.isPortionNull(id));
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message getPresetPortion(int id){
    db.openDatabase();
    messageBody=String.valueOf(db.getPresetPortion(id));
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message setPresetPortion(String mealtime,int id, int eaten,
                                  int caloriesEaten){
    db.openDatabase();
    boolean b=db.setPresetPortion(mealtime,id,eaten,caloriesEaten);
    db.closeDatabase();
    messageBody="result-returned-was-"+b;
    return m=new Message(messageBody);
  }

  public Message getPresetCalories(int id){
    db.openDatabase();
    messageBody=String.valueOf(db.getPresetCalories(id));
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message setUpProgressBar(){
    db.openDatabase();
    double d=db.setUpProgressBar();
    db.closeDatabase();
    if(d!=0){
      messageBody=String.valueOf(d);
    }
    else{
      messageBody="0";
    }
    return m=new Message(messageBody);
  }

  public Message getCaloriesForTime(String s){
    db.openDatabase();
    int x=db.getCaloriesForTime(s);
    db.closeDatabase();
    if(x!=-1){
      messageBody=String.valueOf(x);
    }
    else {
      messageBody="0";
    }
    return m=new Message(messageBody);
  }

  public Message removeFromCaloriesTable(int id, String meal){
    db.openDatabase();
    if(db.removeFromCaloriesTable(id,meal)){
      messageBody="removed-successfully";
    }
    else{
      messageBody="dish-not-removed";
    }
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message removeTempDishItem(int id){
    db.openDatabase();
    if(db.removeTempDishItem(id)){
      messageBody="item-removed";
    }
    else{
      messageBody="item-not-removed";
    }
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message getTempDishCalories(){
    db.openDatabase();
    if(db.getTempDishCalories()==-1){
      messageBody="calories-not-returned";
    }
    else{
      messageBody=String.valueOf(db.getTempDishCalories());
    }
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  //this saves the tempDish values in add table into the itemDish table and then
  //into the dish table before deleting everything.
  public Message saveNewDish(String name, int cals) {
    db.openDatabase();
    if (db.saveNewDish(AddDishController.spacer(name), cals)) {
      messageBody = "tempDish-added-to-dish-successfully";
    } else {
      messageBody = "tempDish-did-not-add";
    }
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  //this saves the tempDish values in add table into the itemDish table and then
  //into the dish table before deleting everything.
  public Message updateDish(int id, int cals) {
    db.openDatabase();
    if (db.updateDish(id, cals)) {
      messageBody = "dish-updated-successfully";
    } else {
      messageBody = "dish-did-not-update";
    }
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message dropTempDish(){
    db.openDatabase();
    db.dropTempDish();
    db.closeDatabase();
    return m=new Message("tempDish-table-reset");
  }

  public Message getCaloriesAndAmounts(int id){
    db.openDatabase();
    ArrayList<ArrayList<String>>wrapperJohn=new ArrayList<>();
    wrapperJohn.add(db.getAmount(id));
    db.closeDatabase();
    if(wrapperJohn.get(0)==null){
      messageBody="no-data-returned";
    }
    else {
      data        = wrapperJohn;
      messageBody = "calories-and-amounts-returned";
    }
    return m=new Message(messageBody,data);
  }

  public Message saveTempDish(int id, int calories, String amount){
    db.openDatabase();
    if(db.saveTempDish(id,calories,amount)){
      messageBody="tempDish-saved";
    }
    else {
      messageBody = "tempDish-did-not-save";
    }
    db.closeDatabase();
    return m=new Message(messageBody);
  }

  public Message getTempItems(){
    db.openDatabase();
    dataItem=db.getTempItems();
    db.closeDatabase();
    if (!dataItem.isEmpty()){
     messageBody="item-list-returned";
    }
    else{
      messageBody="no-items-returned";
    }
    return m=new Message(false,messageBody,dataItem);
  }

  public Message addItemToDish(int id) {
    db.openDatabase();
    boolean add = db.isAddItemToDish(id);
    db.closeDatabase();
    if (add) {
      return m = new Message("dish-added-to-to-temp");
    } else {
      return m = new Message("add-failed");
    }
  }

  public Message getItemChooserListKeyword(String keyword){
    db.openDatabase();
    dataItem=db.getItemChooserListKeyword(keyword);
    db.closeDatabase();
    if (dataItem.isEmpty()){
      messageBody="no-item-list-returned";
    }
    else {
      messageBody = "item-choose-list-returned";
    }
    return m=new Message(false,messageBody,dataItem);
  }

  public Message getItemChooserList(String type) {
    db.openDatabase();
    dataItem=db.getItemChooserList(type);
    db.closeDatabase();
    if (dataItem.isEmpty()){
      messageBody="no-item-list-returned";
    }
    else {
      messageBody = "item-choose-list-returned";
    }
    return m=new Message(false,messageBody,dataItem);
  }

  public Message getCalories() {
    db.openDatabase();
    messageBody = String.valueOf(db.getCalories());
    db.closeDatabase();
    return m = new Message(messageBody);
  }

  public Message register(int calorieGoal, int height, double weight) {
    //we can try to add it
    db.openDatabase();
    boolean a = (db.isAddedUser(calorieGoal, height, weight));

    if (!a) {
      messageBody = "add-failed";
      db.closeDatabase();
      return m = new Message(messageBody);
    }
    if (a) {
      messageBody = "added-user";
      db.closeDatabase();
      return m = new Message(messageBody);
    }

    db.closeDatabase();
    return m = new Message(messageBody);
  }

  public Message getMealList() {
    db.openDatabase();
    dataDish = db.getMealList();
    db.closeDatabase();
    if ((dataDish==null)||(dataDish.isEmpty())) {
      messageBody = "no-list-returned";
      return m = new Message(messageBody);
    } else {
      messageBody = "returned-list";
    }
    return m = new Message(false,false,messageBody, dataDish);
  }

  public Message addDishToTable(String mealTime, String dishID,
                                String calories) {
    db.openDatabase();
    if (db.isAddDishToTable(mealTime, dishID, calories)) {
      messageBody = "dish-added";
    }
    else {
      messageBody = "add-failed";
    }
    db.closeDatabase();
    return m = new Message(messageBody);
  }

  public Message getTodaysFoodEaten() {
    //TODO may need a try catch for null data
    db.openDatabase();
    dataDish = db.getTodaysFoodEaten(LocalDate.now().toString());
    db.closeDatabase();
    if ((dataDish==null)||(dataDish.isEmpty())) {
      messageBody = "no-list-returned";
      return m = new Message(messageBody);
    } else {
      messageBody = "returned-list";
    }
    return m = new Message(false,false,messageBody, dataDish);
  }

  public void resetDatabase(){
    db.openDatabase();
    db.resetDatabase("tabledef", "tabledebugdata");
    db.closeDatabase();
  }

}