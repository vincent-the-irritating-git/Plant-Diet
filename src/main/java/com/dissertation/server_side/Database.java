package com.dissertation.server_side;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

public class Database {

  private Connection conn = null;

  /**
   * Returns the current database connection.
   *
   * @return the database connection.
   */
  public Connection getConnection() {
    return conn;
  }

  /**
   * Default constructor. Connects to the database.
   */
  public Database() {
    openDatabase();
  }

  public void openDatabase() {

    try {
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite::resource:dissertation" +
                                         "/PlantDiet.db");
      System.out.println("Connexion established!");
    } catch (SQLException | ClassNotFoundException throwables) {
      throwables.printStackTrace();
    }
  }

  public void closeDatabase() {
    try {
      conn.close();
      System.out.println("Connexion closed!");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  //takes email and password
  //if they match, return the userid to the client
  //and let them know they can sign in
  @Deprecated
  public Boolean signIn(String email, String password) {
    String query = "SELECT userid FROM person WHERE email=? AND " +
                   "password=?";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, email);
      ps.setString(2, password);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        if (!rs.isBeforeFirst()) {
          return true;
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return false;
  }

  //attempts to add user, does if successful
  public Boolean isAddedUser(int calorieGoal, int height,
                             double weight) {
    String query =
        "INSERT INTO PERSON(calorie_goal,height,weight) VALUES (?,?,?)";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1, calorieGoal);
      ps.setInt(2, height);
      ps.setDouble(3, weight);

      int i = ps.executeUpdate();
      return i > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean addWeight(String weight) {
    String query = "INSERT INTO weight (weight) VALUES(?)";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1, Integer.parseInt(weight));
      int i = ps.executeUpdate();
      return i > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public int getCalories() {
    String query = "SELECT calorie_goal FROM person";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        return (int) rs.getObject("calorie_goal");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public boolean isRegistered(){
    String query="SELECT * FROM person";
    try {
      Statement s = conn.createStatement();
      ResultSet rs=s.executeQuery(query);
      if (rs.isBeforeFirst()){
        return true;
      }
    }catch (SQLException e){e.printStackTrace();}
    return false;
  }

  public ArrayList<ArrayList<Dish>> getMealList() {

    //we need to be careful here and not break all the methods that depend on
    // this
    // TODO won't return id
    String query =
        "SELECT dish.dishID,dish.name, CASE MIN(isVegan) WHEN 0 THEN 0 ELSE 1 END vegan, preset_calories FROM dish JOIN itemDish ON dish .dishID=itemDish.dish JOIN item ON item.itemID=itemDish.item GROUP BY dish.dishID";
    try {
      PreparedStatement ps = conn.prepareStatement(query);

      ResultSet                    rs              = ps.executeQuery();
      ArrayList<ArrayList<Dish>>dish=new ArrayList<>();
      if (rs.isBeforeFirst()) {
        while (rs.next()) {
          ArrayList<Dish> temp = new ArrayList<>();
          temp.add(new Dish
                       ((Integer)rs.getObject("dishID"),
                        null,
                        (String) rs.getObject("name"),
                        (Integer)rs.getObject("vegan"),
                        (Integer)rs.getObject("preset_calories")));
          dish.add(temp);
        }
        return dish;
      } else {
        return null;
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  public int getPresetPortion(int id){
    String query="SELECT amount_eaten FROM dish WHERE dishID=?";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1,id);
      ResultSet rs=ps.executeQuery();
      if (!rs.isBeforeFirst()){
        return -1;
      }
      return (int) rs.getObject("amount_eaten");
    }catch (SQLException e){e.printStackTrace();}
        return -1;
  }

  public boolean setPresetPortion(String mealtime,int id, int eaten,
                                  int caloriesEaten) {
    //updates the portion from dish
    String query = "UPDATE dish SET amount_eaten=? WHERE dishID=?";
    //updates the portion from calories
    String query2 =
        "UPDATE calories SET portion=? WHERE dish=? AND meal=? AND date=(date" +
        "('now'))";
    //updates teh calories from calories
    String query3 =
        "UPDATE calories SET calories=? WHERE dish=? AND meal=? AND date=(date" +
        "('now'))";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1, eaten);
      ps.setInt(2, id);
      PreparedStatement ps2 = conn.prepareStatement(query2);
      ps2.setInt(1, eaten);
      ps2.setInt(2, id);
      ps2.setString(3,mealtime);
      PreparedStatement ps3 = conn.prepareStatement(query3);
      ps3.setInt(1, caloriesEaten);
      ps3.setInt(2, id);
      ps3.setString(3,mealtime);
      int x = ps.executeUpdate();
      int y = ps2.executeUpdate();
      int z = ps3.executeUpdate();
      return x > 0 && y > 0 && z > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public int getPresetCalories(int id){
    String query="SELECT preset_calories FROM dish WHERE dishID=?";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1,id);
      ResultSet rs=ps.executeQuery();
      if (!rs.isBeforeFirst()){
        return -1;
      }
      return (int) rs.getObject("preset_calories");
    }catch (SQLException e){e.printStackTrace();}
    return -1;
  }

  public ArrayList<ArrayList<String>> getDetails() {
    String query = "SELECT * FROM person";
    ArrayList<ArrayList<String>> details = new ArrayList<>();
    try {
      Statement                    s       = conn.createStatement();
      ResultSet                    rs      = s.executeQuery(query);
      while (rs.next()) {
        ArrayList<String> temp = new ArrayList<>();
        String height =
            String.valueOf((Integer) rs.getObject("height"));
        String weight =
            String.valueOf((Double) rs.getObject("weight"));
        String calorie_goal = String.valueOf((Integer) rs.getObject(
            "calorie_goal"));
        temp.add(height);
        temp.add(weight);
        temp.add(calorie_goal);
        details.add(temp);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return details;
  }

  public String changeHeight(String height) {
    String query = "UPDATE person SET height=?";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1, Integer.parseInt(height));
      int x=0;
      x=ps.executeUpdate();
      if (x==0){
        return "change-failed";
      }
      String query2="SELECT height FROM person";
      PreparedStatement ps2=conn.prepareStatement(query2);
      ResultSet rs=ps2.executeQuery();
      return String.valueOf((Integer) rs.getObject("height"));
    } catch (SQLException | NumberFormatException e) {
      e.printStackTrace();
    }
    return "change-failed";
  }

  //get dish details for the double clicking feature for the label array
  public ArrayList<ArrayList<String>>getDishForLabels(int id){
    String query="SELECT dish.name, CASE MIN (isVegan) WHEN 0 THEN 0 ELSE 1 END vegan, preset_calories \n" +
                 "FROM dish JOIN itemDish ON dish.dishID=itemDish.dish JOIN item ON item.itemID=itemDish.item \n" +
                 "WHERE dishID=? GROUP BY dish.dishID";
    ArrayList<ArrayList<String>>list=new ArrayList<>();
    try{
      PreparedStatement ps=conn.prepareStatement(query);
      ps.setInt(1, id);
      ResultSet rs=ps.executeQuery();
      while (rs.next()) {
        ArrayList<String> temp = new ArrayList<>();
        temp.add((String) rs.getObject("name"));
        temp.add(String.valueOf((Integer) rs.getObject("vegan")));
        temp.add(String.valueOf((Integer) rs.getObject("preset_calories")));
        list.add(temp);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return list;
  }

  public ArrayList<ArrayList<String>>getFrequencies(String mealtime){
    String query="SELECT dish.dishID,COUNT (name) AS total,name FROM dish " +
                 "JOIN calories ON dish.dishID=calories.dish WHERE calories" +
                 ".meal=? GROUP BY dishID ORDER BY total DESC";
    ArrayList<ArrayList<String>>list=new ArrayList<>();
    try{
      PreparedStatement ps=conn.prepareStatement(query);
      ps.setString(1,mealtime);
      ResultSet rs=ps.executeQuery();
      while (rs.next()){
        ArrayList<String>temp=new ArrayList<>();
        temp.add(String.valueOf((Integer)rs.getObject("dishID")));
        temp.add(String.valueOf((Integer)rs.getObject("total")));
        temp.add((String) rs.getObject("name"));
        list.add(temp);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return list;
  }

  public String changeWeight(String weight) {
    String query = "UPDATE person SET weight=?";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setDouble(1, Double.parseDouble(weight));
      int x=0;
      x=ps.executeUpdate();
      if (x==0){
        return "change-failed";
      }
      String query2="SELECT weight FROM person";
      PreparedStatement ps2=conn.prepareStatement(query2);
      ResultSet rs=ps2.executeQuery();
      return String.valueOf((Double) rs.getObject("weight"));
    } catch (SQLException | NumberFormatException e) {
      e.printStackTrace();
    }
    return "change-failed";
  }

  public String changeCalorieGoal(String calorieGoal) {
    String query = "UPDATE person SET calorie_goal=?";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1, Integer.parseInt(calorieGoal));
      int x=0;
      x=ps.executeUpdate();
      if (x==0){
        return "change-failed";
      }
      String query2="SELECT calorie_goal FROM person ";
      PreparedStatement ps2=conn.prepareStatement(query2);
      ResultSet rs=ps2.executeQuery();
      return String.valueOf((Integer) rs.getObject("calorie_goal"));
    } catch (SQLException | NumberFormatException e) {
      e.printStackTrace();
    }
    return "change-failed";
  }

  //
  public int getCustomPortion(int id, String mealtime){
    String query="SELECT portion FROM calories WHERE dish=? AND meal=? AND " +
              "date=(date('now'))";
    try{
      PreparedStatement ps=conn.prepareStatement(query);
      ps.setInt(1,id);
      ps.setString(2,mealtime);
      ResultSet rs=ps.executeQuery();
      if (!rs.isBeforeFirst() || rs.getObject("portion")==null){
        return -1;
      }
      else {
        return (int) rs.getObject("portion");
      }
    }catch (SQLException | NullPointerException e){e.printStackTrace();}
    return -1;
  }

  //should return the list of meals eaten only on the day specified
  public ArrayList<ArrayList<Dish>> getTodaysFoodEaten(String date) {

    String query = "SELECT dish.dishID,dish.name, meal, date AS eaten, CASE " +
                   "MIN" +
                   "(isVegan) WHEN" +
                   " 0 " +
                   "THEN 0 ELSE 1 END vegan, preset_calories FROM dish JOIN " +
                   "itemDish ON dish" +
                   ".dishID=itemDish.dish JOIN calories ON calories.dish=dish" +
                   ".dishID JOIN item ON item.itemID=itemDish.item WHERE " +
                   "eaten=?" +
                   " " +
                   "GROUP BY dish.dishID, calories.meal";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, date);

      ResultSet                    rs              = ps.executeQuery();
      ArrayList<ArrayList<Dish>> dish           = new ArrayList<>();
      if (rs.isBeforeFirst()) {
        while (rs.next()) {
          ArrayList<Dish>temp=new ArrayList<>();
          temp.add(new Dish((Integer)rs.getObject("dishid"),(String) rs.getObject(
              "meal"),
                            (String) rs.getObject("name"),
                            (Integer)rs.getObject("vegan"),
                            (Integer)rs.getObject(
                                "preset_calories")));
          dish.add(temp);
        }
        return dish;
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  //delete dish from editDish
  public boolean removeFromEditDish(int id){
    String query="DELETE FROM dish WHERE dishID=?";
    String query2="DELETE FROM itemDish WHERE dish=?";
    try{
      PreparedStatement ps=conn.prepareStatement(query);
      ps.setInt(1,id);
      int x=ps.executeUpdate();
      if (x<=0){
        return false;
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    try{
      PreparedStatement ps=conn.prepareStatement(query2);
      ps.setInt(1,id);
      int x=ps.executeUpdate();
      return x>0;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return false;
  }

  //removes a dish from todays food
  public boolean removeFromCaloriesTable(int dish, String meal) {
    String query = "DELETE FROM calories WHERE dish=? AND meal=? AND date=" +
                   "(date('now'))";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1, dish);
      ps.setString(2, meal);
      int x=ps.executeUpdate();
      return x>0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /*
  this means "can we add to the table: true/false!"
   */
  public boolean isAddDishToTable(String mealTime, String dishID,
                                  String calories) {
    String query = "INSERT INTO calories(meal,dish,calories) VALUES(?,?,?)";
    //checks it's not already in the table
    String checkQuery ="SELECT meal,dish FROM calories WHERE dish=? AND " +
                       "meal=? AND date=(date('now'))";

    try{
      PreparedStatement ps=conn.prepareStatement(checkQuery);
      ps.setInt(1,Integer.parseInt(dishID));
      ps.setString(2,mealTime);
      ResultSet rs=ps.executeQuery();
      if (rs.isBeforeFirst()){
        return false;
      }
    }catch (SQLException e){e.printStackTrace();}

    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, mealTime);
      ps.setInt(2, Integer.parseInt(dishID));
      ps.setInt(3, Integer.parseInt(calories));
      int x = ps.executeUpdate();
      return x > 0;
    } catch (NumberFormatException | SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public double setUpProgressBar() {
    String query =
        "SELECT TOTAL(calories)AS tot FROM calories WHERE date=(date('now'))";
    String query2 = "SELECT calorie_goal FROM person";
    double i      = 0;
    double g      = 0;
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      i = (double) rs.getObject("tot");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      PreparedStatement ps2 = conn.prepareStatement(query2);
      ResultSet rs2 = ps2.executeQuery();
      g = (int) rs2.getObject("calorie_goal");
      double d = i / g;
      return d;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return -1;
  }

  public ArrayList<ArrayList<Item>> getItemChooserList(String type) {
    String query = "SELECT *\n" +
                   "FROM item\n" +
                   "WHERE type=?";
    ArrayList<ArrayList<Item>> itemList = new ArrayList<>();
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, type);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ArrayList<Item> temp = new ArrayList<>();
        temp.add(new Item((Integer) rs.getObject("itemID"),
                          (String) rs.getObject("name"),
                          (String) rs.getObject("shop"),
                          (Integer) rs.getObject("isVegan"),
                          (String) rs.getObject("amount"),
                          (Integer) rs.getObject("calories"),
                          (String) rs.getObject("type")));

        itemList.add(temp);
      }
      return itemList;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

public int getCaloriesForTime(String s) {
  String query = "SELECT TOTAL(calories) AS tot FROM calories WHERE " +
                 "meal=? AND date=(date('now'))";
  try {
    PreparedStatement ps = conn.prepareStatement(query);
    ps.setString(1, s);
    ResultSet rs = ps.executeQuery();
    double    d  = (double) rs.getObject("tot");
    return (int) d;
  } catch (SQLException e) {
    e.printStackTrace();
  }
  return -1;
}

//saves an item into tempDish
  public boolean saveTempDish(int id, int calories, String amount) {
    String updateValues = "UPDATE tempDish\n" +
                          "SET setCalories=?, setAmount=? WHERE item=?";
    try {
      PreparedStatement ps = conn.prepareStatement(updateValues);
      ps.setDouble(1, calories);
      ps.setString(2, amount);
      ps.setInt(3, id);
      int bool = ps.executeUpdate();
      return bool > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean removeTempDishItem(int id){
    String query="DELETE FROM tempDish WHERE item=?";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1, id);
      int x=ps.executeUpdate();
      return x>0;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return false;
  }

  public boolean getAmountFromItemDish(int id, int dishID) {
    String query = "SELECT setAmount FROM itemDish WHERE item=? and dish=?";
    String updater ="UPDATE tempDish SET setAmount=? WHERE item=?";
    String[]val=new String[1];
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1, id);
      ps.setInt(2, dishID);
      ResultSet         rs   = ps.executeQuery();
      while (rs.next()) {
         val[0]=String.valueOf(rs.getObject("setAmount"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    try{
      PreparedStatement ps2=conn.prepareStatement(updater);
      ps2.setString(1,val[0]);
      ps2.setInt(2,id);
      int x=ps2.executeUpdate();
      return x>0;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  public ArrayList<String> getAmount(int id) {
    String query = "SELECT setAmount FROM tempDish WHERE item=?";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1, id);
      ResultSet         rs   = ps.executeQuery();
      ArrayList<String> vals = new ArrayList<>();
      while (rs.next()) {
        vals.add(rs.getString("setAmount"));
      }
      return vals;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public int getTempDishCalories(){
    String query="SELECT total(setCalories)AS total FROM tempDish";
    try {
      Statement s = conn.createStatement();
      ResultSet rs=s.executeQuery(query);
      double d=(Double)rs.getObject("total");
      return (int) d;
    }catch (SQLException e){e.printStackTrace();}
    return -1;
  }

  public boolean updateDish(int id, int cals) {
    //get the dynamic calories and amounts from the tableview
    String             getItems  = "SELECT item FROM tempDish";
    String             getAmount = "SELECT setAmount FROM tempDish";
    ArrayList<Integer> items     = new ArrayList<>();
    ArrayList<String>  amounts   = new ArrayList<>();
    try {
      Statement s  = conn.createStatement();
      Statement s2 = conn.createStatement();
      ResultSet i  = s.executeQuery(getItems);
      ResultSet a  = s2.executeQuery(getAmount);
      if (!i.isBeforeFirst() || !a.isBeforeFirst()) {
        return false;
      }
      while (i.next()) {
        items.add((Integer) i.getObject("item"));
      }
      while (a.next()) {
        amounts.add(String.valueOf(a.getObject("setAmount")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      //drop the values already in itemdDish
      String            dropItemDish = "DELETE FROM itemDish WHERE dish=?";
      PreparedStatement ps           = conn.prepareStatement(dropItemDish);
      ps.setInt(1, id);
      int x = 0;
      x = ps.executeUpdate();
      if (x <= 0) {
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      //add the values
      String            query = "INSERT INTO itemDish VALUES(?,?,?)";
      PreparedStatement ps    = conn.prepareStatement(query);
      int               bool  = 0;
      for (int x = 0; x < items.size(); ++x) {
        ps.setInt(1, id);
        ps.setInt(2, items.get(x));
        ps.setString(3, amounts.get(x));
        bool = ps.executeUpdate();
      }
      if (bool <= 0) {
        return false;
      }
    } catch (SQLException e) {
      System.out.println();
      e.printStackTrace();
    }

    try {
      //add the new dish to the dish table
      String query =
          "UPDATE dish SET preset_calories=? WHERE dishID=?";
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1, cals);
      ps.setInt(2, id);
      int bool = ps.executeUpdate();
      return bool > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean saveNewDish(String name, int cals) {
    //get the dynamic calories and amounts from the tableview
    String             getItems  = "SELECT item FROM tempDish";
    String             getAmount = "SELECT setAmount FROM tempDish";
    ArrayList<Integer> items     = new ArrayList<>();
    ArrayList<String>  amounts   = new ArrayList<>();
    try {
      Statement s  = conn.createStatement();
      Statement s2 = conn.createStatement();
      ResultSet i  = s.executeQuery(getItems);
      ResultSet a  = s2.executeQuery(getAmount);
      if (!i.isBeforeFirst() || !a.isBeforeFirst()) {
        return false;
      }
      while (i.next()) {
        items.add((Integer) i.getObject("item"));
      }
      while (a.next()) {
        amounts.add(String.valueOf(a.getObject("setAmount")));
      }
      //this gets the new id to use, as we can't get it to autoincrement
      String    query3    = "SELECT MAX(dish) AS max FROM itemDish";
      int       maxNumber = 0;
      Statement s3        = conn.createStatement();
      ResultSet rs        = s3.executeQuery(query3);
      if (rs.getObject("max")==null) {
        maxNumber=1;
      }
     else{
        maxNumber = (int) rs.getObject("max") + 1;
      }
      //add the values
      String            query4 = "INSERT INTO itemDish VALUES(?,?,?)";
      PreparedStatement s4     = conn.prepareStatement(query4);
      int               bool   = 0;
      for (int x = 0; x < items.size(); ++x) {
        s4.setInt(1, maxNumber);
        s4.setInt(2, items.get(x));
        s4.setString(3, amounts.get(x));
        bool = s4.executeUpdate();
      }
      if (bool < 0) {
        return false;
      }
      //add the new dish to the dish table
      String query5 =
          "INSERT INTO dish (name,preset_calories) VALUES (?,?)";
      PreparedStatement ps2 = conn.prepareStatement(query5);
      ps2.setString(1, name);
      ps2.setDouble(2, cals);
      int bool2 = ps2.executeUpdate();
      return bool2 > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  //TODO put a boolean check on this
  public void dropTempDish() {
    String query = "drop table tempDish;\n" +
                   "\n" +
                   "CREATE TABLE tempDish(\n" +
                   "item int NOT NULL UNIQUE,\n" +
                   "setCalories int,\n" +
                   "setAmount string,\n" +
                   "FOREIGN KEY (item) REFERENCES item(itemID)\n" +
                   ")";
    try {
      Statement s = conn.createStatement();
      s.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean isPortionNull(int id){
    String query="SELECT portion FROM calories WHERE dish=? and date=(date" +
                 "('now'))";
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1,id);
      ResultSet rs = ps.executeQuery();
      ArrayList<Integer>i=new ArrayList<>();
      while (rs.next()) {
        i.add((Integer) rs.getObject("portion"));
      }
      if (i.get(0)==null){
        return true;
      }
    } catch (IndexOutOfBoundsException|SQLException e){e.printStackTrace();
    }
  return false;
  }

  public ArrayList<ArrayList<Item>> getTempItems(int id) {
    String          query = "SELECT * FROM item join itemdish ON itemID=item " +
                            "WHERE dish=?";
    ArrayList<Item> temp  = new ArrayList<>();
    try {
      PreparedStatement ps  = conn.prepareStatement(query);
      ps.setInt(1,id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        temp.add(new Item((Integer) rs.getObject("itemID"),
                          (String) rs.getObject(
                              "name"),
                          (String) rs.getObject("shop"),
                          (Integer) rs.getObject(
                              "isVegan"),
                          (String) rs.getObject("amount"),
                          (Integer) rs.getObject(
                              "calories"),
                          (String) rs.getObject("type")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    ArrayList<ArrayList<Item>> tempWrap = new ArrayList<>();
    tempWrap.add(temp);
    return tempWrap;
  }

  public ArrayList<ArrayList<Item>> getTempItems() {
    String          query = "SELECT * FROM item join tempDish ON itemID=item";
    ArrayList<Item> temp  = new ArrayList<>();
    try {
      Statement s  = conn.createStatement();
      ResultSet rs = s.executeQuery(query);
      while (rs.next()) {
        temp.add(new Item((Integer) rs.getObject("itemID"),
                          (String) rs.getObject(
                              "name"),
                          (String) rs.getObject("shop"),
                          (Integer) rs.getObject(
                              "isVegan"),
                          (String) rs.getObject("amount"),
                          (Integer) rs.getObject(
                              "calories"),
                          (String) rs.getObject("type")));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    ArrayList<ArrayList<Item>> tempWrap = new ArrayList<>();
    tempWrap.add(temp);
    return tempWrap;
  }

  public boolean addCustomDish(String s, int i, String z) {
    int    vegan = 99;
    //get teh corresponding vegan itemID
    if (z.equals("Vegan")){
      vegan=-2;
    }
    if (z.equals("Vegetarian")){
      vegan=-3;
    }
    String query = "INSERT INTO dish (name,preset_calories) VALUES " +
                   "(?,?)";
    //TODO add identical thing into item and set a vegan value
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, s);
      ps.setInt(2, i);
      int x=ps.executeUpdate();
      if (x<=0){
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    //now we need the dishID to add the vegan component to item
    int dishID;
    String getID="SELECT MAX (dishID) AS num FROM dish LIMIT 1";
    try{
      Statement st=conn.createStatement();
      ResultSet rs=st.executeQuery(getID);
      dishID=(Integer) rs.getObject("num");
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return false;
    }
    //now we insert the vegan component
    String isVegan="INSERT INTO itemDish(dish,item) VALUES (?,?)";
    try{
      PreparedStatement ps=conn.prepareStatement(isVegan);
      ps.setInt(1,dishID);
      ps.setInt(2,vegan);
      int x=ps.executeUpdate();
      return x>0;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return false;
  }

  //this method simply adds the foreign key for items to a temp table for a
  // new dish. creates the table if not already extant
  public boolean isAddItemToDish(int id) {
    String create = "CREATE TABLE IF NOT EXISTS tempDish(\n" +
                    "item int NOT NULL UNIQUE,\n" +"setCalories int,\n" +
                    "setAmount string,\n" +
                    "FOREIGN KEY (item) REFERENCES item(itemID)\n" +
                    ")";
    try {
      Statement s = conn.createStatement();
      s.executeUpdate(create);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return false;
    }

    try {
      String            query = "INSERT INTO tempDish (item) VALUES (?)";
      PreparedStatement ps    = conn.prepareStatement(query);
      ps.setInt(1, id);
      int x = ps.executeUpdate();
      return x > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  //TODO not very sophisticated, could be improved?
  public ArrayList<ArrayList<Item>> getItemChooserListKeyword(String keyword) {
    String query = "SELECT *\n" +
                   "FROM item\n" +
                   "WHERE name LIKE (?)";
    ArrayList<ArrayList<Item>> itemList = new ArrayList<>();
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, '%' + keyword + '%');
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ArrayList<Item> temp = new ArrayList<>();
        temp.add(new Item((Integer) rs.getObject("itemID"),
                          (String) rs.getObject("name"),
                          (String) rs.getObject("shop"),
                          (Integer) rs.getObject("isVegan"),
                          (String) rs.getObject("amount"),
                          (Integer) rs.getObject("calories"),
                          (String) rs.getObject("type")));

        itemList.add(temp);
      }
      return itemList;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void dropTables() {

    String[] commands = {
        "PRAGMA foreign_keys=off;",
        "drop table if exists person;",
        "drop table if exists item;",
        "drop table if exists dish;",
        "drop table if exists itemdish;",
        "drop table if exists calories;",
        "drop table if exists weight;",
        "drop table if exists tempDish;"};

    for (String query : commands) {

      try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public void resetDatabase(String file, String file2) {

    dropTables();

    ArrayList<String> defs = loadSQL(file);

    ArrayList<String> data = loadSQL(file2);

    executeSQLUpdates(defs);
    executeSQLUpdates(data);
  }

  private void executeSQLUpdates(ArrayList<String> commands) {

    for (String query : commands) {

      try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private ArrayList<String> loadSQL(String sqlfile) {

    /*
     * This method is to be used only by the resetDatabase() code.
     * Do not use it yourself to load your own SQL.
     */

    ArrayList<String> commands = new ArrayList<String>();

    try {
      BufferedReader reader =
          new BufferedReader(new FileReader(sqlfile + ".sql"));

      String command = "";

      String line = "";

      while ((line = reader.readLine()) != null) {

        if (line.contains(";")) {
          command += line;
          command = command.trim();
          commands.add(command);
          command = "";
        } else {
          line = line.trim();
          command += line + " ";
        }
      }

      reader.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return commands;

  }
}
