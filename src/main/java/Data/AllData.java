package Data;

import org.eclipse.jgit.lib.Ref;

import java.util.ArrayList;

public class AllData {
    public  ArrayList<JavaFile> listOfJavaFiles;
    public  ArrayList<EdgeFile_File> listOfEdgesFile_File;
    public  ArrayList<Method> listOfMethods=new ArrayList<>();

    public ArrayList<Package> listOfPackages=new ArrayList<>();

    public  ArrayList<EdgeMethod_Package> listOfEdgesMethod_Package;
    public  ArrayList<EdgeMethod_Method> listOfEdgesMethod_Method;

    public ArrayList<EdgeMethod_Package.EdgePackage_Package> listOfEdgesPackage_Package;

    public ArrayList<EdgeMethod_File> listOfEdgesMethod_File = new ArrayList<>();


    public ArrayList<EdgeMethod_File> getListOfEdgesMethod_File() {
        return listOfEdgesMethod_File;
    }

    public void setListOfEdgesMethod_File(ArrayList<EdgeMethod_File> listOfEdgesMethod_File) {
        this.listOfEdgesMethod_File = listOfEdgesMethod_File;
    }

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

    public ArrayList<EdgeMethod_Package.EdgePackage_Package> getListOfEdgesPackage_Package() {
        return listOfEdgesPackage_Package;
    }

    public void setListOfEdgesPackage_Package(ArrayList<EdgeMethod_Package.EdgePackage_Package> listOfEdgesPackage_Package) {
        this.listOfEdgesPackage_Package = listOfEdgesPackage_Package;
    }



}