package iakka.platform.domain.user.controller;

import iakka.platform.domain.user.service.UserPointService;
import iakka.platform.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

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

    @Test
    public void 사용자_본인이_요청하면_삭제된다() {
        Long userId = 1L;
        UserDetails currentUser = mock(UserDetails.class);
        when(currentUser.getUsername()).thenReturn("user1");

        when(userService.isCurrentUser(userId, currentUser)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(userId, currentUser);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService).deleteUserById(userId);
    }

    @Test
    public void 본인이_아닌_경우_삭제_거부된다() {
        Long userId = 1L;
        UserDetails currentUser = mock(UserDetails.class);
        when(currentUser.getUsername()).thenReturn("hacker");

        when(userService.isCurrentUser(userId, currentUser)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(userId, currentUser);

        assertEquals(403, response.getStatusCodeValue());
        verify(userService, never()).deleteUserById(userId);
    }
}
