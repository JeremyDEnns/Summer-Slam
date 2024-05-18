import java.util.*;
import java.io.*;

public class Lib {
  public static HashMap<String, Integer> chooseActivities(String name, String cabin) {
    HashMap<String, Integer> choices = new HashMap<String, Integer>();

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

      int choice = 0;

      Scanner strInput = new Scanner(System.in);

      System.out.print(choice_selection + 1 + ". " + activity_list[choice_selection] + "  ");


      try {
        String choice_str = strInput.next();
        choice = Integer.valueOf(choice_str);
      }
      catch (Exception e) {
        System.out.println("Invalid Input");
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

  public static void save(String file_name, ArrayList<Camper> campers, boolean[][] open_days) {
    String[] activity_list = new String[]{"Zip Line", "Trail Ride", "Climbing Wall", "Bazooka Ball", "Canoeing", "Archery", "Willson Ball", "Disc Golf", "Board Games", "Bracelet Making", "Volleyball", "Basketball", "Soccer", "Shower Time", "Quiet Time"};
    
    File camper_folder = new File("Saves/" + file_name + "/Campers");
    
    File[] camper_files = camper_folder.listFiles();

    for (File file : camper_files) {
      file.delete(); //deletes all campers from folder
    }

    try {
      File variable_file = new File("Saves/" + file_name + "/Variables.txt");

      variable_file.createNewFile();

      FileWriter writer = new FileWriter(variable_file);

      writer.write("Open days: ");

      for (int i = 0; i < open_days.length; i++) {
        writer.write(open_days[i][0] + ",");
        writer.write(open_days[i][1] + ",");
        writer.write(open_days[i][2] + ",");
        writer.write(open_days[i][3] + ", ");
      }

      writer.close();

    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    try {
      for (Camper camper : campers) {
        File camper_file = new File ("Saves/" + file_name + "/Campers/C" + campers.indexOf(camper) + ".txt");
        camper_file.createNewFile();

        FileWriter writer = new FileWriter(camper_file);

        writer.write("Name: "+ camper.name + "\n");
        writer.write("Cabin: " + camper.cabin + "\n");

        writer.write("Choices: ");

        for (String activity_name : activity_list) {
          writer.write(activity_name + ":" + camper.choices.get(activity_name) + ", ");
        }

        writer.close();
      }
      
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean[][] loadDays(String file_name) {
    boolean[][] open_days = {{true, true, true, true},{true, true, false, true},{true, true, true, true},{true, true, false, true},{true, true, true, true},{true, true, true, true},{true, false, true, false},{false, true, false, false},{false, false, true, false},{true, false, false, true},{false, false, true, false},{false, false, false, true},{false, true, false, false},{false, true, true, true},{true, false, true, false}};

    try {
      File variable_file = new File("Saves/" + file_name + "/Variables.txt");

      Scanner file_reader = new Scanner(variable_file);

      String days_string = file_reader.nextLine().split(": ")[1];

      String[] activity_days = days_string.split(", ");

      for (int i = 0; i < activity_days.length; i++) {
        String[] activity_day = activity_days[i].split(",");

        for (int ii = 0; ii < activity_day.length; ii++) {
          if (activity_day[ii].equals("true")) {
            open_days[i][ii] = true;
          }
          else if (activity_day[ii].equals("false")) {
            open_days[i][ii] = false;
          }
        }
      }
      file_reader.close();
    }
    catch (Exception e) {
    }
    return open_days;

  }

  public static ArrayList<Camper> load(String file_name) {
    String[] activity_list = new String[]{"Zip Line", "Trail Ride", "Climbing Wall", "Bazooka Ball", "Canoeing", "Archery", "Willson Ball", "Disc Golf", "Board Games", "Bracelet Making", "Volleyball", "Basketball", "Soccer", "Shower Time", "Quiet Time"};

    ArrayList<Camper> campers = new ArrayList<Camper>();
    try {
      File camper_folder = new File ("Saves/" + file_name + "/Campers");

      File[] camper_list = camper_folder.listFiles();

      for (int i = 0; i < camper_list.length; i++) {
        File camper_file = camper_list[i];

        Scanner fileReader = new Scanner(camper_file);

        String name = fileReader.nextLine().split(": ")[1];
        String cabin = fileReader.nextLine().split(": ")[1];

        String choice_values = fileReader.nextLine().split(": ")[1];
        String[] choice_array = choice_values.split(", ");

        HashMap<String, Integer> choices = new HashMap<String, Integer>();

        for (int ii = 0; ii < choice_array.length; ii++) {
          String[] choice_mapping = choice_array[ii].split(":");
          choices.put(choice_mapping[0], Integer.valueOf(choice_mapping[1]));
        }

        Camper new_camper = new Camper(name, cabin);

        for (String activity : activity_list) {
          new_camper.setChoice(activity, choices.get(activity));
        }

        campers.add(new_camper);

      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return campers;
  }

  public static String capitalize (String text) {
    String new_text = "";
    String[] words = text.split(" ");
    for (int i = 0; i < words.length; i++) {
      new_text += words[i].substring(0, 1).toUpperCase();
      new_text += words[i].substring(1).toLowerCase();
      if (i < words.length - 1) {
        new_text += " ";
      }
    }

    return new_text;
  }

  public static int maxCampers (ArrayList<ArrayList<Camper>> list) {
    int max = 0;
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).size() > max) {
        max = list.get(i).size();
      }
    }
    return max;
  }
}
