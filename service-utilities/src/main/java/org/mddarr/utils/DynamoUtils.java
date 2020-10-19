package org.mddarr.utils;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;

public class DynamoUtils {
    public static Boolean isEmpty(AmazonDynamoDB database, String tableName) {
        ScanRequest scanRequest = new ScanRequest().withTableName(tableName).withLimit(1);
        return database.scan(scanRequest).getScannedCount() == 0;
    }

}
