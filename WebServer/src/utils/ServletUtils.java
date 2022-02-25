package utils;

import engine.Engine;
import engine.GraphsManager;
import engine.SystemEngine;

import engine.TasksManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import user.UserManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE = "userManager";
    private static final String SYSTEM_ENGINE_ATTRIBUTE = "systemEngine";
    private static final String TASKS_MANAGER_ATTRIBUTE = "taskManager";
    private static final String GRAPHS_MANAGER_ATTRIBUTE = "graphsManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object engineManagerLock = new Object();
    private static final Object tasksManagerLock = new Object();
    private static final Object graphsManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {
        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE);
    }

    public static SystemEngine getSystemEngine(ServletContext servletContext) {
        synchronized (engineManagerLock) {
            if (servletContext.getAttribute(SYSTEM_ENGINE_ATTRIBUTE) == null) {
                servletContext.setAttribute(SYSTEM_ENGINE_ATTRIBUTE, new SystemEngine());
            }
        }
        return (SystemEngine) servletContext.getAttribute(SYSTEM_ENGINE_ATTRIBUTE);
    }

    public static GraphsManager getGraphsManager(ServletContext servletContext) {
        synchronized (graphsManagerLock) {
            if (servletContext.getAttribute(GRAPHS_MANAGER_ATTRIBUTE) == null) {
                servletContext.setAttribute(GRAPHS_MANAGER_ATTRIBUTE, new GraphsManager());
            }
        }
        return (GraphsManager) servletContext.getAttribute(GRAPHS_MANAGER_ATTRIBUTE);
    }

    public static TasksManager getTasksManager(ServletContext servletContext) {
        synchronized (tasksManagerLock) {
            if (servletContext.getAttribute(TASKS_MANAGER_ATTRIBUTE) == null) {
                servletContext.setAttribute(TASKS_MANAGER_ATTRIBUTE, new TasksManager());
            }
        }
        return (TasksManager) servletContext.getAttribute(TASKS_MANAGER_ATTRIBUTE);
    }

//    public static int getIntParameter(HttpServletRequest request, String name) {
//        String value = request.getParameter(name);
//        if (value != null) {
//            try {
//                return Integer.parseInt(value);
//            } catch (NumberFormatException numberFormatException) {
//            }
//        }
//        return INT_PARAMETER_ERROR;
//    }

    public static Map<String, String> validateRequestQueryParams (HttpServletRequest request, String[] paramsNames) throws Exception {
        Map<String, String> paramsMap = new HashMap<>();
        for(String paramType : paramsNames){
            String param = request.getParameter(paramType).trim();
            if( param == null || param.isEmpty()){
                throw new Exception("The param" + paramType + "is invalid");
            }
            paramsMap.put(paramType, request.getParameter(paramType));
        }
        return paramsMap;
    }

    public static String getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        return (reader.lines().collect(Collectors.joining()));
    }
}
