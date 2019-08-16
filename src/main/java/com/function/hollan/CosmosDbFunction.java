package com.function.hollan;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Cosmos DB trigger.
 */
public class CosmosDbFunction {

    
    @FunctionName("CosmosDbFunction")
    @EventHubOutput(name = "event", eventHubName = "myEventHub", connection = "AzureEventHubConnection")
    public String run(
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
        return "SomeMessage";
    }
}
