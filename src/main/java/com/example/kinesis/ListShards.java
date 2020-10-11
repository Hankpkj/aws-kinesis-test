package com.example.kinesis;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.KinesisException;
import software.amazon.awssdk.services.kinesis.model.ListShardsRequest;
import software.amazon.awssdk.services.kinesis.model.ListShardsResponse;

public class ListShards {
    public static void main(String[] args) {

        final String USAGE = "\n" +
                "Usage:\n" +
                "    ListShards <streamName>\n\n" +
                "Where:\n" +
                "    streamName - The Kinesis data stream (i.e., StockTradeStream)\n\n" +
                "Example:\n" +
                "    ListShards StockTradeStream\n";

        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String name = args[0];

        Region region = Region.AP_NORTHEAST_2;
        KinesisClient kinesisClient = KinesisClient.builder()
                .region(region)
                .build();


        listKinShards(kinesisClient, name);
    }
    public static void listKinShards(KinesisClient kinesisClient, String streamName){

        try {
            ListShardsRequest listShardsRequest = ListShardsRequest.builder()
                    .streamName(streamName)
                    .build();
            ListShardsResponse listShardsResponse = kinesisClient.listShards(listShardsRequest);
            System.out.println(listShardsRequest.streamName() + " has " + listShardsResponse );

        } catch (KinesisException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("listCheck done");
    }
}
