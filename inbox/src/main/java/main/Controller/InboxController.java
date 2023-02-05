package main.Controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import main.Model.EmailListItem;
import main.Model.Folder;
import main.Repository.EmailListItemRepository;
import main.Repository.FolderRepository;
import main.Service.FolderService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class InboxController {
    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderService folderService;

    @Autowired
    private EmailListItemRepository emailListItemRepository;

    @RequestMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println(principal);
        return principal.getAttribute("name");
    }

    @GetMapping(value = "/")
    public String homePage(@RequestParam(required = false) String folder,
                           @AuthenticationPrincipal OAuth2User principal,
                           Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        } else {
            //fetch folders
            if (!StringUtils.hasText(folder)) {
                folder = "Inbox";
            }
            //Gupta-Vandana
            String userId = principal.getAttribute("login");
            List<Folder> userFolders = folderRepository.findAllById(userId);
            List<Folder> defaultFolders = folderService.fetchFolders(userId);
            model.addAttribute("userFolders", userFolders);
            model.addAttribute("defaultFolders", defaultFolders);

            //fetch messages
            List<EmailListItem> emailList = emailListItemRepository.findAllByKey_IdAndKey_Label(userId, folder);
            PrettyTime prettyTime = new PrettyTime();
            emailList.stream().forEach(emailListItem -> {
                UUID uuid = emailListItem.getKey().getTimeUUID();
                Date emailDateTime = new Date(Uuids.unixTimestamp(uuid));
                emailListItem.setAgoTimeString(prettyTime.format(emailDateTime));
            });
            model.addAttribute("emailList", emailList);
            model.addAttribute("folderName", folder);
            return "inbox-page";
        }
    }
}
