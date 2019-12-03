package Data;

public class JavaFile {
    private String javaFileName;
    private int size;

    public JavaFile(String javaFileName, int size) {
        this.javaFileName = javaFileName;
        this.size = size;
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

    @Override
    public String toString() {
        return "JavaFile{" +
                "javaFileName='" + javaFileName + '\'' +
                ", size=" + size +
                '}';
    }
}
