package com.robmux.quickopen.config;

public class QuickOpenConfig {
    private String repoUrlTemplate = "https://{repo}/";
    private String prUrlTemplate = "https://{repo}/pulls";

    public String getRepoUrlTemplate() {
        return repoUrlTemplate;
    }

    public void setRepoUrlTemplate(String repoUrlTemplate) {
        this.repoUrlTemplate = repoUrlTemplate;
    }

    public String getPrUrlTemplate() {
        return prUrlTemplate;
    }

    public void setPrUrlTemplate(String prUrlTemplate) {
        this.prUrlTemplate = prUrlTemplate;
    }
}
