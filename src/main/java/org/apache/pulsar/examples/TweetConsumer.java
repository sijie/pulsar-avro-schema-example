package org.apache.pulsar.examples;

import java.util.concurrent.ConcurrentMap;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.apache.pulsar.client.impl.schema.AvroSchema;

/**
 * Consumer to consume tweets in Avro format.
 */
public class TweetConsumer {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: TweetProducer [service-url]");
            return;
        }
        String serviceUrl = args[0];

        try (PulsarClient client = PulsarClient.builder()
            .serviceUrl(serviceUrl)
            .build()) {

            try (Consumer<TwitterSchema> consumer = client.newConsumer(AvroSchema.of(TwitterSchema.class))
                 .topic("twitter-avro")
                 .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                 .subscriptionName("schema-tester")
                 .subscribe()) {

                Message<TwitterSchema> msg;
                while ((msg = consumer.receive()) != null) {
                    System.out.println("Receive tweet : " + msg.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
