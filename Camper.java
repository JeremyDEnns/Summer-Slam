import java.util.*;
import java.io.*;

public class Camper {
  public String name;
  public String cabin;
  public HashMap<String, Integer> choices;
  public HashMap<Integer, Activity> activities;
  public HashMap<String, Boolean> choice_filled;
  public HashMap<Integer, Boolean> day_filled;
  public ArrayList<HashMap<String, Integer>> choice_map;
  public ArrayList<Integer> personal_demand;
  public String[] activity_list;
  public ArrayList<ArrayList<Activity>> alternate_arrangements;

  public Camper(String set_name, String set_cabin) {
    name = set_name;
    cabin = set_cabin;
    choices = new HashMap<String, Integer>();
    choice_filled = new HashMap<String, Boolean>(); //records which choices have already been filled
    day_filled = new HashMap<Integer, Boolean>();
    activities = new HashMap<Integer, Activity>();
    choice_map = new ArrayList<HashMap<String, Integer>>();
    personal_demand = new ArrayList<Integer>();
    alternate_arrangements = new ArrayList<ArrayList<Activity>>();
    activity_list = new String[]{"Zip Line", "Trail Ride", "Climbing Wall", "Bazooka Ball", "Canoeing", "Archery", "Willson Ball", "Disc Golf", "Board Games", "Bracelet Making", "Volleyball", "Basketball", "Soccer", "Shower Time", "Quiet Time"};
    for (int i = 1; i <= 4; i++) {
      day_filled.put(i, false);
    }
    for (String i : activity_list) {
      choice_filled.put(i, false);
    }
    
  }

  public void setChoiceMap() {

    boolean[][] set_days = {{true, true, true, true},{true, true, false, true},{true, true, true, true},{true, true, false, true},{true, true, true, true},{true, true, true, true},{true, false, true, false},{false, true, false, false},{false, false, true, false},{true, false, false, true},{false, false, true, false},{false, false, false, true},{false, true, false, false},{false, true, true, true},{true, false, true, false}};
    
    choice_map = new ArrayList<HashMap<String, Integer>>();
    for (int i = 0; i < 4; i++) {
      choice_map.add(new HashMap<String, Integer>());
      for (int ii = 0; ii < activity_list.length; ii++) {
        int mapping = 0;
        if (choices.get(activity_list[ii]) != 0 && set_days[ii][i] == true) {
          mapping = 9 - choices.get(activity_list[ii]);
        }
        choice_map.get(i).put(activity_list[ii], mapping);
      }
    }
    personal_demand = Lib.addCollumns(choice_map, activity_list);
  }

  public void setChoice(String activity, int choice) {
    choices.put(activity, choice);
  }

  public void setActivity(Activity activity, int day) {
    activities.put(day, activity);
  }

  public int getChoice (String activity) {
    return choices.get(activity);
  } 
}
