package main.Service;

import main.Model.Folder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FolderService {

    public List<Folder> fetchFolders(String userId){
        return Arrays.asList(
                new Folder(userId,"Inbox","blue"),
                new Folder(userId,"Sent Inbox","green"),
                new Folder(userId,"Important","red")
        );
    }
}
