package SpringApplication;

import ApiData.ApiData;
import ApiData.DirPath;
import ApiData.Edge;
import ApiData.Node;
import Data.AllData;
import ApiData.ApiXmlModel;
import Data.DataGuide;
import Export.XMLFileBuilder;
import com.google.gson.Gson;
import com.jamesmurty.utils.XMLBuilder2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Properties;


@RestController
public class GraphApplicationController {
    AllData allData = new AllData();
    DataGuide dataSet = new DataGuide();
    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/file_file", method = RequestMethod.POST)
    public ResponseEntity dirPath(@RequestBody String path) {

        Gson gson = new Gson();
        DirPath dir = gson.fromJson(path, DirPath.class);
        try {
            dataSet.setPath(dir.getPath());
            dataSet.findModuleDependencies(allData);
            dataSet.FilesConnections();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        ArrayList<Node> tempNodes = new ArrayList<Node>();
        ArrayList<Edge> tempEdge = new ArrayList<Edge>();

        for (int i = 0; i < allData.getListOfJavaFiles().size(); i++) {
            tempNodes.add(new Node(allData.getListOfJavaFiles().get(i)));
        }
        for (int i = 0; i < allData.getListOfEdgesFile_File().size(); i++) {
            tempEdge.add(new Edge(allData.getListOfEdgesFile_File().get(i)));
        }

        ApiData temp = new ApiData(tempNodes, tempEdge);

        return new ResponseEntity(gson.toJson(temp), HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/method_method", method = RequestMethod.POST)
    public ResponseEntity methodToMethod(@RequestBody String path) {
        Gson gson = new Gson();
        DirPath dir = gson.fromJson(path, DirPath.class);
        try {
            dataSet.setPath(dir.getPath());
            dataSet.findModuleDependencies(allData);
            dataSet.MethodConnections();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }


        ArrayList<Node> tempNodes = new ArrayList<Node>();
        ArrayList<Edge> tempEdge = new ArrayList<Edge>();

        for (int i = 0; i < allData.getListOfMethods().size(); i++) {
            tempNodes.add(new Node(allData.getListOfMethods().get(i)));
        }
        for (int i = 0; i < allData.getListOfEdgesMethod_Method().size(); i++) {
            tempEdge.add(new Edge(allData.getListOfEdgesMethod_Method().get(i)));
        }

        ApiData temp = new ApiData(tempNodes, tempEdge);

        return new ResponseEntity(gson.toJson(temp), HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/method_package", method = RequestMethod.POST)
    public ResponseEntity methodToPackage(@RequestBody String path) {
        Gson gson = new Gson();
        DirPath dir = gson.fromJson(path, DirPath.class);

        try {
            dataSet.setPath(dir.getPath());
            dataSet.findModuleDependencies(allData);
            dataSet.FilesConnections();
            dataSet.MethodConnections();
            dataSet.ModuleConnections();
            dataSet.MethodFileConnections();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }


        ArrayList<Node> tempNodes = new ArrayList<Node>();
        ArrayList<Edge> tempEdge = new ArrayList<Edge>();

        for (int i = 0; i < allData.getListOfMethods().size(); i++) {
            tempNodes.add(new Node(allData.getListOfMethods().get(i)));
        }
        for (int i = 0; i < allData.getListOfPackages().size(); i++) {
            tempNodes.add(new Node(allData.getListOfPackages().get(i)));
        }
        for (int i = 0; i < allData.getListOfEdgesFile_File().size(); i++) {
            tempEdge.add(new Edge(allData.getListOfEdgesFile_File().get(i)));
        }

        ApiData temp = new ApiData(tempNodes, tempEdge);

        return new ResponseEntity(gson.toJson(temp), HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/package_package", method = RequestMethod.POST)
    public ResponseEntity packageToPackage(@RequestBody String path) {
        Gson gson = new Gson();
        DirPath dir = gson.fromJson(path, DirPath.class);
        try {
            dataSet.setPath(dir.getPath());
            dataSet.findModuleDependencies(allData);
            dataSet.ModuleConnections();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }


        ArrayList<Node> tempNodes = new ArrayList<Node>();
        ArrayList<Edge> tempEdge = new ArrayList<Edge>();

        for (int i = 0; i < allData.getListOfPackages().size(); i++) {
            tempNodes.add(new Node(allData.getListOfPackages().get(i)));
        }
        for (int i = 0; i < allData.getListOfEdgesPackage_Package().size(); i++) {
            tempEdge.add(new Edge(allData.getListOfEdgesPackage_Package().get(i)));
        }

        ApiData temp = new ApiData(tempNodes, tempEdge);

        return new ResponseEntity(gson.toJson(temp), HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/xml_export", method = RequestMethod.GET)
    public ResponseEntity xmlExport(@RequestBody String path) {
        Gson gson = new Gson();
        DirPath dir = gson.fromJson(path, DirPath.class);
        ApiXmlModel model = new ApiXmlModel();

        try {
            dataSet.findModuleDependencies(allData);
            dataSet.FilesConnections();
            dataSet.MethodConnections();
            dataSet.ModuleConnections();
            dataSet.MethodFileConnections();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();

        File files = new File("files.xml");
        File methods = new File("methods.xml");
        File modules = new File("modules.xml");

        try {
            xmlFileBuilder.addElements(dataSet.getFileOneFileTwoWeight());
            XMLBuilder2 builderOne = xmlFileBuilder.getBuilder();
            PrintWriter writerOne = new PrintWriter(files);
            Properties propertiesOne = xmlFileBuilder.getProperties();
            builderOne.toWriter(writerOne, propertiesOne);

            xmlFileBuilder.addElements(dataSet.getMethodOneMethodTwoWeight());
            XMLBuilder2 builderTwo = xmlFileBuilder.getBuilder();
            PrintWriter writerTwo = new PrintWriter(methods);
            Properties propertiesTwo = xmlFileBuilder.getProperties();
            builderTwo.toWriter(writerTwo, propertiesTwo);

            xmlFileBuilder.addElements(dataSet.getModuleOneModuleTwoWeight());
            XMLBuilder2 builderThree = xmlFileBuilder.getBuilder();
            PrintWriter writer = new PrintWriter(modules);
            Properties propertiesThree = xmlFileBuilder.getProperties();
            builderThree.toWriter(writer, propertiesThree);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }


        DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        Document filesXml;
        Document methodsXml;
        Document modulesXml;

        try {
            builder = documentBuilder.newDocumentBuilder();

            filesXml = builder.parse(files);
            methodsXml = builder.parse(methods);
            modulesXml = builder.parse(methods);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer;
        StringWriter writer = new StringWriter();

        try {
            transformer = factory.newTransformer();

            transformer.transform(new DOMSource(filesXml), new StreamResult(writer));
            model.setFiles(writer.toString());

            writer.flush();

            transformer.transform(new DOMSource(methodsXml), new StreamResult(writer));
            model.setMethods(writer.toString());

            writer.flush();

            transformer.transform(new DOMSource(modulesXml), new StreamResult(writer));
            model.setModules(writer.toString());

            writer.flush();


        } catch (TransformerException e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(gson.toJson(model), HttpStatus.OK);

    }

}

