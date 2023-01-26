package main;

import main.Model.Folder;
import main.Repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

@SpringBootApplication
public class InboxApp {

    public static void main(String[] args) {
        SpringApplication.run(InboxApp.class, args);
    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> {
            builder.withCloudSecureConnectBundle(bundle);
        };
    }
@Autowired   FolderRepository folderRepository;

    @PostConstruct
    public void init() {
		folderRepository.save(new Folder("Gupta-Vandana","sent","purple"));
        folderRepository.save(new Folder("Gupta-Vandana","inbox","blue"));
        folderRepository.save(new Folder("Gupta-Vandana","draft","brown"));
    }

}
