package com.function.hollan;

import com.microsoft.azure.functions.annotation.*;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.FileReader;
import java.util.Properties;

import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Cosmos DB trigger.
 */
public class CosmosDbFunctionPotential2 {
    private final static String EVENTHUB = "myhub";
    private static Producer<String, String> producer = createProducer();

    /**
     * This function will be invoked when there are inserts or updates in the
     * specified database and collection.
     */
    @FunctionName("CosmosDbFunction")
    public void run(
            @CosmosDBTrigger(name = "items", databaseName = "itemsDb", collectionName = "items", leaseCollectionName = "functionLease", connectionStringSetting = "CosmosDbConnectionString", createLeaseCollectionIfNotExists = true) Object[] items,
            final ExecutionContext context) {
        context.getLogger().info("Java Cosmos DB trigger function executed.");
        context.getLogger().info("Documents count: " + items.length);

        // Do some work
        final ProducerRecord<String, String> record = new ProducerRecord<String, String>(EVENTHUB, "Some Key", "Some Message");
        producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    System.out.println(exception);
                    System.exit(1);
                }
            }
        });
    }

    private static Producer<String, String> createProducer() {
        if (producer != null)
            return producer;
        else {
            try {
                Properties properties = new Properties();
                properties.load(new FileReader("src/main/resources/producer.config"));
                properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
                properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
                properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
                producer = new KafkaProducer<>(properties);
                return producer;
            } catch (Exception e) {
                System.out.println("Failed to create producer with exception: " + e);
                System.exit(0);
                return null; // unreachable
            }
        }
    }
}
