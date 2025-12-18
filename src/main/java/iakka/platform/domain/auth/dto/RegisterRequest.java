package iakka.platform.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String userId;
    private String username;
    private String password;
    private String realName;
    private String phoneNumber;
}
