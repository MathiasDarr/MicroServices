package org.mddarr.ordersservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class.getName());

    public static void deleteDynamoTable(){

    }


    public static File fetchOrdersFromResources(){
        try {
            File file = new ClassPathResource("orders.json").getFile();
            InputStream in = new FileInputStream(file);
            return file;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}