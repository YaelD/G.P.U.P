package servlets.dashboard_servlets;

import com.google.gson.Gson;
import constants.Constants;
import engine.Engine;
import engine.GraphsManager;
import exceptions.GraphNotExistException;
import exceptions.InvalidDependencyException;
import exceptions.TargetNotExistException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


@WebServlet(name = "findCycle", urlPatterns = {"/find_cycle"})
public class FindCycleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        if(usernameFromSession == null){    //user is not logged in - error!!
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else{
            String[] paramsNames = {Constants.SOURCE_TARGET, Constants.GRAPH_NAME};
            PrintWriter body = response.getWriter();

            try{
                Map<String, String> mapParams = ServletUtils.validateRequestQueryParams(request, paramsNames);
                String sourceTargetParameter = mapParams.get(Constants.SOURCE_TARGET);
                String graphNameParameter = mapParams.get(Constants.GRAPH_NAME);

                GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                Gson gson = new Gson();
                List<String> findCycle = graphsManager.findCycle(sourceTargetParameter, graphNameParameter);
                String json = gson.toJson(findCycle);
                body.print(json);
                body.flush();

            } catch(Exception e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                body.print(e.getMessage());
            }
        }
    }
}

