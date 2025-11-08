package vn.agest.selenium.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {

    static {
        String configFile = System.getProperty("log4j.configurationFile");
        if (configFile == null || configFile.isEmpty()) {
            configFile = "src/main/resources/logging/log4j2-prod.xml";
            System.setProperty("log4j.configurationFile", configFile);
        }
        System.out.println("[LoggerManager] Using log4j2 config: " + configFile);
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
