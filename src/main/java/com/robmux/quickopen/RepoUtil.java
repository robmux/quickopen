package com.robmux.quickopen;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.diagnostic.Logger;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RepoUtil {

    private static final Logger LOG = Logger.getInstance(RepoUtil.class);

    public static String findModuleLine(VirtualFile goModFile) {
        LOG.info("Reading go.mod file: " + goModFile.getPath());
        try {
            for (String line : Files.readAllLines(Paths.get(goModFile.getPath()))) {
                if (line.startsWith("module ")) {
                    LOG.debug("Found module line: " + line);
                    return line;
                }
            }
        } catch (IOException ex) {
            LOG.error("Error reading go.mod file", ex);
        }
        LOG.warn("Module line not found in go.mod file");
        return null;
    }

    public static void openInBrowser(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;  // Default to https if no protocol is provided
        }

        LOG.info("Opening URL in browser: " + url);
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
                LOG.info("Successfully opened URL: " + url);
            } catch (IOException | URISyntaxException ex) {
                LOG.error("Error opening URL in browser", ex);
            }
        } else {
            LOG.error("Desktop is not supported on this platform");
        }
    }
}
