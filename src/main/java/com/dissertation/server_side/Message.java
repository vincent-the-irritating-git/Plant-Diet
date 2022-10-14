package com.dissertation.server_side;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

  private static final long serialVersionUID = 1L;
  private String message;
  private ArrayList<ArrayList<String>> response = new ArrayList<ArrayList<String>>();
  private ArrayList<ArrayList<Item>>   itemData =new ArrayList<>();
  private ArrayList<ArrayList<Dish>>   dishData =new ArrayList<>();

  //so this is the constructor for just messages
  public Message(String message) {

    this.message = message;
    response = null;
  }

  //...and this is the one for both messages and data
  public Message(String message, ArrayList<ArrayList<String>> data) {

    this.message = message;
    response = data;
  }

  //...and this is the one for both messages and dataItem
  public Message(boolean filler,String message,
                 ArrayList<ArrayList<Item>> data) {

    this.message  = message;
    this.itemData = data;
  }

  //...and this is the one for both messages and dataDish
  public Message(boolean filler,boolean filler2,String message,
                 ArrayList<ArrayList<Dish>>data){
    this.message=message;
    this.dishData=data;
  }

  public ArrayList<ArrayList<String>> getData() {
    return response;
  }

  public String getMessage() {
    return message;
  }

  public ArrayList<ArrayList<Item>> getItemData() {
    return itemData;
  }

  public ArrayList<ArrayList<Dish>>getDishData(){
    return dishData;
  }
}
