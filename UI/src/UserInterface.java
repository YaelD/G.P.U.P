import exceptions.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

public class UserInterface {


    private Engine engine;

    private final String PRINT_DELIMETER = "------------------------------------------------------------\n";

    public UserInterface() {
        this.engine = new SystemEngine();
    }


    public void getInputFromUser() {
        boolean exit = false;
        Scanner in = new Scanner(System.in);
        while (!exit) {
            System.out.println(PRINT_DELIMETER);
            System.out.println("Please choose a number between 1-6:" +
                    "\n1. Read File." +
                    "\n2. Get information about Targets graph." +
                    "\n3. Get information about Target." +
                    "\n4. Find Path between two Targets." +
                    "\n5. Active task." +
                    "\n6. Exit");
            try {
                System.out.print("Please enter your choice: ");
                int choice = in.nextInt();
                System.out.println(PRINT_DELIMETER);
                switch (choice) {
                    case 1:
                        this.loadFile();
                        break;
                    case 2:
                        this.printGraphInfo();
                        break;
                    case 3:
                        this.printTarget();
                        break;
                    case 4:
                        this.findPaths();
                        break;
                    case 5:
                        this.activeTask();
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice, please try again");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input: " + in.nextLine());
            }
        }
    }

    private void findPaths() {
        if(!engine.isFileLoaded())
        {
            System.out.println("A file was not loaded to the system yet");
            return;
        }
        System.out.println(PRINT_DELIMETER);
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter first Target's name:");
        String firstTargetName = in.next();
        System.out.println("Please enter second Target's name:");
        String secondTargetName = in.next();
        System.out.println("Please enter the relation between the targets:");
        String relation = in.next();
        Collection<List<String>> paths = null;
        try {
            paths = engine.getPaths(firstTargetName, secondTargetName, relation);
            if (paths.isEmpty()) {
                System.out.println("There are not paths from target " + firstTargetName + " to target " + secondTargetName);
            } else {
                printPaths(paths);
            }
        } catch (TargetNotExistException e) {
            System.out.println("The target" + e.getName() + " does not exist");
        } catch (InvalidDependencyException e) {
            System.out.println("The dependency: " + e.getDependency() + " is not supported in the system");
        }
    }

    private void printPaths(Collection<List<String>> paths) {
        for (List<String> path : paths) {
            String currPath = "Path: ";
            for (int i = 0; i < path.size() - 1; ++i) {
                currPath += (path.get(i) + "->");
            }
            currPath += path.get(path.size() - 1);
            System.out.println(currPath);
        }
    }

    private void loadFile() {
        System.out.println(PRINT_DELIMETER);
        boolean finish = false;
        while (!finish) {
            System.out.println("Please enter the full path of file:(for return please press '0')");
            Scanner in = new Scanner(System.in);
            String path = in.next();
            if (!path.equals("0")) {
                try {
                    engine.readFile(path);
                    System.out.println("File loaded successfully!");
                    finish = true;
                } catch (InvalidFileException e) {
                    System.out.println("The file in: " + e.getPath() + "is invalid because" + e.getReason());
                } catch (DependencyConflictException e) {
                    System.out.println("There is a dependency conflict between Targets:"
                            + e.getFirstTarget() + "," + e.getSecondTarget() + "dependency:" + e.getDependencyType());
                } catch (DuplicateTargetsException e) {
                    System.out.println("The target " + e.getTargetName() + "appears several times in the graph");
                } catch (InvalidDependencyException e) {
                    System.out.println("The dependency: " + e.getDependency() + " is not supported in the system");
                } catch (TargetNotExistException e) {
                    System.out.println("The target" + e.getName() + " does not exist");
                }
            } else {
                finish = true;
            }
        }
    }


    private void printGraphInfo() {
        if(!engine.isFileLoaded())
        {
            System.out.println("A file was not loaded to the system yet");
            return;
        }
        System.out.println(PRINT_DELIMETER);
        GraphDTO graphDTO = null;
        graphDTO = engine.getGraphDTO();
        System.out.println("The name of the Graph: " + graphDTO.getName());
        System.out.println("Number of targets: " + graphDTO.getNumOfTargets());
        System.out.println("Number of Leaves: " + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.LEAF) +
                "\nNumber of Middles: " + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.MIDDLE) +
                "\nNumber of Roots: " + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.ROOT) +
                "\nNumber of Independents: " + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.INDEPENDENT));


    }


    private void printTarget() {
        if(!engine.isFileLoaded())
        {
            System.out.println("A file was not loaded to the system yet");
            return;
        }
        System.out.println(PRINT_DELIMETER);
        boolean finish = false;
        while (!finish) {
            System.out.println("Please enter the target's name:(to return to main menu, type '0')");
            Scanner in = new Scanner(System.in);
            String targetName = in.next();
            if (targetName.equals("0")) {
                finish = true;
            }
            else {
                try {
                    TargetDTO target = engine.getTarget(targetName);
                    printTargetDetails(target);
                    finish = true;
                } catch (TargetNotExistException e) {
                    System.out.println("Target with name: " + e.getName() + "does not exist");
                }
            }
        }
    }

    private void printTargetDetails(TargetDTO target) {
        System.out.println(PRINT_DELIMETER);
        System.out.println("Target's name: " + target.getName());
        System.out.println("Target's place: " + target.getPlace());
        if (!target.getDependsOn().isEmpty()) {
            System.out.println("dependsOn: ");
            for (String targetName : target.getDependsOn()) {
                System.out.println("        " + targetName);
            }
        } else {
            System.out.println("There is no targets this target is depends on");
        }
        if (!target.getRequiredFor().isEmpty()) {
            System.out.println("requiredFor: ");
            for (String targetName : target.getRequiredFor()) {
                System.out.println("        " + targetName);
            }
        } else {
            System.out.println("There is no targets this target is required for");
        }
        if (target.getInfo()!= null) {
            System.out.println("Target-info: " + target.getInfo());
        } else {
            System.out.println("There is not Target-info in this target");
        }

    }


    private void activeTask(){
        if(!engine.isFileLoaded())
        {
            System.out.println("A file was not loaded to the system yet");
            return;
        }
        System.out.println(PRINT_DELIMETER);
        System.out.println("Please enter the type of the task: ");
        Scanner in = new Scanner(System.in);
        int taskType = in.nextInt();
        if(taskType == 1){
            SimulationTaskParamsDTO simulationTaskParams = getParamsOfSimulationTask();
            if(simulationTaskParams == null){
                System.out.println("Returning to main menu");
                return;
            }
            Consumer<TargetDTO> printStrConsumer = targetDTO -> {
                System.out.println("Process result: " + targetDTO.getRunResult().getStatus());
                if(targetDTO.getInfo() != null){
                    System.out.println("Target info:" + targetDTO.getInfo() + "\n");
                }
                if(!targetDTO.getRunResult().equals(RunResults.SKIPPED)){
                    System.out.println("Process Start time:" + targetDTO.getStartingTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
                    System.out.println("Process End time:" + targetDTO.getEndingTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
                    System.out.println("The dependent Targets that were opened:\n" + targetDTO.getTargetsThatCanBeRun());
                    if(targetDTO.getRunResult().equals(RunResults.FAILURE)){
                        System.out.println("The targets that won't be able to process are: \n" + targetDTO.getSkippedFathers());
                    }
                }
            };
            GraphDTO graphDTO = engine.activateTask(printStrConsumer, simulationTaskParams, TaskType.SIMULATION_TASK);
        }


    }

    private SimulationTaskParamsDTO getParamsOfSimulationTask() {
        boolean isRandom = false;
        int processTime = 0;
        double successRate = 0,successWithWarnings = 0;
        System.out.println("Please enter the next Simulation task parameters: ");
        processTime = getInputInt("Please enter the process time in MilliSeconds: (whole positive number)",
                "Invalid input, The input should be a positive number",
                "Invalid input, The input should be a whole positive number", 1,Integer.MAX_VALUE);
        if(processTime == -1){
            return null;
        }
        int random = getInputInt("Do you want the process time to be randomized? 1-Yes, 0-No",
                "Invalid Input, you should enter 1 for yes or 0 for No",
                "Invalid input, The input should be a number", 0,1);
        if(random == 1){
            isRandom = true;
        }
        else if(random == 0){
            isRandom = false;
        }
        else{
            return null;
        }
        successRate = this.getInputDouble("\"Please enter process's success rate:(a positive number between 0 to 1)",
                "Invalid input, The input should be between 0 to 1","Invalid input, The input should be a number",
                0,1);
        if(successRate == -1){
            return null;
        }
        successWithWarnings = getInputDouble("Please enter process's success with warnings rate:(a positive number between 0 to 1)",
                "Invalid input, The input should be a positive number between 0 to 1","Invalid input, The input should be a number between 0 to 1",
                0, 1);
        if(successWithWarnings == -1){
            return null;
        }

        SimulationTaskParamsDTO simulationTaskDTO = new SimulationTaskParamsDTO(processTime, isRandom, successRate, successWithWarnings);
        return simulationTaskDTO;
    }

    private double getInputDouble(String inputMessage, String invalidInputMessage, String exceptionMessage, double minVal, double maxVal){
        double res = 0;
        boolean finish = false;
        System.out.println(inputMessage);
        System.out.println("To return please press '-'.");
        Scanner in = new Scanner(System.in);
        while(!finish){
            String ans = in.next();
            if(ans.equals("-")){
                finish = true;
                res = -1;
            }
            else{
                try {
                        res = Double.parseDouble(ans);
                        finish = true;
                        if (res < minVal || res > maxVal) {
                            System.out.println(invalidInputMessage);
                            finish = false;
                        }
                    }catch (NumberFormatException e) {
                        System.out.println(exceptionMessage);
                    }
            }
        }
        return res;
    }


    private int getInputInt(String inputMessage, String invalidInputMessage, String exceptionMessage, double minVal, double maxVal){
        int res = 0;
        boolean finish = false;
        System.out.println(inputMessage);
        System.out.println("To return please press '-'.");
        Scanner in = new Scanner(System.in);
        while(!finish){
            String ans = in.next();
            if(ans.equals("-")){
                finish = true;
                res = -1;
            }
            else{
                try {
                    res = Integer.parseInt(ans);
                    finish = true;
                    if (res < minVal || res > maxVal) {
                        System.out.println(invalidInputMessage);
                        finish = false;
                    }
                }catch (NumberFormatException e) {
                    System.out.println(exceptionMessage);
                }
            }
        }
        return res;
    }
}
