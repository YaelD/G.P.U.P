package admin_engine;

import dto.TaskParamsDTO;

import java.util.HashMap;
import java.util.Map;

public class Utilities {

    public static Map<String, TaskParamsDTO> TASK_PARAMS = new HashMap<>();
    public static Map<String, Integer> TASK_APPEARANCE_COUNTER = new HashMap<>();

    public static Map<String, String> TASK_NAME_TO_ORIGINAL = new HashMap<>();


    public static String getOriginalTaskString(String currName){
        String res = currName;
        while (res != TASK_NAME_TO_ORIGINAL.get(currName)){
            res = TASK_NAME_TO_ORIGINAL.get(res);
        }
        return res;
    }

}
