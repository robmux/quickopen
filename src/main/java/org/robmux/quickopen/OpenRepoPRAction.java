package org.robmux.quickopen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.diagnostic.Logger;

public class OpenRepoPRAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(OpenRepoPRAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        LOG.info("OpenRepoPRAction triggered");

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

        String moduleLine = RepoUtil.findModuleLine(goModFile);
        if (moduleLine != null) {
            String moduleUrl = moduleLine.replace("module ", "").trim();
            String pullRequestsUrl = moduleUrl + "/pulls";
            LOG.info("Opening pull requests URL: " + pullRequestsUrl);
            RepoUtil.openInBrowser(pullRequestsUrl);
        } else {
            LOG.warn("Module line not found in go.mod");
        }
    }
}
