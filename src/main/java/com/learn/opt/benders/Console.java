package com.learn.opt.benders;

public class Console {
  public static void main(String[] args) {
    // create a problem instance
    Instance inst = new Instance();
    inst.readData("/Users/klian/dev/facility-location-benders/instance/cap41.txt");
    System.out.println(inst.toString());

    MIPModel mipModel = new MIPModel(inst);
    mipModel.solve();
  }
}
