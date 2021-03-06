package ApiData;

import Data.EdgeFile_File;
import Data.EdgeMethod_File;
import Data.EdgeMethod_Method;
import Data.EdgeMethod_Package;

public class Edge {

    private Node from;
    private Node to;

    public Edge(Node from, Node to) {
        this.from = from;
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

    public Edge(EdgeMethod_Package.EdgePackage_Package temp){
        this.from = new Node(temp.getPackageFrom());
        this.to = new Node(temp.getPackageTo());
    }

    public Edge(EdgeMethod_File temp){
        this.from = new Node(temp.getMethod());
        this.to = new Node(temp.getFile());

    }
}