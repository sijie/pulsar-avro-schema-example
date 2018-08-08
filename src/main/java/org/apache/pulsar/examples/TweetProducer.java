package org.apache.pulsar.examples;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.impl.schema.AvroSchema;

/**
 * Producer to produce tweets in Avro format.
 */
public class TweetProducer {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: TweetProducer [service-url]");
            return;
        }
        String serviceUrl = args[0];

        try (PulsarClient client = PulsarClient.builder()
            .serviceUrl(serviceUrl)
            .build()) {
            try (Producer<TwitterSchema> producer = client.newProducer(AvroSchema.of(TwitterSchema.class))
                 .topic("twitter-avro")
                 .create()) {
                for (int i = 0; i < 10; i++) {
                    TwitterSchema tweet = new TwitterSchema("user-" + i, "tweet-" + i, System.currentTimeMillis());
                    producer.send(tweet);
                }
                System.out.println("Successfully produce 10 tweets");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
