package iakka.platform.domain.trend.controller;

import iakka.platform.domain.trend.dto.TrendContentDto;
import iakka.platform.domain.trend.service.TrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trend")
public class TrendController {

    private final TrendService trendService;

    @GetMapping
    public List<TrendContentDto> getTrends() {
        return trendService.getTrendingContent();
    }
}
