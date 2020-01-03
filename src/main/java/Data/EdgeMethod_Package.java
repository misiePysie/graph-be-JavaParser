package Data;

public class EdgeMethod_Package {
    private Method methodFrom;
    private Package packageTo;
    private int weight;

    public EdgeMethod_Package(Method methodFrom, Package packageTo) {
        this.methodFrom = methodFrom;
        this.packageTo = packageTo;
    }

    public EdgeMethod_Package() {
    }

    public EdgeMethod_Package(Method methodFrom, Package packageTo, int weight) {
        this.methodFrom = methodFrom;
        this.packageTo = packageTo;
        this.weight = weight;
    }

    public Method getMethodFrom() {
        return methodFrom;
    }

    public void setMethodFrom(Method methodFrom) {
        this.methodFrom = methodFrom;
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
        return "EdgeMethod_Package{" +
                "methodFrom=" + methodFrom +
                ", packageTo=" + packageTo +
                ", weight=" + weight +
                '}';
    }

    public static class EdgePackage_Package {
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

        public EdgePackage_Package() {
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
}

