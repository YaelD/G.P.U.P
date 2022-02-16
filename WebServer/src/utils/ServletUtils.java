package utils;

import engine.Engine;
import engine.SystemEngine;

import jakarta.servlet.ServletContext;
import user.UserManager;

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
}
