package com.example.kinesis;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.*;
import software.amazon.awssdk.services.kinesis.model.Record;

import java.util.ArrayList;
import java.util.List;

public class GetRecords {
    public static void main(String[] args) {
        final String USAGE = "\n" +
                "Usage:\n" +
                "    GetRecords <streamName>\n\n" +
                "Where:\n" +
                "    streamName - The Kinesis data stream to read from (i.e., StockTradeStream)\n\n" +
                "Example:\n" +
                "    GetRecords streamName\n";

        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String streamName = args[0];

        Region region = Region.AP_NORTHEAST_2;

        KinesisClient kinesisClient = KinesisClient.builder()
                .region(region)
                .build();

        getStockData(kinesisClient, streamName);
    }

    public static void getStockData(KinesisClient kinesisClient, String streamName) {

        String shardIterator;
        String lastShardId = null;

        DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder()
                .streamName(streamName)
                .build();

        List<Shard> shardList = new ArrayList<>();

        DescribeStreamResponse describeStreamResponse;
        do {
            describeStreamResponse = kinesisClient.describeStream(describeStreamRequest);
            shardList.addAll(describeStreamResponse.streamDescription().shards());

            if ( shardList.size() > 0 ) {
                lastShardId = shardList.get(shardList.size() - 1).shardId();
            }
        } while (describeStreamResponse.streamDescription().hasMoreShards());

        GetShardIteratorRequest getShardIteratorRequest = GetShardIteratorRequest.builder()
                .streamName(streamName)
                .shardIteratorType("TRIM_HORIZON")
                .shardId(shardList.get(0).shardId())
                .build();

        GetShardIteratorResponse getShardIteratorResponse = kinesisClient.getShardIterator(getShardIteratorRequest);
        shardIterator = getShardIteratorResponse.shardIterator();

        // Continuously read data records from a shard
        List<Record> records;

        // Create a GetRecordsRequest with the existing shardIterator,
        // and set maximum records to return to 1000
        GetRecordsRequest recordsRequest = GetRecordsRequest.builder()
                .shardIterator(shardIterator)
                .limit(1000)
                .build();

        GetRecordsResponse result = kinesisClient.getRecords(recordsRequest);

        // Put result into a record list, result might be empty
        records = result.records();

        // Print records
        for (Record record : records) {
            SdkBytes byteBuffer = record.data();
            System.out.println(String.format("Seq No: %s - %s", record.sequenceNumber(),
                    new String(byteBuffer.asByteArray())));
        }
        }
    }

