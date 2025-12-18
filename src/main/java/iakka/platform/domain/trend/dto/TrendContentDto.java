package iakka.platform.domain.trend.dto;

import iakka.platform.domain.trend.TrendType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendContentDto {
    private Long id;
    private String title;
    private TrendType type;
    private int currentViews;
    private int pastViews;
    private int increasedViews;
}
