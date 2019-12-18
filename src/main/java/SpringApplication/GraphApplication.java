package SpringApplication;
//import Export.XMLFileBuilder;
import Export.XMLFileBuilder;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import Data.AllData;
import Data.DataGuide;
import com.jamesmurty.utils.XMLBuilder2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;


@SpringBootApplication
public class GraphApplication {

    public static void main (String args[]) throws IOException, NoSuchFieldException, ClassNotFoundException, UnsolvedSymbolException {
        SpringApplication.run(GraphApplication.class, args);

    }
}