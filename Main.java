import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Main {
  public boolean[][] open_days;
  public static void main(String[] args) {
    Scanner strInput = new Scanner(System.in);
    Scanner intInput = new Scanner(System.in);

    ArrayList<Camper> campers = new ArrayList<Camper>();
    ArrayList<Activity> activities = new ArrayList<Activity>();

    boolean[][] open_days = {{true, true, true, true},  //zipline
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

    File save_file = new File("");

    try {
      File save_folder = new File("Saves");

      if (!save_folder.exists()) {
        save_folder.mkdir();
      }

      File import_folder = new File("Imports");

      if (!import_folder.exists()) {
        import_folder.mkdir();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    String[] activity_list = {"Zip Line", "Trail Ride", "Climbing Wall", "Bazooka Ball", "Canoeing", "Archery", "Willson Ball", "Disc Golf", "Board Games", "Bracelet Making", "Volleyball", "Basketball", "Soccer", "Shower Time", "Quiet Time"};

    String mode = "manual";

    String fileName = "file";
    
    if (true) {
      System.out.println("\n1. Open from file");
      System.out.println("2. Create new");
      System.out.println("3. Test Assignment\n");
  
      while (true) { //file selection
        System.out.print("Enter choice: ");
        String choice = strInput.nextLine();
    
        if (choice.equals("1")) { //open existing file
          System.out.print("Enter file name: ");
          fileName = strInput.nextLine();
          try {
            File savesFolder = new File("Saves");
    
            save_file = new File(savesFolder, fileName);
  
            Path path = Paths.get("Saves/" + fileName);
  
            if (Files.exists(path)) { //file exists
              System.out.println("File opened successfully");
  
              System.out.println("Loading campers...");
  
              campers = Lib.load(fileName);
              open_days = Lib.loadDays(fileName);
  
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
          fileName = strInput.nextLine();
          try {
            save_file = new File("Saves/" + fileName);
            if (!save_file.exists()) {
              save_file.mkdir();
              File camper_folder = new File("Saves/" + fileName + "/Campers");
              camper_folder.mkdir();
              File activity_folder = new File("Saves/" + fileName + "/Activities");
              activity_folder.mkdir();

              Lib.save(fileName, campers, open_days);
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
        else if (choice.equals("3")) {
          mode = "auto";
          break;
        }
        else {
          System.out.println("Invalid Input\n");
        }

      }
      //System.out.print("\033[H\033[2J");
      System.out.flush();
    }

    for (int i = 0; i < activity_list.length; i++) {
      activities.add(new Activity(activity_list[i], open_days));
    }

    //entering data

    if (mode.equals("manual")) {
      while (true) {
        //System.out.print("\033[H\033[2J");
        System.out.flush();
        String action = "add";
        System.out.println("1. Add camper");
        System.out.println("2. Edit camper");
        System.out.println("3. Remove camper");
        System.out.println("4. Complete and assign activities");
        System.out.println("5. Import Campers");
        System.out.println("6. Settings");
        

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
          else if (choice.equals("5")) {
            action = "import";
            break;
          }
          else if (choice.equals("6")) {
            action = "settings";
            break;
          }
        }
  
        if (action.equals("add")) {
          ArrayList<String> cabin_names = new ArrayList<String>(Arrays.asList("Highland House", "Riverwest", "Coaldale Cottage", "Gem Abode", "Lendrum Lodge", "Vaux Hollow", "Linden Hut", "Dalhousie Den", "Crestwood Chalet", "Sunwest"));
          ArrayList<String> short_cabin_names = new ArrayList<String>(Arrays.asList("Highland", "River West", "Coaldale", "Gem", "Lendrum", "Vaux", "Linden", "Dalhousie", "Crestwood", "Sun West"));
          System.out.print("\033[H\033[2J");
          System.out.print("Camper Name: ");
          String name = strInput.nextLine();

          String cabin = "";

          while (true) {
            System.out.print("Camper Cabin: ");
            cabin = strInput.nextLine();

            cabin = Lib.capitalize(cabin);

            if (cabin_names.contains(cabin)) {
              break;
            }
            else if (short_cabin_names.contains(cabin)) {
              cabin = cabin_names.get(short_cabin_names.indexOf(cabin));
              break;
            }
            else {
              System.out.println("\nCabin does not exist\n");
            }
            
          }
          Camper camper = new Camper(name, cabin);
  
          while (true) {
            camper.choices = Lib.chooseActivities(name, cabin, new ArrayList<Integer>());
            
            System.out.println("\n1. Continue");
            System.out.println("2. Redo Selections\n");
            System.out.print("Select Option: ");
    
            String choice = strInput.nextLine();
    
            if (!choice.equals("2")) {
              campers.add(camper);
              Lib.save(fileName, campers, open_days);
              System.out.println("");
              break;
            }
          }
        }
        if (action.equals("edit")) {
          Camper camper_choice = Lib.selectCamper(campers);
  
          while (true) {
            ArrayList<Integer> previous_choices = new ArrayList<Integer>();
            for (String i : activity_list) {
              previous_choices.add(camper_choice.choices.get(i));
            }

            camper_choice.choices = Lib.chooseActivities(camper_choice.name, camper_choice.cabin, previous_choices);
  
            System.out.println("\n1. Continue");
            System.out.println("2. Redo Selections\n");
            System.out.print("Select Option: ");
  
            String choice = strInput.nextLine();
  
            if (!choice.equals("2")) {
              System.out.println("");
              break;
            }
          }
          Lib.save(fileName, campers, open_days);
        }
        else if (action.equals("remove")) {
          Camper camper_choice = Lib.selectCamper(campers);
  
          System.out.print("Do you want to remove " + camper_choice.name + " from the list? (y/n): ");
          String confirm = strInput.nextLine();
  
          if (confirm.equals("y")) {
            campers.remove(camper_choice);
          }
          Lib.save(fileName, campers, open_days);
        }
        else if (action.equals("assign")) {
          new Assign(campers, activities);
          Assign.display(campers, activities);
          Assign.saveAssignments(fileName, campers, activities);
          break;
        }
        else if (action.equals("import")) {

          System.out.print("File name: ");

          String file_name = strInput.nextLine();

          if (file_name.length() > 4) {
            if (!file_name.substring(file_name.length()-4).equals(".csv")) {
              file_name += ".csv";
            }
          }
          else {
            file_name += ".csv";
          }
          
          try {
            File import_file = new File("Imports/" + file_name);

            if (import_file.exists()) {
              Scanner fileReader = new Scanner(import_file);

              while (fileReader.hasNextLine()) {
                String camper_line = fileReader.nextLine();

                String[] camper_data = camper_line.split(",");

                if (!Lib.capitalize(camper_data[0]).equals("Name")) {
                  String cabin = camper_data[1];
                  Lib.correctSpelling(cabin, 0, camper_data[0]);

                  Camper new_camper = new Camper(camper_data[0], cabin);

                  for (String i : activity_list) {
                    new_camper.choices.put(i, 0);
                  }

                  for (int i = 2; i <= 9; i++) {
                    String activity = "";
                    if (camper_data.length > i) {
                      activity = Lib.correctSpelling(camper_data[i], i-1, new_camper.name);
                    }
                    else {
                      activity = Lib.correctSpelling(" ", i-1, new_camper.name);
                    }

                    new_camper.setChoice(activity, i-1);
                  }
                  campers.add(new_camper);
                }
                Lib.save(fileName, campers, open_days);
              }
              fileReader.close();
            }
            else {
              System.out.println("File does not exist\n");
            }

          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
        else if (action.equals("settings")) {
          int choice = 0;

          String green_code = "\u001B[32m";
          String red_code = "\u001B[31m";
          String reset_code = "\u001B[0m";

          while (true) {
            System.out.print("\033[H\033[2J");

            for (int i = 0; i < activities.size(); i++) {
              System.out.print((i + 1) + ". " + activities.get(i).name + "   ");
              for (int ii = 0; ii < 4; ii++) {
                if (activities.get(i).days_open[ii]) {
                  System.out.print(green_code + "open  ");
                }
                else {
                  System.out.print(red_code + "closed  ");
                }

                System.out.print(reset_code);
              }
              System.out.println();
            }
            System.out.println();

            System.out.print("Select day to adjust: ");

            choice = 0;

            try {
              choice = intInput.nextInt() - 1;
              break;
            }
            catch (Exception e) {
              continue;
            }
          }

          Activity selected_activity = activities.get(choice);

          System.out.println("Select open/closed\n");

          for (int i = 0; i < 4; i++) {
            while (true) {
              System.out.print("Day " + (i + 1) + ": ");

              String open_choice = strInput.nextLine();

              if (open_choice.equals("open")) {
                selected_activity.days_open[i] = true;
                break;
              }
              else if (open_choice.equals("closed")) {
                selected_activity.days_open[i] = false;
                break;
              }
              else {
                System.out.println("Invalid Input\n");
              }
            }
          }
          Lib.save(fileName, campers, open_days);
        }
      }
    }
    else {
      ArrayList<String> camper_names = new ArrayList<String>(Arrays.asList("Adam", "Benjamin", "Caleb", "Daniel", "Ethan", "Finn", "Gabriel", "Henry", "Isaac", "Jacob", "Liam", "Mason", "Noah", "Oliver", "Peter", "Quinn", "Ryan", "Samuel", "Theodore", "Uriel", "Vincent", "William", "Xavier", "Yusuf", "Zachary", "Andrew", "Bryce", "Christopher", "David", "Elijah", "Felix", "George", "Harrison", "Ian", "Joseph", "Kevin", "Lucas", "Matthew", "Nathan", "Oscar", "Patrick", "Raymond", "Simon", "Thomas", "Victor", "Wyatt", "Xander", "Yosef", "Zane", "Marcus", "Abigail", "Bella", "Charlotte", "Daisy", "Emma", "Faith", "Grace", "Hannah", "Isabella", "Julia", "Kate", "Lily", "Mia", "Nora", "Olivia", "Penelope", "Quinn", "Rachel", "Sophia", "Taylor", "Amy", "Violet", "Willow", "Mindy", "Yara", "Zara", "Amelia", "Brooklyn", "Chloe", "Diana", "Eleanor", "Fiona", "Georgia", "Hazel", "Ivy", "Jasmine", "Katherine", "Luna", "Madison", "Natalie", "Ophelia", "Poppy", "Riley", "Samantha", "Tessa", "Grace", "Victoria", "Wendy", "Raya", "Zoe"));

      //ArrayList<String> camper_names = new ArrayList<String>(Arrays.asList("John", "Matthew", "Luke", "Mark", "Rachel", "Sarah", "Jack"));

      //ArrayList<String> camper_names = new ArrayList<String>(Arrays.asList("001", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013", "014", "015", "016", "017", "018", "019", "020", "021", "022", "023", "024", "025", "026", "027", "028", "029", "030", "031", "032", "033", "034", "035", "036", "037", "038", "039", "040", "041", "042", "043", "044", "045", "046", "047", "048", "049", "050", "051", "052", "053", "054", "055", "056", "057", "058", "059", "060", "061", "062", "063", "064", "065", "066", "067", "068", "069", "070", "071", "072", "073", "074", "075", "076", "077", "078", "079", "080", "081", "082", "083", "084", "085", "086", "087", "088", "089", "090", "091", "092", "093", "094", "095", "096", "097", "098", "099", "100"));

      ArrayList<String> cabins = new ArrayList<String>(Arrays.asList("Highland House", "Riverwest", "Coaldale Cottage", "Gem Abode", "Lendrum Lodge", "Vaux Hollow", "Linden Hut", "Dalhousie Den", "Crestwood Chalet", "Sunwest"));

      boolean end = false;

      int a = 0;
      while (true) {
        a++;
        if (mode.equals("test")) {
          System.out.println("Iteration " + a);
        }
        campers = new ArrayList<Camper>();
        activities = new ArrayList<Activity>();

        for (int i = 0; i < activity_list.length; i++) {
          activities.add(new Activity(activity_list[i], open_days));
        }
        
        for (int i = 0; i < camper_names.size(); i++) {
          Camper camper = new Camper(camper_names.get(i), cabins.get(i/10));
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
        }
  
        new Assign(campers, activities);
        
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
        
        if (mode.equals("auto") || end == true) {
          break;
        }
      }

      if (mode.equals("test")) {
        for (Camper camper : campers) {
          System.out.println(camper.name + ": " + camper.choices + "\n");
        }
      }

      Assign.display(campers, activities);


    }
  }
}
