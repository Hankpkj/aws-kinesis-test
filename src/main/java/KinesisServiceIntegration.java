//import com.example.kinesis.*;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.TestMethodOrder;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.kinesis.KinesisClient;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class KinesisServiceIntegration {
//
//    // def KinesisClient -> I will use KinesisClient
//    public static KinesisClient kinesisClient;
//
//    private static String streamName = "kinesis-stream-test";
//    private static String existingDataStream = ""; // now, we have no existingDataStream
//
//    public static void setUp() throws IOException {
//
//        Region region = Region.AP_NORTHEAST_2; // south korea region = ap_northeast_2
//        kinesisClient = KinesisClient.builder()
//                .region(region)
//                .build();
//        // config.properties in resources
//        // if level error -> module setting -> set level 7~9
//        try ( InputStream input = KinesisServiceIntegration.class.getClassLoader().getResourceAsStream("config.properties")){
//
//            Properties prop = new Properties();
//
//            if (input == null) {
//                System.out.println("Sorry, unable to find config.properties");
//                // return;
//            }
//
//            //load a properties file from class path, inside static method
//            prop.load(input);
//
//            // Populate the data members required for all tests
//            streamName = prop.getProperty("streamName");
//            existingDataStream = prop.getProperty("existingDataStream");
//
//
//        } catch (IOException e) {
//            // System.out.println(e) ; bad code
//            e.printStackTrace();
//        }
//    }
//
//    // first : notnull test
//    @Order(1)
//    public void whenInitializingKinesisService_thenNotNull() {
//        assertNotNull(kinesisClient);
//        System.out.println("createClient passed");
//    }
//
//    @Order(2)
//    public void CreateDataStream() {
//        CreateDataStream.createStream(kinesisClient, streamName);
//        System.out.println("createStream passed");
//    }
//
//    @Order(3)
//    public void DescribeLimits() {
//        DescribeLimits.describeKinLimits(kinesisClient);
//        System.out.println("DescribeLimets passed");
//    }
//
//    @Order(4)
//    public void ListShards() {
//        try {
//            //Wait 60 secs for table to complete
//            TimeUnit.SECONDS.sleep(60);
//            ListShards.listKinShards(kinesisClient, streamName);
//        } catch (InterruptedException e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//        System.out.println("List check passed");
//    }
//
//    @Order(5)
//    public void putRecords() {
//        RecordWriter.setData(kinesisClient, streamName);
//        System.out.println("Set data passed");
//    }
//
//    @Order(6)
//    public void GetRecords() {
//        GetRecords.getStockData(kinesisClient, streamName);
//        System.out.println("Test 6 passed");
//    }
//
//
//}
