package servlets.dashboard_servlets;

import com.google.gson.Gson;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user.User;
import user.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class TasksInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter body = response.getWriter()) {
            Set<UserDTO> userDTOS = new HashSet<>();
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            Set<User> usersList = userManager.getUsers();
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
