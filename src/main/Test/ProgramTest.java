import ApiData.ApiData;
import Data.AllData;
import Data.DataGuide;
import Data.JavaFile;
import Export.XMLFileBuilder;
import com.jamesmurty.utils.XMLBuilder2;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Properties;


class ProgramTest {

    @Test
    void FilesNamesTest() throws IOException, ClassNotFoundException, NoSuchFieldException{
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.fileToFileConnection();
        Assert.assertFalse(allData.listOfJavaFiles.isEmpty());
    }

    @Test
    void FilesConnectionsTest() throws IOException, ClassNotFoundException,NoSuchFieldException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.fileToFileConnection();
        Assert.assertFalse(allData.listOfEdgesFile_File.isEmpty());
    }
    @Test
    void MethodFileConnectionsTest() throws IOException, ClassNotFoundException,NoSuchFieldException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.methodToFileConnection();
        Assert.assertFalse(allData.listOfEdgesMethod_File.isEmpty());
    }
    @Test
    void PackageConnectionsTest() throws IOException, ClassNotFoundException,NoSuchFieldException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.packageToPackageConnection();
        Assert.assertFalse(allData.listOfEdgesPackage_Package.isEmpty());
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
        dataGuide.methodToMethodConnection();
        Assert.assertFalse(allData.listOfEdgesMethod_Method.isEmpty());

    }


    @Test
    void ModuleConnectionsTest() throws NoSuchFieldException, IOException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.packageToPackageConnection();
        Assert.assertFalse(allData.listOfEdgesPackage_Package.isEmpty());

    }
    @Test
    void MethodsModuleConnectionsTest() throws NoSuchFieldException, IOException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.packageToPackageConnection();
        Assert.assertFalse(allData.listOfEdgesMethod_Package.isEmpty());

    }
    @Test
    void ModuleTest() throws NoSuchFieldException, IOException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.packageToPackageConnection();
        Assert.assertFalse(allData.listOfPackages.isEmpty());

    }
    @Test
    void ApiDataGetEdgesNotNull()
    {
        ApiData apiData=new ApiData();
        Assert.assertNotNull(apiData.getEdges());
    }
    @Test
    void ApiDataGetNodesNotNull()
    {
        ApiData apiData=new ApiData();
        Assert.assertNotNull(apiData.getNodes());
    }

    @Test
    void MethodModuleConnectionsTest() throws NoSuchFieldException, IOException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.packageToPackageConnection();
        Assert.assertFalse(allData.listOfEdgesMethod_Package.isEmpty());
    }

    @Test
    void MethodFileTest() throws IOException, ClassNotFoundException, NoSuchFieldException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.methodToMethodConnection();
        Assert.assertFalse(allData.listOfMethods.isEmpty());
    }
     @Test
    void FileNameIsNotNullTest() throws NoSuchFieldException, IOException, ClassNotFoundException {
         AllData allData = new AllData();
         DataGuide dataGuide = new DataGuide();
         dataGuide.findModuleDependencies(allData);
         dataGuide.fileToFileConnection();
         for (JavaFile file:allData.listOfJavaFiles
              ) {
             Assert.assertNotNull(file.getJavaFileName());
         }

     }


    @Test
    void XMLFilesTest() throws IOException, NoSuchFieldException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.fileToFileConnection();
        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();
        xmlFileBuilder.addElements(dataGuide.getFileOneFileTwoWeight());
        XMLBuilder2 builderOne = xmlFileBuilder.getBuilder();
        PrintWriter writerOne = new PrintWriter("files.xml");
        Properties propertiesOne = xmlFileBuilder.getProperties();
        builderOne.toWriter(writerOne, propertiesOne);
        Assert.assertNotNull(builderOne);
    }
    @Test
    void XMLFilesMethods() throws IOException, NoSuchFieldException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.methodToMethodConnection();
        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();
        xmlFileBuilder.addElements(dataGuide.getMethodOneMethodTwoWeight());
        XMLBuilder2 builderTwo = xmlFileBuilder.getBuilder();
        PrintWriter writerTwo = new PrintWriter("methods.xml");
        Properties propertiesTwo = xmlFileBuilder.getProperties();
        builderTwo.toWriter(writerTwo, propertiesTwo);
        Assert.assertNotNull(builderTwo);
    }
    @Test
    void XMLFilesModules() throws IOException, NoSuchFieldException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.packageToPackageConnection();
        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();
        xmlFileBuilder.addElements(dataGuide.getModuleOneModuleTwoWeight());
        XMLBuilder2 builderThree = xmlFileBuilder.getBuilder();
        PrintWriter writer = new PrintWriter("modules.xml");
        Properties propertiesThree = xmlFileBuilder.getProperties();
        builderThree.toWriter(writer, propertiesThree);
        Assert.assertNotNull(builderThree);
    }
}