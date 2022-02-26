package utils;


import constants.Constants;
import engine.ExceptionMessages;
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

    public static Task setUserTasks(HttpServletRequest request, Task task, String operation){
        Map<String, Task> userTasks = getUserTasks(request);
        HttpSession session = request.getSession(false);
        Task taskToReturn = null;
        if(userTasks == null){
            userTasks = new HashMap<>();
        }
        synchronized (userTasks){
            if(operation.equals(Constants.ADD_TASK)){
                userTasks.put(task.getTaskName(),task);
            }else{
                taskToReturn = userTasks.remove(task.getTaskName());
            }
        }
        session.setAttribute(Constants.USER_TASKS, userTasks);
        return taskToReturn;
    }

//    public static boolean isTaskExistInSession(HttpServletRequest request, String taskName){
//        Map<String, Task> userTasks = getUserTasks(request);
//        if(userTasks == null){
//            return false;
//        }
//        return userTasks.containsKey(taskName);
//    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
