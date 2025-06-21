package com.dna_testing_system.dev.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.awt.Desktop;
import java.net.URI;

@Configuration
public class ApplicationInitConfig implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ApplicationInitConfig.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        launchBrowser();
    }

    private void launchBrowser() throws Exception {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI("http://localhost:8080/index"));
        } else {
            log.warn("Desktop is not supported, cannot launch Browser");
            log.info("Starting to launch Browser with ProcessBuilder...");
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                // Windows
                ProcessBuilder pb = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", "http://localhost:8080/index");
                pb.start();
            } else {
                log.warn("Unsupported OS. Cannot open browser automatically.");
            }
        }
    }
}