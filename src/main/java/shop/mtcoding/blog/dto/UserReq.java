package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

public class UserReq {
    
    @Setter
    @Getter
    public static class joinReqDto {
        private String username;
        private String password;
        private String email;
    }
}
