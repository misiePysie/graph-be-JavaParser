package ApiData;

public class ApiXmlModel {

    private String files;
    private String methods;
    private String modules;

    public ApiXmlModel(String files, String methods, String modules) {
        this.files = files;
        this.methods = methods;
        this.modules = modules;
    }

    public ApiXmlModel() {
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }
}
