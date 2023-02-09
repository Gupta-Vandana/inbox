package main.Controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import main.Model.Email;
import main.Model.EmailListItem;
import main.Model.EmailListItemKey;
import main.Model.Folder;
import main.Repository.EmailListItemRepository;
import main.Repository.EmailRepository;
import main.Repository.FolderRepository;
import main.Repository.UnreadEmailStatsRepository;
import main.Service.FolderService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EmailViewController {

    @Autowired
    FolderRepository folderRepository;
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    FolderService folderService;
    @Autowired
    EmailListItemRepository emailListItemRepository;
    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    @GetMapping(value = "/emails/{id}")
    public String emailView(
            @RequestParam(defaultValue = "Inbox") String folder,
            @PathVariable UUID id, @AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        } else {
            //fetch folders
            String userId = principal.getAttribute("login");
            List<Folder> userFolders = folderRepository.findAllById(userId);
            List<Folder> defaultFolders = folderService.fetchFolders(userId);
            model.addAttribute("userFolders", userFolders);
            model.addAttribute("defaultFolders", defaultFolders);

            Optional<Email> optionalEmail = emailRepository.findById(id);
            if (!optionalEmail.isPresent()) {
                return "inbox-page";
            }
            Email email = optionalEmail.get();
            String toIds = String.join(", ", email.getTo());
            model.addAttribute("email", email);
            model.addAttribute("toIds", toIds);

            EmailListItemKey key = new EmailListItemKey();
            key.setId(userId);
            key.setLabel(folder);
            key.setTimeUUID(email.getId());

            Optional<EmailListItem> optionalEmailListItem = emailListItemRepository.findById(key);
            if (optionalEmailListItem.isPresent()) {
                EmailListItem emailListItem = optionalEmailListItem.get();
                if (emailListItem.isUnread()) {
                    emailListItem.setUnread(false);
                    emailListItemRepository.save(emailListItem);
                    unreadEmailStatsRepository.decrementUnreadCount(userId, folder);
                }
            }
            model.addAttribute("stats", folderService.mapCountToLabels(userId));
            return "email-page";
        }
    }
}
