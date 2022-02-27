package servlets.dashboard_servlets;

import com.google.gson.Gson;
import constants.Constants;
import dto.ExecutionTargetDTO;
import dto.TargetDTO;
import dto.TaskParamsDTO;
import engine.Engine;
import engine.ExceptionMessages;
import engine.SystemEngine;
import engine.TasksManager;
import general_enums.RunStatus;
import general_enums.TaskStatus;
import general_enums.TaskType;
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
import java.util.*;

@WebServlet(name = "TaskExecution", urlPatterns = {"/task_execution"})
public class TaskExecutionServlet extends HttpServlet {

    //registering worker to task
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
                if (userTasks != null && userTasks.containsKey(taskName)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    body.print("The user already registered to the task");
                } else {
                    Task task = tasksManager.workerRegistrationToTask(taskName, usernameFromSession);
                    TaskParamsDTO taskParamsDTO = tasksManager.toTaskParamsDTO(task);
                    SessionUtils.setUserTasks(request, task, Constants.ADD_TASK);
                    response.setStatus(HttpServletResponse.SC_OK);
                    Gson gson = new Gson();
                    String json = gson.toJson(taskParamsDTO);
                    body.print(json);
                    body.flush();
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
            }
        }
    }

    //sending targets for execution
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        String userTypeFromSession = SessionUtils.getUserType(request);
        Map<String, Task> userTasks = SessionUtils.getUserTasks(request);
        if (usernameFromSession == null) {    //user is not logged in - error!!
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else if (userTypeFromSession == null || !userTypeFromSession.equals(Constants.WORKER)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            String[] paramsNames = {Constants.NUMBER_OF_TARGETS};
//            PrintWriter body =
            try{
                Map<String, String> mapParams = null;
                mapParams = ServletUtils.validateRequestQueryParams(request, paramsNames);
                String numOfTargets = mapParams.get(Constants.NUMBER_OF_TARGETS).trim();
                if (userTasks == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().print(Constants.WORKER_NOT_REGISTER_TASK);
                }
                else {
                    TasksManager tasksManager = ServletUtils.getTasksManager(getServletContext());
                    Set<String> finishedTaskName = checkTaskStatus(request, userTasks, usernameFromSession);
                    Set<TargetDTO> targetDTOs = new HashSet<>();
                    if(!userTasks.isEmpty()){
                        targetDTOs = tasksManager.getTaskTargetForExecution(userTasks.values(), Integer.parseInt(numOfTargets));
                    }
                    response.setStatus(HttpServletResponse.SC_OK);
                    Gson gson = new Gson();
                    String targetJson = gson.toJson(targetDTOs);
                    String tasksNameJson = gson.toJson(finishedTaskName);
                    List<String> responseBody = new ArrayList<>();
                    responseBody.add(targetJson);
                    responseBody.add(tasksNameJson);
                    String respBody = gson.toJson(responseBody);
                    response.getWriter().print(respBody);
//                    body.flush();
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
            }
        }
    }


    private Set<String> checkTaskStatus(HttpServletRequest request, Map<String, Task> userTasks, String userName) {
        Map<String, Task> clonedUserTasks = new HashMap<>(userTasks);
        Set<String> finishedTasksName = new HashSet<>();
        for(Task currTask : clonedUserTasks.values()){
            if(currTask.getStatus().equals(TaskStatus.FINISHED) ||
                    currTask.getStatus().equals(TaskStatus.STOPPED)){
                Task removedTask = SessionUtils.setUserTasks(request, currTask, Constants.REMOVE_TASK);
                removedTask.removeWorkerFromTask(userName);
                finishedTasksName.add(removedTask.getTaskName());
            }
        }
        return finishedTasksName;
    }

    //servlet that gets target during their running and update the targets in the system
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String userTypeFromSession = SessionUtils.getUserType(request);
        Map<String, Task> userTasks = SessionUtils.getUserTasks(request);
        if (usernameFromSession == null) {    //user is not logged in - error!!
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else if (userTypeFromSession == null || !userTypeFromSession.equals(Constants.WORKER)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else if (userTasks == null) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().print("The user has not registered to any task yet");
        } else {
            String[] paramsNames = {Constants.TASK_NAME};
            PrintWriter body = response.getWriter();
            try{
                Map<String, String> mapParams = null;
                mapParams = ServletUtils.validateRequestQueryParams(request, paramsNames);
                TasksManager tasksManager = ServletUtils.getTasksManager(getServletContext());
                String taskName = mapParams.get(Constants.TASK_NAME).trim();
                if (!userTasks.containsKey(taskName)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    body.print("The user has not registered to the task");
                }
                String executionTargetDtoStr = ServletUtils.getRequestBody(request);
                ExecutionTargetDTO executionTargetDTO = new Gson().fromJson(executionTargetDtoStr, ExecutionTargetDTO.class);
                int priceForTarget = tasksManager.updateTargetRunResult(executionTargetDTO);
                if(priceForTarget == 0){
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
                else{
                    response.setStatus(HttpServletResponse.SC_OK);

                    response.getWriter().print(priceForTarget);
                }

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
            }
        }
    }
}
