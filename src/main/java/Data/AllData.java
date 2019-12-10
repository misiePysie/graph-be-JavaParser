package Data;

import java.util.ArrayList;

public class AllData {
    private  ArrayList<JavaFile> listOfJavaFiles;
    private  ArrayList<EdgeFile_File> listOfEdgesFile_File;
    private  ArrayList<Method> listOfMethods=new ArrayList<>();

    private ArrayList<Package> listOfPackages=new ArrayList<>();

    private  ArrayList<EdgeMethod_Package> listOfEdgesMethod_Package;
    private  ArrayList<EdgeMethod_Method> listOfEdgesMethod_Method;
    public AllData() {
    }

    public ArrayList<JavaFile> getListOfJavaFiles() {
        return listOfJavaFiles;
    }

    public void setListOfJavaFiles(ArrayList<JavaFile> listOfJavaFiles) {
        this.listOfJavaFiles = listOfJavaFiles;
    }

    public ArrayList<EdgeFile_File> getListOfEdgesFile_File() {
        return listOfEdgesFile_File;
    }

    public void setListOfEdgesFile_File(ArrayList<EdgeFile_File> listOfEdgesFile_File) {
        this.listOfEdgesFile_File = listOfEdgesFile_File;
    }

    public ArrayList<Method> getListOfMethods() {
        return listOfMethods;
    }

    public void setListOfMethods(ArrayList<Method> listOfMethods) {
        this.listOfMethods = listOfMethods;
    }

    public ArrayList<EdgeMethod_Package> getListOfEdgesMethod_Package() {
        return listOfEdgesMethod_Package;
    }

    public void setListOfEdgesMethod_Package(ArrayList<EdgeMethod_Package> listOfEdgesMethod_Package) {
        this.listOfEdgesMethod_Package = listOfEdgesMethod_Package;
    }

    public ArrayList<EdgeMethod_Method> getListOfEdgesMethod_Method() {
        return listOfEdgesMethod_Method;
    }

    public void setListOfEdgesMethod_Method(ArrayList<EdgeMethod_Method> listOfEdgesMethod_Method) {
        this.listOfEdgesMethod_Method = listOfEdgesMethod_Method;
    }

    public ArrayList<Package> getListOfPackages() {
        return listOfPackages;
    }

    public void setListOfPackages(ArrayList<Package> listOfPackages) {
        this.listOfPackages = listOfPackages;
    }
}