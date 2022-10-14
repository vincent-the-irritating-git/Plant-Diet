package com.dissertation.tests;

import com.dissertation.SelectItemController;
import org.junit.jupiter.api.*;
import com.dissertation.server_side.Database;
import com.dissertation.server_side.Item;
import com.dissertation.server_side.Message;
import com.dissertation.server_side.Protocol;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class SelectItemControllerTest {

  private SelectItemController sic =null;
  private Database             db  =null;
  private Protocol p=new Protocol();

  @BeforeEach
  void setUp() {
    sic=new SelectItemController();
    db=new Database();
  }

  @Test
  @DisplayName("checks to see if loadItemChooser(String) will load correctly")
  void checkItemChooser() {
    ArrayList<ArrayList<Item>> i       =new ArrayList<>();
    Message                    message =new Message("itemChooser-table"+" "+
                                                    "fruit + veg");
    p.protocolToUse(message);
    assertEquals("no-item-list-returned",p.getM().getMessage());
    assertEquals(i,p.getM().getItemData());
  }

  @AfterEach
  void reset(){
    if (db.getConnection() != null) {
      db.closeDatabase();
    }
    db.openDatabase();
    db.resetDatabase("tabledef", "tabledebugdata");
    db.closeDatabase();
  }


}