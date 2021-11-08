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
        return 0;
    }

    public int getNumOfSuccess(){
        return 0;
    }

    public int getNumOfWarnings(){
        return 0;
    }

    public int getNumOfFrozen(){
        return 0;
    }
}
