package main.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import main.Model.Email;
import main.Model.EmailListItem;
import main.Model.EmailListItemKey;
import main.Repository.EmailListItemRepository;
import main.Repository.EmailRepository;
import main.Repository.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailListItemRepository emailListItemRepository;
    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    public void sendEmail(String from, List<String> to, String body, String subject) {
        Email email = new Email();
        email.setTo(to);
        email.setBody(body);
        email.setSubject(subject);
        email.setFrom(from);
        email.setId(Uuids.timeBased());
        emailRepository.save(email);

        to.forEach(toId -> {
            EmailListItem emailListItem = createEmailListItem(to, subject, email, toId, "Inbox");
            emailListItemRepository.save(emailListItem);
            unreadEmailStatsRepository.incrementUnreadCount(toId, "Inbox");
        });
        EmailListItem sentItems = createEmailListItem(to, subject, email, from, "sent");
        sentItems.setUnread(false);
        emailListItemRepository.save(sentItems);
    }

    public boolean doesHaveAccess(Email email, String userId) {
        return (userId.equals(email.getFrom()) || email.getTo().contains(userId));
    }

    public String getReplySubject(String subject) {
        return "Re: " + subject;
    }

    public String getReplyBody(Email email) {
        return "\n\n\n ---------------------- \n" +
                "From: " + email.getFrom() + "\n" +
                "To: " + email.getTo() + "\n\n" +
                email.getBody();
    }

    private EmailListItem createEmailListItem(List<String> to, String subject, Email email, String itemOwner, String folder) {
        EmailListItemKey key = new EmailListItemKey();
        key.setId(itemOwner);
        key.setLabel(folder);
        key.setTimeUUID(email.getId());

        EmailListItem emailListItem = new EmailListItem();
        emailListItem.setKey(key);
        emailListItem.setTo(to);
        emailListItem.setSubject(subject);
        emailListItem.setUnread(true);

        emailListItemRepository.save(emailListItem);
        return emailListItem;
    }
}
