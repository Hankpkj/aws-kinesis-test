package com.example.kinesis;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.CreateStreamRequest;
import software.amazon.awssdk.services.kinesis.model.KinesisException;

public class CreateDataStream {

    public static void main(String[] args) {

        final String USAGE = "\n" +
                "Usage:\n" +
                "    CreateDataStream kinesis-stream-test\n\n" +
                "Where:\n" +
                "    CreateDataStream - The Kinesis data stream (i.e., StockTradeStream)\n\n" +
                "Example:\n" +
                "    CreateDataStream StockTradeStream\n";

        // initial call in cmd or code etc..
        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        // call with streamName like (shell : $ java CreateDataStream kinesis-strea-test )
        // set streamName
        String streamName = args[0];

        // set region
        Region region = Region.AP_NORTHEAST_2;
        KinesisClient kinesisClient = KinesisClient.builder()
                .region(region)
                // .role(someRole)
                .build();

        // it is Head !!
        createStream(kinesisClient, streamName);
    }

    public static void createStream(KinesisClient kinesisClient, String streamName) {

        try {
            CreateStreamRequest streamReq = CreateStreamRequest.builder()
                    .streamName(streamName)
                    .shardCount(3)
                    .build();

            // kinesisClient.createStream need CreateStreamRequest credential ( name , # of shards etc .. )
            kinesisClient.createStream(streamReq);
        } catch (KinesisException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }

}
