package SpringApplication;

import ApiData.ApiData;
import ApiData.DirPath;
import ApiData.Edge;
import ApiData.Node;
import Data.AllData;
import Data.DataGuide;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;



@RestController
public class GraphApplicationController {

@CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path="/file_file", method = RequestMethod.POST)
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

    for(int i=0;i<allData.getListOfJavaFiles().size();i++){
        tempNodes.add(new Node(allData.getListOfJavaFiles().get(i)));
    }
    for(int i =0;i<allData.getListOfEdgesFile_File().size();i++){
        tempEdge.add(new Edge(allData.getListOfEdgesFile_File().get(i)));
    }

    ApiData temp = new ApiData(tempNodes,tempEdge);

    return new ResponseEntity(gson.toJson(temp), HttpStatus.OK);

}

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path="/method_method", method = RequestMethod.POST)
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

        for(int i=0;i<allData.getListOfMethods().size();i++){
            tempNodes.add(new Node(allData.getListOfMethods().get(i)));
        }
        for(int i =0;i<allData.getListOfEdgesMethod_Method().size();i++){
            tempEdge.add(new Edge(allData.getListOfEdgesMethod_Method().get(i)));
        }

        ApiData temp = new ApiData(tempNodes,tempEdge);

        return new ResponseEntity(gson.toJson(temp), HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path="/method_package", method = RequestMethod.POST)
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

        for(int i=0;i<allData.getListOfMethods().size();i++){
            tempNodes.add(new Node(allData.getListOfMethods().get(i)));
        }
        for(int i =0;i<allData.getListOfPackages().size();i++){
            tempNodes.add(new Node(allData.getListOfPackages().get(i)));
        }
        for(int i =0;i<allData.getListOfEdgesFile_File().size();i++){
            tempEdge.add(new Edge(allData.getListOfEdgesFile_File().get(i)));
        }

        ApiData temp = new ApiData(tempNodes,tempEdge);

        return new ResponseEntity(gson.toJson(temp), HttpStatus.OK);

    }

    @CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path="/package_package", method = RequestMethod.POST)
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

        for(int i=0;i<allData.getListOfPackages().size();i++){
            tempNodes.add(new Node(allData.getListOfPackages().get(i)));
        }
        for(int i =0;i<allData.getListOfEdgesPackage_Package().size();i++){
            tempEdge.add(new Edge(allData.getListOfEdgesPackage_Package().get(i)));
        }

        ApiData temp = new ApiData(tempNodes,tempEdge);

        return new ResponseEntity(gson.toJson(temp), HttpStatus.OK);

    }

}