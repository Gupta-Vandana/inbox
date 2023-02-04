package main;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import main.Model.EmailListItem;
import main.Model.EmailListItemKey;
import main.Model.Folder;
import main.Repository.FolderRepository;
import main.Repository.EmailListItemRepository;
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

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    EmailListItemRepository emailListItemRepository;

    @PostConstruct
    public void init() {
        folderRepository.save(new Folder("Gupta-Vandana", "sent", "purple"));
        folderRepository.save(new Folder("Gupta-Vandana", "inbox", "blue"));
        folderRepository.save(new Folder("Gupta-Vandana", "draft", "brown"));

        for (int i = 0; i < 10; i++) {
            EmailListItemKey key = new EmailListItemKey();
            key.setId("Gupta-Vandana");
            key.setLabel("Inbox");
            key.setTimeUUID(Uuids.timeBased());

            EmailListItem emailListItem = new EmailListItem();
            emailListItem.setKey(key);
            emailListItem.setTo(Arrays.asList("Gupta-Vandana"));
            emailListItem.setSubject("Subject " + i);
            emailListItem.setUnread(true);

            emailListItemRepository.save(emailListItem);
        }
    }

}
