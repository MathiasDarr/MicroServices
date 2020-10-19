package org.mddarr.ordersservice.utils;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class OrdersTableUtils {
    private static final Logger log = LoggerFactory.getLogger(OrdersTableUtils.class);
    private static final String TABLE = "Orders";
    public static void createOrdersTable(AmazonDynamoDB amazonDynamoDB){
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(TABLE)
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long) 1).withWriteCapacityUnits((long) 1));

        // Attribute definitions for table partition and sort keys
        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("orderID").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("orderCreationDate").withAttributeType("N"));

        // Attribute definition for index primary key attributes
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("customerID").withAttributeType("S"));

        createTableRequest.setAttributeDefinitions(attributeDefinitions);

        // Key schema for table
        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement().withAttributeName("orderID").withKeyType(KeyType.HASH)); // Partition key
        tableKeySchema.add(new KeySchemaElement().withAttributeName("orderCreationDate").withKeyType(KeyType.RANGE)); // Sort key

        createTableRequest.setKeySchema(tableKeySchema);

        ArrayList<GlobalSecondaryIndex> globalSecondaryIndices = new ArrayList<GlobalSecondaryIndex>();

//        // OrderCreationDateIndex
//        LocalSecondaryIndex orderCreationDateIndex = new LocalSecondaryIndex().withIndexName("orderCreationDateIndex");

        GlobalSecondaryIndex customerIndex = new GlobalSecondaryIndex().withIndexName("customerOrderIndex")
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long) 1).withWriteCapacityUnits((long) 1));

        // Key schema for CustomerIndex
        ArrayList<KeySchemaElement> customerIndexKeySchema = new ArrayList<KeySchemaElement>();
        customerIndexKeySchema.add(new KeySchemaElement().withAttributeName("customerID").withKeyType(KeyType.HASH)); // Partition key
        customerIndexKeySchema.add(new KeySchemaElement().withAttributeName("orderCreationDate").withKeyType(KeyType.RANGE)); // Sort
        // key

        customerIndex.setKeySchema(customerIndexKeySchema);
        // Projection (with list of projected attributes) for OrderCreationDateIndex
        Projection projection = new Projection().withProjectionType(ProjectionType.INCLUDE);

        ArrayList<String> nonKeyAttributes = new ArrayList<String>();
        nonKeyAttributes.add("productCategory");
        nonKeyAttributes.add("productName");
        projection.setNonKeyAttributes(nonKeyAttributes);

        customerIndex.setProjection(projection);

        globalSecondaryIndices.add(customerIndex);

        // Add index definitions to CreateTable request
        createTableRequest.setGlobalSecondaryIndexes(globalSecondaryIndices);

        // Create the table
        dynamoDB.createTable(createTableRequest);

        // Wait for table to become active
        System.out.println("Waiting for " + TABLE + " to become ACTIVE...");
        try {
            Table table = dynamoDB.getTable(TABLE);
            table.waitForActive();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("Error creating table '{}'", TABLE, e);
            throw new RuntimeException(e);
        }

    }

    public static void loadOrdersData(AmazonDynamoDB amazonDynamoDB) throws IOException {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable(TABLE);
        JsonParser parser = new JsonFactory().createParser(Utils.fetchOrdersFromResources());
        JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();
        ObjectNode currentNode;

        while (iter.hasNext()) {
            currentNode = (ObjectNode) iter.next();
            long order_time = currentNode.path("order_time").asLong();
            String customerID = currentNode.path("customerId").asText();
            String orderId = currentNode.path("orderID").asText();
            String order_status = currentNode.path("order_status").asText();
            String productsListString = currentNode.path("products").asText();
            String brandsListString = currentNode.path("brands").asText();
            String quantitiesListString = currentNode.path("quantities").asText();
            String pricesListString = currentNode.path("prices").asText();

            ArrayList<String> productStringsList = new ArrayList<>(Arrays.asList(productsListString.split(",")));

            List<String> productsList = Arrays.asList(productsListString.split(","));
            List<String> brands = Arrays.asList(brandsListString.split(","));

            List<String> quantitiesStringList = Arrays.asList(quantitiesListString.split(","));
            List<Long> quantities = new ArrayList<>(quantitiesStringList.stream().map(Long::valueOf).collect(Collectors.toList()));
//
//
            List<String> pricesStringList = Arrays.asList(pricesListString.split(","));
            List<Double> prices = new ArrayList<>(pricesStringList.stream().map(Double::valueOf).collect(Collectors.toList()));

//            List<Long> quantities = Arrays.asList(2L, 3L);
//            List<Double> prices = Arrays.asList(2.2, 3.1);

            Set<String> products = new HashSet<>(productsList);
            Item item;

            item = new Item().withPrimaryKey("customerID",customerID)
                    .withString("orderID", orderId )
                    .withNumber("orderCreationDate", order_time)
                    .withList("quantities", quantities)
                    .withList("brands", brands)
                    .withList("prices", prices)
                    .withList("products",productStringsList)
                    .withString("orderState", order_status);
            DateTime date = new DateTime(Long.valueOf(order_time * 1000L), DateTimeZone.UTC);
            try {
                table.putItem(item);
            }
            catch (Exception e) {
                System.err.println("Unable to add product: " + " " + products);
                System.err.println(e.getMessage());
                break;
            }

        }
        parser.close();
    }
}
