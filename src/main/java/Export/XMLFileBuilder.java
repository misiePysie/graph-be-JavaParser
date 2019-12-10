package Export;

import com.jamesmurty.utils.XMLBuilder2;

import javax.xml.transform.OutputKeys;
import java.util.Properties;

public class XmlBuilder {

    private XMLBuilder2 builder;
    private Properties properties;
    private static Integer elementID;

    public XmlBuilder() {
        builder = XMLBuilder2.create("Project")
                .a("DocumentationType", "html")
                .a("ExporterVersion", "12.2")
                .a("Name", "untitled")
                .a("UmlVersion", "2.x")
                .a("Xml_structure", "simple")
                .e("Models");
        properties = new Properties();
        properties.put(OutputKeys.METHOD, "xml");
        properties.put(OutputKeys.INDENT, "yes");
        properties.put("{http://xml.apache.org/xslt}indent-amount", "2");
    }
}

