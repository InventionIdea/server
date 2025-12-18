package iakka.platform.domain.trend.service;

import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.idea.repository.IdeaRepository;
import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.post.repository.PostRepository;
import iakka.platform.domain.trend.TrendType;
import iakka.platform.domain.trend.entity.TrendSnapshot;
import iakka.platform.domain.trend.repository.TrendSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TrendServiceTest {

    private final PostRepository postRepository = mock(PostRepository.class);
    private final IdeaRepository ideaRepository = mock(IdeaRepository.class);
    private final TrendSnapshotRepository snapshotRepository = mock(TrendSnapshotRepository.class);

    private final TrendService trendService = new TrendService(postRepository, ideaRepository, snapshotRepository);

    @Test
    void shouldReturnTrendContentSortedByIncreasedViews() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Post 1");
        post.setViews(120);

        when(postRepository.findAll()).thenReturn(Collections.singletonList(post));

        TrendSnapshot snapshot = new TrendSnapshot();
        snapshot.setViewCountAtSnapshot(100);

        when(snapshotRepository.findLatestSnapshotBefore(eq(TrendType.POST), eq(1L), any(LocalDateTime.class)))
                .thenReturn(Optional.of(snapshot));

        assertThat(trendService.getTrendingContent())
                .hasSize(1)
                .first()
                .matches(dto -> dto.getIncreasedViews() == 20);
    }
}
