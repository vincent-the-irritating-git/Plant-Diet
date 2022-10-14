package com.dissertation.server_side;

import javafx.beans.property.*;

public class Dish {

  private StringProperty  time;
  private StringProperty  name;
  private IntegerProperty id = new SimpleIntegerProperty(255);
  private IntegerProperty isVegan;
  private StringProperty  veganString;
  private IntegerProperty calories;
  private IntegerProperty portion=new SimpleIntegerProperty();

  public String getVeganString() {
    return veganString.get();
  }

  public StringProperty veganStringProperty() {
    String x;
    if (String.valueOf(isVegan()).equals("0")) {
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

  public int getPortion() {
    return portion.get();
  }

  public IntegerProperty portionProperty() {
    return portion;
  }

  public void setPortion(int portion) {
    this.portion.set(portion);
  }

  public String getTime() {
    return time.get();
  }

  public StringProperty timeProperty() {
    return time;
  }

  public void setTime(String time) {
    this.time.set(time);
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public int isVegan() {
    return isVegan.get();
  }

  public IntegerProperty isVeganProperty() {
    return isVegan;
  }

  public void setIsVegan(int isVegan) {
    this.isVegan.set(isVegan);
  }

  public int getCalories() {
    return calories.get();
  }

  public IntegerProperty caloriesProperty() {
    return calories;
  }

  public void setCalories(int calories) {
    this.calories.set(calories);
  }

  public int getId() {
    return id.get();
  }

  public IntegerProperty idProperty() {
    return id;
  }

  public void setId(int id) {
    this.id.set(id);
  }

  public Dish() {}

  public Dish(int id, String time, String name, int isVegan,
              int calories) {
    this.id   = new SimpleIntegerProperty(id);
    this.time = new SimpleStringProperty(time);
    this.name = new SimpleStringProperty(name);
    this.isVegan  = new SimpleIntegerProperty(isVegan);
    this.calories = new SimpleIntegerProperty(calories);
  }

  @Override
  public String toString() {
    return getId()+"\n"+getTime()+"\n"+getName() + "\n" + isVegan() + "\n" + getCalories() +
           "\n";
  }

}
