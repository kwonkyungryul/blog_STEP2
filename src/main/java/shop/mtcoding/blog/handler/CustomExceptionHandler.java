package shop.mtcoding.blog.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.util.Script;

@RestControllerAdvice
public class CustomExceptionHandler {
    
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customException(CustomException e) { // ResponseEntity 데이터를 리턴할 때 쓴다.
        return new ResponseEntity<>(Script.back(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> custimApiException(CustomApiException e) { // ResponseEntity 데이터를 리턴할 때 쓴다.
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), e.getStatus());
    }
}