package com.learn.opt.benders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * define a class that represents a facility location problem instance.
 */
public class Instance {
  /**
   * total number of facilities
   */
  private int numFacilities;

  /**
   * total number of customers
   */
  private int numCustomers;

  /**
   * facility capacities
   * size: 1 * numFacilities
   */
  private int[] capacities;

  /**
   * facility fixed costs
   * size: 1 * numFacilities
   */
  private int[] fixedCosts;

  /**
   * customer demands
   * size: 1 * numCustomers
   */
  private int[] demands;

  /**
   * allocation cost from facilities to customers
   * size: numCustomers * numFacilities
   */
  private double[][] flowCosts;

  public Instance() {
    numCustomers = 0;
    numFacilities = 0;
    capacities = null;
    fixedCosts = null;
    demands = null;
    flowCosts = null;
  }

  public void readData(String filename) {
    try {
      File file = new File(filename);
      Scanner sc = new Scanner(file);

      sc.useDelimiter("\n\r\\s+");

      numFacilities = sc.nextInt();
      numCustomers = sc.nextInt();

      capacities = new int[numFacilities];
      fixedCosts = new int[numFacilities];
      demands = new int[numCustomers];
      flowCosts = new double[numCustomers][numFacilities];
      for (int i = 0; i < numCustomers; ++i) {
        flowCosts[i] = new double[numFacilities];
      }

      // read in facility capacities and fixed cost
      for (int i = 0; i < numFacilities; ++i) {
        capacities[i] = sc.nextInt();
        fixedCosts[i] = sc.nextInt();
      }

      // read in customer demand and flow cost
      for (int i = 0; i < numCustomers; ++i) {
        demands[i] = sc.nextInt();
        for (int f = 0; f < numFacilities; ++f) {
          flowCosts[i][f] = sc.nextDouble();
        }
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public int getNumFacilities() {
    return numFacilities;
  }

  public int getNumCustomers() {
    return numCustomers;
  }

  public int getCapacity(int fIdx) {
    return capacities[fIdx];
  }

  public int getFixedCost(int fIdx) {
    return fixedCosts[fIdx];
  }

  public int getDemand(int cIdx) {
    return demands[cIdx];
  }

  public double getFlowCost(int fIdx, int cIdx) {
    return flowCosts[cIdx][fIdx];
  }

  @Override
  public String toString() {
    return "Instance{" +
        "numFacilities=" + numFacilities +
        ", numCustomers=" + numCustomers +
        ", capacities=" + Arrays.toString(capacities) +
        ", fixedCosts=" + Arrays.toString(fixedCosts) +
        ", demands=" + Arrays.toString(demands) +
        ", flowCosts=" + Arrays.toString(flowCosts) +
        '}';
  }
}
