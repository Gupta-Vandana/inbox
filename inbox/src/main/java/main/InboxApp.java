package main;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import main.Model.Email;
import main.Model.EmailListItem;
import main.Model.EmailListItemKey;
import main.Model.Folder;
import main.Repository.EmailRepository;
import main.Repository.FolderRepository;
import main.Repository.EmailListItemRepository;
import main.Repository.UnreadEmailStatsRepository;
import main.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Arrays;

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

    @Autowired FolderRepository folderRepository;
    @Autowired EmailService emailService;

    @Autowired UnreadEmailStatsRepository unreadEmailStatsRepository;

    @PostConstruct
    public void init() {
        folderRepository.save(new Folder("Gupta-Vandana", "Work", "purple"));
        folderRepository.save(new Folder("Gupta-Vandana", "Home", "blue"));
        folderRepository.save(new Folder("Gupta-Vandana", "Family", "brown"));

        for (int i = 0; i < 10; i++) {
            emailService.sendEmail("Gupta-Vandana", Arrays.asList("Gupta-Vandana", "abc"), "Hello" + i, "New Mail");
        }
        emailService.sendEmail("abc",Arrays.asList("abc","def"),"Hello ","Body");
    }

}
