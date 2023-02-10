package shop.mtcoding.blog.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Reply {
    private Integer id;
    private String comment;
    private Integer userId;
    private Integer boardId;
    private Timestamp createdAt;
}
