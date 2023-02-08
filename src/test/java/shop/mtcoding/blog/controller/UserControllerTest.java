package shop.mtcoding.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.model.User;

/*
 * SpringBootTest는 통합테스트 (실제 환경과 동일하게 Bean이 생성됨) // 통합테스트 = 다 띄운다.
 * AutoConfigureMockMvc는 Mock 환경(가짜 IOC컨테이너)에 MockMvc Bean이 생성됨
 */

@Transactional
@AutoConfigureMockMvc // 안 적어주면 MockMvc다 DI가 안 된다.
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // Mock라고 하면 실제환경과 별개로 가짜 환경이 존재한다. 포트도 랜덤으로 뜬다.
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void join_test() throws Exception {
        // given
        String requestBody = "username=cos&password=1234&email=cos@nate.com";

        // when
        ResultActions resultActions = mvc.perform(post("/join").content(requestBody).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        // then
        resultActions.andExpect(status().is3xxRedirection());
    }

    @Test
    public void login_test() throws Exception {
        // given
        String requestBody = "username=cos&password=1234";

        // when
        // mvc.perform 으로 컨트롤러를 때려볼 수 있다. content로 값을 넣으면 타입을 정해주어야 한다. -> content.Type
        ResultActions resultActions = mvc.perform(post("/login").content(requestBody).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        HttpSession session = resultActions.andReturn().getRequest().getSession();
        User principal = (User) session.getAttribute("principal");
        System.out.println(principal.getUsername());

        // then
        assertThat(principal.getUsername()).isEqualTo("ssar");
        resultActions.andExpect(status().is3xxRedirection());
    }
}