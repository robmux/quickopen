package com.robmux.quickopen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.diagnostic.Logger;

import com.robmux.quickopen.config.ProjectRepoUtil;
import com.robmux.quickopen.config.ProjectType;
import org.jetbrains.annotations.NotNull;

import com.robmux.quickopen.config.ConfigUtil;
import com.robmux.quickopen.config.QuickOpenConfig;

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
                log.warn("Unsupported project type");
                break;
        }
    }

    private void openGolangRepo(Project project) {
        VirtualFile goModFile = project.getBaseDir().findChild("go.mod");
        if (goModFile == null) {
            log.warn("go.mod file not found");
            return;
        }

        log.info("go.mod file: " + goModFile.getPath());

        String moduleLine = RepoUtil.findModuleLine(goModFile);
        if (moduleLine != null) {
            String module = moduleLine.replace("module ", "").trim();
            String repoUrl = config.getRepoUrlTemplate().replace("{repo}", module);
            log.info("Opening repository URL: " + repoUrl);
            RepoUtil.openInBrowser(repoUrl);
        } else {
            log.warn("Module line not found in go.mod");
        }
    }

}
