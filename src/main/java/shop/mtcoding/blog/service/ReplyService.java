package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.ReplyRepository;

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
}
