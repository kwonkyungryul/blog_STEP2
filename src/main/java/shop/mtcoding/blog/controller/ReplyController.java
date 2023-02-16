package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.ReplyService;

@Controller
public class ReplyController {

    @Autowired
    private HttpSession session;

    @Autowired
    private ReplyService replyService;

    @DeleteMapping("/reply/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("비정상적인 접근입니다. 로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }

        replyService.댓글삭제(id, principal.getId(), principal.getRole());

        return new ResponseEntity<>(new ResponseDto<>(1, "댓글삭제 성공", null), HttpStatus.OK);
    }

    @PostMapping("/reply")
    public String save(ReplySaveReqDto replySaveReqDto) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("비정상적인 접근입니다. 로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }

        if (replySaveReqDto.getComment() == null || replySaveReqDto.getComment().isEmpty()) {
            throw new CustomApiException("comment를 작성해주세요.");
        }

        if (replySaveReqDto.getBoardId() == null) {
            throw new CustomApiException("boardId가 필요합니다.");
        }

        replyService.댓글쓰기(replySaveReqDto, principal.getId());

        return "redirect:/board/" + replySaveReqDto.getBoardId();
    }


}
