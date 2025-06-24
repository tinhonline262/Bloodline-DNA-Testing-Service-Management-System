package com.dna_testing_system.dev.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URI;

@Slf4j
@Configuration
@Component
public class ApplicationInitConfig implements ApplicationRunner {
    public void run(ApplicationArguments args) throws Exception {
        launchBrowser();
    }

    private void launchBrowser() throws Exception{
        if (Desktop.isDesktopSupported())
            Desktop.getDesktop().browse(new URI("http://localhost:8080/v1/index"));
        else {
            log.warn("Desktop is not supported, cannot launch Browser");
            log.info("Starting to launch Browser with OS Call System...");
            String os = System.getProperty("os.name").toLowerCase();
            Runtime runtime = Runtime.getRuntime();
            if (os.contains("win")) {
                // Windows
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:8080/v1/index");
            } else {
                log.warn("Unsupported OS. Cannot open browser automatically.");
            }
        }
    }
}