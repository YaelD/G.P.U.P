package servlets.dashboard_servlets;

import com.google.gson.Gson;
import dto.TaskDTO;
import dto.UserDTO;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import task.Task;
import user.User;
import user.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TasksInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter body = response.getWriter()) {
            Set<TaskDTO> taskDTOS = new HashSet<>();
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(getServletContext());
            Map<String, Task> usersList = engine.getTasksInSystem();

            Set<>
            for(User user : usersList){
                UserDTO userDTO = user.makeUserDTO();
                userDTOS.add(userDTO);
            }
            String json = gson.toJson(userDTOS);
            body.print(json);
            body.flush();
        }
    }
}
