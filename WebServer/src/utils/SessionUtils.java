package utils;


import constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import task.Task;

import java.util.HashMap;
import java.util.Map;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getUserType (HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USER_TYPE) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static Map<String,Task> getUserTasks (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USER_TASKS) : null;
        return sessionAttribute != null ? (Map<String,Task>)sessionAttribute : null;
    }

    public static void setUserTasks(HttpServletRequest request, Task task, String operation){
        Map<String, Task> userTasks = getUserTasks(request);
        HttpSession session = request.getSession(false);
        if(userTasks == null){
            userTasks = new HashMap<>();
        }
        if(operation.equals(Constants.ADD_TASK)){
            userTasks.put(task.getTaskName(),task);
        }else{
            userTasks.remove(task.getTaskName());
        }
       session.setAttribute(Constants.USER_TASKS, userTasks);
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
