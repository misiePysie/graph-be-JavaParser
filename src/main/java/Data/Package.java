package Data;

public class Package {
    private String id;
    private String packageName;
    private int circleSize;

    public Package(String id, String packageName, int size) {
        this.id = id;
        this.packageName = packageName;
        this.circleSize = size;
    }

    public Package(String packageName) {
        this.packageName = packageName;
    }


    public Package() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getCircleSize() {
        return circleSize;
    }

    public void setCircleSize(int size) {
        this.circleSize = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Package{" +
                "packageName='" + packageName + '\'' +
                ", circleSize=" + circleSize +
                '}';
    }
}