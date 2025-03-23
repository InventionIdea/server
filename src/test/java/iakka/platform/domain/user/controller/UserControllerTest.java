package iakka.platform.domain.user.controller;

import iakka.platform.domain.user.service.UserPointService;
import iakka.platform.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserService userService;
    private UserPointService pointService;
    private UserController userController;

    @BeforeEach
    public void 설정() {
        userService = mock(UserService.class);
        pointService = mock(UserPointService.class);
        userController = new UserController(userService, pointService);
    }

    @Test
    public void 포인트_추가_요청시_정상_응답을_반환한다() {
        ResponseEntity<String> response = userController.addPoints(1L, 100);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Points added successfully", response.getBody());
        verify(pointService).addPoints(1L, 100);
    }

    @Test
    public void 포인트_차감_정상처리() {
        ResponseEntity<String> response = userController.deductPoints(1L, 50);
        assertEquals(200, response.getStatusCodeValue());
        verify(pointService).deductPoints(1L, 50);
    }

    @Test
    public void 포인트_차감_실패시_에러메시지_반환한다() {
        doThrow(new RuntimeException("잔액 부족")).when(pointService).deductPoints(1L, 999);

        ResponseEntity<String> response = userController.deductPoints(1L, 999);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("잔액 부족", response.getBody());
    }

    @Test
    public void 포인트_조회시_현재값을_반환한다() {
        when(pointService.getPoints(1L)).thenReturn(500);
        ResponseEntity<Integer> response = userController.getPoints(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(500, response.getBody());
    }
}
