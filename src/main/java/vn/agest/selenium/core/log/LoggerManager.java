package vn.agest.selenium.core.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LoggerManager {

    static {
        String configFile = System.getProperty("log4j.configurationFile");

        if (configFile == null || configFile.isEmpty()) {

            configFile = "logging/log4j2-prod.xml";

            System.setProperty("log4j.configurationFile", configFile);
        }

    }

    private LoggerManager() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
