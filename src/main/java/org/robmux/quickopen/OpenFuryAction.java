package org.robmux.quickopen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.diagnostic.Logger;

public class OpenFuryAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(OpenRepoAction.class);
    private String furyRouteTemplate = "https://web.furycloud.io//summary";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        LOG.info("OpenFuryAction triggered");

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
            String moduleName = moduleLine.replace("module github.com/melisource/fury_", "").trim();
            int insertPosition = furyRouteTemplate.indexOf("//", furyRouteTemplate.indexOf("://") + 3) + 1;

            // use StringBuilder to build the fury url
            StringBuilder updatedUrl = new StringBuilder(furyRouteTemplate);
            updatedUrl.insert(insertPosition, moduleName);

            LOG.info("Opening repository URL: " + updatedUrl.toString());
            RepoUtil.openInBrowser(updatedUrl.toString());
        } else {
            LOG.warn("Module line not found in go.mod");
        }
    }
}
