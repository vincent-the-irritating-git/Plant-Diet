package com.dissertation.tests;

import com.dissertation.DirectDishController;
import org.junit.jupiter.api.*;
import com.dissertation.server_side.Database;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)

class DirectDishControllerTest {

  DirectDishController dcc;
  Database             db;

  @BeforeEach
  void setUp() {
    dcc=new DirectDishController();
    db=new Database();
  }

  @Test
  @DisplayName("check checkValues correctly error checks input")
  void checkCheckValues(){
    assertTrue(dcc.checkValues("pasta balls","200","Vegan"));
    assertFalse(dcc.checkValues("","200","Vegan"));
    assertFalse(dcc.checkValues("pasta balls","","Vegan"));
    assertFalse(dcc.checkValues("pasta balls","200",""));
    assertFalse(dcc.checkValues("pasta balls","sacrifice","Vegan"));
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