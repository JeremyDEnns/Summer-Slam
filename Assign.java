import java.util.*;
import java.io.*;

public class Assign {
  private ArrayList<Camper> campers;
  private ArrayList<Activity> activities;
  private ArrayList<String> activity_list;
  private ArrayList<ArrayList<Activity>> limited_activities;
  
  public Assign(ArrayList<Camper> set_campers, ArrayList<Activity> set_activities) {
    campers = set_campers;
    activities = set_activities;

    activity_list = new ArrayList<String>(Arrays.asList("Zip Line", "Trail Ride", "Climbing Wall", "Bazooka Ball", "Canoeing", "Archery", "Willson Ball", "Disc Golf", "Board Games", "Bracelet Making", "Volleyball", "Basketball", "Soccer", "Shower Time", "Quiet Time"));

    limitedActivities();

    for (Camper camper : campers) {
      camper.setChoiceMap();
    }

    assignActivities();
  }

  public void limitedActivities() {
    limited_activities = new ArrayList<ArrayList<Activity>>(); //list of activities sorted by number of days open
    for (int i = 0; i < 4; i++) {
      limited_activities.add(new ArrayList<Activity>());
    }
    for (Activity activity : activities) {
      int days_open = 0;
      for (int i = 0; i < activity.days_open.length; i++) {
        if (activity.days_open[i]) {
          days_open++;
        }
      }
      if (days_open > 0) {
        limited_activities.get(days_open - 1).add(activity);
      }
    }
  } 

  public void limitedDayAssignments(int level) {
    for (Camper camper : campers) {
      for (int i = 1; i <= 4; i++) { //iterates through top 4 choice levels
        for (int ii = 0; ii < activity_list.size(); ii++) {
          
          Activity current_activity = activities.get(ii);
          
          if (camper.choices.get(activity_list.get(ii)) == i && limited_activities.get(level - 1).contains(current_activity)) { //choice matches choice level and activity matches limitation level

            ArrayList<String> excluded_activities = new ArrayList<String>();

            for (int iii = 0; iii < 4; iii++) {
              if (camper.activities.get(iii) != null) {
                excluded_activities.add(camper.activities.get(iii).name);
              }
            }

            ArrayList<Double> personal_demand = Lib.setPersonalDemand(camper.choices, excluded_activities); //calculates personal demand for each activity, excluding activities already assigned to camper
              
            for (int iii = 0; iii < 4; iii++) { //loops through days
              if (current_activity.days_open[iii] == false || camper.day_filled.get(iii + 1) == true) { //activity is closed or camper has already filled day 
                personal_demand.set(iii, 100.0); //day will not be pickable
              }
              else {
                personal_demand.set(iii, personal_demand.get(iii) + (current_activity.campers.get(iii).size() + current_activity.campers.get(iii).size())/100.0 + iii/1000); //prioritizes days with fewer campers. iii/1000 ensures that no 2 days have the same exact value
              }
            }

            ArrayList<Integer> day_ranking = Lib.rankValues(personal_demand);
            
            if (day_ranking.size() > 0) {
              current_activity.admitCamper(day_ranking.get(0), camper); //adds camper to the activity
            }
          }
        }
      }
    }
    if (level < 4) {
      limitedDayAssignments(level + 1);
    }
  }

  public void unassignableActivities() {
    for (Camper camper : campers) {
      for (int a = 0; a < 2; a++) { //unassignable activity can occur up to 2 times
        int unassigned_day = 5; //5 means all days assigned
        
        for (int i = 0; i < 4; i++) {
          if (camper.day_filled.get(i + 1) == false) {
            unassigned_day = i;
            break;
          }
        }
  
        if (unassigned_day < 5) { //an activity slot is unfilled
          ArrayList<Activity> subsequent_choices = new ArrayList<Activity>();

          int activity_index = 5;

          while (activity_index < 8) { //adds activities in order of choice number starting from 5th
            for (int i = 0; i < activity_list.size(); i++) {
              if (camper.choices.get(activity_list.get(i)) == activity_index) {
                subsequent_choices.add(activities.get(i)); //adds choice in order
                activity_index++;
              }
            }
          }
          
          for (Activity activity : subsequent_choices) {
            if (activity.days_open[unassigned_day] == true) { //open on that day, best choice
              activity.admitCamper(unassigned_day, camper);
              break;
            }
          }
        }
      }
    }
  }

  public void alternateArrangements() {
    for (Camper camper : campers) {
      ArrayList<Activity> activity_list = new ArrayList<Activity>();
      for (int i = 0; i < 4; i++) { //loops through days
        for (int ii = 0; ii < 4; ii++) {
          try {
            if (camper.activities.get(ii).open_days == i + 1) { //sorts activities by how many days are open
              activity_list.add(camper.activities.get(ii)); //creates list of camper's activities
            }
          }
          catch (Exception e) {
            e.printStackTrace();
            System.out.println("");
            System.out.println(camper.name);
            System.out.println(camper.choices);
            System.out.println(camper.personal_demand);

            
            for (int iii = 0; iii < 4; iii++) {
              if (camper.activities.get(iii) != null) {
                System.out.println("day " + iii + ". " + camper.activities.get(iii).name);
              }
            }
            System.out.println(camper.activities.get(5)); //returns an error
          }
        }
      }

      ArrayList<ArrayList<Activity>> general_list = new ArrayList<ArrayList<Activity>>(); //list of previous iterations of alternate assignments, replaced by temp list

      general_list.add(new ArrayList<Activity>(Arrays.asList(null, null, null, null)));
      
      for (int i = 0; i < activity_list.size(); i++) { //loops through activities
        ArrayList<ArrayList<Activity>> temp_list = new ArrayList<ArrayList<Activity>>(); //current iteration of alternate assignments

        for (int ii = 0; ii < general_list.size(); ii++) {
          ArrayList<Integer> available_days = new ArrayList<Integer>();
          
          for (int iii = 0; iii < camper.personal_demand.size(); iii++) { //day ranking, not in order
            if (general_list.get(ii).get(iii) == null && activity_list.get(i).days_open[iii] == true) { //slot is unoccupied and activity is open
              available_days.add(iii);
            }
          }

          for (int iii = 0; iii < available_days.size(); iii++) {
            ArrayList<Activity> current_arrangement = new ArrayList<Activity>(general_list.get(ii));
            current_arrangement.set(available_days.get(iii), activity_list.get(i));

            temp_list.add(current_arrangement);
          }
        }

        general_list = new ArrayList<ArrayList<Activity>>();

        for (int ii = 0; ii < temp_list.size(); ii++) {
          general_list.add(new ArrayList<Activity>(temp_list.get(ii))); //creates a copy of each value instead of tying them together
        }
        temp_list = new ArrayList<ArrayList<Activity>>();
        
      }
      for (ArrayList<Activity> i : general_list) {
        camper.alternate_arrangements.add(new ArrayList<Activity>(i)); //adds each arrangement to camper's list as a copy
      }
    }
  }

  public void assignAlternates() {
    for (Activity activity : activities) {
      activity.dayDemands();
      for (int i = 0; i < 4; i++) { //days
        if (activity.campers.get(i).size() > activity.capacity) {
          ArrayList<HashMap<String, Integer>> potential_alternates = new ArrayList<HashMap<String, Integer>>();
          
          for (Camper camper : activity.campers.get(i)) {
            
            for (ArrayList<Activity> arrangement : camper.alternate_arrangements) { //check validity of each arrangement
              boolean valid = true;
              for (int ii = 0; ii < 4; ii++) {
                if (arrangement.get(ii).campers.get(ii).size() >= arrangement.get(ii).capacity) { //activity is full
                  valid = false;
                }
              }
              if (valid) {
                HashMap<String, Integer> potential_alternate = new HashMap<String, Integer>();
                
                int demand_score = 0;
                for (int ii = 0; ii < 4; ii++) {
                  demand_score += arrangement.get(ii).day_demand.get(ii);
                }
                potential_alternate.put("Score", demand_score);

                int camper_index = 0;

                for (int ii = 0; ii < campers.size(); ii++) {
                  if (campers.get(ii).equals(camper)) {
                    camper_index = ii;
                    break;
                  }
                }

                potential_alternate.put("Camper", camper_index);

                potential_alternate.put("Day 1", activities.indexOf(arrangement.get(0)));
                potential_alternate.put("Day 2", activities.indexOf(arrangement.get(1)));
                potential_alternate.put("Day 3", activities.indexOf(arrangement.get(2)));
                potential_alternate.put("Day 4", activities.indexOf(arrangement.get(3)));

                Lib.addInOrder(potential_alternate, potential_alternates, "Score"); //adds to potential alternates in order of score
                
              }
            }
          }

          while (activity.campers.get(i).size() > activity.capacity) {
            if (potential_alternates.size() > 0) {
              HashMap<String, Integer> alternate_arrangement = potential_alternates.get(0);
  
              int camper_index = alternate_arrangement.get("Camper");
  
              Camper camper = campers.get(camper_index);
  
              for (int ii = 0; ii < 4; ii++) {
                camper.activities.get(ii).campers.get(ii).remove(camper); //removes camper from activity
              }
  
              for (int ii = 0; ii < 4; ii++) {
                activities.get(alternate_arrangement.get("Day " + (ii + 1))).campers.get(ii).add(camper); //adds camper to activity
                camper.activities.put(ii, activities.get(alternate_arrangement.get("Day " + (ii + 1)))); //adds activity to camper's schedule
              }
  
              for (int ii = 0; ii < potential_alternates.size(); ii++) {
                if (potential_alternates.get(ii).get("Camper") == camper_index) {
                  potential_alternates.remove(ii); //removes other alternates involving the same camper
                }
              }
            }
            else {
              break;
            }
          }
        }
      }
    }
  }

  public void nextChoices() {
    for (Activity activity : activities) {
      for (int i = 0; i < 4; i++) {
        if (activity.campers.get(i).size() > activity.capacity) { //activity is over capacity
          reassign(5, i, activity);
        }
      }
    }
  }

  public void reassign(int choice_allowance, int day, Activity activity) {
    ArrayList<HashMap<String, Integer>> potential_reassignments = new ArrayList<HashMap<String, Integer>>();
    
    for (Camper camper : activity.campers.get(day)) {

      for (int i = 0; i < activities.size(); i++) {
        Activity current_activity = activities.get(i);
        
        if (camper.choices.get(current_activity.name) <= choice_allowance && camper.choices.get(current_activity.name) > 0 && camper.choice_filled.get(current_activity.name) == false && current_activity.days_open[day] == true && current_activity.campers.get(day).size() < current_activity.capacity) { //camper's choice is valid
          HashMap<String, Integer> potential_reassignment = new HashMap<String, Integer>();
          potential_reassignment.put("Camper", campers.indexOf(camper));
          potential_reassignment.put("Activity", activities.indexOf(current_activity));
          potential_reassignment.put("Score", current_activity.day_demand.get(day));

          Lib.addInOrder(potential_reassignment, potential_reassignments, "Score");

          
        }
      }
    }
    while (activity.campers.get(day).size() > activity.capacity) {
      if (potential_reassignments.size() == 0) {
        reassign(choice_allowance + 1, day, activity); //reassigns campers with higher choice levels
        break;
      }
      
      HashMap<String, Integer> current_reassignment = potential_reassignments.get(0);

      Camper camper = campers.get(current_reassignment.get("Camper"));
      Activity set_activity = activities.get(current_reassignment.get("Activity"));

      activity.campers.get(day).remove(camper);
      camper.choice_filled.put(activity.name, false);
      
      camper.activities.put(day, set_activity);
      set_activity.campers.get(day).add(camper);
      camper.choice_filled.put(set_activity.name, true);

      int camper_index = potential_reassignments.get(0).get("Camper");
      
      int new_activity_index = potential_reassignments.get(0).get("Activity");
      int current_activity_index = activities.indexOf(activity);
      
      for (int i = 0; i < potential_reassignments.size(); i++) {
        if (potential_reassignments.get(i).get("Camper") == camper_index) {
          potential_reassignments.remove(i);
        }
      }

      ArrayList<HashMap<String, Integer>> reevaluated_reassignments = new ArrayList<HashMap<String, Integer>>(); //reassignments that have updated demand and need to be reevaluated

      int index = 0;
      while (index < potential_reassignments.size()) {
        if (potential_reassignments.get(index).get("Activity") == new_activity_index || potential_reassignments.get(index).get("Activity") == current_activity_index) { //reassignment needs to be reevaluated
          reevaluated_reassignments.add(new HashMap<String, Integer>(potential_reassignments.get(index)));
          potential_reassignments.remove(index);
        }
        else {
          index++;
        }
      }

      activity.dayDemands(); //reevaluates day demands due to update in camper placement
      set_activity.dayDemands();

      for (int i = 0; i < reevaluated_reassignments.size(); i++) {
        Lib.addInOrder(reevaluated_reassignments.get(i), potential_reassignments, "Score"); //adds reassignment back into list with new ranking
        potential_reassignments.remove(0);
      }
      
    }
  }

  public void assignActivities () {

    limitedDayAssignments(1);

    unassignableActivities();

    for (int i = 0; i < 3; i++) {
      alternateArrangements();
  
      for (Activity activity : activities) {
        activity.dayDemands();
      }
  
      assignAlternates();
  
      nextChoices();
    }
    
  }

  public static void display(ArrayList<Camper> campers, ArrayList<Activity> activities) {
    System.out.println("Activity List:\n");

    String red_code = "\u001B[31m";
    String blue_code = "\u001B[34m";
    String green_code = "\u001B[32m";
    String reset_code = "\u001B[0m";

    for (Activity activity : activities) {
      System.out.println(green_code + activity.name + reset_code + "\n");

      System.out.print(blue_code);
      System.out.printf("%-29s %-29s %-29s %-29s %n %n", "Monday", "Tuesday", "Wednesday", "Thursday");
      System.out.print(reset_code);

      int max = Lib.maxCampers(activity.campers);

      if (max == 0) {
          max = 1;
        }

      for (int i = 0; i < max; i++) {
        for (int ii = 0; ii < 4; ii++) {
          if (i < activity.campers.get(ii).size()) {
            System.out.printf("%-30s", " " + activity.campers.get(ii).get(i).name);
          }
          else if (i == 0 && activity.days_open[ii] == false) {
            System.out.printf("%-39s", red_code + " Closed" + reset_code);
          }
          else {
            System.out.printf("%-30s", "");
          }
        }
        System.out.println();
      }
      System.out.println();
    }

    System.out.println("\nCamper list:\n");

    ArrayList<String> cabins = new ArrayList<String>(Arrays.asList("Highland House", "Riverwest", "Coaldale Cottage", "Gem Abode", "Lendrum Lodge", "Vaux Hollow", "Linden Hut", "Dalhousie Den", "Crestwood Chalet", "Sunwest"));

    for (String cabin : cabins) {
      System.out.println(green_code + cabin + reset_code + "\n");

      System.out.print(blue_code);

      System.out.printf("%-30s %-20s %-20s %-20s %-20s %n", "Name:", "Monday", "Tuesday", "Wednesday", "Thursday");

      System.out.print(reset_code);

      for (Camper camper : campers) {
        if (camper.cabin.equals(cabin)) {
          System.out.printf("%-30s %-20s %-20s %-20s %-20s %n", camper.name, camper.activities.get(0).name, camper.activities.get(1).name, camper.activities.get(2).name, camper.activities.get(3).name);
        }
      }
      System.out.println();
    }
  }

  public static void saveAssignments (String saveFile, ArrayList<Camper> campers, ArrayList<Activity> activities) {

    try {
      File activity_file = new File("");

      int file_num = 0;

      String file_name = "";

      while (true) { //creates
        file_name = "Saves/" + saveFile + "/" + saveFile + "Activities";
        if (file_num > 0) {
          file_name += file_num;
        }

        file_name += ".csv";

        activity_file = new File(file_name);

        if (activity_file.exists()) {
          file_num++;
        }
        else {
          activity_file.createNewFile();
          break;
        }
      }

      FileWriter writer = new FileWriter(activity_file);

      writer.write("Activity,Monday,Tuesday,Wednesday,Thursday\n");

      for (Activity activity : activities) {

        int max = Lib.maxCampers(activity.campers);

        if (max == 0) {
          max = 1;
        }

        for (int i = 0; i < max; i++) {
          writer.write(activity.name + ",");
          for (int ii = 0; ii < 4; ii++) {
            if (i < activity.campers.get(ii).size()) {
              writer.write(activity.campers.get(ii).get(i).name + ",");
            }
            else {
              writer.write(",");
            }
          }
          writer.write("\n");
        }
      }
      writer.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    

    try {
      File camper_file = new File("");

      int file_num = 0;

      while (true) { //creates
        String file_name = "Saves/" + saveFile + "/" + saveFile + "_Camper_list";
        if (file_num > 0) {
          file_name += file_num;
        }
        file_name += ".csv";

        camper_file = new File(file_name);

        if (camper_file.exists()) {
          file_num++;
        }
        else {
          camper_file.createNewFile();
          break;
        }
      }

      FileWriter writer = new FileWriter(camper_file);

      ArrayList<String> cabins = new ArrayList<String>(Arrays.asList("Highland House", "Riverwest", "Coaldale Cottage", "Gem Abode", "Lendrum Lodge", "Vaux Hollow", "Linden Hut", "Dalhousie Den", "Crestwood Chalet", "Sunwest"));

      writer.write("Name,Cabin,Monday,Tuesday,Wednesday,Thursday\n");

      for (String cabin : cabins) {
        for (Camper camper : campers) {
          if (camper.cabin.equals(cabin)) {
            writer.write(camper.name + "," + camper.cabin + "," + camper.activities.get(0).name + "," + camper.activities.get(1).name + "," + camper.activities.get(2).name + "," + camper.activities.get(3).name + "\n");
          }
        }
      }
      writer.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
