package shop.mtcoding.blog.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardResp.BoardMainResponseDto;

@MybatisTest
public class BoardRepositoryTest {
    
    @Autowired
    private BoardRepository boardRepository; // F(X) - DS(X) - C(X) - S(X) - R(O) - MyBatis(O) - DB(O)  메모리에 필요한 것만 띄운다(@MybatisTest)

    @Test
    public void findAllWithUser_test() throws Exception {
        // given
        ObjectMapper om = new ObjectMapper(); // Jackson라이브러리가 들고있음

        // when
        List<BoardMainResponseDto> boardMainResponseDto = boardRepository.findAllWithUser();
        String responseBody = om.writeValueAsString(boardMainResponseDto);
        System.out.println("테스트 : size : " + responseBody);

        // then
        assertThat(boardMainResponseDto.get(5).getUsername()).isEqualTo("love");
    }
}