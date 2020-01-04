import Data.AllData;
import Data.DataGuide;
import Export.XMLFileBuilder;
import com.jamesmurty.utils.XMLBuilder2;
import javassist.NotFoundException;
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
        System.out.println(DataGuide.getClassesNames());
    }

    @Test
    void FilesConnectionsTest() throws IOException, ClassNotFoundException,NoSuchFieldException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.FilesConnections();
        System.out.println(dataGuide.getAllData().listOfEdgesFile_File);
    }

    @Test
    void methodOneMethodTwoWeightTest() throws IOException, ClassNotFoundException, NoSuchFieldException{
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.MethodConnections();
        System.out.println(DataGuide.getMethodOneMethodTwoWeight());
    }

    @Test
    void MethodConnectionsTest() throws NoSuchFieldException, IOException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.MethodConnections();
        System.out.println(dataGuide.getAllData().listOfEdgesMethod_Method);

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
        System.out.println(dataGuide.getAllData().listOfEdgesPackage_Package);

    }

    @Test
    void MethodModuleConnectionsTest() throws NoSuchFieldException, IOException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.ModuleConnections();
        dataGuide.MethodFileConnections();
        System.out.println(dataGuide.getAllData().listOfEdgesMethod_Package);
    }

    @Test
    void MethodFileTest() throws IOException, ClassNotFoundException, NoSuchFieldException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.MethodFileConnections();
    }

    @Test
    void XMLFilesTest() throws IOException, NoSuchFieldException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.FilesConnections();
        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();
        xmlFileBuilder.addElements(dataGuide.getFileOneFileTwoWeight());
        XMLBuilder2 builderOne = xmlFileBuilder.getBuilder();
        PrintWriter writerOne = new PrintWriter("files.xml");
        Properties propertiesOne = xmlFileBuilder.getProperties();
        builderOne.toWriter(writerOne, propertiesOne);
    }
    @Test
    void XMLFilesMethods() throws IOException, NoSuchFieldException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.MethodConnections();
        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();
        xmlFileBuilder.addElements(dataGuide.getMethodOneMethodTwoWeight());
        XMLBuilder2 builderTwo = xmlFileBuilder.getBuilder();
        PrintWriter writerTwo = new PrintWriter("methods.xml");
        Properties propertiesTwo = xmlFileBuilder.getProperties();
        builderTwo.toWriter(writerTwo, propertiesTwo);
    }
    @Test
    void XMLFilesModules() throws IOException, NoSuchFieldException, ClassNotFoundException {
        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.findModuleDependencies(allData);
        dataGuide.ModuleConnections();
        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();
        xmlFileBuilder.addElements(dataGuide.getModuleOneModuleTwoWeight());
        XMLBuilder2 builderThree = xmlFileBuilder.getBuilder();
        PrintWriter writer = new PrintWriter("modules.xml");
        Properties propertiesThree = xmlFileBuilder.getProperties();
        builderThree.toWriter(writer, propertiesThree);
    }
}