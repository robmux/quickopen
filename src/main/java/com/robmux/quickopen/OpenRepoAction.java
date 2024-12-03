package com.robmux.quickopen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.diagnostic.Logger;

import com.robmux.quickopen.config.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.robmux.quickopen.config.ProjectType.GO;

public class OpenRepoAction extends AnAction {

    private static final Logger log = Logger.getInstance(OpenRepoAction.class);
    private final QuickOpenConfig config = ConfigUtil.loadConfig();
    private final ProjectRepoUtil projectRepoUtil = new ProjectRepoUtil();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        log.info("OpenRepoAction triggered");

        Project project = e.getProject();
        if (project == null) {
            log.warn("Project is null");
            return;
        }

        ProjectType projectType = projectRepoUtil.GetProjectType(project);
        switch (projectType) {
            case GO:
                openGolangRepo(project);
                break;
           default:
                log.warn("Unsupported project type "+projectType);
                break;
        }
    }

    private void openGolangRepo(Project project) {
        OpenerConfig openerConfig = config.getOpenerForType(GO.toString());
        if (openerConfig == null) {
            openerConfig = getGolangRepoConfig();
            openerConfig.setActions(getDefaultActionConfigs(openerConfig));
        }

        // TODO: Refactor to create dynamic actions and get the correct index
        var file = openerConfig.getActions().get(0).getFile();
        VirtualFile goModFile = project.getBaseDir().findChild(file);
        if (goModFile == null) {
            log.warn("go.mod file not found");
            return;
        }

        log.info("go.mod file: " + goModFile.getPath());

        String moduleLine = RepoUtil.findModuleLine(goModFile);
        if (moduleLine != null) {
            String module = moduleLine.replace("module ", "").trim();
            String templateURL = openerConfig.getActions().get(0).getUrlTemplate();
            String repoUrl = templateURL.replace("{source}", module);
            log.info("Opening repository URL: " + repoUrl);
            RepoUtil.openInBrowser(repoUrl);
        } else {
            log.warn("Module line not found in go.mod");
        }
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
        repoAction.setName("Open "+openerConfig.getBehavior() + " repo");
        repoAction.setUrlTemplate("https://{source}");

        ActionConfig repoPRsAction = new ActionConfig();
        repoPRsAction.setFile("go.mod");
        repoPRsAction.setKind("DIRECT");
        repoPRsAction.setName("Open "+openerConfig.getBehavior() + " repo prs");
        repoPRsAction.setUrlTemplate("https://{source}/pulls");

        List<ActionConfig> defaultActions = new ArrayList<>();
        defaultActions.add(repoAction);
        defaultActions.add(repoPRsAction);
        return defaultActions;
    }


}
