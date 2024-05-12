import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Main {
  public static void main(String[] args) {
    Scanner strInput = new Scanner(System.in);
    Scanner intInput = new Scanner(System.in);

    ArrayList<Camper> campers = new ArrayList<Camper>();
    ArrayList<Activity> activities = new ArrayList<Activity>();

    File save_file = new File("");
    
    String[] activity_list = {"Zip Line", "Trail Ride", "Climbing Wall", "Bazooka Ball", "Canoeing", "Archery", "Willson Ball", "Disc Golf", "Board Games", "Bracelet Making", "Volleyball", "Basketball", "Soccer", "Shower Time", "Quiet Time"};

    for (int i = 0; i < activity_list.length; i++) {
      activities.add(new Activity(activity_list[i]));
    }

    String mode = "manual";
    
    if (false) {
      System.out.println("\n1. Open from file");
      System.out.println("2. Create new\n");
  
      while (true) { //file selection
        System.out.print("Enter choice: ");
        String choice = strInput.nextLine();
    
        if (choice.equals("1")) { //open exiisting file
          System.out.print("Enter file name: ");
          String fileName = strInput.nextLine();
          try {
            File savesFolder = new File("Saves");
    
            save_file = new File(savesFolder, fileName);
  
            Path path = Paths.get("Saves/" + fileName);
  
            if (Files.exists(path)) { //file exists
              System.out.println("File opened successfully");
  
              System.out.println("Loading campers...");
  
              //load campers
  
              for (int i = 0; i < 7; i++) {
                System.out.print(String.format("\033[%dA",1)); // Move up
                System.out.print("\033[2K");
              }
  
              break;
            }
            else { //file does not exist
              save_file.delete();
              System.out.println("File does not exist\n");
            }
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
        else if (choice.equals("2")) { //create new file
          System.out.print("create file name (name may not contain '/'): ");
          String fileName = strInput.nextLine();
          try {
            save_file = new File("Saves/" + fileName);
            if (!save_file.exists()) {
              save_file.mkdir();
              break;
            }
            else {
              System.out.println("File already exists");
            }
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      System.out.print("\033[H\033[2J");
      System.out.flush();
    }

    //entering data

    if (mode.equals("manual")) {
      while (true) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        String action = "add";
        if (campers.size() > 0) {
          System.out.println("1. Add camper");
          System.out.println("2. Edit camper");
          System.out.println("3. Remove camper");
          System.out.println("4. Complete and assign activities");
  
          while (true) {
            System.out.print("\nSelect Option: ");
    
            String choice = strInput.nextLine();
  
            if (choice.equals("1")) {
              action = "add";
              break;
            }
            else if (choice.equals("2")) {
              action = "edit";
              break;
            }
            else if (choice.equals("3")) {
              action = "remove";
              break;
            }
            else if (choice.equals("4")) {
              System.out.print("\nAre you sure you want to complete and assign activities? (y/n): ");
              String confirm = strInput.nextLine();
  
              if (confirm.equals("y")) {
                action = "assign";
                break;
              }
            }
          }
        }
  
        if (action.equals("add")) {
          System.out.print("\033[H\033[2J");
          System.out.print("Camper Name: ");
          String name = strInput.nextLine();
          System.out.print("Camper Cabin: ");
          String cabin = strInput.nextLine();
          Camper camper = new Camper(name, cabin);
  
          while (true) {
            camper.choices = Lib.chooseActivities(name, cabin);
            
            System.out.println("\n1. Continue");
            System.out.println("2. Redo Selections\n");
            System.out.print("Select Option: ");
    
            String choice = strInput.nextLine();
    
            if (!choice.equals("2")) {
              campers.add(camper);
              System.out.println("");
              break;
            }
          }
        }
        if (action.equals("edit")) {
          Camper camper_choice = Lib.selectCamper(campers);
  
          while (true) {
            camper_choice.choices = Lib.chooseActivities(camper_choice.name, camper_choice.cabin);
  
            System.out.println("\n1. Continue");
            System.out.println("2. Redo Selections\n");
            System.out.print("Select Option: ");
  
            String choice = strInput.nextLine();
  
            if (!choice.equals("2")) {
              System.out.println("");
              break;
            }
          }
        }
        else if (action.equals("remove")) {
          Camper camper_choice = Lib.selectCamper(campers);
  
          System.out.print("Do you want to remove " + camper_choice.name + " from the list? (y/n): ");
          String confirm = strInput.nextLine();
  
          if (confirm.equals("y")) {
            campers.remove(camper_choice);
          }
        }
        else if (action.equals("assign")) {
          Assign Activity_assigner = new Assign(campers, activities);
          break;
        }
      }
    }
    else {
      ArrayList<String> camper_names = new ArrayList<String>(Arrays.asList("John", "Mark", "Luke", "Matthew", "Rachel", "Sarah", "Jack", "James", "Kate", "David", "Mary", "Max", "Susie", "Jessica", "Derek", "Lisa", "Amy", "Josh", "Jacob", "Jenny", "Paul", "Noah", "Liam", "Olivia", "Emma", "Oliver", "Ava", "Charlotte", "Elijah", "Sophia", "Aiden", "Isabella", "Mia", "Ethan", "Madison", "Luna", "William", "Abigail", "Harper", "Mason", "Evelyn", "Emily", "Elizabeth", "Avery", "Ella", "Scarlett", "Grace", "Peter", "Susan", "Edmund", "Lucy", "Nora", "Aaron", "Chloe", "Hannah", "Joseph", "Harry", "Ron", "Jane", "Jude", "Jordan", "Katherine", "Michael", "Morgan", "Amelia", "Evan", "Victoria", "Aria", "Anthony", "Eli", "Kayla", "Jeremy", "Kimberly", "Justin", "Samantha", "Dylan", "Aubrey", "Natalie", "Samuel", "Zoe", "Leo", "Mackenzie", "Alexander", "Addison", "Kylie", "Jason", "Ariana", "Elias", "Alice", "Ezra", "Maya", "Asher", "Audrey", "Caleb", "Bella", "Levi", "Aurora", "Jaxon", "Stella", "Nicholas"));

      //ArrayList<String> camper_names = new ArrayList<String>(Arrays.asList("John", "Matthew", "Luke", "Mark", "Rachel", "Sarah", "Jack"));

      //ArrayList<String> camper_names = new ArrayList<String>(Arrays.asList("001", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013", "014", "015", "016", "017", "018", "019", "020", "021", "022", "023", "024", "025", "026", "027", "028", "029", "030", "031", "032", "033", "034", "035", "036", "037", "038", "039", "040", "041", "042", "043", "044", "045", "046", "047", "048", "049", "050", "051", "052", "053", "054", "055", "056", "057", "058", "059", "060", "061", "062", "063", "064", "065", "066", "067", "068", "069", "070", "071", "072", "073", "074", "075", "076", "077", "078", "079", "080", "081", "082", "083", "084", "085", "086", "087", "088", "089", "090", "091", "092", "093", "094", "095", "096", "097", "098", "099", "100"));

      int iterations = 1;

      if (mode.equals("test")) {
        iterations = 500;
      }

      boolean end = false;

      int a = 0;
      while (true) {
        a++;
        System.out.println("Iteration " + a);
        campers = new ArrayList<Camper>();
        activities = new ArrayList<Activity>();

        for (int i = 0; i < activity_list.length; i++) {
          activities.add(new Activity(activity_list[i]));
        }
        
        for (int i = 0; i < camper_names.size(); i++) {
          Camper camper = new Camper(camper_names.get(i), "Cabin");
          ArrayList<Integer> activity_choices = new ArrayList<Integer>();
  
          for (int ii = 0; ii < activities.size(); ii++) {
            for (int iii = 0; iii < activities.get(ii).open_days; iii++) { //activities that are open longer have higher demand
              activity_choices.add(ii);
            }
          }
  
          ArrayList<Integer> chosen_activities = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
  
          for (int ii = 1; ii <= 8; ii++) { //chooses top 8 activities, ii represents choice #
            int activity_choice = activity_choices.get(Lib.randInt(0, activity_choices.size() - 1)); //the index of the activity chosen
            chosen_activities.set(activity_choice, ii);
            int index = 0;
            while (index < activity_choices.size()) { //removes that choice from the list
              if (activity_choices.get(index) == activity_choice) {
                activity_choices.remove(index);
              }
              else {
                index++;
              }
            }
          }
  
          for (int ii = 0; ii < chosen_activities.size(); ii++) {
            camper.setChoice(activity_list[ii], chosen_activities.get(ii)); //sets the camper's choices
          }
    
          campers.add(camper);

          if (mode.equals("auto")) {
            System.out.println(camper.name + ": " + camper.choices + "\n");
          }
        }
  
        Assign Activity_assigner = new Assign(campers, activities);
        
        Lib.setActivities(campers, activities); //set campers' activities

        if (mode.equals("test")) {
          for (Activity activity : activities) {
            for (int i = 0; i < 4; i++) {
              if (activity.campers.get(i).size() > activity.capacity) {
                System.out.println("Test " + a + "   " + activity.name + " day " + (i+1) + "   " + (activity.campers.get(i).size() - activity.capacity) + " over");
                end = true;
              }
            }
          }
        }

        ArrayList<Double> satisfaction_stats = Lib.satisfactionStats(campers);
        System.out.println("Average satisfaction: " + satisfaction_stats.get(0) + "  Minimum satisfaction: " + satisfaction_stats.get(1));
        
        if (mode.equals("auto") || end == true) {
          break;
        }
      }

      System.out.println("Done");

      if (mode.equals("test")) {
        for (Camper camper : campers) {
          System.out.println(camper.name + ": " + camper.choices + "\n");
        }
      }
      
      for (Activity activity : activities) {
        System.out.println("\n\u001B[32m" + activity.name + "\u001B[0m");
        for (int i = 0; i < activity.campers.size(); i++) {
          System.out.print("\u001B[34m" + "day " + (i + 1) + "\u001B[0m " + activity.campers.get(i).size() + " campers ");
          if (activity.campers.get(i).size() > activity.capacity) {
            System.out.println("\u001B[31m" + "FULL - " + (activity.campers.get(i).size() - activity.capacity) + " over\u001B[0m");
          }
          else {
            System.out.println("");
          }
          if (activity.days_open[i] == false) {
            System.out.println("\u001B[31m   Closed  \u001B[0m");
          }
          for (int ii = 0; ii < activity.campers.get(i).size(); ii++) {
            System.out.println("   " + activity.campers.get(i).get(ii).name);
          }
          System.out.println("");
        }
      }

      for (Camper camper : campers) {
        System.out.println("\n" + camper.name);
        for (int i = 0; i < 4; i++) {
          if (camper.activities.get(i) != null) {
            System.out.println("day " + (i + 1) + ": " + camper.activities.get(i).name + "  choice: " + camper.choices.get(camper.activities.get(i).name));
          }
          else {
            System.out.println("day " + (i + 1) + ":\u001B[31m no activity \u001B[0m");
          }
        }
      }
    }
  }
}
