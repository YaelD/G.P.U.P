package servlets.dashboard_servlets;

import engine.Engine;
import engine.GraphsManager;
import exceptions.DependencyConflictException;
import exceptions.DuplicateTargetsException;
import exceptions.InvalidDependencyException;
import exceptions.TargetNotExistException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Collection;

@WebServlet(name = "UploadFile", urlPatterns = {"/admin/upload_file"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        if(usernameFromSession != null){
            Collection<Part> parts = request.getParts();
            for(Part part :parts){
                try {
                    graphsManager.loadFile(part.getInputStream(), usernameFromSession);
                } catch(Exception e){
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println(e.getMessage());
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
