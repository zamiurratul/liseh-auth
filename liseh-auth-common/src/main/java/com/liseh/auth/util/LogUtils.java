package com.liseh.auth.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class LogUtils {
    private static final Logger LOGGER = LogManager.getLogger(LogUtils.class);

//    public static void error(String message) {
//        String errorMessage = String.format("Exception: %tc | %s", new Date(), message);
//        LOGGER.error(errorMessage);
//    }

    public static void error(Logger logger, String purpose, String message, String cause) {
        String errorMessage = String.format("Exception: %tc | %s | %s | %s", new Date(), purpose, message, cause);
        logger.error(errorMessage);
    }
}
