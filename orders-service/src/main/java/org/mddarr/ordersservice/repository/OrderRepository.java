package org.mddarr.ordersservice.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mddarr.ordersservice.dto.Order;
import org.mddarr.ordersservice.dto.OrderRequest;
import org.mddarr.ordersservice.utils.OrdersTableUtils;
import org.mddarr.utils.DynamoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Repository
public class OrderRepository {
    private final static String TABLE = "Orders";
    private static final Logger log = LoggerFactory.getLogger(OrderRepository.class);

    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDBMapper mapper;

    public OrderRepository(AmazonDynamoDB db){
        this.amazonDynamoDB = db;
        this.mapper = new DynamoDBMapper(amazonDynamoDB);
    }

    public List<OrderEntity> findAll() {
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(TABLE)).build();
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB, mapperConfig);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List <OrderEntity> orders = mapper.scan(OrderEntity.class, scanExpression);
        return orders;
    }

    public List<OrderEntity> fetchCustomerOrders(String customerID, String orderDate){
        Map<String, AttributeValue> vals = new HashMap<>();
        vals.put(":cid", new AttributeValue().withS(customerID));
        vals.put(":orderDate", new AttributeValue().withN(orderDate));

        DynamoDBQueryExpression<OrderEntity> queryExp = new DynamoDBQueryExpression<OrderEntity>()
                .withKeyConditionExpression("customerID = :cid and orderCreationDate > :orderDate")
                .withIndexName("customerOrderIndex")
                .withConsistentRead(false)
                .withExpressionAttributeValues(vals);
        List<OrderEntity> orders =  mapper.query(OrderEntity.class, queryExp);
        return orders;
    }

    public List<OrderEntity> fetchCustomerOrders(String customerID){
        Map<String, AttributeValue> vals = new HashMap<>();
        vals.put(":cid", new AttributeValue().withS(customerID));

        DynamoDBQueryExpression<OrderEntity> queryExp = new DynamoDBQueryExpression<OrderEntity>()
                .withKeyConditionExpression("customerID = :cid")
                .withIndexName("customerOrderIndex")
                .withConsistentRead(false)
                .withExpressionAttributeValues(vals);
        List<OrderEntity> orders =  mapper.query(OrderEntity.class, queryExp);
        return orders;
    }

    String customerID;
    List<String> productBrands;
    List<String> productsNames;
    List<Long> quantities;
    List<Double> prices;

    public Order put(OrderRequest order) {
        String orderID = UUID.randomUUID().toString();
        long creationDate = System.currentTimeMillis();
        mapper.save(new OrderEntity(orderID, creationDate, order.getCustomerID(), order.getProductBrands(), order.getProductsNames(),order.getQuantities(),order.getPrices(), "PENDING"));
        return new Order(orderID, creationDate, order.getCustomerID(), order.getProductBrands(), order.getProductsNames(),order.getQuantities(),order.getPrices(), "PENDING");
    }

    public Optional<OrderEntity> get(String orderID, long creationDate) {
        OrderEntity entity = mapper.load(OrderEntity.class, orderID, creationDate);
        return Optional.ofNullable(entity);
    }

    public void delete(String orderID, long orderCreationDate) {
        mapper.delete(new OrderEntity(orderID, orderCreationDate));
    }

    @PostConstruct
    public void init() throws IOException {
        ListTablesResult tables = amazonDynamoDB.listTables();
        boolean tableExists=false;
        for(String tableName: tables.getTableNames()){
            if(tableName.equals(TABLE)){
                tableExists = true;
                break;
            }
        }
        if(!tableExists){
            OrdersTableUtils.createOrdersTable(amazonDynamoDB);
            OrdersTableUtils.loadOrdersData(amazonDynamoDB);
        }else{
            if(DynamoUtils.isEmpty(amazonDynamoDB, TABLE))
                OrdersTableUtils.loadOrdersData(amazonDynamoDB);
        }
    }

    @DynamoDBTable(tableName=TABLE)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderEntity {

        @DynamoDBHashKey(attributeName="orderID")
        private String orderID;
        @DynamoDBRangeKey(attributeName = "orderCreationDate")
        private long orderCreationDate;
        @DynamoDBIndexHashKey(globalSecondaryIndexName = "customerOrderIndex", attributeName = "customerID")
        private String customerID;
        @DynamoDBAttribute(attributeName="brands")
        private List<String> brands;
        @DynamoDBAttribute(attributeName="products")
        private List<String> products;
        @DynamoDBAttribute(attributeName="quantities")
        private List<Long> quantities;
        @DynamoDBAttribute(attributeName="prices")
        private List<Double> prices;
        @DynamoDBAttribute(attributeName="orderState")
        private String orderState;

        public OrderEntity(String orderID, long creationDate){
            this.orderID = orderID;
            this.orderCreationDate = creationDate;
        }
    }
}
