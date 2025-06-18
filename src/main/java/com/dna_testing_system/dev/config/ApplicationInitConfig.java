package com.dna_testing_system.dev.config;

import com.dna_testing_system.dev.entity.Role;
import com.dna_testing_system.dev.enums.RoleType;
import com.dna_testing_system.dev.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig implements ApplicationRunner {
    RoleRepository roleRepository;

    public void run(ApplicationArguments args) throws Exception {
        initRoleDatabase();
        launchBrowser();
    }

    private void initRoleDatabase() {
        for (RoleType roleType : RoleType.values()) {
            addRoleIfNotExists(roleType);
        }
    }

    private void addRoleIfNotExists(RoleType roleType) {
        if (!roleRepository.existsByRoleName(roleType.name())) {
            Role role = Role.builder()
                    .roleName(roleType.name())
                    .roleDescription(roleType.getDescription())
                    .isActive(true)
                    .build();
            roleRepository.save(role);
        }
    }

    private void launchBrowser() throws Exception{
        if (Desktop.isDesktopSupported())
            Desktop.getDesktop().browse(new URI("http://localhost:8080/index"));
        else {
            log.warn("Desktop is not supported, cannot launch Browser");
            log.info("Starting to launch Browser with OS Call System...");
            String os = System.getProperty("os.name").toLowerCase();
            Runtime runtime = Runtime.getRuntime();
            if (os.contains("win")) {
                // Windows
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:8080/index");
            } else {
                log.warn("Unsupported OS. Cannot open browser automatically.");
            }
        }
    }
}
