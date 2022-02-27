package servlets.dashboard_servlets;

import constants.Constants;
import engine.TasksManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import task.Task;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "UnregisterExecution", urlPatterns = {"/unregister_execution"})
public class UnregisterWorker extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String userTypeFromSession = SessionUtils.getUserType(request);
        Map<String, Task> userTasks = SessionUtils.getUserTasks(request);
        if (usernameFromSession == null) {    //user is not logged in - error!!
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else if (userTypeFromSession == null || !userTypeFromSession.equals(Constants.WORKER)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            String[] paramsNames = {Constants.TASK_NAME};
            PrintWriter body = response.getWriter();
            try {
                Map<String, String> mapParams = null;
                mapParams = ServletUtils.validateRequestQueryParams(request, paramsNames);
                TasksManager tasksManager = ServletUtils.getTasksManager(getServletContext());
                String taskName = mapParams.get(Constants.TASK_NAME).trim();
                if (userTasks != null && !userTasks.containsKey(taskName)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    body.print("The user already unregistered from the task");
                } else {
                    Task task = tasksManager.removeWorkerFromTask(taskName, usernameFromSession);
                    SessionUtils.setUserTasks(request, task, Constants.REMOVE_TASK);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
            }
        }
    }
}
