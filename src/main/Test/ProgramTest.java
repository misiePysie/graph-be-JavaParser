import Data.AllData;
import Data.DataGuide;
import Data.JavaFile;
import Export.XMLFileBuilder;
import com.jamesmurty.utils.XMLBuilder2;
import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

class ProgramTest {


    @Test
    void FilesNamesTest() throws IOException, ClassNotFoundException, NoSuchFieldException{
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.FilesConnections();
        Assert.assertFalse(allData.listOfJavaFiles.isEmpty());
    }

    @Test
    void FilesConnectionsTest() throws IOException, ClassNotFoundException,NoSuchFieldException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.FilesConnections();
        Assert.assertFalse(allData.listOfEdgesFile_File.isEmpty());
    }

    //todo: co ma sprawdzac ten test?
//    @Test
//    void methodOneMethodTwoWeightTest() throws IOException, ClassNotFoundException, NoSuchFieldException{
//        AllData allData = new AllData();
//        DataGuide dataGuide = new DataGuide();
//        dataGuide.findModuleDependencies(allData);
//        dataGuide.MethodConnections();
//        System.out.println(DataGuide.getMethodOneMethodTwoWeight());
//    }

    @Test
    void MethodConnectionsTest() throws NoSuchFieldException, IOException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.MethodConnections();
        Assert.assertFalse(allData.listOfEdgesMethod_Method.isEmpty());

    }

    @Test
    void moduleOneModuleTwoWeightTest() throws IOException, ClassNotFoundException, NoSuchFieldException{
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.ModuleConnections();
        System.out.println(DataGuide.getModuleOneModuleTwoWeight());
    }

    @Test
    void ModuleConnectionsTest() throws NoSuchFieldException, IOException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.ModuleConnections();
        Assert.assertFalse(allData.listOfEdgesPackage_Package.isEmpty());

    }

    @Test
    void MethodModuleConnectionsTest() throws NoSuchFieldException, IOException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.ModuleConnections();
        Assert.assertFalse(allData.listOfEdgesMethod_Package.isEmpty());
    }

    @Test
    void MethodFileTest() throws IOException, ClassNotFoundException, NoSuchFieldException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.MethodConnections();
        Assert.assertFalse(allData.listOfMethods.isEmpty());
    }
     @Test
    void FileNameIsNotNullTest() throws NoSuchFieldException, IOException, ClassNotFoundException {
         AllData allData = new AllData();
         DataGuide dataGuide = new DataGuide();
         dataGuide.findModuleDependencies(allData);
         dataGuide.FilesConnections();
         for (JavaFile file:allData.listOfJavaFiles
              ) {
             Assert.assertNotNull(file.getJavaFileName());
         }

     }

//    @Test
//    void XMLFilesTest() throws IOException, NoSuchFieldException, ClassNotFoundException {
//        AllData allData = new AllData();
//        DataGuide dataGuide = new DataGuide();
//        dataGuide.findModuleDependencies(allData);
//        dataGuide.FilesConnections();
//        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();
//        xmlFileBuilder.addElements(dataGuide.getFileOneFileTwoWeight());
//        XMLBuilder2 builderOne = xmlFileBuilder.getBuilder();
//        PrintWriter writerOne = new PrintWriter("files.xml");
//        Properties propertiesOne = xmlFileBuilder.getProperties();
//        builderOne.toWriter(writerOne, propertiesOne);
//    }
//    @Test
//    void XMLFilesMethods() throws IOException, NoSuchFieldException, ClassNotFoundException {
//        AllData allData = new AllData();
//        DataGuide dataGuide = new DataGuide();
//        dataGuide.findModuleDependencies(allData);
//        dataGuide.MethodConnections();
//        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();
//        xmlFileBuilder.addElements(dataGuide.getMethodOneMethodTwoWeight());
//        XMLBuilder2 builderTwo = xmlFileBuilder.getBuilder();
//        PrintWriter writerTwo = new PrintWriter("methods.xml");
//        Properties propertiesTwo = xmlFileBuilder.getProperties();
//        builderTwo.toWriter(writerTwo, propertiesTwo);
//    }
//    @Test
//    void XMLFilesModules() throws IOException, NoSuchFieldException, ClassNotFoundException {
//        AllData allData = new AllData();
//        DataGuide dataGuide = new DataGuide();
//        dataGuide.findModuleDependencies(allData);
//        dataGuide.ModuleConnections();
//        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();
//        xmlFileBuilder.addElements(dataGuide.getModuleOneModuleTwoWeight());
//        XMLBuilder2 builderThree = xmlFileBuilder.getBuilder();
//        PrintWriter writer = new PrintWriter("modules.xml");
//        Properties propertiesThree = xmlFileBuilder.getProperties();
//        builderThree.toWriter(writer, propertiesThree);
//    }
}