package iakka.platform.domain.trend.service;

import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.idea.repository.IdeaRepository;
import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.post.repository.PostRepository;
import iakka.platform.domain.trend.TrendType;
import iakka.platform.domain.trend.dto.TrendContentDto;
import iakka.platform.domain.trend.entity.TrendSnapshot;
import iakka.platform.domain.trend.repository.TrendSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrendService {

    private final PostRepository postRepository;
    private final IdeaRepository ideaRepository;
    private final TrendSnapshotRepository snapshotRepository;

    public List<TrendContentDto> getTrendingContent() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime snapshotBaseline = now.minusDays(1);

        List<TrendContentDto> result = new ArrayList<>();

        for (Post post : postRepository.findAll()) {
            int currentViews = post.getViews();
            int pastViews = snapshotRepository
                    .findLatestSnapshotBefore(TrendType.POST, post.getId(), snapshotBaseline)
                    .map(TrendSnapshot::getViewCountAtSnapshot)
                    .orElse(0);

            result.add(TrendContentDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .type(TrendType.POST)
                    .currentViews(currentViews)
                    .pastViews(pastViews)
                    .increasedViews(currentViews - pastViews)
                    .build());
        }

        for (Idea idea : ideaRepository.findAll()) {
            int currentViews = idea.getViews();
            int pastViews = snapshotRepository
                    .findLatestSnapshotBefore(TrendType.IDEA, idea.getId(), snapshotBaseline)
                    .map(TrendSnapshot::getViewCountAtSnapshot)
                    .orElse(0);

            result.add(TrendContentDto.builder()
                    .id(idea.getId())
                    .title(idea.getTitle())
                    .type(TrendType.IDEA)
                    .currentViews(currentViews)
                    .pastViews(pastViews)
                    .increasedViews(currentViews - pastViews)
                    .build());
        }

        result.sort(Comparator.comparingInt(TrendContentDto::getIncreasedViews).reversed());
        return result;
    }
}
