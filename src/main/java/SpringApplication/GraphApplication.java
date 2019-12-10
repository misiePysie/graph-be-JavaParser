package SpringApplication;
import Export.XMLFileBuilder;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.jamesmurty.utils.XMLBuilder2;
import Data.AllData;
import Data.DataGuide;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GraphApplication {
    public static void main (String args[]) throws IOException, NoSuchFieldException, ClassNotFoundException, UnsolvedSymbolException {
        //SpringApplication.run(GraphApplication.class, args);
        DataGuide obj = new DataGuide();
        obj.findModuleDependencies();

    }
}
