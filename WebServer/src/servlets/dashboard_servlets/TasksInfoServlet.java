package servlets.dashboard_servlets;

import com.google.gson.Gson;
import dto.TaskDTO;
import engine.Engine;
import exceptions.TaskExistException;
import general_enums.TaskType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import task.CompilationTask;
import task.SimulationTask;
import task.Task;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "TasksInfo", urlPatterns = {"/tasks_list"})
public class TasksInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter body = response.getWriter()) {
            Set<TaskDTO> taskDTOS = new HashSet<>();
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(getServletContext());
            Map<String, Task> tasksInSystem = engine.getTasksInSystem();
            Collection<Task> tasks = tasksInSystem.values();
            for(Task task : tasks){
                TaskDTO taskDTO = task.createTaskDTO();
                taskDTOS.add(taskDTO);
            }
            String json = gson.toJson(taskDTOS);
            body.print(json);
            body.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Engine engine = ServletUtils.getEngine(getServletContext());
        Task task = null;
        //String createdTask = ServletUtils.getRequestBody(request);
        //we can do it also like this:
        TaskDTO createdTaskDTO = (TaskDTO) new Gson().fromJson(request.getReader(), TaskDTO.class);

        //TaskDTO createdTaskDTO = (TaskDTO) new Gson().fromJson(createdTask, TaskDTO.class);
        if(createdTaskDTO.getTaskType().equals(TaskType.COMPILATION_TASK)){
            task = CompilationTask.createCompilationTaskFromDTO(createdTaskDTO);
        }
        else{
            task = SimulationTask.createSimulationTaskFromDTO(createdTaskDTO);
        }
        try {
            engine.addTaskToSystem(task);
        } catch (TaskExistException e) {
            e.printStackTrace();
        }
        //TODO: ENTER THE TASK INTO THE TASKS IN SYSTEM STRUCTURE

    }
}
