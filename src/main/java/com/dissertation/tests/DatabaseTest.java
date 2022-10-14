package com.dissertation.tests;

import org.junit.jupiter.api.*;
import com.dissertation.server_side.Database;
import com.dissertation.server_side.Dish;
import com.dissertation.server_side.Item;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class DatabaseTest {

  private Database                   db        = null;
  private ArrayList<ArrayList<Dish>> blankDish = new ArrayList<>();
  private ArrayList<ArrayList<Item>> blankItem = new ArrayList<>();


  @BeforeEach
  void setUp() {
    db = new Database();
    db.openDatabase();
  }

  @Test
  @DisplayName("checks to see if the weight booleans are correct")
  void checkWeight() {
    boolean a = db.addWeight("150");
    assertTrue(a);
  }

  @Test
  @DisplayName("checks the get calorie goal method")
  void checkCalorieGoal() {
    assertEquals(2000, db.getCalories());
  }

  @Test
  @DisplayName("checks the getMealList() method, should return every meal " +
               "they've" +
               " eaten")
  void checkGetMeals() {
    ArrayList<ArrayList<Dish>> blankDish = new ArrayList<>();

    assertNotEquals(blankDish, db.getMealList());
    assertNotNull(db.getMealList());
  }

  @Test
  @DisplayName("check isRegistered() to see if we need to load the login " +
               "screen")
  void checkIsRegistered(){
    assertTrue(db.isRegistered());
  }

  @Test
  @DisplayName("checks to see if the table updates by double clicking works " +
               "and sends to calories table")
  void checkIsAddToTable() {
    assertTrue(db.isAddDishToTable("breakfast","4","400"));
  }

  @Test
  @DisplayName("checks to see if the table not updates by double clicking " +
               "works " +
               "and sends to calories table")
  void checkIsNotAddToTable() {
    assertFalse(db.isAddDishToTable( "breakfast", "2", "320"));
  }

  @Test
  @DisplayName("checks to see if the method returns the food eaten on the date " +
               "specified")
  void checkGetTodaysFood() {
    //food in the past
    assertNotEquals(blankDish, db.getTodaysFoodEaten("2021-08-03"));
    assertNotNull(db.getTodaysFoodEaten("2021-08-03"));
    //food today
    assertNotEquals(blankDish,
                    db.getTodaysFoodEaten(
                                          LocalDate.now().toString()));
    //no food eaten
    assertNull(db.getTodaysFoodEaten("2021-08-02"));
  }

  @Test
  @DisplayName("checks to see if the method returns the food eaten on the date " +
               "specified")
  void checkGetTodaysFoodPast() {
    //food in the past
    assertNotEquals(blankDish, db.getTodaysFoodEaten("2021-08-03"));
    assertNotNull(db.getTodaysFoodEaten("2021-08-03"));
  }

  @Test
  @DisplayName("checks to see if the method returns the food eaten on the date " +
               "specified")
  void checkGetTodaysFoodToday() {
    //food today
    assertNotEquals(blankDish,
                    db.getTodaysFoodEaten(
                        LocalDate.now().toString()));
    assertNotNull(db.getTodaysFoodEaten(
                      LocalDate.now().toString()));
  }

  @Test
  @DisplayName("checks to see if the method returns the food eaten on the date " +
               "specified")
  void checkGetTodaysFoodNothingEaten() {
    //no food eaten
    assertNull(db.getTodaysFoodEaten("2021-08-02"));
  }

  @Test
  @DisplayName("check getCustomPortion(int, String)")
  void checkCustomPortion(){
    assertNotEquals(-1,db.getCustomPortion(2,"breakfast"));
    assertEquals(100,db.getCustomPortion(2,"breakfast"));
    assertEquals(-1,db.getCustomPortion(4,"pudding"));
    db.isAddDishToTable("dinner","2","250");
    assertEquals(-1,db.getCustomPortion(2,"dinner"));
  }

  @Test
  @DisplayName("check setUpProgressBar(double) to see if it works")
  void setUpProgressBar(){
    assertNotEquals(-1,db.setUpProgressBar());
  }

  @Test
  @DisplayName("check getPresetPortion(int)")
  void checkPresetPortion(){
    assertNotEquals(-1,db.getPresetPortion(1));
  }

  @Test
  @DisplayName("check setPresetPortion(int)")
  void checkSetPortion(){
    assertTrue(db.setPresetPortion("breakfast",2,70, 160));
    assertFalse(db.setPresetPortion("pudding",2,70,160));
  }

  @Test
  @DisplayName("check isPortionNull(int)")
  void checkIsPortionNull(){
    assertFalse(db.isPortionNull(2));
    assertTrue(db.isPortionNull(1));
  }

  @Test
  @DisplayName("checks getCalorieForTime(String) returns an int of total " +
               "calories")
  void checkGetCalorieForTime(){
    assertNotEquals(-1,db.getCaloriesForTime("breakfast"));
    assertNotEquals(0,db.getCaloriesForTime("breakfast"));
    assertEquals(0,db.getCaloriesForTime("tea"));
  }

  @Test
  @DisplayName("checks to see if the itemChooser method returns the correct " +
               "list of Items")
  void checkGetItemChooser() {
    assertNotEquals(blankItem, db.getItemChooserList("bread"));
    assertNotEquals(blankItem, db.getItemChooserList("\"meat\""));
    assertNotEquals(blankItem, db.getItemChooserList("fruit, veg and pulses"));
    assertEquals(blankItem, db.getItemChooserList("DEBUG"));

  }

  @Test
  @DisplayName("checks the ItemChooser by keyword")
  void checkItemChooserKeyword() {
    assertNotEquals(blankItem, db.getItemChooserListKeyword("ham"));
    assertNotEquals(blankItem, db.getItemChooserListKeyword("ha"));
    assertNotEquals(blankItem, db.getItemChooserListKeyword("am"));
    assertNotEquals(blankItem, db.getItemChooserListKeyword("brown brea"));
    assertEquals(blankItem, db.getItemChooserListKeyword("DEBUG"));
  }

  @Test
  @DisplayName("checks the temp table add and creation")
  void checkTempAdd() {
    assertTrue(db.isAddItemToDish(1));
  }

  @Test
  @DisplayName("check the item list for the temp table")
  void checkTempList() {
    ArrayList<Item>blankArray=new ArrayList<>();
    assertEquals(blankArray,db.getTempItems().get(0));
    db.isAddItemToDish(1);
    assertNotEquals(blankArray,db.getTempItems().get(0));
  }

  @Test
  @DisplayName("check if the tempDish update is working")
  void checkTempDish() {
    assertFalse(db.saveTempDish(5, 56, "THIRD"));
    db.isAddItemToDish(5);
    assertTrue(db.saveTempDish(5, 56, "THIRD"));
  }

  @Test
  @DisplayName("check removeTempDishItem(int)")
  void checkRemoveItem(){
    db.isAddItemToDish(1);
    assertTrue(db.removeTempDishItem(1));
    assertFalse(db.removeTempDishItem(-1));
  }

  @Test
  @DisplayName("checks if removeFromCaloriesTable(int, String) has succeeded")
  void checkRemoveFromCalories(){
    assertTrue(db.removeFromCaloriesTable(2,"breakfast"));
    assertFalse(db.removeFromCaloriesTable(2,"tea"));
    assertFalse(db.removeFromCaloriesTable(0,"rich evans"));
  }

  @Test
  @DisplayName("checks to see if we can retrieve preset amounts in tempDish")
  void checkCaloriesAndAmounts() {
    ArrayList<String> blank = new ArrayList<>();
    assertEquals(blank, db.getAmount(5));
    db.isAddItemToDish(5);
    db.saveTempDish(5,200,"HALF");
    assertNotEquals(blank, db.getAmount(5));
    assertNotNull(db.getAmount(5));
    }

//  @Test
//  @DisplayName("checks to see if we can retrieve preset amounts for editable " +
//               "items in tempDish")
//  void checkCaloriesAndAmountsEditable() {
//    ArrayList<String> blank = new ArrayList<>();
//    assertNotEquals(blank, db.getAmount(1));
//    assertNotEquals(null, db.getAmount(1).get(0));
//  }

  @Test
  @DisplayName("checks to see if the tempDish save works")
  void checkSaveTempDish() {
    assertFalse(db.saveNewDish("chocolate surprise", 670));
    db.isAddItemToDish(5);
    db.saveTempDish(5,200,"HALF");
    assertTrue(db.saveNewDish("chocolate surprise", 670));
  }

  @Test
  @DisplayName("checks getDetails() returns our details")
  void checkGetDetails() {
    ArrayList<ArrayList<String>> blank = new ArrayList<>();
    assertNotEquals(null, db.getDetails());
    assertNotEquals(blank, db.getDetails());
  }

  @Test
  @DisplayName("checks changeHeight(String)")
    void checkChangeHeight(){
    assertEquals("181",db.changeHeight("181"));
    assertNotEquals("DEBUG",db.changeHeight("DEBUG"));
  }

  @Test
  @DisplayName("checks changeWeight(String)")
  void checkChangeWeight(){
    assertEquals("75.5",db.changeWeight("75.5"));
    assertNotEquals("DEBUG",db.changeWeight("DEBUG"));
  }

  @Test
  @DisplayName("checks getDishForLabels(int)")
  void checkGetDishForLabels(){
    ArrayList<ArrayList<String>>blank=new ArrayList<>();
    assertNotNull(db.getDishForLabels(255));
    assertEquals(blank,db.getDishForLabels(255));
    assertNotEquals(blank,db.getDishForLabels(1));
    assertNotNull(db.getDishForLabels(1));
  }

  @Test
  void checkGetDishComponents(){
    assertNotNull(db.getTempItems(1));
    assertNotEquals(blankItem,db.getTempItems(1).get(0));
    assertEquals(blankItem,db.getTempItems(255).get(0));
  }

  @Test
  void checkGetAmountFromItemDish(){
    assertFalse(db.getAmountFromItemDish(1,1));
    db.isAddItemToDish(1);
    db.saveTempDish(5,200,"1");
    assertTrue(db.getAmountFromItemDish(1,1));
  }

  @Test
  @DisplayName("check getFrequencies(String)")
  void checkGetFrequencies(){
    ArrayList<ArrayList<String>>blank=new ArrayList<>();
    assertNotNull(db.getFrequencies("breakfast"));
    assertNotEquals(blank,db.getFrequencies("breakfast"));
    assertEquals(blank,db.getFrequencies("pudding"));
  }

  @Test
  @DisplayName("check the total from tempDish")
  void getTempDishCalories() {
    assertEquals(0, db.getTempDishCalories());
    db.isAddItemToDish(5);
    db.saveTempDish(5,200,"HALF");
    assertEquals(200, db.getTempDishCalories());
  }

  @Test
  @DisplayName("check addCustomDish(String,int,String) adds the custom dish")
  void checkAddCustomDish(){
    assertTrue(db.addCustomDish("DEBUG",300,"Vegan"));
  }

  //TODO this doesn't work
  @Test
  @DisplayName("check updateDish(int,int)")
  void checkUpdateDish() {
    assertFalse(db.updateDish(4, 350));
    db.isAddItemToDish(5);
    db.saveTempDish(5,200,"2");
    assertTrue(db.updateDish(4, 350));
  }

  @AfterEach
  void reset() {
    if (db.getConnection() != null) {
      db.closeDatabase();
    }
    db.openDatabase();
    db.resetDatabase("tabledef", "tabledebugdata");
    db.closeDatabase();
  }

}