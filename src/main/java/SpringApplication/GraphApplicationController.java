package SpringApplication;

import ApiData.ApiData;
import ApiData.DirPath;
import ApiData.Edge;
import ApiData.Node;
import Data.AllData;
import Data.DataGuide;
import Export.XMLFileBuilder;
import com.google.gson.Gson;
import com.jamesmurty.utils.XMLBuilder2;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

@Controller
@RestController
public class GraphApplicationController {
    AllData allData = new AllData();
    DataGuide dataSet = new DataGuide();

    public GraphApplicationController() {
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path= "/path", method = RequestMethod.POST)
    public ResponseEntity setPath(@RequestBody String path){

        Gson gson = new Gson();
        try {
            DirPath dir = gson.fromJson(path, DirPath.class);

            if(!dir.getPath().endsWith("java")) {
               throw new IllegalArgumentException();
            }
            dataSet.setPath(dir.getPath());
            dataSet.findModuleDependencies(allData);
            dataSet.fileToFileConnection();
            dataSet.methodToMethodConnection();
            dataSet.packageToPackageConnection();
            dataSet.methodToFileConnection();
        } catch(IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }



        return new ResponseEntity(null, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/file_file", method = RequestMethod.GET)
    public ResponseEntity fileToFile() {
        Gson gson = new Gson();

        ArrayList<Node> tempNodes = new ArrayList<Node>();
        ArrayList<Edge> tempEdge = new ArrayList<Edge>();

        if(allData.getListOfJavaFiles().size()==0){
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

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
    @RequestMapping(path = "/method_method", method = RequestMethod.GET)
    public ResponseEntity methodToMethod() {

        Gson gson = new Gson();

        ArrayList<Node> tempNodes = new ArrayList<Node>();
        ArrayList<Edge> tempEdge = new ArrayList<Edge>();

        if(allData.getListOfMethods().size()==0){
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

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
    @RequestMapping(path = "/method_package", method = RequestMethod.GET)
    public ResponseEntity methodToPackage() {

        Gson gson = new Gson();

        ArrayList<Node> tempNodes = new ArrayList<Node>();
        ArrayList<Edge> tempEdge = new ArrayList<Edge>();

        if(allData.getListOfMethods().size() == 0){
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        for (int i = 0; i < allData.getListOfMethods().size(); i++) {
            tempNodes.add(new Node(allData.getListOfMethods().get(i)));
        }
        for (int i = 0; i < allData.getListOfPackages().size(); i++) {
            tempNodes.add(new Node(allData.getListOfPackages().get(i)));
        }
        for (int i = 0; i < allData.getListOfEdgesMethod_Package().size(); i++) {
            tempEdge.add(new Edge(allData.getListOfEdgesMethod_Package().get(i)));
        }

        ApiData temp = new ApiData(tempNodes, tempEdge);

        return new ResponseEntity(gson.toJson(temp), HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/package_package", method = RequestMethod.GET)
    public ResponseEntity packageToPackage() {
        Gson gson = new Gson();

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
    @RequestMapping(path="/file_method", method = RequestMethod.GET)
    public ResponseEntity fileToMethod(){
        Gson gson = new Gson();

        ArrayList<Edge> tempEdges = new ArrayList<>();
        ArrayList<Node> tempNodes = new ArrayList<>();

        if(allData.getListOfMethods().size() == 0 || allData.getListOfEdgesMethod_File().size() ==0 ){
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        for(int i=0;i<allData.getListOfJavaFiles().size();i++){
            tempNodes.add(new Node(allData.getListOfJavaFiles().get(i)));
        }

        for(int i = 0;i<allData.getListOfMethods().size();i++){
            tempNodes.add(new Node(allData.getListOfMethods().get(i)));
        }

        for (int i = 0; i<allData.getListOfEdgesMethod_File().size();i++){
            tempEdges.add(new Edge(allData.getListOfEdgesMethod_File().get(i)));
        }

        ApiData response = new ApiData(tempNodes, tempEdges);

        return new ResponseEntity(gson.toJson(response), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/export_files", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> xmlFilesDownload() {

        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();

        File files = new File("files.xml");

        try {
            xmlFileBuilder.addElements(dataSet.getFileOneFileTwoWeight());
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

        return new ResponseEntity<>(resource,HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/export_methods", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> xmlMethodsDownload() {

        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();

        File methods = new File("methods.xml");

        try {
            xmlFileBuilder.addElements(dataSet.getMethodOneMethodTwoWeight());
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

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path = "/export_modules", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> xmlModulesDownload() {

        XMLFileBuilder xmlFileBuilder = new XMLFileBuilder();

        File modules = new File("modules.xml");

        try {
            xmlFileBuilder.addElements(dataSet.getModuleOneModuleTwoWeight());
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

        return new ResponseEntity(resource, HttpStatus.OK);
    }



}

