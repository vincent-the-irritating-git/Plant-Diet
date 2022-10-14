package com.dissertation.tests;

import org.junit.jupiter.api.*;
import com.dissertation.server_side.Database;
import com.dissertation.server_side.Item;
import com.dissertation.server_side.Message;
import com.dissertation.server_side.Protocol;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ItemChooserControllerTest {

  private Database db = null;
  private Protocol                   p        =null;
  private ArrayList<ArrayList<Item>> itemList =new ArrayList<>();

  @BeforeEach
  void setUp() {
    db = new Database();
    db.openDatabase();
    p=new Protocol();
  }

  @Test
  @DisplayName("check to see if the itemList initialises on scene change")
  void setUpTable(){
    String type="bread";
    Message message =new Message("itemChooser-table" + " " + type);
    p.protocolToUse(message);
    itemList=p.getM().getItemData();
    assertEquals(itemList,p.getM().getItemData());
    assertEquals("aldi",p.getM().getItemData().get(0).get(0).getShop());
  }

  @Test
  @DisplayName("check to see if addItemToDish(int) works")
  void checkAddItem(){
    Message message=new Message("add-item-to-new-dish"+" "+8);
    p.protocolToUse(message);
    assertEquals("dish-added-to-to-temp",p.getM().getMessage());
  }

  @AfterEach
  void reset() {
//    itemList.clear();
    if (db.getConnection() != null) {
      db.closeDatabase();
    }
    db.openDatabase();
    db.resetDatabase("tabledef", "tabledebugdata");
    db.closeDatabase();
  }
}