package com.dissertation.server_side;

import javafx.beans.property.*;

public class Item {

  public int getItemID() {
    return itemID.get();
  }

  public IntegerProperty itemIDProperty() {
    return itemID;
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public String getShop() {
    return shop.get();
  }

  public StringProperty shopProperty() {
    return shop;
  }

  public int getIsVegan() {
    return isVegan.get();
  }

  public IntegerProperty isVeganProperty() {
    return isVegan;
  }

  public StringProperty amountProperty() {
    return amount;
  }

  public String getAmount() {
    return amount.get();
  }

  public int getCalories() {
    return calories.get();
  }

  public IntegerProperty caloriesProperty() {
    return calories;
  }

  public String getType() {
    return type.get();
  }

  public StringProperty typeProperty() {
    return type;
  }

  private IntegerProperty itemID;
  private StringProperty  name;
  private StringProperty  shop;
  private IntegerProperty isVegan;
  private StringProperty  amount;
  private IntegerProperty calories;
  private StringProperty  type;
  //for showing on the table
  private StringProperty       veganString;
  private IntegerProperty caloriesBound=new SimpleIntegerProperty();

  public void setCaloriesBound(int caloriesBound) {
    this.caloriesBound.set(caloriesBound);
  }

  private DoubleProperty perCent=new SimpleDoubleProperty(1);

  public double getPerCent() {
    return perCent.get();
  }

  public DoubleProperty perCentProperty() {
    return perCent;
  }

  public void setPerCent(double perCent) {
    this.perCent.set(perCent);
  }

  public IntegerProperty caloriesBoundProperty() {
    return caloriesBound;
    }

  public int getCaloriesBoundValue(){
    return this.caloriesBound.get();
  }


  //this does the conversion of boolean to string for the table in addDish
  public StringProperty veganStringProperty() {
    String x;
    if (String.valueOf(getIsVegan()).equals("0")) {
      x="Ve";
    }
    else {
      x="V";
    }
    return new SimpleStringProperty(x);
  }

  public void setVeganString(String veganString) {
    this.veganString.set(veganString);
  }

  public Item() {}

  public Item(int itemID, String name, String shop, int isVegan,
              String amount,
              int calories, String type) {
    this.itemID   = new SimpleIntegerProperty(itemID);
    this.name     = new SimpleStringProperty(name);
    this.shop     = new SimpleStringProperty(shop);
    this.isVegan  = new SimpleIntegerProperty(isVegan);
    this.amount   = new SimpleStringProperty(amount);
    this.calories = new SimpleIntegerProperty(calories);
    this.type     = new SimpleStringProperty(type);
  }

  @Override
  public String toString() {
    return (getItemID() +
            "\n" +
            getName() +
            "\n" +
            getShop() +
            "\n" +
            getIsVegan() +
            "\n" +
            getAmount() +
            "\n" +
            getCalories() +
            "\n" +
            getType());
  }

}

