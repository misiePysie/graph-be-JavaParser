package Data;

public class JavaFile {
    private String id;
    private String javaFileName;
    private int size;
    private int circleSize;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JavaFile(String id, String javaFileName, int size) {
        this.id = id;
        this.javaFileName = javaFileName;
        this.size = size;
        this.circleSize=100;
    }

    public JavaFile() {
    }

    public String getJavaFileName() {
        return javaFileName;
    }

    public void setJavaFileName(String javaFileName) {
        this.javaFileName = javaFileName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCircleSize() {
        return circleSize;
    }

    public void setCircleSize(int circleSize) {
        this.circleSize = circleSize;
    }

    @Override
    public String toString() {
        return "JavaFile{" +
                "javaFileName='" + javaFileName + '\'' +
                ", size=" + size +
                ", circleSize="+ circleSize+
                ", id="+ id+
                '}';
    }
}