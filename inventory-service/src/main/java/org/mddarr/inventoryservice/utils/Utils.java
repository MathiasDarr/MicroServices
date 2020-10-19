package org.mddarr.inventoryservice.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class.getName());

    public static File fetchOrdersFromResources(){
        try {
            File file = new ClassPathResource("products.json").getFile();
            return file;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}