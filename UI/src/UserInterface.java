import exceptions.*;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
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
            engine.activateTask(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    System.out.println(s);
                }
            }, simulationTaskParams, TaskType.SIMULATION_TASK);
        }


    }

    private SimulationTaskParamsDTO getParamsOfSimulationTask() {
        System.out.println("Please enter the params...");
        Scanner in = new Scanner(System.in);
        int processTime = in.nextInt();
        boolean isRandom = in.nextBoolean();
        double successRate = in.nextDouble();
        double successWithWarnings = in.nextDouble();
        SimulationTaskParamsDTO simulationTaskDTO = new SimulationTaskParamsDTO(processTime, isRandom, successRate, successWithWarnings);
        return simulationTaskDTO;
    }

}
