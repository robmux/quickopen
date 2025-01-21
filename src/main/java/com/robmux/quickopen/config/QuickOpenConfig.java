package com.robmux.quickopen.config;

import java.util.List;

public class QuickOpenConfig {
    private String defaultRepoUrlTemplate = "https://{source}";
    private String defaultPrUrlTemplate = "https://{source}/pulls";
    private List<OpenerConfig> openers;

    // Getters and Setters
    public String getDefaultRepoUrlTemplate() {
        return defaultRepoUrlTemplate;
    }

    public void setDefaultRepoUrlTemplate(String defaultRepoUrlTemplate) {
        this.defaultRepoUrlTemplate = defaultRepoUrlTemplate;
    }

    public String getDefaultPrUrlTemplate() {
        return defaultPrUrlTemplate;
    }

    public void setDefaultPrUrlTemplate(String defaultPrUrlTemplate) {
        this.defaultPrUrlTemplate = defaultPrUrlTemplate;
    }

    public List<OpenerConfig> getOpeners() {
        return openers;
    }

    public void setOpeners(List<OpenerConfig> openers) {
        this.openers = openers;
    }

    public OpenerConfig getOpenerForType(String name) {
        if (openers == null || openers.isEmpty()) {
            return null;
        }

        OpenerConfig opener = null;
        for (OpenerConfig openerConfig : this.openers) {
            if (openerConfig.getBehavior().equals(name)) {
                opener = openerConfig;
                break;
            }
        }

        return opener;
    }
}
