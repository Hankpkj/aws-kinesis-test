package com.example.kinesis;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamRequest;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamResponse;
import software.amazon.awssdk.services.kinesis.model.KinesisException;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

import javax.xml.crypto.Data;

public class RecordWriter {
    public static void main(String[] args) {

        final String USAGE = "\n" +
                "Usage:\n" +
                "    StockTradesWriter <streamName>\n\n" +
                "Where:\n" +
                "    streamName - The Kinesis data stream to which records are written (i.e., StockTradeStream)\n\n" +
                "Example:\n" +
                "    StockTradesWriter streamName\n";

        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String streamName = args[0];

        Region region = Region.AP_NORTHEAST_2;
        KinesisClient kinesisClient = KinesisClient.builder()
                .region(region)
                .build();

        // 1. validation check
        // 2. write data
        validateStream(kinesisClient, streamName);
        setData( kinesisClient, streamName);
    }

    public static void setData(KinesisClient kinesisClient, String streamName) {
        try {
            DataGenerator dataGenerator = new DataGenerator();

            // generating 50 records
            int index = 50;
            for (int x = 0; x < index ; x ++) {
                StockTrade stockTrade = dataGenerator.getRandomTrade();
                sendStockTrade(stockTrade, kinesisClient, streamName);
                Thread.sleep(100);
            }

        } catch (KinesisException | InterruptedException e) {
            System.err.println(e.getMessage());
            System.exit(1);

        }
        System.out.println("setData done!");
    }

    public static void sendStockTrade(StockTrade stockTrade, KinesisClient kinesisClient, String streamName) {

        byte[] bytes = stockTrade.toJsonAsBytes();
        // The bytes could be null if there is an issue with the JSON serialization by the Jackson JSON library.
        if (bytes == null) {
            System.out.println("Could not get JSON bytes for stock trade");
        } else {
        System.out.println("Putting trade data : " + stockTrade.toString());
        PutRecordRequest putRecordRequest = PutRecordRequest.builder()
                .data(SdkBytes.fromByteArray(bytes))
                .partitionKey(stockTrade.getTickerSymbol())
                .streamName(streamName)
                .build();
        try {
            kinesisClient.putRecord(putRecordRequest);
        } catch (KinesisException e) {
            System.err.println(e.getMessage());
        }
        }
    }

    public static void validateStream(KinesisClient kinesisClient, String streamName) {
        try {
            DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder()
                    .streamName(streamName)
                    .build();

            DescribeStreamResponse describeStreamResponse = kinesisClient.describeStream(describeStreamRequest);

            if (!describeStreamResponse.streamDescription().streamStatus().toString().equals("ACTIVE")) {
                System.err.println("Stream " + streamName + " is not active");
                System.exit(1);
            }
        } catch (KinesisException e) {
            System.err.println("Error found while describing the stream " + streamName);
            System.err.println(e);
            System.exit(1);
        }
    }
}
