package iakka.platform.domain.trend.repository;

import iakka.platform.domain.trend.TrendType;
import iakka.platform.domain.trend.entity.TrendSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TrendSnapshotRepository extends JpaRepository<TrendSnapshot, Long> {

    @Query("SELECT ts FROM TrendSnapshot ts WHERE ts.type = :type AND ts.targetId = :targetId AND ts.snapshotAt = (SELECT MAX(s.snapshotAt) FROM TrendSnapshot s WHERE s.type = :type AND s.targetId = :targetId AND s.snapshotAt <= :before)")
    Optional<TrendSnapshot> findLatestSnapshotBefore(TrendType type, Long targetId, LocalDateTime before);
}
