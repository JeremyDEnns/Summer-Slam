import java.util.*;
import java.io.*;

public class Activity {
  public String name;
  public int capacity;
  public boolean[] days_open;
  public int open_days;
  public ArrayList<ArrayList<Camper>> campers;
  public ArrayList<Integer> day_demand;

  public Activity(String set_name, boolean[][] set_days) {
    name = set_name;
    capacity = 0;
    days_open = new boolean[4];
    campers = new ArrayList<ArrayList<Camper>>();
    for (int i = 0; i < 4; i++) {
      campers.add(new ArrayList<Camper>());
    }

    String[] activity_list = {"Zip Line", "Trail Ride", "Climbing Wall", "Bazooka Ball", "Canoeing", "Archery", "Willson Ball", "Disc Golf", "Board Games", "Bracelet Making", "Volleyball", "Basketball", "Soccer", "Shower Time", "Quiet Time"};

    int[] capacities = {14, 12, 14, 14, 14, 14, 16, 20, 20, 20, 20, 20, 20, 16, 16};

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

}
