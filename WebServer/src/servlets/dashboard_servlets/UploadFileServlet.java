package servlets.dashboard_servlets;

import engine.GraphsManager;
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

        try{
            if(usernameFromSession != null){
                Collection<Part> parts = request.getParts();
                for(Part part :parts){
                    graphsManager.loadFile(part.getInputStream(), usernameFromSession);
                }
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        catch(Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(e.getMessage());
        }
    }
}
