package utils;

import engine.Engine;
import engine.SystemEngine;

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
    private static final String ENGINE_ATTRIBUTE = "engine";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object engineManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {
        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE);
    }

    public static Engine getEngine(ServletContext servletContext) {
        synchronized (engineManagerLock) {
            if (servletContext.getAttribute(ENGINE_ATTRIBUTE) == null) {
                servletContext.setAttribute(ENGINE_ATTRIBUTE, new SystemEngine());
            }
        }
        return (Engine) servletContext.getAttribute(ENGINE_ATTRIBUTE);
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
