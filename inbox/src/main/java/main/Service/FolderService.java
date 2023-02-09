package main.Service;

import main.Model.Folder;
import main.Model.UnreadEmailStats;
import main.Repository.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FolderService {
    @Autowired
    UnreadEmailStatsRepository unreadEmailStatsRepository;

    public List<Folder> fetchFolders(String userId) {
        return Arrays.asList(
                new Folder(userId, "Inbox", "blue"),
                new Folder(userId, "Sent Inbox", "green"),
                new Folder(userId, "Important", "red")
        );
    }

    public Map<String, Integer> mapCountToLabels(String userId) {
        List<UnreadEmailStats> stats = unreadEmailStatsRepository.findAllById(userId);
        return stats
                .stream()
                .collect(Collectors.toMap(UnreadEmailStats::getLabel, UnreadEmailStats::getUnreadCount));
    }
}
