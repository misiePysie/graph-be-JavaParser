package ApiData;

import Data.JavaFile;
import Data.Method;
import Data.Package;

public class Node {
    private String label;
    private int size;
    private double fileSize;

    public Node(String label, int size, double fileSize) {
        this.label = label;
        this.size = size;
        this.fileSize = fileSize;
    }

    public Node(JavaFile temp){
        this.label = temp.getJavaFileName();
        this.size = temp.getSize();
        this.fileSize = temp.getCircleSize();
    }

    public Node(Method temp){
        this.label = temp.getMethodName();
        this.size = temp.getWeight();
        this.fileSize = temp.getCircleSize();
    }

    public Node(Package temp){
        this.label=temp.getPackageName();
        this.size = 1;
        this.fileSize = temp.getCircleSize();
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }
}