package servlets.dashboard_servlets;

import com.google.gson.Gson;
import constants.Constants;
import engine.Engine;
import engine.GraphsManager;
import exceptions.GraphNotExistException;
import exceptions.InvalidDependencyException;
import exceptions.TargetNotExistException;
import general_enums.Dependency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "getPaths", urlPatterns = {"/get_paths"})
public class FindPathServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        if(usernameFromSession == null){    //user is not logged in - error!!
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else {
            String[] paramsNames = {Constants.SOURCE_TARGET, Constants.DEPENDENCY, Constants.DESTINATION_TARGET,
                    Constants.GRAPH_NAME};
            PrintWriter body = response.getWriter();
            try{
                Map<String, String> mapParams = ServletUtils.validateRequestQueryParams(request, paramsNames);
                String sourceTargetParameter = mapParams.get(Constants.SOURCE_TARGET);
                String destinationTargetParameter = mapParams.get(Constants.DESTINATION_TARGET);
                String graphNameParameter = mapParams.get(Constants.GRAPH_NAME);
                String dependencyParameter = mapParams.get(Constants.DEPENDENCY);

                GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                Gson gson = new Gson();
                Collection<List<String>> paths = graphsManager.getPaths(sourceTargetParameter, destinationTargetParameter,
                        Dependency.valueOf(dependencyParameter.toUpperCase(Locale.ROOT)), graphNameParameter);
                String json = gson.toJson(paths);
                body.print(json);
                body.flush();

            } catch(Exception e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                body.print(e.getMessage());
            }
        }
    }
}
