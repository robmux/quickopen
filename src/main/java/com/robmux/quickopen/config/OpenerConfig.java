package com.robmux.quickopen.config;

import java.util.List;

public class OpenerConfig {
    private String behavior;
    private String program;
    private List<ActionConfig> actions;

    // Getters and Setters
    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public List<ActionConfig> getActions() {
        return actions;
    }

    public void setActions(List<ActionConfig> actions) {
        this.actions = actions;
    }
}
