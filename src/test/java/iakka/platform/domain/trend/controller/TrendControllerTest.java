package iakka.platform.domain.trend.controller;

import iakka.platform.config.TestSecurityConfig;
import iakka.platform.domain.trend.dto.TrendContentDto;
import iakka.platform.domain.trend.TrendType;
import iakka.platform.domain.trend.service.TrendService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrendController.class)
@Import(TestSecurityConfig.class)
public class TrendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrendService trendService;

    @Test
    void shouldReturnTrendContent() throws Exception {
        TrendContentDto dto = new TrendContentDto();
        dto.setId(1L);
        dto.setTitle("Trending Post");
        dto.setType(TrendType.POST);
        dto.setCurrentViews(150);
        dto.setPastViews(100);
        dto.setIncreasedViews(50);

        when(trendService.getTrendingContent()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/trend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Trending Post"))
                .andExpect(jsonPath("$[0].increasedViews").value(50));
    }
}
