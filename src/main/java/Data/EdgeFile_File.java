package Data;

public class EdgeFile_File {
    private JavaFile fileFrom;
    private JavaFile fileTo;
    private int weight;

    public EdgeFile_File(JavaFile fileFrom, JavaFile fileTo,int weight) {
        this.fileFrom = fileFrom;
        this.fileTo = fileTo;
        this.weight=weight;
    }

    public JavaFile getFileFrom() {
        return fileFrom;
    }

    public void setFileFrom(JavaFile fileFrom) {
        this.fileFrom = fileFrom;
    }

    public JavaFile getFileTo() {
        return fileTo;
    }

    public void setFileTo(JavaFile fileTo) {
        this.fileTo = fileTo;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "EdgeFile_File{" +
                "fileFrom=" + fileFrom +
                ", fileTo=" + fileTo +
                ", weight=" + weight +
                '}';
    }
}
