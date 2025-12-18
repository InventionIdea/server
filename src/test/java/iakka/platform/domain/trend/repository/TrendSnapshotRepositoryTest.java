package iakka.platform.domain.trend.repository;

import iakka.platform.domain.trend.TrendType;
import iakka.platform.domain.trend.entity.TrendSnapshot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TrendSnapshotRepositoryTest {

    @Autowired
    private TrendSnapshotRepository repository;

    @Test
    void shouldSaveAndRetrieveSnapshot() {
        TrendSnapshot snapshot = new TrendSnapshot();
        snapshot.setType(TrendType.POST);
        snapshot.setTargetId(1L);
        snapshot.setViewCountAtSnapshot(100);
        snapshot.setSnapshotAt(LocalDateTime.now().minusDays(1));

        repository.save(snapshot);

        TrendSnapshot found = repository.findLatestSnapshotBefore(TrendType.POST, 1L, LocalDateTime.now())
                .orElseThrow();

        assertThat(found.getViewCountAtSnapshot()).isEqualTo(100);
    }
}
