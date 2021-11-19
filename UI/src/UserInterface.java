import exceptions.*;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private Engine engine;

    public UserInterface() {
        this.engine = new SystemEngine();
    }

    //TODO: check invalid input (like: ss s)
    public void getInputFromUser() {
        boolean exit = false;
        Scanner in = new Scanner(System.in);
        while (!exit) {
            System.out.println("Please choose a number between 1-6:" +
                    "\n1. Read File." +
                    "\n2. Get information about Targets graph." +
                    "\n3. Get information about Target." +
                    "\n4. Find Path between two Targets." +
                    "\n5. Active task." +
                    "\n6. Exit");
            try {
                int choice = in.nextInt();
                switch (choice) {
                    case 1:
                        loadFile();
                        break;
                    case 2:
                        printGraphInfo();
                        break;
                    case 3:
                        printTarget();
                        break;
                    case 4:
                        findPaths();

                        break;
                    case 5:
                        //engine.activateTask();
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice, please try again");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input: " + e.getMessage());
            }
//            catch (NoFileInSystemException e) {
//                e.printStackTrace();
//            } catch (TargetNotExistException e) {
//                e.printStackTrace();
//            } catch (DuplicateTargetsException e) {
//                e.printStackTrace();
//            } catch (InvalidDependencyException e) {
//                e.printStackTrace();
//            } catch (InvalidFileException e) {
//                e.printStackTrace();
//            } catch (DependencyConflictException e) {
//                e.printStackTrace();
//            }


        }
    }

    private void findPaths() {
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
        } catch (NoFileInSystemException e) {
            System.out.println("A file is not loaded to the system yet");
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
        GraphDTO graphDTO = null;
        try {
            graphDTO = engine.getGraphDTO();
            System.out.println("Number of targets:" + graphDTO.getNumOfTargets());
            System.out.println("Number of Leaves:" + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.LEAF) +
                    "Number of Middles:" + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.MIDDLE) +
                    "Number of Roots:" + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.ROOT) +
                    "Number of Independents:" + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.INDEPENDENT));
        } catch (NoFileInSystemException e) {
            System.out.println("There is no file in the system");
        }
    }


    private void printTarget() {
        //TODO: ask Yael
        //GraphDTO graphDTO = engine.getGraphDTO();
        boolean finish = false;
        while (!finish) {
            System.out.println("Please enter the target's name:(to return to main menu, type '0')");
            Scanner in = new Scanner(System.in);
            String targetName = in.next();
            if (targetName.equals("0")) {
                finish = true;
            } else {
                try {
                    TargetDTO target = engine.getTarget(targetName);
                    printTargetDetails(target);
                } catch (TargetNotExistException e) {
                    System.out.println("Target with name: " + e.getName() + "does not exist");
                } catch (NoFileInSystemException e) {
                    System.out.println("There is no file in the system");
                }
            }
        }
    }

    private void printTargetDetails(TargetDTO target) {
        System.out.println("Target's name:" + target.getName());
        System.out.println("Target's place" + target.getPlace());
        if (!target.getDependsOn().isEmpty()) {
            System.out.println("dependsOn: ");
            for (TargetDTO targetDTO : target.getDependsOn()) {
                System.out.println("        " + targetDTO.getName());
            }
        } else {
            System.out.println("There is no targets this target is depends on");
        }
        if (!target.getRequiredFor().isEmpty()) {
            System.out.println("requiredFor: ");
            for (TargetDTO targetDTO : target.getRequiredFor()) {
                System.out.println("        " + targetDTO.getName());
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

}
