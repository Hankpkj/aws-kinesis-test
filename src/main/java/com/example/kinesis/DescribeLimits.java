package com.example.kinesis;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.DescribeLimitsRequest;
import software.amazon.awssdk.services.kinesis.model.DescribeLimitsResponse;
import software.amazon.awssdk.services.kinesis.model.KinesisException;

public class DescribeLimits {
    public static void main(String[] args) {
        // snippet-start:[kinesis.java2.DescribeLimits.client]
        Region region = Region.US_EAST_1;
        KinesisClient kinesisClient = KinesisClient.builder()
                .region(region)
                .build();

        // this is Head !!
        describeKinLimits(kinesisClient);
    }

    public static void describeKinLimits(KinesisClient kinesisClient) {

        try {
            // need not parameter DescribeLimitsRequest
            DescribeLimitsRequest request = DescribeLimitsRequest.builder()
                    .build();

            DescribeLimitsResponse response = kinesisClient.describeLimits(request);

            System.out.println("Number of open shards: " + response.openShardCount());
            System.out.println("Maximum shards allowed: " + response.shardLimit());
        } catch (KinesisException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done");


    }
}
