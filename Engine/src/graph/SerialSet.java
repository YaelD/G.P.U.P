package graph;

import exceptions.SerialSetException;
import exceptions.TargetNotExistException;
import target.RunStatus;

import target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
public class SerialSet {

    private String name;
    private List<Target> targetList = new ArrayList<>();
    private boolean isMonitorFree = false;

    public SerialSet(String name, List<Target> targetList) {
        this.name = name;
        this.targetList = targetList;
    }

    public String getName() {
        return name;
    }

    public List<Target> getTargetsSet() {
        return targetList;
    }

    public static void checkIfSetTargetExistInGraph(List<String> targetsName, Graph graph, List<Target> targetList, String serialSetName) throws SerialSetException {
        for(String currTarget : targetsName){
            if(!graph.getTargetGraph().containsKey(currTarget)){
                throw new SerialSetException(currTarget, serialSetName);
            }
            else{
                targetList.add(graph.getTargetGraph().get(currTarget));
            }
        }
    }

    public synchronized void getSerialSetMonitor(){
        while (!isMonitorFree){
            try{
                System.out.println(Thread.currentThread().getName() + "going to wait for " + this.name + " SerialSet monitor");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + ": got: " + this.name + " SerialSet monitor");
        isMonitorFree = false;
        return;
    }

//    public synchronized void updateTargetsRunStatus(Target target) {
//        for(Target currTarget : this.getTargetsSet()){
//            if(!currTarget.getName().equals(target.getName())){
//                if(currTarget.getRunStatus().equals(RunStatus.WAITING)) {
//                    currTarget.setRunStatus(RunStatus.FROZEN);
//                }
//            }
//        }
//    }
}
