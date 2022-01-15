package graph;

import exceptions.SerialSetException;

import target.Target;

import java.util.ArrayList;
import java.util.List;

public class SerialSet {

    private String name;
    private List<Target> targetList = new ArrayList<>();
    private boolean canGetMonitor = true;

    public SerialSet(String name, List<Target> targetList) {
        this.name = name;
        this.targetList = targetList;
        this.canGetMonitor = true;

    }

    public String getName() {
        return name;
    }

    public List<Target> getTargetsList() {
        return targetList;
    }

    public static void checkIfSetTargetExistInGraph(List<String> targetsName, Graph graph, SerialSet currSerialSet, String serialSetName) throws SerialSetException {
        for(String currTarget : targetsName){
            if(!graph.getTargetGraph().containsKey(currTarget)){
                throw new SerialSetException(currTarget, serialSetName);
            }
            else{
                currSerialSet.getTargetsList().add(graph.getTarget(currTarget));
                graph.getTarget(currTarget).getSerialSetsContainer().getSerialSetList().add(currSerialSet);
            }
        }
    }

    public synchronized void getSerialSetMonitor(){
        //Thread A got the lock!!!
        while (!canGetMonitor){  //
            try{
//                synchronized (Task.printDummy){
//                    System.out.println(Thread.currentThread().getName() + " going to wait for " + this.name + " SerialSet monitor");
//                }
                this.wait(); //wait and release the lock
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        canGetMonitor = false;
//        synchronized (Task.printDummy){
//            System.out.println(Thread.currentThread().getName() + ": got: " + this.name + " SerialSet monitor");
//        }
        return;
        //Free the lock!
    }


    public synchronized void freeMonitor(){
        canGetMonitor = true;
        this.notifyAll();
//        synchronized (Task.printDummy){
//            System.out.println(Thread.currentThread().getName() + ": release " + this.name + " SerialSet monitor");
//        }

    }

}
