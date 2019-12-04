package Data;

public class Method {
    private String methodName;
    private int size;

    public Method(String methodName) {
        this.methodName = methodName;
        this.size=10;
    }

    public Method() {
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

    @Override
    public String toString() {
        return "Method{" +
                "methodName='" + methodName + '\'' +
                ", size=" + size +
                '}';
    }
}
