package org.robmux.quickopen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.diagnostic.Logger;

public class OpenRepoAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(OpenRepoAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        LOG.info("OpenRepoAction triggered");

        Project project = e.getProject();
        if (project == null) {
            LOG.warn("Project is null");
            return;
        }

        VirtualFile goModFile = project.getBaseDir().findChild("go.mod");
        if (goModFile == null) {
            LOG.warn("go.mod file not found");
            return;
        }

        LOG.info("go.mod file: " + goModFile.getPath());

        String moduleLine = RepoUtil.findModuleLine(goModFile);
        if (moduleLine != null) {
            String moduleUrl = moduleLine.replace("module ", "").trim();
            LOG.info("Opening repository URL: " + moduleUrl);
            RepoUtil.openInBrowser(moduleUrl);
        } else {
            LOG.warn("Module line not found in go.mod");
        }
    }
}
