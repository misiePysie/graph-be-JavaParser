package Export;

import Data.AllData;
import com.jamesmurty.utils.XMLBuilder2;
import javax.xml.transform.OutputKeys;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Properties;

public class XMLFileBuilder {

    private XMLBuilder2 builder;
    private Properties properties;
    private static Integer elementID;

    public Properties getProperties() {
        return properties;
    }

    public XMLBuilder2 getBuilder() {
        return builder;
    }

//    public XMLFileBuilder() {
//        builder = XMLBuilder2.create("Project")
//                .a("DocumentationType", "html")
//                .a("ExporterVersion", "12.2")
//                .a("Name", "untitled")
//                .a("UmlVersion", "2.x")
//                .a("Xml_structure", "simple")
//                .e("Models");
//
//
//        properties = new Properties();
//        properties.put(OutputKeys.METHOD, "xml");
//        properties.put(OutputKeys.INDENT, "yes");
//        properties.put("{http://xml.apache.org/xslt}indent-amount", "2");
//    }

    public void elementsAndAttrtoXML(){
    }

}

