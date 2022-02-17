package servlets.dashboard_servlets;

import com.google.gson.Gson;
import dto.GraphDTO;
import dto.UserDTO;
import engine.Engine;
import graph.Graph;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user.User;
import user.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


@WebServlet(name = "UsersInfo", urlPatterns = {"/users_list"})
public class UsersInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        try (PrintWriter body = response.getWriter()) {
            Set<UserDTO> userDTOS = new HashSet<>();
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(getServletContext());

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
