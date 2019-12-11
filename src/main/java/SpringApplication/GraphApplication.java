package SpringApplication;
import Export.XMLFileBuilder;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.jamesmurty.utils.XMLBuilder2;
import Data.AllData;
import Data.DataGuide;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

@SpringBootApplication
public class GraphApplication {
    public static void main (String args[]) throws IOException, NoSuchFieldException, ClassNotFoundException, UnsolvedSymbolException {
       // SpringApplication.run(GraphApplication.class, args);
        DataGuide obj = new DataGuide();
        obj.findModuleDependencies();

//        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();
//        xmlFileBuilder.elementsAndAttrtoXML();
//        XMLBuilder2 builder = xmlFileBuilder.getBuilder();
//        PrintWriter writer = new PrintWriter("files.xml");
//        Properties properties = xmlFileBuilder.getProperties();
//        builder.toWriter(writer, properties);

    }
}
