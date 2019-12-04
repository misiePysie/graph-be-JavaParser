package Data;


public class EdgeMethod_Method {

    private Method methodFrom;
    private Method methodTo;
    private int weight;

    public EdgeMethod_Method(Method methodFrom, Method methodTo, int weight) {
        this.methodFrom = methodFrom;
        this.methodTo = methodTo;
        this.weight = weight;
    }

    public Method getMethodFrom() {
        return methodFrom;
    }

    public void setMethodFrom(Method methodFrom) {
        this.methodFrom = methodFrom;
    }

    public Method getMethodTo() {
        return methodTo;
    }

    public void setMethodTo(Method methodTo) {
        this.methodTo = methodTo;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "EdgeMethod_Method{" +
                "methodFrom=" + methodFrom +
                ", methodTo=" + methodTo +
                "}";
    }
}
