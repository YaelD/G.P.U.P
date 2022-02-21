package general_enums;

public enum TaskStatus {

    NEW,
    ACTIVE,
    SUSPENDED,
    STOPPED,
    FINISHED;

    public static boolean contains(String type){
        boolean isContain = false;
        for(TaskStatus taskStatus : values()){
            if(taskStatus.name().equals(type)){
                isContain = true;
            }
        }
        return isContain;
    }
}
