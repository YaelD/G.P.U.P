import engine.Engine;
import task.TaskType;

public class UserInterface {


    private Engine engine;

    private final String PRINT_LINE = "------------------------------------------------------------\n";
//
////    public UserInterface() {
////        this.engine = new SystemEngine();
////    }
//
//
//    private void printMenu(){
//        System.out.println("Please choose a number between 1-6:" +
//                "\n1. Read File." +
//                "\n2. Get information about Targets graph." +
//                "\n3. Get information about target." +
//                "\n4. Find Path between two Targets." +
//                "\n5. Active task." +
//                "\n6. Check for cycle." +
//                "\n7. Exit" +
//                "\n8. What if");
//    }
//
//
//    public void RunUI() {
//        boolean exit = false;
//        Scanner in = new Scanner(System.in);
//        while (!exit) {
//            System.out.println(PRINT_LINE);
//            printMenu();
//            try {
//                System.out.print("Please enter your choice: ");
//                int choice = in.nextInt();
//                System.out.println(PRINT_LINE);
//                switch (choice) {
//                    case 1:
//                        this.loadFileToSystem();
//                        break;
//                    case 2:
//                        this.printGraphInfo();
//                        break;
//                    case 3:
//                        this.getTargetInfo();
//                        break;
//                    case 4:
//                        this.findPathsBetweenTargets();
//                        break;
//                    case 5:
//                        this.activeSystemTask();
//                        break;
//                    case 6:
//                        this.checkForCycleInGraph();
//                        break;
//                    case 7:
//                        exit = true;
//                        break;
//                    case 8:
//                        this.whatIf();
//                        break;
//                    default:
//                        System.out.println("Invalid choice, please try again");
//                        break;
//                }
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid Input: " + in.nextLine());
//            }
//        }
//    }
//
//    private void whatIf() {
//        //System.out.println(engine.whatIf("J", Dependency.REQUIRED_FOR));
//
//
//    }
//
//    private void checkForCycleInGraph() {
//        if(!engine.isFileLoaded()){
//            System.out.println("A file was not loaded to the system yet");
//            return;
//        }
//        Scanner in = new Scanner(System.in);
//        System.out.println("Please enter the Name of the target to search for cycle: ");
//        String targetName = in.next();
//
//        try {
//            List<String> cycle = null;
//            //List<String> cycle = engine.findCycle(targetName);
//            if(cycle == null){
//                System.out.println("There is no cycle including " + targetName);
//            }
//            else{
//                System.out.println("Found cycle: ");
//                System.out.println(printAPath(cycle));
//            }
//        } catch (TargetNotExistException e) {
//            System.out.println("The target is not exist in the graph");
//        }
//    }
//
//    private void findPathsBetweenTargets() {
//        if(!engine.isFileLoaded())
//        {
//            System.out.println("A file was not loaded to the system yet");
//            return;
//        }
//        Dependency dependency = Dependency.REQUIRED_FOR;
//        System.out.println(PRINT_LINE);
//        Scanner in = new Scanner(System.in);
//        System.out.println("Please enter first Target's name:");
//        String firstTargetName = in.next();
//        System.out.println("Please enter second Target's name:");
//        String secondTargetName = in.next();
//        //System.out.println("Please enter the relation between the targets:");
//        int dependencyChoice = getInputInt("Please enter the relation between the targets:" +
//                "\n1. RequiredFor" +
//                "\n2. DependsOn", "Invalid input, please try again",
//                "You should enter a whole number", 1, 2);
//        switch (dependencyChoice){
//            case 1:
//                dependency = Dependency.REQUIRED_FOR;
//                break;
//            case 2:
//                dependency = Dependency.DEPENDS_ON;
//                break;
//            case -1:
//                System.out.println("Returning to main menu");
//                return;
//        }
//        //String relation = in.next();
//        Collection<List<String>> paths = null;
//        try {
//            //paths = engine.getPaths(firstTargetName, secondTargetName, relation);
//            //paths = engine.getPaths(firstTargetName, secondTargetName, dependency);
//            if (null) {
//                System.out.println("There are not paths from target " + firstTargetName + " to target " + secondTargetName);
//            } else {
//                printPaths(paths);
//            }
//        } catch (TargetNotExistException e) {
//            System.out.println("The target " + e.getName() + " does not exist");
//        } catch (InvalidDependencyException e) {
//            System.out.println("The dependency: " + e.getDependency() + " is not supported in the system");
//        }
//    }
//
//    private void printPaths(Collection<List<String>> paths) {
//        for (List<String> path : paths) {
//            String currPath = printAPath(path);
//            System.out.println(currPath);
//        }
//    }
//
//    private String printAPath(List<String> path){
//        String currPath = "";
//        for (int i = 0; i < path.size() - 1; ++i) {
//            currPath += (path.get(i) + "->");
//        }
//        currPath += path.get(path.size() - 1);
//        return currPath;
//    }
//
//    private void loadFileToSystem() {
//        System.out.println(PRINT_LINE);
//        boolean finish = false;
//        while (!finish) {
//            System.out.println("Please enter the full path of file:(for return please press '0')");
//            Scanner in = new Scanner(System.in);
//            String path = in.nextLine();
//            if (!path.equals("0")) {
////                try {
////                    engine.loadFile(path);
////                    System.out.println("File loaded successfully!");
////                    finish = true;
////                } catch (InvalidFileException e) {
////                    System.out.println("The file in: " + e.getPath() + " is invalid because " + e.getReason());
////                } catch (DependencyConflictException e) {
////                    System.out.println("There is a dependency conflict between Targets: "
////                            + e.getFirstTarget() + ", " + e.getSecondTarget() + " dependency: " + e.getDependencyType());
////                } catch (DuplicateTargetsException e) {
////                    System.out.println("The target " + e.getTargetName() + " appears several times in the graph");
////                } catch (InvalidDependencyException e) {
////                    System.out.println("The dependency: " + e.getDependency() + " is not supported in the system");
////                } catch (TargetNotExistException e) {
////                    System.out.println("The target " + e.getName() + " does not exist");
////                } catch (SerialSetException e) {
////                    System.out.println("The Target " + e.getTargetName() + "In serial Set" + e.getSerialSetName() + "doesnt exist");
////                } catch (DupSerialSetsNameException e) {
////                    e.printStackTrace();
////                }
//            } else {
//                finish = true;
//            }
//        }
//    }
//
//
//    private void printGraphInfo() {
//        if(!engine.isFileLoaded())
//        {
//            System.out.println("A file was not loaded to the system yet");
//            return;
//        }
//        System.out.println(PRINT_LINE);
//        GraphDTO graphDTO = engine.getGraphDTO();
//        System.out.println("The name of the graph: " + graphDTO.getName());
//        System.out.println("Number of targets: " + graphDTO.getNumOfTargets());
//        System.out.println("Number of Leaves: " + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.LEAF) +
//                "\nNumber of Middles: " + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.MIDDLE) +
//                "\nNumber of Roots: " + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.ROOT) +
//                "\nNumber of Independents: " + graphDTO.getNumOfTargetsInPlace(PlaceInGraph.INDEPENDENT));
//
//
//    }
//
//
//    private void getTargetInfo() {
//        if(!engine.isFileLoaded()) {
//            System.out.println("A file was not loaded to the system yet");
//            return;
//        }
//        System.out.println(PRINT_LINE);
//        boolean finish = false;
//        while (!finish) {
//            System.out.println("Please enter the target's name:(to return to main menu, type '0')");
//            Scanner in = new Scanner(System.in);
//            String targetName = in.next();
//            if (targetName.equals("0")) {
//                finish = true;
//            }
//            else {
//                try {
//                    //TargetDTO target = engine.getTarget(targetName);
//                    printTargetDetails(target);
//                    finish = true;
//                } catch (TargetNotExistException e) {
//                    System.out.println("target.Target with name: " + e.getName() + "does not exist");
//                }
//            }
//        }
//    }
//
//    private void printTargetDetails(TargetDTO target) {
//        System.out.println(PRINT_LINE);
//        System.out.println("target.Target's name: " + target.getName());
//        System.out.println("target.Target's place: " + target.getPlace());
//        if (!target.getDependsOn().isEmpty()) {
//            System.out.println("dependsOn: ");
//            for (String targetName : target.getDependsOn()) {
//                System.out.println("        " + targetName);
//            }
//        } else {
//            System.out.println("There is no targets this target is depends on");
//        }
//        if (!target.getRequiredFor().isEmpty()) {
//            System.out.println("requiredFor: ");
//            for (String targetName : target.getRequiredFor()) {
//                System.out.println("        " + targetName);
//            }
//        } else {
//            System.out.println("There is no targets this target is required for");
//        }
//        if (target.getInfo()!= null) {
//            System.out.println("Target-info: " + target.getInfo());
//        } else {
//            System.out.println("There is not target.Target-info in this target");
//        }
//
//    }
//
//    private void activeSystemTask(){
//        boolean isIncremental = false;
//        int threadNumber;
//        TaskType taskType = TaskType.SIMULATION_TASK;
//        if(!engine.isFileLoaded())
//        {
//            System.out.println("A file was not loaded to the system yet");
//            return;
//        }
//        if(true){
//            System.out.println("There is a cycle in the graph, cannot active task");
//            return;
//        }
//        System.out.println(PRINT_LINE);
//                int inputTaskType = getInputInt("Please enter the type of the task: " +
//                        "\n1. Simulation task\n2.Compilation task",
//                        "Invalid input, please choose a number between 1 to 2",
//                        "Invalid input, The input should be a number", 1, 2);
//                switch (inputTaskType){
//                    case -1:
//                        System.out.println("Returning to main menu");
//                        return;
//                    case 1:
//                        taskType = TaskType.SIMULATION_TASK;
//                        break;
//                    case 2:
//                        taskType = TaskType.COMPILATION_TASK;
//                        break;
//                }
//            int incrementalInput = getInputInt("Do you want to run the task incrementally?" +
//                    "\n1. Yes" +
//                    "\n2. No", "Invalid input, please enter 1 or 2", "Invalid input, please enter a number", 1,2);
//            switch (incrementalInput){
//                case -1:
//                    System.out.println("Returning to main menu");
//                    return;
//                case 1:
////                    if(engine.isRunInIncrementalMode(taskType)){
////                        isIncremental = true;
////                    }
////                    else{
////                        isIncremental = false;
////                        System.out.println("The task cannot run incrementally." +
////                                "The "+ taskType.getTaskType() + " task run from Scratch by default");
////                    }
//                    break;
//                case 2:
//                    isIncremental = false;
//                    break;
//            }
//            System.out.println("Enter number of thread:");
//            Scanner in = new Scanner(System.in);
//            threadNumber = in.nextInt();
//            runSystemTask(isIncremental, taskType, threadNumber);
//    }
//
//
//    private void runSystemTask(boolean isIncremental, TaskType taskType, int threadNumber){
//
//        TaskParamsDTO taskParams = null;
//        switch (taskType){
//            case SIMULATION_TASK:
//                taskParams = getParamsOfSimulationTask();
//                break;
//            case COMPILATION_TASK:
//                taskParams = getParamsOfCompilationTask();
//                break;
//        }
//
//        if(taskParams == null){
//            System.out.println("Returning to main menu");
//            return;
//        }
//
//        Consumer<TargetDTO> printStrConsumer = targetDTO -> {
//            printTargetRunResult(targetDTO);
//        };
//        GraphDTO taskResults = null;
////        System.out.println(PRINT_LINE);
////        System.out.println("Initiating task......");
//        //taskResults = engine.activateTask(printStrConsumer, taskParams, taskType, isIncremental, threadNumber);
//        //printTaskRunResults(taskResults);
//    }
//
//    private TaskParamsDTO getParamsOfCompilationTask() {
//        Scanner in = new Scanner(System.in);
//        String sourceDir, destinationDir;
//        System.out.println("Enter source dir:");
//        sourceDir = in.nextLine();
//        System.out.println("Enter destination dir:");
//        destinationDir = in.nextLine();
//        CompilationTaskParamsDTO compilationTaskParamsDTO = new CompilationTaskParamsDTO(sourceDir,destinationDir);
//        return compilationTaskParamsDTO;
//    }
//
//    private void printTargetRunResult(TargetDTO targetDTO) {
//        System.out.println(PRINT_LINE);
//        System.out.println("Target name: " + targetDTO.getName()+ "\n") ;
//        System.out.println("Process result: " + targetDTO.getRunResult().getStatus() + "\n");
//        if(targetDTO.getInfo() != null){
//            System.out.println("Target info:" + targetDTO.getInfo() + "\n");
//        }
//        if(!targetDTO.getRunResult().equals(RunResults.SKIPPED)){
////            System.out.println("Process Start time:" + targetDTO.getStartingTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
////            System.out.println("Process End time:" + targetDTO.getEndingTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
//            if(!targetDTO.getTargetsThatCanBeRun().isEmpty()){
//                System.out.println("The dependent Targets that were opened:\n" + targetDTO.getTargetsThatCanBeRun() + "\n");
//            }
//            if(targetDTO.getRunResult().equals(RunResults.FAILURE)){
//                if(!targetDTO.getSkippedFathers().isEmpty()){
//                    System.out.println("The targets that won't be able to process are: \n" + targetDTO.getSkippedFathers() + "\n");
//                }
//            }
//        }
//        if(targetDTO.getTaskRunResult() != null){
//            System.out.println("Run result:\n" + targetDTO.getTaskRunResult());
//        }
//        System.out.println(PRINT_LINE);
//
//
//    }
//
//    private void printTaskRunResults(GraphDTO taskResults) {
//        Duration duration = Duration.ofMillis(taskResults.getRunTime());
//        long seconds = duration.getSeconds();
//        long HH = seconds / 3600;
//        long MM = (seconds % 3600) / 60;
//        long SS = seconds % 60;
//        String runTime = String.format("%02d:%02d:%02d", HH, MM, SS);
//        System.out.println("Task's run time: " + runTime);
//        System.out.println("There are " + taskResults.getNumOfTargetsRunResult(RunResults.SUCCESS) + " Targets that finished with Success");
//        System.out.println("There are " + taskResults.getNumOfTargetsRunResult(RunResults.WARNING) + " Targets that finished with Warning");
//        System.out.println("There are " + taskResults.getNumOfTargetsRunResult(RunResults.FAILURE) + " Targets that failed");
//        System.out.println("There are " + taskResults.getNumOfTargetsRunResult(RunResults.SKIPPED) + " Targets that skipped");
//        System.out.println("The graph targets are: ");
//        for(TargetDTO target: taskResults.getTargets().values()){
//            System.out.println("    Target: " + target.getName());
//            System.out.println("    Run result: " +target.getRunResult().getStatus());
//            if(target.getRunResult().equals(RunResults.SUCCESS) || target.getRunResult().equals(RunResults.WARNING)
//            || target.getRunResult().equals(RunResults.FAILURE)){
//                duration = Duration.ofMillis(target.getRunTime());
//                seconds = duration.getSeconds();
//                HH = seconds / 3600;
//                MM = (seconds % 3600) / 60;
//                SS = seconds % 60;
//                runTime = String.format("%02d:%02d:%02d", HH, MM, SS);
//                System.out.println("    Runtime: " + runTime);
//            }
//            System.out.println("----------------------");
//        }
//
//    }
//
//    private SimulationTaskParamsDTO getParamsOfSimulationTask() {
//        boolean isRandom = false;
//        int processTime = 0;
//        double successRate = 0,successWithWarnings = 0;
//        System.out.println("Please enter the next Simulation task parameters: ");
//        processTime = getInputInt("Please enter the process time in MilliSeconds: (whole positive number)",
//                "Invalid input, The input should be a positive number",
//                "Invalid input, The input should be a whole positive number", 1,Integer.MAX_VALUE);
//        if(processTime == -1){
//            return null;
//        }
//        int random = getInputInt("Do you want the process time to be randomized?" +
//                        "\n1. Yes" +
//                        "\n2. No",
//                "Invalid Input, you should enter 1 for yes or 2 for No",
//                "Invalid input, The input should be a number", 1,2);
//        switch (random){
//            case 1:
//                isRandom = true;
//                break;
//            case 2:
//                isRandom = false;
//                break;
//            default:
//                return null;
//        }
//        successRate = this.getInputDouble("Please enter process's success rate:(a positive number between 0 to 1)",
//                "Invalid input, The input should be between 0 to 1","Invalid input, The input should be a number",
//                0,1);
//        if(successRate == -1){
//            return null;
//        }
//        successWithWarnings = getInputDouble("Please enter process's success with warnings rate:(a positive number between 0 to 1)",
//                "Invalid input, The input should be a positive number between 0 to 1","Invalid input, The input should be a number between 0 to 1",
//                0, 1);
//        if(successWithWarnings == -1){
//            return null;
//        }
//
//        SimulationTaskParamsDTO simulationTaskDTO = new SimulationTaskParamsDTO(processTime, isRandom, successRate, successWithWarnings);
//        return simulationTaskDTO;
//    }
//
//    private double getInputDouble(String inputMessage, String invalidInputMessage, String exceptionMessage, double minVal, double maxVal){
//        double res = 0;
//        boolean finish = false;
//        System.out.println(inputMessage);
//        System.out.println("To return please press '-'.");
//        Scanner in = new Scanner(System.in);
//        while(!finish){
//            String ans = in.next();
//            if(ans.equals("-")){
//                finish = true;
//                res = -1;
//            }
//            else{
//                try {
//                        res = Double.parseDouble(ans);
//                        finish = true;
//                        if (res < minVal || res > maxVal) {
//                            System.out.println(invalidInputMessage);
//                            finish = false;
//                        }
//                    }catch (NumberFormatException e) {
//                        System.out.println(exceptionMessage);
//                    }
//            }
//        }
//        return res;
//    }
//
//
//    private int getInputInt(String inputMessage, String invalidInputMessage, String exceptionMessage, double minVal, double maxVal){
//        int res = 0;
//        boolean finish = false;
//        System.out.println(inputMessage);
//        System.out.println("To return please press '-'.");
//        Scanner in = new Scanner(System.in);
//        while(!finish){
//            String ans = in.next();
//            if(ans.equals("-")){
//                finish = true;
//                res = -1;
//            }
//            else{
//                try {
//                    res = Integer.parseInt(ans);
//                    finish = true;
//                    if (res < minVal || res > maxVal) {
//                        System.out.println(invalidInputMessage);
//                        finish = false;
//                    }
//                }catch (NumberFormatException e) {
//                    System.out.println(exceptionMessage);
//                }
//            }
//        }
//        return res;
//    }
}
