package Data;

public class Method {
    private String methodName;
    private int size;
    private int weight;

    public Method(String methodName) {
        this.methodName = methodName;
        this.size=10;
    }

    public Method() {
    }

    public Method(String methodName, int weight) {
        this.methodName = methodName;
        this.weight = weight;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Method{" +
                "methodName='" + methodName + '\'' +
                ", size=" + size +
                ", weight=" + weight +
                '}';
    }
}
