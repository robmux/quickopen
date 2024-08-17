package com.robmux.quickopen.config;

import com.google.gson.Gson;
import com.intellij.openapi.diagnostic.Logger;

import java.io.File;
import java.io.FileReader;

public class ConfigUtil {
    private static final Logger log = Logger.getInstance(ConfigUtil.class);

    private static final String configFilePath = System.getProperty("user.home") + "/quickopen/.quickopen-config.json";

    public static QuickOpenConfig loadConfig() {
        File configFile = new File(configFilePath);

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                QuickOpenConfig config =  new Gson().fromJson(reader, QuickOpenConfig.class);
                return config;
            } catch (Exception e) {
                log.error("Error reading configuration file", e);
                return new QuickOpenConfig(); // Fallback to default config
            }
        }

        log.warn("Configuration file not found, using default configuration");
        return new QuickOpenConfig(); // Fallback to default config

    }
}
