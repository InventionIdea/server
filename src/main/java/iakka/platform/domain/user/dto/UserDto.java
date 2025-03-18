package iakka.platform.domain.user.dto;

import iakka.platform.domain.user.entity.User;

public class UserDto {
    private Long id;
    private String username;
    private int points;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.points = user.getPoints();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public int getPoints() { return points; }
}
