package main.Controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import main.Model.Email;
import main.Model.EmailListItem;
import main.Model.Folder;
import main.Repository.EmailRepository;
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
import org.springframework.web.bind.annotation.PathVariable;

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
    @GetMapping(value = "/emails/{id}")
    public String emailView(@PathVariable UUID id, @AuthenticationPrincipal OAuth2User principal, Model model) {
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
            if(!optionalEmail.isPresent()){
                return "inbox-page";
            }
            model.addAttribute("email",optionalEmail.get());
            return "email-page";
        }
    }
}
