package org.mddarr.inventoryservice.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import org.mddarr.inventoryservice.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
public class ProductsTableUtils {

    private static final Logger log = LoggerFactory.getLogger(ProductsTableUtils.class);
    private static final String TABLE = "Products";

    public static void createProductsTable(AmazonDynamoDB amazonDynamoDB) {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("brandID").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("productName").withAttributeType("S"));

        List<KeySchemaElement> primaryKeySchema = new ArrayList<KeySchemaElement>();
        primaryKeySchema.add(new KeySchemaElement().withAttributeName("brandID").withKeyType(KeyType.HASH));
        primaryKeySchema.add(new KeySchemaElement().withAttributeName("productName").withKeyType(KeyType.RANGE));

        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName(TABLE)
                .withKeySchema(primaryKeySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(1L)
                        .withWriteCapacityUnits(1L));

        List<KeySchemaElement> categoryIndexKeySchema = new ArrayList<KeySchemaElement>();
        categoryIndexKeySchema.add(new KeySchemaElement().withAttributeName("category").withKeyType(KeyType.HASH)); // Partition key
        categoryIndexKeySchema.add(new KeySchemaElement().withAttributeName("productName").withKeyType(KeyType.RANGE)); // Sort

//        // OrderCreationDateIndex
//        LocalSecondaryIndex orderCreationDateIndex = new LocalSecondaryIndex().withIndexName("orderCreationDateIndex");

//        GlobalSecondaryIndex customerIndex = new GlobalSecondaryIndex().withIndexName("customerOrderIndex")
//                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long) 1).withWriteCapacityUnits((long) 1));
//
//        // Key schema for CustomerIndex

//        // key
//
//        customerIndex.setKeySchema(customerIndexKeySchema);
//        // Projection (with list of projected attributes) for OrderCreationDateIndex
//        Projection projection = new Projection().withProjectionType(ProjectionType.INCLUDE);
//
//        ArrayList<String> nonKeyAttributes = new ArrayList<String>();
//        nonKeyAttributes.add("productCategory");
//        nonKeyAttributes.add("productName");
//        projection.setNonKeyAttributes(nonKeyAttributes);
//
//        customerIndex.setProjection(projection);
//
//        globalSecondaryIndices.add(customerIndex);
//
//        // Add index definitions to CreateTable request
//        createTableRequest.setGlobalSecondaryIndexes(globalSecondaryIndices);

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


//        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
//
//        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(TABLE)
//                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long) 1).withWriteCapacityUnits((long) 1));
//
//        List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
//        attributeDefinitions.add(new AttributeDefinition().withAttributeName("productID").withAttributeType("S"));
//        attributeDefinitions.add(new AttributeDefinition().withAttributeName("productBrand").withAttributeType("S"));
////        attributeDefinitions.add(new AttributeDefinition().withAttributeName("productCategory").withAttributeType("S"));
//        attributeDefinitions.add(new AttributeDefinition().withAttributeName("price").withAttributeType("N"));
//
//        createTableRequest.setAttributeDefinitions(attributeDefinitions);
//
//        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
//        tableKeySchema.add(new KeySchemaElement().withAttributeName("productID").withKeyType(KeyType.HASH)); // Partition key
//        tableKeySchema.add(new KeySchemaElement().withAttributeName("price").withKeyType(KeyType.RANGE)); // Partition key
//        createTableRequest.setKeySchema(tableKeySchema);
//
//        ArrayList<GlobalSecondaryIndex> globalSecondaryIndices = new ArrayList<GlobalSecondaryIndex>();
//
////        GlobalSecondaryIndex productBrandIndex = new GlobalSecondaryIndex()
////                .withIndexName("productBrandIndex")
////                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long) 1).withWriteCapacityUnits((long) 1));
////
////        ArrayList<KeySchemaElement> productBrandIndexKeySchema = new ArrayList<KeySchemaElement>();
////        productBrandIndexKeySchema.add(new KeySchemaElement().withAttributeName("productBrand").withKeyType(KeyType.HASH)); // Partition key
////        productBrandIndexKeySchema.add(new KeySchemaElement().withAttributeName("productName").withKeyType(KeyType.RANGE)); // Sort
////
////        productBrandIndex.setKeySchema(productBrandIndexKeySchema);
////
////        Projection projection = new Projection().withProjectionType(ProjectionType.INCLUDE);
////
////        ArrayList<String> nonKeyAttributes = new ArrayList<String>();
////        nonKeyAttributes.add("price");
////        nonKeyAttributes.add("imageURL");
////        projection.setNonKeyAttributes(nonKeyAttributes);
////
////        productBrandIndex.setProjection(projection);
////        globalSecondaryIndices.add(productBrandIndex);
//
////        GlobalSecondaryIndex productCategoryIndex = new GlobalSecondaryIndex()
////                .withIndexName("productCategoryIndex")
////                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long) 1).withWriteCapacityUnits((long) 1));
//
////        ArrayList<KeySchemaElement> productCategoryIndexKeySchema = new ArrayList<KeySchemaElement>();
////        productBrandIndexKeySchema.add(new KeySchemaElement().withAttributeName("productCategory").withKeyType(KeyType.HASH)); // Partition key
////        productBrandIndexKeySchema.add(new KeySchemaElement().withAttributeName("price").withKeyType(KeyType.RANGE)); // Sort
////        productCategoryIndex.setKeySchema(productCategoryIndexKeySchema);
////        globalSecondaryIndices.add(productCategoryIndex);
//
////        createTableRequest.setGlobalSecondaryIndexes(globalSecondaryIndices);
//
//        dynamoDB.createTable(createTableRequest);
//
//        System.out.println("Waiting for " + TABLE + " to become ACTIVE...");
//        try {
//            Table table = dynamoDB.getTable(TABLE);
//            table.waitForActive();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            log.error("Error creating table '{}'", TABLE, e);
//            throw new RuntimeException(e);
//        }
    }

    public static void loadProductsData(AmazonDynamoDB amazonDynamoDB) throws IOException {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

        Table table = dynamoDB.getTable(TABLE);
        JsonParser parser = new JsonFactory().createParser(Utils.fetchOrdersFromResources());
        JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();
        ObjectNode currentNode;
        while (iter.hasNext()) {
            currentNode = (ObjectNode) iter.next();
            double price = currentNode.path("price").asDouble();
            String productName = currentNode.path("productName").asText();
            String imageURL = currentNode.path("image_url").asText();
            String brand = currentNode.path("brand").asText();
            String category = currentNode.path("category").asText();

            try {
                table.putItem(new Item()
                        .withPrimaryKey("brandID", brand)
                        .withString("productName",productName)
                        .withNumber("price", price)
                        .withString("imageURL",imageURL)
                        .withString("category", category));
                System.out.println("PutItem succeeded: " + price + " " + productName);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        parser.close();
    }
}
