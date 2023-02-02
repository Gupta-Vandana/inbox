package main.Controller;

import main.Model.Folder;
import main.Repository.FolderRepository;
import main.Service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class InboxController {
    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderService folderService;

    @RequestMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println(principal);
        return principal.getAttribute("name");
    }

    @GetMapping(value = "/")
    public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("name"))) {
            return "index";
        } else {
            String userId = principal.getAttribute("name");
            List<Folder> userFolders = folderRepository.findAllById(userId);
            List<Folder> defaultFolders = folderService.fetchFolders(userId);
            model.addAttribute("folders", userFolders);
            model.addAttribute("defaultFolders",defaultFolders);
            return "inbox-page";
        }

    }

}
