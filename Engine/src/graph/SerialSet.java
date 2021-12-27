package graph;

import exceptions.SerialSetException;

import target.Target;

import java.util.ArrayList;
import java.util.List;

public class SerialSet {

    private String name;
    private List<Target> targetList = new ArrayList<>();
    private boolean canGetMonitor = false;

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

    public static void checkIfSetTargetExistInGraph(List<String> targetsName, Graph graph, SerialSet currSerialSet, String serialSetName) throws SerialSetException {
        for(String currTarget : targetsName){
            if(!graph.getTargetGraph().containsKey(currTarget)){
                throw new SerialSetException(currTarget, serialSetName);
            }
            else{
                currSerialSet.getTargetsSet().add(graph.getTarget(currTarget));
                graph.getTarget(currTarget).getSerialSetsContainer().getSerialSetList().add(currSerialSet);
            }
        }
    }

    public synchronized void getSerialSetMonitor(){
        //Thread A got the lock!!!
        while (!canGetMonitor){  //
            try{
                System.out.println(Thread.currentThread().getName() + "going to wait for " + this.name + " SerialSet monitor");
                this.wait(); //wait and release the lock
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + ": got: " + this.name + " SerialSet monitor");
        canGetMonitor = false;
        return;
        //Free the lock!
    }


    public synchronized void freeMonitor(){
        canGetMonitor = true;
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
