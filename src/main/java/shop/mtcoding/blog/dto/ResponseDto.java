package shop.mtcoding.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ResponseDto<T> {
    private Integer code;
    private String msg;
    private T data;
}
