package SpringApplication;

import ApiData.ApiData;
import ApiData.Path;
import ApiData.Edge;
import ApiData.Node;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static SpringApplication.GraphApplication.allData;
import static SpringApplication.GraphApplication.dataSet;

@RestController
public class GraphApplicationController {

@CrossOrigin(origins = "http://localhost:8080")
    @ResponseBody
    @RequestMapping(path="/dir", method = RequestMethod.POST)
    public ResponseEntity dirPath(@RequestBody String path) {
    Gson gson = new Gson();
    Path dir = gson.fromJson(path, Path.class);

    if (dataSet == null) {
        try {
            dataSet.setPath(dir.getPath());
           allData = dataSet.findModuleDependencies();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

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
    @RequestMapping(path="/reset", method = RequestMethod.POST)
    public ResponseEntity dataReset() {
        dataSet= null;
        if(dataSet==null) {
            return new ResponseEntity(null, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(null, HttpStatus.CONFLICT);
        }
}
}