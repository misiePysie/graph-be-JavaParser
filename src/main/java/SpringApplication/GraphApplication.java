package SpringApplication;

import Data.DataGuide;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GraphApplication {
    public static void main (String args[]) throws IOException, NoSuchFieldException, ClassNotFoundException{
        //SpringApplication.run(GraphApplication.class, args);
        DataGuide amwjp = new DataGuide();
        amwjp.findModuleDependencies("/Users/dominikstrama/Desktop/graph-be-JavaParser/src/main/java");

    }
}
