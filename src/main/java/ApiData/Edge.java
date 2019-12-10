package ApiData;

import Data.EdgeFile_File;
import Data.EdgeMethod_Method;
import Data.EdgeMethod_Package;
import Data.EdgePackage_Package;

public class Edge {

    private Node from;
    private Node to;

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public Edge(EdgeFile_File temp){
        this.from = new Node(temp.getFileFrom());
        this.to = new Node(temp.getFileTo());
    }

    public Edge (EdgeMethod_Method temp){
        this.from = new Node(temp.getMethodFrom());
        this.to = new Node(temp.getMethodTo());
    }

    public Edge(EdgeMethod_Package temp){
        this.from = new Node(temp.getMethodFrom());
        this.to = new Node(temp.getPackageTo());
    }

    public Edge(EdgePackage_Package temp){
        this.from = new Node(temp.getPackageFrom());
        this.to = new Node(temp.getPackageTo());
    }
}
