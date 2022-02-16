package servlets.dashboard_servlets;

import engine.Engine;
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

        Engine engine = ServletUtils.getEngine(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);

        if(usernameFromSession != null){
            Collection<Part> parts = request.getParts();
            for(Part part :parts){
                try {
                    engine.loadFile(part.getInputStream(), usernameFromSession);
                } catch (DependencyConflictException e) {
                    e.printStackTrace();
                } catch (DuplicateTargetsException e) {
                    e.printStackTrace();
                } catch (InvalidDependencyException e) {
                    e.printStackTrace();
                } catch (TargetNotExistException e) {
                    e.printStackTrace();
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
