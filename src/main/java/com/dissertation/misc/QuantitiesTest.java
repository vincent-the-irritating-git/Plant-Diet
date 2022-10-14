package com.dissertation.misc;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QuantitiesTest extends Quantities {

  Quantities q;

  @BeforeEach
  @Test
  void setUp() {
    q = new QuantitiesTest();
  }

  @Test
  @DisplayName("checks if the container is loading the right quantity options")
  void enumTestValues() {
    ArrayList<Quantities.quantity> vals2 = new ArrayList<>();
    vals2.add(quantity.TABLESPOON);
    vals2.add(quantity.TEASPOON);
    assertEquals(vals2, q.addValues("spoon"));
    vals2.clear();
    vals2.add(quantity.GLASS);
    assertEquals(vals2, q.addValues("bottle"));
    vals2.clear();
    vals2.add(quantity.WHOLE);
    assertEquals(vals2, q.addValues("whole bottle"));
  }

  @Test
  void blankTest() {
    ArrayList<Quantities.quantity> vals2 = new ArrayList<>();
    assertEquals(vals2, q.addValues("salad fingers"));
  }

  @Test
  void bagTest() {
    ArrayList<Quantities.quantity> vals2 = new ArrayList<>();
    vals2.add(quantity.WHOLE);
    vals2.add(quantity.HALF);
    vals2.add(quantity.THIRD);
    vals2.add(quantity.QUARTER);
    assertEquals(vals2, q.addValues("bag"));
    assertEquals(1,vals2.get(0).getPercent());
    assertEquals(0.5,vals2.get(1).getPercent());
  }

  @Test
  void bagTestString(){
    ArrayList<String>blank=new ArrayList<>();
    assertNotEquals(blank,q.addValuesString("bag"));
  }

  @Test
  void getEnumFromString(){
    assertEquals(quantity.WHOLE,q.getQuantityFromString("WHOLE"));
    assertEquals(0.5,q.getQuantityFromString("HALF").getPercent());
  }

  @Test
  void bindTest(){
    IntegerProperty num=new SimpleIntegerProperty(10);
    NumberBinding binder= Bindings.add(5,num);
    IntegerProperty intTest=new SimpleIntegerProperty((Integer) binder.getValue());
    assertEquals(15,binder.getValue());
    assertEquals(15,intTest.getValue());
//    NumberBinding b=Bindings.multiply(5,this.calories);
//    caloriesBound=new SimpleIntegerProperty((Integer) b.getValue());
  }

  @Test
    void spoonTest(){
      ArrayList<Quantities.quantity> vals2 = new ArrayList<>();
      vals2.add(quantity.TABLESPOON);
      vals2.add(quantity.TEASPOON);
      assertEquals(vals2, q.addValues("spoon"));

    }
  }

