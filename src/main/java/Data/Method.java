package Data;

public class Method {
    private String id;
    private String methodName;
    private int circleSize;
    private int weight;

    public Method(String id, String methodName) {
        this.id = id;
        this.methodName = methodName;
        this.circleSize =10;
    }

    public Method() {
    }

    public Method(String id, String methodName, int weight) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Method{" +
                "id="+ id+'\''+
                "methodName='" + methodName + '\'' +
                ", circleSize=" + circleSize +
                '}';
    }
}