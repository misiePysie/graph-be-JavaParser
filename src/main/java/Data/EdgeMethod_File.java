package Data;

public class EdgeMethod_File {
    private Method method;
    private JavaFile file;


    public EdgeMethod_File(Method method, JavaFile file) {
        this.method = method;
        this.file = file;
    }

    public EdgeMethod_File() {

    }

    public JavaFile getFile() {
        return file;
    }

    public void setFile(JavaFile file) {
        this.file = file;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "EdgeMethod_File{" +
                "method=" + method +
                ", file=" + file +
                '}';
    }
}
