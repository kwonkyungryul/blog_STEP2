package shop.mtcoding.blog.dto.board;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

public class BoardResp {
    
    @Setter
    @Getter
    public static class BoardMainResponseDto {
        private Integer id;
        private String title;
        private String thumbnail;
        private String username;
        private Timestamp createdAt;
    }

    @Setter
    @Getter
    public static class BoardDetailResponseDto {
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private String username;
    }
}
