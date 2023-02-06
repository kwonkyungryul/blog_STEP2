package shop.mtcoding.blog.dto.board;

import lombok.Getter;
import lombok.Setter;

public class BoardResp {
    
    @Setter
    @Getter
    public static class BoardMainResponseDto {
        private Integer id;
        private String title;
        private String username;
        // Thumbnail 추후에 필요
    }
}
