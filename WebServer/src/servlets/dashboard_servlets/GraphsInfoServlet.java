package servlets.dashboard_servlets;

import com.google.gson.Gson;
import dto.GraphDTO;
import engine.Engine;
import engine.GraphsManager;
import graph.Graph;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "GraphInfo", urlPatterns = {"/graphs_list"})
public class GraphsInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<GraphDTO> graphsToSend = new ArrayList<>();
        GraphsManager graphsManager = ServletUtils.getGraphsManager(getServletContext());
        Map<String, Graph> graphsInSystem = graphsManager.getGraphsInSystem();
        Collection<Graph> graphsList = graphsInSystem.values();
        for(Graph graph: graphsList){
            GraphDTO graphDTO = graph.makeDTO();
            graphsToSend.add(graphDTO);
        }

        Gson gson = new Gson();
        String json = gson.toJson(graphsToSend);

        PrintWriter body = resp.getWriter();
        body.print(json);
        body.flush();
    }
}
