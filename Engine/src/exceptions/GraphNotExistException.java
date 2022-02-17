package exceptions;

public class GraphNotExistException extends Exception{

    private String graphName;

    public GraphNotExistException(String graphName) {
        this.graphName = graphName;
    }

    public String getName() {
        return graphName;
    }
}
