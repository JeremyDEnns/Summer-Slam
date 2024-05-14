import java.util.*;
import java.io.*;

public class Lib {
  public static HashMap<String, Integer> chooseActivities(String name, String cabin) {
    HashMap<String, Integer> choices = new HashMap<String, Integer>();

    Scanner intInput = new Scanner(System.in);
    
    boolean reset = true;

    ArrayList<String> choice_options = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"));
    String[] activity_list = {"Zip Line", "Trail Ride", "Climbing Wall", "Bazooka Ball", "Canoeing", "Archery", "Willson Ball", "Disc Golf", "Board Games", "Bracelet Making", "Volleyball", "Basketball", "Soccer", "Shower Time", "Quiet Time"};

    int choice_selection = 0;

    while (choice_selection < activity_list.length) {
      if (reset) {
        reset = false;
        System.out.print("\033[H\033[2J");
        System.out.println("Camper Name: " + name);
        System.out.println("Camper Cabin: " + cabin);
        System.out.println("\nEnter activity choice (0 for not chosen):");

        for (int i = 0; i < choice_selection; i++) {
          System.out.println(i + 1 + ". " + activity_list[i] + "  " + choices.get((activity_list[i])));
        }
      }
      System.out.print(choice_selection + 1 + ". " + activity_list[choice_selection] + "  ");

      int choice = 0;

      try {
        choice = intInput.nextInt();
      }
      catch (InputMismatchException e) {
        System.out.println("Invalid Input");
        continue;
      }
      catch (Exception e) {
        e.printStackTrace();
        continue;
      }

      if (choice >= 0 && choice <= 8) {
        if (choice_options.contains(Integer.toString(choice))) {
          choice_options.remove(Integer.toString(choice));
          choices.put(activity_list[choice_selection], choice);
          choice_selection++;
        }
        else if (choice == 0) {
          choices.put(activity_list[choice_selection], choice);
          choice_selection++;
        }
        else {
          System.out.println("Invalid Choice");
          reset = true;
        }
      }
      else {
        System.out.println("Invalid Choice");
        reset = true;
      }
    }
    return choices;
  } 
  public static Camper selectCamper(ArrayList<Camper> campers) {
    Scanner intInput = new Scanner(System.in);
    
    System.out.print("\033[H\033[2J");
    for (int i = 0; i < campers.size(); i++) {
      System.out.println(i + 1 + ". " + campers.get(i).name);
    }
    Camper camper_choice = new Camper("", "");
    while (true) {
      System.out.print("\nSelect Camper: ");
      try {
        int choice = intInput.nextInt();

        if (choice > 0 && choice <= campers.size()) {
          camper_choice = campers.get(choice - 1);
          break;
        }
        else {
          System.out.println("Invalid Choice\n");
          continue;
        }
      }
      catch (InputMismatchException e) {
        System.out.println("Invalid Input\n");
        continue;
      }
      catch (Exception e) {
        e.printStackTrace();
        System.out.println("");
        continue;
      }
    }
    return camper_choice;
  }
  
  public static ArrayList<Integer> addCollumns(ArrayList<HashMap<String, Integer>> array, String[] keys) {
    ArrayList<Integer> column_sums = new ArrayList<Integer>();
    for (int i = 0; i < array.size(); i++) {
      int collumn_sum = 0;
      for (String ii : keys) {
        collumn_sum += array.get(i).get(ii);
      }
      column_sums.add(collumn_sum);
    }
    return column_sums;
  }

  public static ArrayList<Integer> rankValues(ArrayList<Double> list) {
    HashMap<Double, Integer> placements = new HashMap<Double, Integer>();

    for (int i = 0; i < list.size(); i++) {
      placements.put(list.get(i), i);
    }

    Collections.sort(list);

    ArrayList<Integer> day_ranking = new ArrayList<Integer>();
    for (Double i : list) {
      if (i < 100) {
        day_ranking.add(placements.get(i));
      }
    }

    return day_ranking;
  }

  public static void setActivities(ArrayList<Camper> campers, ArrayList<Activity> activities) {
    for (Camper camper : campers) {
      for (Activity activity : activities) {
        for (int i = 0; i < 4; i++) { //loops through days
          if (activity.campers.get(i).contains(camper)) { //camper is assigned to activity on that day
            camper.setActivity(activity, i);
          }
        }
      }
    }
  }

  public static int randInt(int min, int max) {
    return (int) (Math.random() * (max - min + 1) + min);
  }

  public static ArrayList<HashMap<String, Integer>> addInOrder(HashMap<String, Integer> map, ArrayList<HashMap<String, Integer>> list, String value_key) {
    int value = map.get(value_key); //the value to sort by
    map.put("value", value);

    if (list.size() == 0) {
      list.add(map);
    }

    int index = 0;

    for (int ii = 0; ii < list.size(); ii++) {
      if (list.get(ii).get("value") <= value) {
        index = ii+1;
      }
    }

    if (index == list.size()) {
      list.add(map);
    }
    else {
      list.add(index, map);
    }
    return list;
  }

  public static double round(double num, int places) {
    num *= Math.pow(10, places);
    num = Math.round(num);
    num /= Math.pow(10, places);
    return num;
  }

  public static ArrayList<Double> setPersonalDemand(HashMap<String, Integer> choices, ArrayList<String> excluded_activities) {

    String[] activity_list = new String[] {"Zip Line", "Trail Ride", "Climbing Wall", "Bazooka Ball", "Canoeing", "Archery", "Willson Ball", "Disc Golf", "Board Games", "Bracelet Making", "Volleyball", "Basketball", "Soccer", "Shower Time", "Quiet Time"};

    boolean[][] set_days = {{true, true, true, true},{true, true, false, true},{true, true, true, true},{true, true, false, true},{true, true, true, true},{true, true, true, true},{true, false, true, false},{false, true, false, false},{false, false, true, false},{true, false, false, true},{false, false, true, false},{false, false, false, true},{false, true, false, false},{false, true, true, true},{true, false, true, false}};

    ArrayList<HashMap<String, Integer>> choice_map = new ArrayList<HashMap<String, Integer>>();
    
    for (int i = 0; i < 4; i++) {
      choice_map.add(new HashMap<String, Integer>());
      for (int ii = 0; ii < activity_list.length; ii++) {
        int mapping = 0;
        if (choices.get(activity_list[ii]) != 0 && set_days[ii][i] == true && !excluded_activities.contains(activity_list[ii])) {
          mapping = 9 - choices.get(activity_list[ii]);
        }
        choice_map.get(i).put(activity_list[ii], mapping);
      }
    }
    
    ArrayList<Integer> int_personal_demand = addCollumns(choice_map, activity_list);

    ArrayList<Double> personal_demand = new ArrayList<Double>();

    for (int i = 0; i < int_personal_demand.size(); i++) {
      personal_demand.add(Double.valueOf(int_personal_demand.get(i)));
    }

    return personal_demand;
  }

  public static ArrayList<Double> satisfactionStats (ArrayList<Camper> campers) {
    Double satisfaction_score_total = 0.0;
    Double min_satisfaction_score = 100.0;
    
    for (Camper camper : campers) {
      int sum = 0;
      for (int i = 0; i < 4; i++) {
        sum += camper.choices.get(camper.activities.get(i).name); //adds up the choices for each activity (10 = best, 26 = worst)
      }

      Double satisfaction_score = Double.valueOf(1625/sum - 62.5); //10 = 100, 26 = 0

      satisfaction_score_total += satisfaction_score;
      if (satisfaction_score < min_satisfaction_score) {
        min_satisfaction_score = satisfaction_score;
      }
    }

    Double satisfaction_score_avg = satisfaction_score_total/campers.size();
    
    ArrayList<Double> satisfaction_scores = new ArrayList<Double>();
    satisfaction_scores.add(satisfaction_score_avg);
    satisfaction_scores.add(min_satisfaction_score);

    return satisfaction_scores;
  }

  public static void save(String file_name, ArrayList<Camper> campers, ArrayList<Activity> activities) {
    File save_file = new File ("Saves/" + file_name);

    FileWriter writer = new FileWriter(save_file);

    writer.write("text");

    writer.close();
  }
}
