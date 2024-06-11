package org.robmux.quickopen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenRepoAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        VirtualFile goModFile = project.getBaseDir().findChild("go.mod");
        if (goModFile == null) {
            return;
        }

        String moduleLine = findModuleLine(goModFile);
        if (moduleLine != null) {
            String moduleUrl = moduleLine.replace("module ", "").trim();
            openInBrowser(moduleUrl);
        }
    }

    private String findModuleLine(VirtualFile goModFile) {
        try {
            for (String line : Files.readAllLines(Paths.get(goModFile.getPath()))) {
                if (line.startsWith("module ")) {
                    return line;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void openInBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(URI.create(url));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}