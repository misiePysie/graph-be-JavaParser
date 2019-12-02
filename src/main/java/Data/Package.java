package Data;

public class Package {
    private String packageName;
    private int size;

    public Package(String packageName, int size) {
        this.packageName = packageName;
        this.size = size;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Package{" +
                "packageName='" + packageName + '\'' +
                ", size=" + size +
                '}';
    }
}
