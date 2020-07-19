package com.learn.opt.benders;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * define a class that represents a facility location problem instance.
 */
public final class Instance {
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
  private double[] fixedCosts;

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

  /**
   * read data from file
   * @param filename name of the input file
   */
  public void readData(String filename) {
    try {
      File file = new File(filename);
      Scanner sc = new Scanner(file);

      numFacilities = sc.nextInt();
      numCustomers = sc.nextInt();

      // create data objects
      capacities = new int[numFacilities];
      fixedCosts = new double[numFacilities];
      demands = new int[numCustomers];
      flowCosts = new double[numCustomers][numFacilities];
      for (int i = 0; i < numCustomers; ++i) {
        flowCosts[i] = new double[numFacilities];
      }

      // read in facility capacities and fixed cost
      for (int f = 0; f < numFacilities; ++f) {
        capacities[f] = sc.nextInt();
        fixedCosts[f] = sc.nextDouble();
      }

      // read in customer demand and flow cost
      for (int c = 0; c < numCustomers; ++c) {
        demands[c] = sc.nextInt();
        for (int f = 0; f < numFacilities; ++f) {
          flowCosts[c][f] = sc.nextDouble() / demands[c];
        }
      }

      sc.close();
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

  public double getFixedCost(int fIdx) {
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
    StringBuilder builder = new StringBuilder("Instance{");
    builder.append("numFacilities: ").append(numFacilities)
        .append("\nnumCustomers: ").append(numCustomers);
    for (int f = 0; f < numFacilities; ++f) {
      builder.append("\nfacility ").append(f).append(": capacity: ")
          .append(capacities[f])
          .append("\tfixedCost: ")
          .append(fixedCosts[f]);
    }
    for (int c = 0; c < numCustomers; ++c) {
      builder.append("\ncustomer ").append(c).append(": demand: ").append(demands[c]);
    }
    for (int c = 0; c < numCustomers; ++c) {
      builder.append("\n");
      for (int f = 0; f < numFacilities; ++f) {
        builder.append(flowCosts[c][f]).append("\t");
      }
    }
    return builder.toString();
  }
}
