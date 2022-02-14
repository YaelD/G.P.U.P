package utils;


import constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import task.Task;

import java.util.Map;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static Map<String,Task> getUserTasks (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USER_TASKS) : null;
        return sessionAttribute != null ? (Map<String,Task>)sessionAttribute : null;
    }


    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
