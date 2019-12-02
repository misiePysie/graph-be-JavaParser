package Data;

public class EdgePackage_Package {
    private Package packageFrom;
    private Package packageTo;
    private int weight;

    public EdgePackage_Package(Package packageFrom, Package packageTo) {
        this.packageFrom = packageFrom;
        this.packageTo = packageTo;
    }

    public EdgePackage_Package(Package packageFrom, Package packageTo, int weight) {
        this.packageFrom = packageFrom;
        this.packageTo = packageTo;
        this.weight = weight;
    }

    public Package getPackageFrom() {
        return packageFrom;
    }

    public void setPackageFrom(Package packageFrom) {
        this.packageFrom = packageFrom;
    }

    public Package getPackageTo() {
        return packageTo;
    }

    public void setPackageTo(Package packageTo) {
        this.packageTo = packageTo;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "EdgePackage_Package{" +
                "packageFrom=" + packageFrom +
                ", packageTo=" + packageTo +
                ", weight=" + weight +
                '}';
    }
}
