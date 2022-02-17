package servlets.dashboard_servlets;

import com.google.gson.Gson;
import constants.Constants;
import engine.Engine;
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
import java.util.Set;

@WebServlet(name = "whatIf", urlPatterns = {"/what_if"})
public class WhatIfServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        if(usernameFromSession == null){    //user is not logged in - error!!
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else{
            String sourceTargetParameter = request.getParameter(Constants.SOURCE_TARGET);
            String graphNameParameter = request.getParameter(Constants.GRAPH_NAME);
            String dependencyParameter = request.getParameter(Constants.DEPENDENCY);
            Engine engine = ServletUtils.getEngine(getServletContext());

            //if it's a valid request
            if(requestValidation(sourceTargetParameter, graphNameParameter, dependencyParameter)){

                response.setContentType("application/json");
                try (PrintWriter body = response.getWriter()) {
                    Gson gson = new Gson();
                    Set<String> whatIf = engine.whatIf(sourceTargetParameter.trim(),
                            Dependency.valueOf(dependencyParameter.trim()), graphNameParameter.trim());
                    String json = gson.toJson(whatIf);
                    body.print(json);
                    body.flush();
                    response.setStatus(HttpServletResponse.SC_OK); // Is that needed?
                } catch (TargetNotExistException e) {
                    e.printStackTrace(); //TODO: CHANGE IT
                } catch (GraphNotExistException e) {
                    e.printStackTrace(); //TODO: CHANGE IT
                } catch (InvalidDependencyException e) {
                    e.printStackTrace();  //TODO: CHANGE IT
                }
            }
            else{
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }


    private boolean requestValidation(String sourceTargetParameter, String graphNameParameter, String dependencyParameter) {
        boolean isValidRequest = true;

        if (sourceTargetParameter == null || sourceTargetParameter.isEmpty()) {
            isValidRequest = false;
        }
        if (graphNameParameter == null || graphNameParameter.isEmpty()) {
            isValidRequest = false;
        }
        if (dependencyParameter == null || dependencyParameter.isEmpty()) {
            isValidRequest = false;
        }
        return isValidRequest;
    }
}
