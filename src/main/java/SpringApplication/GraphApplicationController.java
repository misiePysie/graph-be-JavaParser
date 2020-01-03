package SpringApplication;

import ApiData.ApiData;
import ApiData.DirPath;
import ApiData.Edge;
import ApiData.Node;
import Data.AllData;
import ApiData.ApiXmlModel;
import Data.DataGuide;
import Export.XMLFileBuilder;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.jamesmurty.utils.XMLBuilder2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import javax.servlet.ServletContext;

import org.springframework.http.MediaType;


@RestController
public class GraphApplicationController {


    @Autowired
    private ServletContext servletContext;

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/file_file", method = RequestMethod.POST)
    public ResponseEntity dirPath(@RequestBody String path) {
        Gson gson = new Gson();
        DirPath dir = gson.fromJson(path, DirPath.class);

        DataGuide dataSet = new DataGuide();
        AllData allData = new AllData();
        try {
            dataSet.setPath(dir.getPath());
            dataSet.findModuleDependencies(allData);
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

        DataGuide dataSet = new DataGuide();
        AllData allData = new AllData();
        try {
            dataSet.setPath(dir.getPath());
            dataSet.findModuleDependencies(allData);
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
        DataGuide dataSet = new DataGuide();
        AllData allData = new AllData();

        try {
            dataSet.setPath(dir.getPath());
            dataSet.findModuleDependencies(allData);
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
        DataGuide dataSet = new DataGuide();
        AllData allData = new AllData();
        try {
            dataSet.setPath(dir.getPath());
            dataSet.findModuleDependencies(allData);
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
    @RequestMapping(path = "/xml_files", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> xmlFilesDownload(@RequestBody String path) {
        Gson gson = new Gson();
        DirPath dir = gson.fromJson(path, DirPath.class);

        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.setPath(dir.getPath());

        try {
            dataGuide.findModuleDependencies(allData);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();

        File files = new File("files.xml");

        try {
            xmlFileBuilder.addElements(dataGuide.getFileOneFileTwoWeight());
            XMLBuilder2 builderOne = xmlFileBuilder.getBuilder();
            PrintWriter writerOne = new PrintWriter(files);
            Properties propertiesOne = xmlFileBuilder.getProperties();
            builderOne.toWriter(writerOne, propertiesOne);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        InputStreamResource resource;

        try {
            resource = new InputStreamResource(new FileInputStream(files));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=files.xml")
                .contentLength(files.length())
                .body(resource);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/xml_methods", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> xmlMethodsDownload(@RequestBody String path) {
        Gson gson = new Gson();
        DirPath dir = gson.fromJson(path, DirPath.class);

        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.setPath(dir.getPath());

        try {
            dataGuide.findModuleDependencies(allData);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();

        File methods = new File("methods.xml");

        try {
            xmlFileBuilder.addElements(dataGuide.getMethodOneMethodTwoWeight());
            XMLBuilder2 builderTwo = xmlFileBuilder.getBuilder();
            PrintWriter writerTwo = new PrintWriter(methods);
            Properties propertiesTwo = xmlFileBuilder.getProperties();
            builderTwo.toWriter(writerTwo, propertiesTwo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        InputStreamResource resource;

        try {
            resource = new InputStreamResource(new FileInputStream(methods));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=files.xml")
                .contentLength(methods.length())
                .body(resource);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/xml_modules", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> xmlModulesDownload(@RequestBody String path) {
        Gson gson = new Gson();
        DirPath dir = gson.fromJson(path, DirPath.class);

        AllData allData = new AllData();
        DataGuide dataGuide = new DataGuide();
        dataGuide.setPath(dir.getPath());

        try {
            dataGuide.findModuleDependencies(allData);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();

        File modules = new File("modules.xml");

        try {
            xmlFileBuilder.addElements(dataGuide.getModuleOneModuleTwoWeight());
            XMLBuilder2 builderThree = xmlFileBuilder.getBuilder();
            PrintWriter writer = new PrintWriter(modules);
            Properties propertiesThree = xmlFileBuilder.getProperties();
            builderThree.toWriter(writer, propertiesThree);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        InputStreamResource resource;

        try {
            resource = new InputStreamResource(new FileInputStream(modules));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=files.xml")
                .contentLength(modules.length())
                .body(resource);
    }

}

