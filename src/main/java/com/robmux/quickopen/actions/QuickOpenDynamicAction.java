package com.robmux.quickopen.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.robmux.quickopen.RepoUtil;
import com.robmux.quickopen.config.ActionConfig;
import org.jetbrains.annotations.NotNull;

public class QuickOpenDynamicAction extends AnAction {
    private final ActionConfig actionConfig;
    private final String behavior;

    public QuickOpenDynamicAction(ActionConfig actionConfig, String behavior) {
        super(actionConfig.getName());
        this.actionConfig = actionConfig;
        this.behavior = behavior;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Logic to execute the action based on the behavior and actionConfig
        if ("DIRECT".equals(actionConfig.getKind())) {
            String url = actionConfig.getUrlTemplate().replace("{source}", "detectedSource");

            RepoUtil.openInBrowser(url);
        } else if ("INLINE".equals(actionConfig.getKind())) {
            // Handle inline action logic
        }
    }
}
