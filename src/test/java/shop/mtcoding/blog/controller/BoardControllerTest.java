package shop.mtcoding.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailResponseDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardMainResponseDto;
import shop.mtcoding.blog.model.User;

/*
 * SpringBootTest는 통합테스트 (실제 환경과 동일하게 Bean이 생성됨) // 통합테스트 = 다 띄운다.
 * AutoConfigureMockMvc는 Mock 환경(가짜 IOC컨테이너)에 MockMvc Bean이 생성됨
 */

@AutoConfigureMockMvc // 안 적어주면 MockMvc다 DI가 안 된다.
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // Mock라고 하면 실제환경과 별개로 가짜 환경이 존재한다. 포트도 랜덤으로 뜬다.
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    private MockHttpSession mockSession;

    @Autowired
    private ObjectMapper om;

    @BeforeEach // Test 메서드 실행 직전에 호출됨
    public void setUp() {
        User user = new User();
        user.setId(1);
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setEmail("ssar@nate.com");
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now())); // 타임스탬프는 현재 시간을 뽑는 메서드가 없다.

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }

    @Test
    public void main_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(
            get("/"));

        resultActions.andExpect(status().isOk());
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<BoardMainResponseDto> dtos = (List<BoardMainResponseDto>) map.get("dtos");
        String model = om.writeValueAsString(dtos);
        System.out.println("테스트 : " + model);

        // then
        assertThat(dtos.size()).isEqualTo(6);
        assertThat(dtos.get(0).getUsername()).isEqualTo("ssar");
        assertThat(dtos.get(0).getTitle()).isEqualTo("1번째 제목");
    }

    @Test
    public void save_test() throws Exception {
        // given
        String title = "가";

        for (int i = 0; i < 99; i++) {
            title += "가";
        }

        String requestBody = "title=" + title + "&content=내용1";

        // when
        ResultActions resultActions = mvc.perform(
            post("/board")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .session(mockSession));

        // then
        resultActions.andExpect(status().is3xxRedirection());
    }

    @Test
    public void detail_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(get("/board/" + id));
        
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        BoardDetailResponseDto dto = (BoardDetailResponseDto) map.get("dto");
        
        System.out.println("테스트 : " + om.writeValueAsString(dto));

        
        // then
        resultActions.andExpect(status().isOk());
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getUsername()).isEqualTo("ssar");
        assertThat(dto.getTitle()).isEqualTo("1번째 제목");
        assertThat(dto.getContent()).isEqualTo("1번째 내용");
        assertThat(dto.getUserId()).isEqualTo(1);
    }

    @Test
    public void delete_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(delete("/board/" + id).session(mockSession));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);
        /*
         * jsonPath
         * 최상위 : $
         * 객체탐색 : 닷(.)
         * 배열 : [0]
         */
        // then
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void update_test() throws Exception {
        // given
        int id = 1;
        BoardUpdateReqDto b = new BoardUpdateReqDto();
        b.setTitle("제목1");
        b.setContent("내용1");
        
        // String requestBody = "title=제목1&content=내용1";

        String json = om.writeValueAsString(b);

        System.out.println(json);

        // when
        ResultActions resultActions = mvc.perform(put("/board/" + id)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .session(mockSession));
        /*
         * jsonPath
         * 최상위 : $
         * 객체탐색 : 닷(.)
         * 배열 : [0]
         */
        // then
        // resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(status().isOk());
    }
}