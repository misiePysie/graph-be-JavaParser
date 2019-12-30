package Export;
import Data.AllData;
import com.jamesmurty.utils.XMLBuilder2;
import javax.xml.transform.OutputKeys;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class XMLFileBuilder {

    private XMLBuilder2 builder;
    private Properties properties;
    private static Integer elementID=1;

    public Properties getProperties() {
        return properties;
    }

    public XMLBuilder2 getBuilder() {
        return builder;
    }

    public XMLFileBuilder() {
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

    public void addElements(HashMap<String, HashMap<String, Integer>> structure) {

        //Loop for creating ID for every class on graph
        HashMap<String, Integer> tmpIDMap = new HashMap();
        for (Map.Entry<String, HashMap<String,Integer>> entry : structure.entrySet()) {
            if(!tmpIDMap.containsKey(entry.getKey())){
                tmpIDMap.put(entry.getKey(),elementID);
                elementID++;
            }
            for (Map.Entry<String, Integer> entry2: entry.getValue().entrySet()) {
                if(tmpIDMap.containsKey(entry2.getKey())){
                    continue;
                }
                else{
                    tmpIDMap.put(entry2.getKey(), elementID);
                    elementID++;
                }
            }
        }
        //System.out.println(tmpIDMap);


        for ( Map.Entry<String,Integer> entry : tmpIDMap.entrySet()) {
                builder.e("Class")
                        .a("Id", Integer.toString(tmpIDMap.get(entry.getKey())))
                        .a("Name", entry.getKey())
                        .up();

        }

        for (Map.Entry<String, HashMap<String,Integer>> entry : structure.entrySet()) {
            for (Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
                //System.out.println("from " + entry.getKey() + "    to  " + entry2.getKey());
                builder.e("Usage")
                        .a("From", Integer.toString(tmpIDMap.get(entry.getKey())))
                        .a("Id", Integer.toString(elementID))
                        .a("To", Integer.toString(tmpIDMap.get(entry2.getKey())))
                        .up();
                elementID++;
            }
        }


    }

}

