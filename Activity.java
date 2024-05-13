import java.util.*;
import java.io.*;

public class Activity {
  public String name;
  public int capacity;
  public boolean[] days_open;
  public int open_days;
  public ArrayList<ArrayList<Camper>> campers;
  public ArrayList<Integer> day_demand;

  public Activity(String set_name) {
    name = set_name;
    capacity = 0;
    days_open = new boolean[4];
    campers = new ArrayList<ArrayList<Camper>>();
    for (int i = 0; i < 4; i++) {
      campers.add(new ArrayList<Camper>());
    }

    String[] activity_list = {"Zip Line", "Trail Ride", "Climbing Wall", "Bazooka Ball", "Canoeing", "Archery", "Willson Ball", "Disc Golf", "Board Games", "Bracelet Making", "Volleyball", "Basketball", "Soccer", "Shower Time", "Quiet Time"};

    int[] capacities = {14, 12, 14, 14, 14, 14, 16, 20, 20, 20, 20, 20, 20, 16, 16};

    boolean[][] set_days = {{true, true, true, true},  //zipline
                            {true, true, false, true}, //trail ride
                            {true, true, true, true}, //climbing wall
                            {true, true, false, true}, //bazooka ball
                            {true, true, true, true}, //canoeing
                            {true, true, true, true}, //archery
                            {true, false, true, false}, //willsonball
                            {false, true, false, false}, //Disc Golf
                            {false, false, true, false}, //board games
                            {true, false, false, true}, //bracelet making
                            {false, false, true, false}, //volleyball
                            {false, false, false, true}, //basketball
                            {false, true, false, false}, //soccer
                            {false, true, true, true}, //shower time
                            {true, false, true, false}}; //quiet time

    for (int i = 0; i < activity_list.length; i++) {
      if (activity_list[i].equals(name)) {
        capacity = capacities[i];
        days_open = set_days[i];
      }
    }

    for (int i = 0; i < 4; i++) {
      if (days_open[i]) {
        open_days++;
      }
    }
  }

  public void admitCamper(int day, Camper camper) {
    campers.get(day).add(camper);
    camper.setActivity(this, day);
    camper.choice_filled.put(name, true);
    camper.day_filled.put(day + 1, true);
  }

  public void dayDemands() {
    day_demand = new ArrayList<Integer>();
    for (int i = 0; i < 4; i++) {
      double demand_score = 0;
      if (days_open[i]) { //day is open
        demand_score = Double.valueOf(campers.get(i).size() * 100 / capacity); //calculate demand score
        demand_score = Lib.round(demand_score, 2);
      }
      day_demand.add((int) demand_score);
    }
  }
  
}
