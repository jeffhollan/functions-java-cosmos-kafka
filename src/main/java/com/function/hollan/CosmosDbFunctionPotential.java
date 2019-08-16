package com.function.hollan;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Cosmos DB trigger.
 */
public class CosmosDbFunctionPotential {
    /**
     * This function will be invoked when there are inserts or updates in the specified database and collection.
     */
    @FunctionName("CosmosDbFunction")
    @EventHubOutput(name = "event", eventHubName = "myEventHub", connection = "AzureEventHubConnection")
    public Event run(
        @CosmosDBTrigger(
            name = "items",
            databaseName = "itemsDb",
            collectionName = "items",
            leaseCollectionName="functionLease",
            connectionStringSetting = "CosmosDbConnectionString",
            createLeaseCollectionIfNotExists = true
        )
        Object[] items,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Cosmos DB trigger function executed.");
        context.getLogger().info("Documents count: " + items.length);

        // Do some work
        Event output = new Event();
        output.Data = "Some Message";
        output.PartitionKey = "Some partition key";
        return output;
    }
}
