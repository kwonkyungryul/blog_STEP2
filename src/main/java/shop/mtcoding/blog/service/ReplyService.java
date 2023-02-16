package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.ReplyRepository;

@Slf4j
@Transactional(readOnly = true)
@Service
public class ReplyService {

    @Autowired
    ReplyRepository replyRepository;
    
    // where 절에 걸리는 파라미터를 앞에 받기 (규칙)
    @Transactional // (rollbackFor = RuntimeException.class)
    public void 댓글쓰기(ReplySaveReqDto replySaveReqDto, int principalId) {
        // 1. content 내용을 document로 받고, img 찾아내서(0, 1, 2) src를 찾아서 thumbnail 추가
        int result = replyRepository.insert(replySaveReqDto.getComment(), replySaveReqDto.getBoardId(), principalId);

        if (result != 1) {
            throw new CustomException("정상적으로 작성되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR); // 500번 에러
        }
    }

    
    @Transactional
    public void 댓글삭제(int id, int principalId, String principalRole) {
        if (!principalRole.equals("ADMIN")) {
            throw new CustomApiException("관리자만 허용된 기능입니다.");
        }

        Reply replyPS = replyRepository.findById(id);
        if (replyPS == null) {
            throw new CustomApiException("댓글이 존재하지 않습니다.");
        }

        if (!principalRole.equals("ADMIN")) {
            if (replyPS.getUserId() != principalId) {
                throw new CustomApiException("해당 댓글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }
        // 1. 인증 OK, 2. 댓글 존재유무 확인, 3. 권한 OK
        try {
            replyRepository.deleteById(id);
        } catch (Exception e) {
            log.error("서버에러" + e.getMessage());
            // 버퍼달고 파일에 쓰기.
            throw new CustomApiException("댓글삭제 실패.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
