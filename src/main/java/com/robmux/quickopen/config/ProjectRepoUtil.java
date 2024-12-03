package com.robmux.quickopen.config;

import com.robmux.quickopen.config.ConfigUtil;
import com.robmux.quickopen.config.QuickOpenConfig;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

public class ProjectRepoUtil {
    private static final Logger log = Logger.getInstance(ProjectRepoUtil.class);

    public ProjectRepoUtil() {
    }

    public ProjectType GetProjectType(Project project) {
        return determineProjectType(project);
    }

    // determineProjectType checks if there is a programming language specific file
    // to know the project structure.
    private ProjectType determineProjectType(Project project) {
        VirtualFile[] files = ProjectRootManager.getInstance(project).getContentRoots();

        for (VirtualFile file : files) {
            if (file.findChild("go.mod") != null) {
                return ProjectType.GO;
            }
            if (file.findChild("composer.json") != null && file.findChild("artisan") != null) {
                return ProjectType.LARAVEL;
            }
            if (file.findChild("requirements.txt") != null || file.findChild("setup.py") != null) {
                return ProjectType.PYTHON;
            }
            if (file.findChild("pom.xml") != null || file.findChild("build.gradle") != null) {
                return ProjectType.JAVA;
            }
        }
        return ProjectType.UNSUPPORTED;
    }
}