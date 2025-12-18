package iakka.platform.domain.trend.entity;

import iakka.platform.domain.trend.TrendType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrendSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TrendType type;

    private Long targetId;

    private int viewCountAtSnapshot;

    private LocalDateTime snapshotAt;
}
