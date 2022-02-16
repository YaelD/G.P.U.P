package servlets.dashboard_servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user.User;
import user.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Set;


@WebServlet(name = "UsersInfo", urlPatterns = {"/users"})
public class UsersInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserManager userManager= ServletUtils.getUserManager(getServletContext());
        Set<User> users = userManager.getUsers();

    }
}
