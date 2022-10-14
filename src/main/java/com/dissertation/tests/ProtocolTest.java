package com.dissertation.tests;

import org.junit.jupiter.api.*;
import com.dissertation.server_side.Dish;
import com.dissertation.server_side.Item;
import com.dissertation.server_side.Message;
import com.dissertation.server_side.Protocol;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ProtocolTest {

  public  String[]                   protocolTest;
  private Protocol                   p;
  private String                     goodE     = "veron451@yahoo.co.uk";
  private ArrayList<ArrayList<Item>> blankItem =new ArrayList<>();

  @BeforeEach
  void setUp() {
    p = new Protocol();
  }

  @Deprecated
  void signinProtocolToUse() {
    Message message = new Message("signin veron451@yahoo.co.uk sprout");
    p.protocolToUse(message);
    assertEquals("signed-in", p.getM().getMessage());
  }

  @Deprecated
  @DisplayName("checks to see if the protocol array contains what it should")
  void arrayTest() {
    Message message = new Message("signin veron451@yahoo.co.uk sprout");
    p.protocolToUse(message);
    protocolTest = message.getMessage().split(" ", 3);
    assertEquals("signin", protocolTest[0]);
//    assertEquals(DatabaseTest.goodEmail, protocolTest[1]);
//    assertEquals(DatabaseTest.goodPass, protocolTest[2]);
  }

  @Test
  @DisplayName("checks to see if the protocol selecter works")
  void invalidProtocolToUse() {
    Message message = new Message("do i not like that");
    p.protocolToUse(message);
    assertEquals("invalid-response", p.getM().getMessage());
  }

  @Test
  @DisplayName("checks to see if the new user is added along with a weight or" +
               " not, or not at all")
  void checkAdd() {
    /*we need to redo this*/
  }

  @Test
  @DisplayName("checks the calorie get")
  void checkCalories() {
    Message message = new Message("get-calories");
    p.protocolToUse(message);
    assertEquals("2000", p.getM().getMessage());
  }

  @Test
  @DisplayName("checks the getMealList()")
  void checkMealListGood() {
    ArrayList<ArrayList<Dish>> dishTestArray = new ArrayList<>();

    Message message = new Message("meals" + " " + goodE);
    p.protocolToUse(message);

    assertEquals("returned-list", p.getM().getMessage());
    assertNotEquals(dishTestArray, p.getM().getDishData());
    assertNotNull(p.getM().getDishData());
  }

  @Test
  @DisplayName("test to see if the add dish to table works")
  void checkAddDishToTable() {
    Message message = new Message("add-dish-to-table" +
                                  " " +
                                  "tea" +
                                  " " +
                                  "1" +
                                  " " +
                                  "400");

    p.protocolToUse(message);
    assertEquals("dish-added", p.getM().getMessage());
    p.protocolToUse(message);
    assertEquals("add-failed", p.getM().getMessage());
    Message message2 =
        new Message("add-dish-to-table" + " " + "pudding" + " " + "4" + " " +
                    "400");
    p.protocolToUse(message2);
    assertEquals("dish-added",p.getM().getMessage());
  }

  @Test
  @DisplayName("checks getTodaysFoodEaten(String)")
  void getTodayFoodGood(){
    //add a dish to the table
    Message goodMessage=
        new Message("add-dish-to-table"+" "+goodE+" "+"tea"+" "+1+" "+50);
    p.protocolToUse(goodMessage);
    //now try and see if it's there
    Message good2Message=new Message("set-the-tables"+" "+goodE);
    p.protocolToUse(good2Message);
    assertEquals("returned-list", p.getM().getMessage());
  }

  @Test
  @DisplayName("checks getTodaysFoodEaten(String)")
  void getTodayFoodBad() {
    Message message = new Message("set-the-tables" + " " + goodE);
    p.protocolToUse(message);
    assertEquals("no-list-returned", p.getM().getMessage());
  }

  @Test
  void itemChooserListCheck(){
    Message message=new Message("itemChooser-table"+" "+"bread");
    p.protocolToUse(message);
    assertEquals("item-choose-list-returned",p.getM().getMessage());
    assertNotEquals(blankItem,p.getM().getItemData());
  }

  @Test
  @DisplayName("check getData() returns person data")
  void checkGetData(){
    ArrayList<ArrayList<String>>blank=new ArrayList<>();
    Message message=new Message("get-details");
    p.protocolToUse(message);
    assertNotEquals(blank,p.getM().getData());
    assertNotNull(p.getM().getData());
  }

  @Test
  @DisplayName("checks getCustomPortion(int, String)")
  void checkGetCustomPortion(){
    Message message=new Message("get-dish-custom-portion"+" "+"2"+" "+"dinner");
    p.protocolToUse(message);
    assertEquals("-1",p.getM().getMessage());
  }

  @Test
  @DisplayName("checks getPresetPortion(int)")
  void checkPresetPortion(){
    Message message=new Message("get-dish-preset-portion"+" "+1);
    p.protocolToUse(message);
    assertEquals("100",p.getM().getMessage());
  }

  @Test
  @DisplayName("checks getPresetPortion(int)")
  void checkPresetPortionBad() {
    Message message2 = new Message("get-dish-preset-portion" + " " + 99);
    p.protocolToUse(message2);
    assertEquals("-1", p.getM().getMessage());
  }

  @Test
  @DisplayName("checks isRegistered()")
  void checkIsRegistered(){
    Message message=new Message("is-registered?");
    p.protocolToUse(message);
    assertEquals("true",p.getM().getMessage());
  }

  @Test
  @DisplayName("checks isPortionNull(int)")
  void checkIsPortionNull(){
    Message message=new Message("is-portion-null"+" "+2);
    p.protocolToUse(message);
    assertEquals("true",p.getM().getMessage());
  }

  @Test
  @DisplayName("checks setUpProgressBar(String)")
  void checkSetUpProgressBar(){
    Message message=new Message("set-up-progress-bar"+" "+goodE);
    p.protocolToUse(message);
    assertNotEquals("0",p.getM().getMessage());
  }

  @Test
  @DisplayName("checks if getCaloriesForTime(String) returns a result and " +
               "wraps it correctly")
  void checkCaloriesForTime(){
    Message message=new Message("get-calories-for-time"+" "+"breakfast");
    p.protocolToUse(message);
    assertNotEquals("nothing-returned",p.getM().getMessage());
    assertNotNull(p.getM().getData());
  }

  @Test
  void itemChooserListCheckKeyword(){
    Message message=new Message("itemChooser-table-keyword"+" "+"delicious " +
                                "ham");
    p.protocolToUse(message);
    assertEquals("no-item-list-returned",p.getM().getMessage());
    assertEquals(blankItem,p.getM().getItemData());

    Message message2=new Message("itemChooser-table-keyword"+" "+"ham");
    p.protocolToUse(message2);
    assertEquals("item-choose-list-returned",p.getM().getMessage());
    assertNotEquals(blankItem,p.getM().getItemData());

    Message message3=new Message("itemChooser-table-keyword"+" "+"fruit + veg");
    p.protocolToUse(message3);
    assertNotEquals("no-item-list-returned",p.getM().getMessage());
  }

  @Test
  void getTempList(){
    Message message=new Message("get-temp-items");
    p.protocolToUse(message);
    assertEquals("item-list-returned",p.getM().getMessage());
    assertNotEquals(blankItem,p.getM().getItemData());
  }

  @Test
  @DisplayName("checks saveTempDish()")
  void saveTempDishGood() {
    Message message =
        new Message("save-tempDish-values" + " " + "5" + " " + "500" + " " +
                    "HALF");
    p.protocolToUse(message);
    assertEquals("tempDish-saved", p.getM().getMessage());
  }

  @Test
  @DisplayName("checks saveTempDish(), failure cases")
  void saveTempDishNFE(){
    Message message2 = new Message("save-tempDish-values" +
                                   " " +
                                   "1" +
                                   " " +
                                   "rich evans" +
                                   " " +
                                   null);
    assertThrows(NumberFormatException.class,()->{
      p.protocolToUse(message2);
    });
  }

  @Test
  @DisplayName("check getDishComponents(int)")
  void checkGetDishComponents(){
    ArrayList<ArrayList<String>>blank=new ArrayList<>();
    Message message=new Message("get-dish-components"+" "+"3");
    p.protocolToUse(message);
    assertNotEquals(blank,p.getM().getData());
    assertNotNull(p.getM().getData());
  }

  @Test
  @DisplayName("check getFrequencies(String)")
  void checkGetFrequencies(){
    ArrayList<ArrayList<String>>blank=new ArrayList<>();
    Message message=new Message("get-frequency-for"+" "+"breakfast");
    p.protocolToUse(message);
    assertNotEquals("no-data",p.getM().getMessage());
    assertNotEquals(blank,p.getM().getData());
  }

  @Test
  @DisplayName(" checks removeTempDishItem(int). should remove an item " +
               "specified")
  void checkRemoveItem(){
    Message message=new Message("remove-temp-dish-item"+" "+"1");
    p.protocolToUse(message);
    assertEquals("item-removed",p.getM().getMessage());
    //for the fail
    Message message2=new Message("remove-temp-dish-item"+" "+"-1");
    p.protocolToUse(message2);
    assertEquals("item-not-removed",p.getM().getMessage());

  }

    @Test
    @DisplayName("checks the saveNewDish method to get the temp values into " +
                 "itemDish and dish")
    void checkSaveNewDish(){
    Message message=
        new Message("save-new-tempDish"+" "+"Rich¬Evans¬Surprise"+" "+5000);
    p.protocolToUse(message);
    assertEquals("tempDish-added-to-dish-successfully",p.getM().getMessage());
    //TODO we need to add a fail testcase
    }

    @Test
    void getTempDishCalories(){
    Message message=new Message("get-tempDish-calories");
    p.protocolToUse(message);
    assertNotEquals("-1",p.getM().getMessage());
    }

  @Test
  @DisplayName("checks to see if there is calories and amount existing in " +
               "the tempDish table")
  void getCaloriesAndAmounts(){
    ArrayList<ArrayList<String>>blank=new ArrayList<>();
    Message message=new Message("get-calories-and-amounts"+" "+5);
    p.protocolToUse(message);
    assertNotEquals(null,p.getM().getData());
    assertNotEquals(blank,p.getM().getData());
    assertEquals("calories-and-amounts-returned",p.getM().getMessage());
    String HALF="HALF";
    assertEquals(HALF,p.getM().getData().get(0).get(0));
  }

  @AfterEach
  void reset() {
    p.resetDatabase();
  }

}