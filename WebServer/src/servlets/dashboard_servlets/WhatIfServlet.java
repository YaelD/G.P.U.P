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
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "whatIf", urlPatterns = {"/what_if"})
public class WhatIfServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        if (usernameFromSession == null) {    //user is not logged in - error!!
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            String[] paramsNames = {Constants.SOURCE_TARGET, Constants.DEPENDENCY, Constants.GRAPH_NAME};

            try (PrintWriter body = response.getWriter()){
                Map<String, String> mapParams = ServletUtils.validateRequestQueryParams(request, paramsNames);
                String sourceTargetParameter = mapParams.get(Constants.SOURCE_TARGET);
                String graphNameParameter = mapParams.get(Constants.GRAPH_NAME);
                String dependencyParameter = mapParams.get(Constants.DEPENDENCY);

                GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                Gson gson = new Gson();
                Set<String> whatIf = graphsManager.whatIf(sourceTargetParameter,
                        Dependency.valueOf(dependencyParameter.toUpperCase(Locale.ROOT)), graphNameParameter);
                String json = gson.toJson(whatIf);
                body.print(json);
                body.flush();


            } catch(Exception e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(e.getMessage());
            }
        }
    }
}
