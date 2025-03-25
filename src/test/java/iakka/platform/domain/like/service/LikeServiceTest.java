package iakka.platform.domain.like.service;

import iakka.platform.domain.like.entity.Like;
import iakka.platform.domain.like.entity.Like.LikeType;
import iakka.platform.domain.like.repository.LikeRepository;
import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeService likeService;

    private final User user = new User();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user.setId(1L);
    }

    @Test
    @DisplayName("좋아요 등록 테스트")
    void likeShouldSaveIfNotExists() {
        when(likeRepository.findByTypeAndTargetIdAndUser(any(), anyLong(), any())).thenReturn(Optional.empty());

        likeService.like(LikeType.IDEA, 200L, user);

        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    @DisplayName("좋아요 해제 테스트")
    void unlikeShouldDeleteIfExists() {
        likeService.unlike(LikeType.POST, 300L, user);
        verify(likeRepository, times(1)).deleteByTypeAndTargetIdAndUser(LikeType.POST, 300L, user);
    }

    @Test
    @DisplayName("좋아요 여부 확인 테스트")
    void isLikedShouldReturnTrueIfPresent() {
        when(likeRepository.findByTypeAndTargetIdAndUser(LikeType.POST, 123L, user))
                .thenReturn(Optional.of(mock(Like.class)));

        boolean result = likeService.isLiked(LikeType.POST, 123L, user);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("좋아요 수 테스트")
    void countLikesShouldReturnCorrectCount() {
        when(likeRepository.countByTypeAndTargetId(LikeType.POST, 321L)).thenReturn(5L);
        long count = likeService.countLikes(LikeType.POST, 321L);
        assertThat(count).isEqualTo(5L);
    }
}
