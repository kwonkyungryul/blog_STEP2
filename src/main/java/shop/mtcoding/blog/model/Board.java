package shop.mtcoding.blog.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Board {
    private Integer id;
    private String title;
    private String content;
    private String thumbnail;
    private Integer userId;
    private Timestamp createdAt;
}
