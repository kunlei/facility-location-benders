package com.learn.opt.benders;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

public class MIPModel {
  private final Instance instance;

  private IloCplex cplex;

  private IloNumVar[] open;
  private IloNumVar[][] flow;

  private IloObjective obj;
  private IloRange[] consDemand;
  private IloRange[] consCapacity;

  public MIPModel(Instance instance) {
    this.instance = instance;

    int numFacilities = instance.getNumFacilities();
    int numCustomers = instance.getNumCustomers();

    try {
      cplex = new IloCplex();

      // create decision variables that indicate whether a facility is open or not
      open = new IloNumVar[numFacilities];
      for (int f = 0; f < numFacilities; ++f) {
        open[f] = cplex.boolVar("open_" + f);
      }

      // create decision variables that represent the demand of customer c that are
      // satisfied by facility f
      flow = new IloNumVar[numFacilities][numCustomers];
      for (int f = 0; f < numFacilities; ++f) {
        for (int c = 0; c < numCustomers; ++c) {
          flow[f][c] = cplex.numVar(0, Double.MAX_VALUE, "flow_" + f + "," + c);
        }
      }

      // create objective function
      IloLinearNumExpr expr = cplex.linearNumExpr();
      for (int f = 0; f < numFacilities; ++f) {
        expr.addTerm(instance.getFixedCost(f), open[f]);
        for (int c = 0; c < numCustomers; ++c) {
          expr.addTerm(instance.getFlowCost(f, c), flow[f][c]);
        }
      }
      obj = cplex.addMinimize(expr, "obj");

      // create constraints
      consDemand = new IloRange[numCustomers];
      for (int c = 0; c < numCustomers; ++c) {
        expr.clear();
        for (int f = 0; f < numFacilities; ++f) {
          expr.addTerm(1.0, flow[f][c]);
        }
        consDemand[c] = cplex.addGe(expr, instance.getDemand(c), "demand_" + c);
      }

      consCapacity = new IloRange[numFacilities];
      for (int f = 0; f < numFacilities; ++f) {
        expr.clear();
        for (int c = 0; c < numCustomers; ++c) {
          expr.addTerm(1.0, flow[f][c]);
        }
        expr.addTerm(-instance.getCapacity(f), open[f]);
        consCapacity[f] = cplex.addLe(expr, 0, "capacity_" + f);
      }

    } catch (IloException e) {
      e.printStackTrace();
    }
  }

  public void solve() {
    try {
      if (cplex.solve()) {
        System.out.println("obj = " + cplex.getObjValue());

        int numFacilities = instance.getNumFacilities();
        int numCustomers = instance.getNumCustomers();
        double[] optOpen = cplex.getValues(open);
        double[][] optFlow = new double[numFacilities][];
        for (int f = 0; f < numFacilities; ++f) {
          optFlow[f] = cplex.getValues(flow[f]);
        }

        for (int f = 0; f < numFacilities; ++f) {
          System.out.print("facility " + f + ": ");
          if (optOpen[f] > 0.99) {
            System.out.println("open, ");
            for (int c = 0; c < numCustomers; ++c) {
              System.out.print(optFlow[f][c] + "\t");
            }
            System.out.println();
          } else {
            System.out.println("close, ");
          }
        }
      }
    } catch (IloException e) {
      e.printStackTrace();
    }
  }

}
