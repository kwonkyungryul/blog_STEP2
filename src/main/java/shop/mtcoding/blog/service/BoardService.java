package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.util.HtmlParse;

@Transactional(readOnly = true)
@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;
    
    // where 절에 걸리는 파라미터를 앞에 받기 (규칙)
    @Transactional // (rollbackFor = RuntimeException.class)
    public void 글쓰기(BoardSaveReqDto boardSaveReqDto, int userId) {
        // 1. content 내용을 document로 받고, img 찾아내서(0, 1, 2) src를 찾아서 thumbnail 추가
        String thumbnail = HtmlParse.getThumbnail(boardSaveReqDto.getContent());


        int result = boardRepository.insert(boardSaveReqDto.getTitle(), boardSaveReqDto.getContent(),  thumbnail, userId);

        if (result != 1) {
            throw new CustomApiException("정상적으로 작성되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR); // 500번 에러
        }
    }

    @Transactional
    public void 게시글삭제(int id, int userId) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomApiException("없는 게시글을 삭제할 수 없습니다.");
        }

        if (userId != boardPS.getUserId()) {
            throw new CustomApiException("해당 게시물을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        try { // 제어권을 가져옴
            boardRepository.deleteById(id);
        } catch(Exception e) {
            // e.getMessage() 대신에 "서버의 일시적인 문제가 생겼습니다" 라고 남기는 이유는, 공격자가 온갖 공격을 하는데에 있어서 에러 메세지를 자세하게 이런이런 부분에서 에러가 떴다고 보여줄 필요가 없다.
            throw new CustomApiException("서버의 일시적인 문제가 생겼습니다.", HttpStatus.INTERNAL_SERVER_ERROR); // 500번 에러
            // 로그를 남겨야 함 (DB, File) -> e.getMessage(), 시간, request정보(IP, 연결기기 ...등) 등등..
        }
    }

    @Transactional
    public void 게시글수정(int id, int principalId, BoardUpdateReqDto boardUpdateReqDto) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomApiException("없는 게시글을 수정할 수 없습니다.");
        }

        if (principalId != boardPS.getUserId()) {
            throw new CustomApiException("해당 게시물을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        String thumbnail = HtmlParse.getThumbnail(boardUpdateReqDto.getContent());

        try { // 제어권을 가져옴
            boardRepository.updateById(id, boardUpdateReqDto.getTitle(), boardUpdateReqDto.getContent(), thumbnail);
        } catch(Exception e) {
            // e.getMessage() 대신에 "서버의 일시적인 문제가 생겼습니다" 라고 남기는 이유는, 공격자가 온갖 공격을 하는데에 있어서 에러 메세지를 자세하게 이런이런 부분에서 에러가 떴다고 보여줄 필요가 없다.
            throw new CustomApiException("서버의 일시적인 문제가 생겼습니다", HttpStatus.INTERNAL_SERVER_ERROR); // 500번 에러
            // 로그를 남겨야 함 (DB, File) -> e.getMessage(), 시간, request정보(IP, 연결기기 ...등) 등등..
        }
    }
}
