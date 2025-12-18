package iakka.platform.domain.like.controller;

import iakka.platform.domain.like.entity.Like.LikeType;
import iakka.platform.domain.like.service.LikeService;
import iakka.platform.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{type}/{targetId}")
    public void like(@PathVariable LikeType type,
                     @PathVariable Long targetId,
                     @RequestBody User user) {
        likeService.like(type, targetId, user);
    }

    @DeleteMapping("/{type}/{targetId}")
    public void unlike(@PathVariable LikeType type,
                       @PathVariable Long targetId,
                       @RequestBody User user) {
        likeService.unlike(type, targetId, user);
    }

    @GetMapping("/{type}/{targetId}/liked")
    public boolean isLiked(@PathVariable LikeType type,
                           @PathVariable Long targetId,
                           @RequestBody User user) {
        return likeService.isLiked(type, targetId, user);
    }

    @GetMapping("/{type}/{targetId}/count")
    public long countLikes(@PathVariable LikeType type,
                           @PathVariable Long targetId) {
        return likeService.countLikes(type, targetId);
    }
}
