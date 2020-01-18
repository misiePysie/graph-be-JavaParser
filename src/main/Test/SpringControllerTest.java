import SpringApplication.GraphApplication;
import SpringApplication.GraphApplicationController;
import ch.qos.logback.core.status.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.http.client.ClientProtocolException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.json.Json;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = GraphApplication.class)
@RunWith(SpringRunner.class)
@WebMvcTest(GraphApplicationController.class)
public class SpringControllerTest {

//    @TestConfiguration
//    static class SpringVontrollerTest {
//
//        @Bean
//        public GraphApp graphApp() {
//            return new GraphApp();
//        }
//    }
        @Autowired
        MockMvc mockMvc;
//    @Autowired
//    GraphApplicationController graphApplicationController=new GraphApplicationController();
    @LocalServerPort
    private int port=8080;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void file_fileShouldReturnJavaClasses() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/path",
                String.class))
                .contains("EdgeMethod_File");
    }

    @Test
    public void contextLoads() throws Exception {
        GraphApplicationController graphApplicationController=new GraphApplicationController();
        assertThat(graphApplicationController).isNotNull();
    }

    public String path="path";

    public String path2="F:\\Java\\Projects\\IOIOIO\\Graf\\graph-be-JavaParser\\src\\main\\java";

    @Test
    public void testPathController(){

        GraphApplicationController graphApplicationController=new GraphApplicationController();
        int code=graphApplicationController.setPath(path).getStatusCodeValue();
        Assert.assertEquals(400,code);

    }
//    @Test
//    public void testFileConnectionsController(){
//        GraphApplicationController graphApplicationController=new GraphApplicationController();
//        Gson gson = new Gson();
//        String json = gson.toJson(path2);
//        graphApplicationController.setPath(json);
//        HttpHeaders khgkj=graphApplicationController.fileConnections().getHeaders();
//
//
//    }

//    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
//    @Test
//    public void testInsertObject() throws Exception {
//        String url = "http://localhost:3030" + "/path";
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        //ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson=mapper.writeValueAsString(path+':'+path2);
////
////
////            ObjectMapper mapper = new ObjectMapper();
////            mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
////            Byte[] stringInBytes= mapper.writeValueAsBytes(object);
//        given().
//                param()
//
//        mockMvc.perform(post(url)
//                .content(requestJson))
//                .andExpect(status().isOk());
//    }
//    private static RequestSpecification spec;
//
//    @BeforeClass
//    public static void initSpec(){
//        spec = new RequestSpecBuilder()
//                .setContentType(ContentType.JSON)
//                .setBaseUri("http://localhost:8080/")
//                .addFilter(new ResponseLoggingFilter())//log request and response for better debugging. You can also only log if a requests fails.
//                .addFilter(new RequestLoggingFilter())
//                .build();
//    }
//    @Test
//    public void useSpec(){
//        given()
//                .spec(spec)
//                .param("limit", 20)
//                .when()
//                .get("blogs")
//                .then()
//                .statusCode(200);
//    }
//
//    @Test
//    public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived()
//            throws ClientProtocolException, IOException {
//
//        // Given
//        String name =RandomStringUtils.randomAlphabetic( 8 );
//        HttpUriRequest request = new HttpGet( "https://api.github.com/users/" + name );
//
//        // When
//        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
//
//        // Then
//        assertEquals(httpResponse.getStatusLine().getStatusCode(),HttpStatus.NOT_FOUND.value());
//    }

//    @Test
//    public void testURL() throws Exception {
//        String strUrl = "http://localhost";
//        int code=200;
//
//        try {
//            URL url = new URL(strUrl);
//            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//           // urlConn.setRequestMethod("GET");
//            urlConn.connect();
//            code=urlConn.getResponseCode();
//
//            assertEquals(code,200);
//        } catch (IOException e) {
//            System.err.println("Error creating HTTP connection");
//            e.printStackTrace();
//            throw e;
//        }
//    }

//    @LocalServerPort
//    private int port=3030;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    public void pathShouldReturnNull() throws Exception {
//        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/path",
//                String.class)).contains("");
//    }

//    @Test
//    public void file_fileShouldReturnDefault() throws Exception {
//        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/file_file",
//                String.class)).contains("EdgeFile_File");
//    }
}