import java.text.SimpleDateFormat;
import java.util.Collection;

public class GraphDTO {

    private int numOfTargets;
    private Collection<TargetDTO> targets;
    private SimpleDateFormat runTime;


    public int getNumOfTargets() {
        return numOfTargets;
    }

    public Collection<TargetDTO> getTargets() {
        return targets;
    }

    public SimpleDateFormat getRunTime() {
        return runTime;
    }

    public int getNumOfLeaves(){
        int counter = 0;
        for(TargetDTO target: targets){
            if(target.getPlace() == PlaceInGraph.LEAF)
                counter++;
        }
        return counter;
    }

    public int getNumOfMiddles(){
        int counter = 0;
        for(TargetDTO target: targets){
            if(target.getPlace() == PlaceInGraph.MIDDLE)
                counter++;
        }
        return counter;
    }

    public int getNumOfRoots(){
        int counter = 0;
        for(TargetDTO target: targets){
            if(target.getPlace() == PlaceInGraph.ROOT)
                counter++;
        }
        return counter;
    }

    public int getNumOfIndependents(){
        int counter = 0;
        for(TargetDTO target: targets){
            if(target.getPlace() == PlaceInGraph.INDEPENDENT)
                counter++;
        }
        return counter;
    }

    public int getNumOfFails(){
        int counter = 0;
        for(TargetDTO target: targets){
            if(target.getRunResult() == RunResults.FAILURE)
                counter++;
        }
        return counter;
    }

    public int getNumOfSuccess(){
        int counter = 0;
        for(TargetDTO target: targets){
            if(target.getRunResult() == RunResults.SUCCESS)
                counter++;
        }
        return counter;
    }

    public int getNumOfWarnings(){
        int counter = 0;
        for(TargetDTO target: targets){
            if(target.getRunResult() == RunResults.WARNING)
                counter++;
        }
        return counter;
    }

    public int getNumOfFrozen(){
        int counter = 0;
        for(TargetDTO target: targets){
            if(target.getRunResult() == RunResults.FROZEN)
                counter++;
        }
        return counter;
    }
}
