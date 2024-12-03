package com.robmux.quickopen.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.robmux.quickopen.ActionsManager;
import com.robmux.quickopen.config.ActionConfig;
import com.robmux.quickopen.config.ConfigUtil;
import com.robmux.quickopen.config.OpenerConfig;
import com.robmux.quickopen.config.QuickOpenConfig;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class QuickOpenActionGroup extends DefaultActionGroup {

    private static final Logger log = Logger.getInstance(QuickOpenActionGroup.class);

    private final QuickOpenConfig config;

    public QuickOpenActionGroup() {
        this.config = ConfigUtil.loadConfig(); // Load configuration (file or default)
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@NotNull AnActionEvent e) {
        List<AnAction> actions = new ArrayList<>();

        // Dynamically create actions from config
        for (OpenerConfig opener : config.getOpeners()) {
            for (ActionConfig action : opener.getActions()) {
                actions.add(createDynamicAction(action));
            }
        }

        return actions.toArray(new AnAction[0]);
    }

    private AnAction createDynamicAction(ActionConfig actionConfig) {
        return new AnAction("* "+ actionConfig.getName()) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                log.info("Executing action: " + actionConfig.getName());
                var actionsManager = new ActionsManager();
                var project = actionsManager.getProject(e);
                if (actionConfig.getKind().equals( "DIRECT")) {
                    actionsManager.openByProjectType(project, actionConfig);
                    return;
                }

                if (actionConfig.getKind().equals( "INLINE")) {
                    actionsManager.openBySelection(project, actionConfig);
                }
            }
        };
    }
}
