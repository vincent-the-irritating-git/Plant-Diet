package com.dissertation.misc;

import java.util.ArrayList;

public abstract class Quantities {

  public enum quantity {
    WHOLE("bag", "tin","whole bottle","can", 1),
    HALF("bag", "tin", 0.5),
    THIRD("bag", "tin", 0.33),
    QUARTER("bag", "tin", 0.25),
    TABLESPOON("spoon",1),
    TEASPOON("spoon",0.33),
    GLASS("bottle",1),
    HANDFUL("handful bag",1);

    public String getContainer() {
      return container;
    }

    public String getContainer2() {
      return container2;
    }

    public double getPercent() {
      return percent;
    }

    private String container;
    private String container2;
    private String container3;
    private String container4;
    private double percent;

    public String getContainer4() {
      return container4;
    }

    public String getContainer3() {
      return container3;
    }

    quantity(String container, double percent) {
      this.container = container;
      this.percent   = percent;
    }

    quantity(String container, String container2, double percent) {
      this.container  = container;
      this.container2 = container2;
      this.percent    = percent;
    }

    quantity(String container,String container2,
             String container3,String container4,double percent){
      this.container  = container;
      this.container2 = container2;
      this.container3=container3;
      this.container4=container4;
      this.percent    = percent;
    }
  }

  //returns an array list of quantities from the type given
  public ArrayList<Quantities.quantity> addValues(String container) {
    ArrayList<Quantities.quantity> vals = new ArrayList<>();
    for (Quantities.quantity s : Quantities.quantity.values()) {
      //now the if part that test the get()
      if (container.equals(s.getContainer()) ||
          container.equals(s.getContainer2())||container.equals(s.getContainer3())||container.equals(s.getContainer4())) {
        vals.add(s);
//        System.out.println(s);
      }
    }
    return vals;
  }

  public ArrayList<String> addValuesString(String container) {
    ArrayList<String> vals = new ArrayList<>();
    for (Quantities.quantity s : Quantities.quantity.values()) {
      //now the if part that test the get()
      if (container.equals(s.getContainer()) ||
          container.equals(s.getContainer2())||container.equals(s.getContainer3())||container.equals(s.getContainer4())) {
        try {
          vals.add(s.toString());
        }catch (Exception e){e.printStackTrace();}
        System.out.println(s);
      }
    }
    return vals;
  }

  public Quantities.quantity getQuantityFromString(String quantity){
    return Quantities.quantity.valueOf(quantity);
  }

}

