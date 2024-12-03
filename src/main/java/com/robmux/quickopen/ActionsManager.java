package com.robmux.quickopen;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.robmux.quickopen.config.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.robmux.quickopen.config.ProjectType.GO;

public class ActionsManager {
    private static final Logger log = Logger.getInstance(OpenRepoAction.class);
    private final ProjectRepoUtil projectRepoUtil = new ProjectRepoUtil();

    public Project getProject(@NotNull AnActionEvent event) {
        return event.getProject();
    }

    public void openByProjectType(@NotNull Project project, @NotNull ActionConfig config) {
        ProjectType projectType = projectRepoUtil.GetProjectType(project);
        switch (projectType) {
            case GO:
                openGolangRepo(project, config);
                break;
            default:
                log.warn("Unsupported project type " + projectType);
                break;
        }
    }

    public void openBySelection(@NotNull Project project, @NotNull ActionConfig config) {
        ProjectType projectType = projectRepoUtil.GetProjectType(project);
        switch (projectType) {
            case GO:
                openGolangRepo(project, config);
                break;
            default:
                log.warn("Unsupported project type " + projectType);
                break;
        }
    }

    private void openGolangRepo(Project project, ActionConfig config) {
        var file = config.getFile();
        VirtualFile goModFile = project.getBaseDir().findChild(file);
        if (goModFile == null) {
            log.warn("go.mod file not found");
            return;
        }

        log.info("go.mod file: " + goModFile.getPath());

        String moduleLine = RepoUtil.findModuleLine(goModFile);

        if (moduleLine == null) {
            log.warn("go.mod file not found");
            return;
        }

        String repoUrl = getUrl(config, moduleLine);
        log.info("Opening repository URL: " + repoUrl);
        RepoUtil.openInBrowser(repoUrl);
    }

    private static @NotNull String getUrl(ActionConfig config, String moduleLine) {
        String searchTemplate = config.getFind();

        int startIdx = searchTemplate.indexOf("{source}");
        int endIdx = moduleLine.length();
        String firstPart = moduleLine.substring(0, startIdx);
        // TODO: Get the source with regex or another strategy
        String secondPart = moduleLine.substring(endIdx);

        String onlySource = moduleLine.replaceAll(firstPart, "");
        onlySource = onlySource.replaceAll(secondPart, "").strip();


        String templateURL = config.getUrlTemplate();
        return templateURL.replace("{source}", onlySource);
    }

    public static OpenerConfig getGolangRepoConfig() {
        OpenerConfig openerConfig = new OpenerConfig();
        openerConfig.setBehavior("GOLANG_DEFAULT");
        openerConfig.setProgram("BROWSER");
        return openerConfig;
    }

    public static List<ActionConfig> getDefaultActionConfigs(OpenerConfig openerConfig) {
        ActionConfig repoAction = new ActionConfig();
        repoAction.setFile("go.mod");
        repoAction.setKind("DIRECT");
        repoAction.setName("Open " + openerConfig.getBehavior() + " repo");
        repoAction.setUrlTemplate("https://{source}");

        ActionConfig repoPRsAction = new ActionConfig();
        repoPRsAction.setFile("go.mod");
        repoPRsAction.setKind("DIRECT");
        repoPRsAction.setName("Open " + openerConfig.getBehavior() + " repo prs");
        repoPRsAction.setUrlTemplate("https://{source}/pulls");

        List<ActionConfig> defaultActions = new ArrayList<>();
        defaultActions.add(repoAction);
        defaultActions.add(repoPRsAction);
        return defaultActions;
    }

}
