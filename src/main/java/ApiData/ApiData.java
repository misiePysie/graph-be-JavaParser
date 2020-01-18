package ApiData;

import java.util.ArrayList;

public class ApiData {
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private ArrayList<Edge> edges = new ArrayList<Edge>();

    public ApiData(ArrayList<Node> nodes, ArrayList<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public ApiData() {
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }


    public ArrayList<Edge> getEdges() {
        return edges;
    }

}
