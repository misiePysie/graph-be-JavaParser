package SpringApplication;

import Data.AllData;
import Data.DataGuide;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GraphApplication {
    public static void main (String args[]) throws IOException, NoSuchFieldException, ClassNotFoundException{
        //SpringApplication.run(GraphApplication.class, args);
        DataGuide obj = new DataGuide();
        AllData allData=new AllData();
        obj.findModuleDependencies("F:\\Java\\Projects\\IOIOIO\\graph-be-JavaParser\\src\\main\\java",allData);

    }
}
