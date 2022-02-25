package servlets.dashboard_servlets;

import com.google.gson.Gson;
import constants.Constants;
import dto.CompilationTaskParamsDTO;
import dto.SimulationTaskParamsDTO;
import dto.TaskDTO;
import dto.TaskParamsDTO;
import engine.Engine;
import engine.GraphsManager;
import engine.TasksManager;
import exceptions.GraphNotExistException;
import exceptions.TargetNotExistException;
import exceptions.TaskExistException;
import general_enums.TaskStatus;
import general_enums.TaskType;
import graph.Graph;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import task.CompilationTask;
import task.SimulationTask;
import task.Task;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "TasksInfo", urlPatterns = {"/tasks_list"})
public class TasksInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        if(usernameFromSession == null){    //user is not logged in - error!!
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else{
            response.setContentType("application/json");
            try (PrintWriter body = response.getWriter()) {
                Set<TaskDTO> taskDTOS = new HashSet<>();
                Gson gson = new Gson();
                TasksManager tasksManager = ServletUtils.getTasksManager(getServletContext());
                Map<String, Task> tasksInSystem = tasksManager.getTasksInSystem();
                Collection<Task> tasks = tasksInSystem.values();
                for(Task task : tasks){
                    TaskDTO taskDTO = task.createTaskDTO();
                    taskDTOS.add(taskDTO);
                }
                String json = gson.toJson(taskDTOS);
                System.out.print("IN GET TASK LIST===>" + json);
                body.print(json);
                body.flush();
            }
        }
    }


    //function that create new task in the system
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        String userTypeFromSession = SessionUtils.getUserType(request);
        if(usernameFromSession == null){    //user is not logged in - error!!
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else if(userTypeFromSession == null || !userTypeFromSession.equals(Constants.ADMIN)){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else{
            GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
            TasksManager tasksManager = ServletUtils.getTasksManager(getServletContext());
            Task task = null;
            String createdTask = ServletUtils.getRequestBody(request);
            TaskParamsDTO createdTaskParamsDTO = (TaskParamsDTO) new Gson().fromJson(createdTask, TaskParamsDTO.class);
            try {
                if(graphsManager.isGraphExistsInSystem(createdTaskParamsDTO.getGraphName())
                        && !tasksManager.isTaskExistInSystem(createdTaskParamsDTO.getTaskName())
                        && graphsManager.isTargetsExistsInGraph(createdTaskParamsDTO.getTargets(), createdTaskParamsDTO.getGraphName())) {
                    Graph graphForTask = graphsManager.getGraphsInSystem().get(createdTaskParamsDTO.getGraphName());
                    if(createdTaskParamsDTO.getTaskType().equals(TaskType.COMPILATION_TASK)){
                        CompilationTaskParamsDTO compilationTaskParamsDTO = (CompilationTaskParamsDTO) new Gson().fromJson(createdTask, CompilationTaskParamsDTO.class);
                        task = CompilationTask.createCompilationTaskFromDTO(compilationTaskParamsDTO, graphForTask);
                    }
                    else{
                        SimulationTaskParamsDTO simulationTaskParamsDTO = (SimulationTaskParamsDTO) new Gson().fromJson(createdTask, SimulationTaskParamsDTO.class);
                        task = SimulationTask.createSimulationTaskFromDTO(simulationTaskParamsDTO, graphForTask);
                    }
                    tasksManager.addTaskToSystem(task);
                    SessionUtils.setUserTasks(request, task, Constants.ADD_TASK);
                }
            } catch(Exception e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
            }
        }

    }

    //function that get a task and it's new status and changed it
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        String userTypeFromSession = SessionUtils.getUserType(request);
        if(usernameFromSession == null){    //user is not logged in - error!!
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else if(userTypeFromSession == null || !userTypeFromSession.equals(Constants.ADMIN)){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else{
            String[] paramsNames = {Constants.TASK_NAME, Constants.TASK_STATUS};
            try(PrintWriter body = response.getWriter()){
                Map<String, String> mapParams = null;
                mapParams = ServletUtils.validateRequestQueryParams(request, paramsNames);
                TasksManager tasksManager = ServletUtils.getTasksManager(getServletContext());
                String taskName = mapParams.get(Constants.TASK_NAME).trim();
                String taskStatus = mapParams.get(Constants.TASK_STATUS).trim();
                if(tasksManager.isTaskExistInSystem(taskName) && TaskStatus.contains(taskStatus)){
                    Task task = tasksManager.updateTaskStatus(taskName, TaskStatus.valueOf(taskStatus.toUpperCase(Locale.ROOT)));
                    if(task == null){
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        body.print("Invalid status");
                    }
                    else{
                        response.setStatus(HttpServletResponse.SC_OK);
                        //SessionUtils.setUserTasks(request, task, Constants.ADD_TASK);
                    }
                }
            }
            catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
            }
        }
    }
}
