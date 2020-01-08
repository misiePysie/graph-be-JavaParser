package Data;

public class Method {
    private String methodName;
    private int circleSize;
    private int weight;

    public Method(String methodName) {
        this.methodName = methodName;
        this.circleSize =10;
    }

    public Method() {
    }

    public Method(String methodName, int weight) {
        this.methodName = methodName;
        this.weight = weight;
        this.circleSize =10;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getCircleSize() {
        return circleSize;
    }

    public void setCircleSize(int circleSize) {
        this.circleSize = circleSize;
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
                ", circleSize=" + circleSize +
                '}';
    }
}