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
    @Autowired
    EmailRepository emailRepository;

    @Autowired
    UnreadEmailStatsRepository unreadEmailStatsRepository;

    @PostConstruct
    public void init() {
        folderRepository.save(new Folder("Gupta-Vandana", "sent", "purple"));
        folderRepository.save(new Folder("Gupta-Vandana", "Inbox", "blue"));
        folderRepository.save(new Folder("Gupta-Vandana", "draft", "brown"));

        unreadEmailStatsRepository.incrementUnreadCount("Gupta-Vandana","Inbox");
        unreadEmailStatsRepository.incrementUnreadCount("Gupta-Vandana","Inbox");
        unreadEmailStatsRepository.incrementUnreadCount("Gupta-Vandana","Inbox");

        for (int i = 0; i < 10; i++) {
            EmailListItemKey key = new EmailListItemKey();
            key.setId("Gupta-Vandana");
            key.setLabel("Inbox");
            key.setTimeUUID(Uuids.timeBased());

            EmailListItem emailListItem = new EmailListItem();
            emailListItem.setKey(key);
            emailListItem.setTo(Arrays.asList("Gupta-Vandana", "abc", "def"));
            emailListItem.setSubject("Subject " + i);
            emailListItem.setUnread(true);

            emailListItemRepository.save(emailListItem);

            Email email = new Email();
            email.setId(key.getTimeUUID());
            email.setFrom("Gupta-Vandana");
            email.setSubject(emailListItem.getSubject());
            email.setBody("Body " + i);
            email.setTo(emailListItem.getTo());
            emailRepository.save(email);

        }
    }

}
