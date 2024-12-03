package com.robmux.quickopen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.robmux.quickopen.config.*;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.diagnostic.Logger;

import static com.robmux.quickopen.OpenRepoAction.getDefaultActionConfigs;
import static com.robmux.quickopen.OpenRepoAction.getGolangRepoConfig;
import static com.robmux.quickopen.config.ProjectType.GO;

public class OpenRepoPRAction extends AnAction {

    private static final Logger log = Logger.getInstance(OpenRepoPRAction.class);

    private final ProjectRepoUtil projectRepoUtil = new ProjectRepoUtil();
    private final QuickOpenConfig config = ConfigUtil.loadConfig();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        log.info("OpenRepoPRAction triggered");

        Project project = e.getProject();
        if (project == null) {
            log.warn("Project is null");
            return;
        }

        ProjectType projectType = projectRepoUtil.GetProjectType(project);
        switch (projectType) {
            case GO:
                openGolangRepoPRs(project);
                break;
            default:
                log.warn("Unsupported project type");
                break;
        }

    }

    private void openGolangRepoPRs(Project project) {
        OpenerConfig openerConfig = config.getOpenerForType(GO.toString());
        if (openerConfig == null) {
            openerConfig = getGolangRepoConfig();
            openerConfig.setActions(getDefaultActionConfigs(openerConfig));
        }


        // TODO: Refactor to create dynamic actions and get the correct index
        VirtualFile goModFile = project.getBaseDir().findChild(openerConfig.getActions().get(1).getFile());
        if (goModFile == null) {
            log.warn("go.mod file not found");
            return;
        }

        String moduleLine = RepoUtil.findModuleLine(goModFile);
        if (moduleLine != null) {
            String moduleUrl = moduleLine.replace("module ", "").trim();
            String pullRequestsUrl = openerConfig.getActions().get(1).getUrlTemplate().replace("{source}", moduleUrl);
            log.info("Opening pull requests URL: " + pullRequestsUrl);
            RepoUtil.openInBrowser(pullRequestsUrl);
        } else {
            log.warn("Module line not found in go.mod");
        }
    }
}
