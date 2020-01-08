package SpringApplication;
//import Export.XMLFileBuilder;
import Export.XMLFileBuilder;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import Data.AllData;
import Data.DataGuide;
import com.jamesmurty.utils.XMLBuilder2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;


@SpringBootApplication
public class GraphApplication {

    public static void main (String args[]) throws IOException, NoSuchFieldException, ClassNotFoundException, UnsolvedSymbolException {
        SpringApplication.run(GraphApplication.class, args);
//        AllData allData = new AllData();
//        DataGuide dataGuide = new DataGuide();
//        dataGuide.findModuleDependencies(allData);

//        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();
//        xmlFileBuilder.addElements(dataGuide.getFileOneFileTwoWeight());
//        XMLBuilder2 builderOne = xmlFileBuilder.getBuilder();
//        PrintWriter writerOne = new PrintWriter("files.xml");
//        Properties propertiesOne = xmlFileBuilder.getProperties();
//        builderOne.toWriter(writerOne, propertiesOne);
//
//        xmlFileBuilder.addElements(dataGuide.getMethodOneMethodTwoWeight());
//        XMLBuilder2 builderTwo = xmlFileBuilder.getBuilder();
//        PrintWriter writerTwo = new PrintWriter("methods.xml");
//        Properties propertiesTwo = xmlFileBuilder.getProperties();
//        builderTwo.toWriter(writerTwo, propertiesTwo);
//
//        xmlFileBuilder.addElements(dataGuide.getModuleOneModuleTwoWeight());
//        XMLBuilder2 builderThree = xmlFileBuilder.getBuilder();
//        PrintWriter writer = new PrintWriter("modules.xml");
//        Properties propertiesThree = xmlFileBuilder.getProperties();
//        builderThree.toWriter(writer, propertiesThree);
    }
}