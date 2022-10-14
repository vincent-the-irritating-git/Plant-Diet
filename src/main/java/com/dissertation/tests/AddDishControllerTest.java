package com.dissertation.tests;

import com.dissertation.AddDishController;
import org.junit.jupiter.api.*;
import com.dissertation.server_side.Database;
import com.dissertation.server_side.Dish;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AddDishControllerTest {

  public static final String DEBUG_EMAIL     = "veron451@yahoo.co.uk";
  public static final String DEBUG_BAD_EMAIL = "greavsie@yahoo.co.uk";

  AddDishController ad;
  Database          db;
  public static final Dish DEBUG_DISH_BREAKFAST = new Dish(1, "breakfast",
                                                          "ham sandwich"
      , 1,
                                                 226);
  public static final Dish DEBUG_DISH_DINNER = new Dish(1, "dinner",
                                                           "ham sandwich"
      , 1,
                                                           226);
  public static final Dish DEBUG_DISH_TEA = new Dish(1, "tea",
                                                           "ham sandwich"
      , 1,
                                                           226);
  public static final Dish DEBUG_DISH_SNACK = new Dish(1, "snack",
                                                           "ham sandwich"
      , 1,
                                                           226);
  public static final Dish DEBUG_DISH_PUDDING = new Dish(1, "pudding",
                                                           "ham sandwich"
      , 1,
                                                           226);


  @BeforeEach
  void setUp() throws IOException {
    ad = new AddDishController();
  }

  @Test
  @DisplayName("checks to see if the search boolean works")
  void checkSearchBoolean() {
    assertFalse(ad.isSearchFindsDish(DEBUG_DISH_BREAKFAST.getName(), "pease pudding"));
    assertFalse(ad.isSearchFindsDish(DEBUG_DISH_BREAKFAST.getName(), "gypsy creams"));
    assertTrue(ad.isSearchFindsDish(DEBUG_DISH_BREAKFAST.getName(), "ham sandwich"));
    assertTrue(ad.isSearchFindsDish(DEBUG_DISH_BREAKFAST.getName(), "ham"));
  }

//  @Test
//  @DisplayName("checks to see if getListOfMeals returns the correct list")
//  void checkReturnList() {
//    ArrayList<String> blank = new ArrayList<>();
//    ArrayList<String> breakfast  = new ArrayList<>();
//    ArrayList<String> dinner  = new ArrayList<>();
//    ArrayList<String> tea  = new ArrayList<>();
//    ArrayList<String> snack  = new ArrayList<>();
//    ArrayList<String> pudding  = new ArrayList<>();
//    ArrayList<ArrayList<Dish>>test=new ArrayList<>();
//
//    breakfast.add(String.valueOf(DEBUG_DISH_BREAKFAST.getID()));
//    breakfast.add(DEBUG_DISH_BREAKFAST.getTime());
//    breakfast.add(DEBUG_DISH_BREAKFAST.getName());
//    breakfast.add(String.valueOf(DEBUG_DISH_BREAKFAST.isVegan()));
//    breakfast.add(DEBUG_DISH_BREAKFAST.getCalories());
//
//    dinner.add(String.valueOf(DEBUG_DISH_DINNER.getID()));
//    dinner.add(DEBUG_DISH_DINNER.getTime());
//    dinner.add(DEBUG_DISH_DINNER.getName());
//    dinner.add(String.valueOf(DEBUG_DISH_DINNER.isVegan()));
//    dinner.add(DEBUG_DISH_DINNER.getCalories());
//
//    tea.add(String.valueOf(DEBUG_DISH_TEA.getID()));
//    tea.add(DEBUG_DISH_TEA.getTime());
//    tea.add(DEBUG_DISH_TEA.getName());
//    tea.add(String.valueOf(DEBUG_DISH_TEA.isVegan()));
//    tea.add(DEBUG_DISH_TEA.getCalories());
//
//    snack.add(String.valueOf(DEBUG_DISH_SNACK.getID()));
//    snack.add(DEBUG_DISH_SNACK.getTime());
//    snack.add(DEBUG_DISH_SNACK.getName());
//    snack.add(String.valueOf(DEBUG_DISH_SNACK.isVegan()));
//    snack.add(DEBUG_DISH_SNACK.getCalories());
//
//    pudding.add(String.valueOf(DEBUG_DISH_PUDDING.getID()));
//    pudding.add(DEBUG_DISH_PUDDING.getTime());
//    pudding.add(DEBUG_DISH_PUDDING.getName());
//    pudding.add(String.valueOf(DEBUG_DISH_PUDDING.isVegan()));
//    pudding.add(DEBUG_DISH_PUDDING.getCalories());
//
//    test.add(breakfast);
//    test.add(dinner);
//    test.add(pudding);
//    test.add(snack);
//    test.add(tea);
//    assertEquals(test, ad.getListOfMeals(DEBUG_EMAIL));
//    assertEquals(blank, ad.getListOfMeals(DEBUG_BAD_EMAIL));
//  }

  @Test
  @DisplayName("test isNumeric(String)")
  void isNumeric(){
    assertTrue(AddDishController.isNumeric("2.5"));
  }

  @Test
  @DisplayName("checks the filtered list we got from the getMealList()")
  void checkList() {
    List<Dish> test = new ArrayList<>();
    test.add(DEBUG_DISH_BREAKFAST);
    assertEquals(test.toString(),
                 ad.prepareListForFiltering().toString());
  }

  @AfterEach
  void reset() {
    db = new Database();
    if (db.getConnection() != null) {
      db.closeDatabase();
    }
    db.openDatabase();
    db.resetDatabase("tabledef", "tabledebugdata");
    db.closeDatabase();
  }
}