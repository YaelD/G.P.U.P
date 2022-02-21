package servlets.dashboard_servlets;

import constants.Constants;
import engine.TasksManager;
import general_enums.TaskStatus;
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
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "TaskExecution", urlPatterns = {"/task_execution"})
public class TaskExecutionServlet extends HttpServlet {

    //registering worker to task
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //TODO: CHECK IF THE USER IS ALREADY REGISTERED TO THE TASK
        String usernameFromSession = SessionUtils.getUsername(request);
        String userTypeFromSession = SessionUtils.getUserType(request);
        if(usernameFromSession == null){    //user is not logged in - error!!
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else if(userTypeFromSession == null || !userTypeFromSession.equals(Constants.WORKER)){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else{
            String[] paramsNames = {Constants.TASK_NAME};
            try{
                Map<String, String> mapParams = null;
                mapParams = ServletUtils.validateRequestQueryParams(request, paramsNames);
                TasksManager tasksManager = ServletUtils.getTasksManager(getServletContext());
                String taskName = mapParams.get(Constants.TASK_NAME).trim();
                Task task = tasksManager.workerRegistrationToTask(taskName, usernameFromSession);
                SessionUtils.setUserTasks(request,task,Constants.ADD_TASK);
                response.setStatus(HttpServletResponse.SC_OK);
            }
            catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(e.getMessage());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
