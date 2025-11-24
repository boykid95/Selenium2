package vn.agest.selenium.core.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.agest.selenium.core.constants.Constants;

public final class LoggerManager {

    private static final String LOG_CONFIG_KEY = "log4j.configurationFile";

    static {
        String configFile = System.getProperty(LOG_CONFIG_KEY);

        if (configFile == null || configFile.isEmpty()) {
            configFile = Constants.LOG_CONFIG_PROD;
        }

        System.setProperty(LOG_CONFIG_KEY, configFile);
    }

    private LoggerManager() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
