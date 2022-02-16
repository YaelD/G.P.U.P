package servlets.dashboard_servlets;

import dto.TargetDTO;
import engine.Engine;
import graph.Graph;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import target.Target;
import utils.ServletUtils;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "GraphInfo", urlPatterns = {"/graph"})
public class GraphsInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Engine engine = ServletUtils.getEngine(getServletContext());
        Map<String, Graph> graphsInSystem = engine.getGraphsInSystem();
        Collection<Graph> graphsList = graphsInSystem.values();
        for(Graph graph: graphsList){
            Collection<Target> targets = graph.getTargets();
            Map<String, TargetDTO> targetsDTOMap = new HashMap<>();
            for(Target target: targets){
                Set<String> requiredFor = new HashSet<>();
                for(Target currTarget: target.getRequiredFor()){
                    requiredFor.add(currTarget.getName());
                }
                Set<String> dependsOn = new HashSet<>();
                for(Target currTarget: target.getDependsOn()){
                    dependsOn.add(currTarget.getName());
                }
                Set<String> totalRequiredFor = new HashSet<>();
                target.getRequiredForAncestors(totalRequiredFor);
                Set<String> totalDependsOn = new HashSet<>();
                target.getDependsOnAncestors(totalDependsOn);
                TargetDTO currTargetDTO =
                        new TargetDTO(target.getName(), target.getPlace(), target.getInfo(),
                                requiredFor,dependsOn, totalRequiredFor, totalDependsOn );
            }
        }



    }
}
