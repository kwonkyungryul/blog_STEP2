package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.BoardService;

@Controller
public class BoardController {

    @Autowired
    HttpSession session;

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @PostMapping("/juso")
    public @ResponseBody String callback(String roadFullAddr) {
        System.out.println(roadFullAddr);
        RestTemplate rt = new RestTemplate(); // 다른 서버에게 http 요청할 때 제일 많이 씀
        return "ok";
    }

    @PutMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> update(@PathVariable int id, @RequestBody BoardUpdateReqDto boardUpdateReqDto) {
        // Spring의 기본 파싱 전략은 x-www-form-url-encoded다.
        // RequestBody를 붙이면 버퍼 그대로 읽는다. 그 다음 컨텐트 타입이 text면 그대로 준다. 컨텐트 타입이 json이면 앞에 적힌 타입으로 파싱해준다.
        // System.out.println(boardUpdateReqDto.getTitle());
        // System.out.println(boardUpdateReqDto.getContent());
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("비정상적인 접근입니다. 로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }

        if (boardUpdateReqDto.getTitle() == null || boardUpdateReqDto.getTitle().isEmpty()) {
            throw new CustomApiException("title을 작성해주세요.");
        }

        if (boardUpdateReqDto.getTitle().length() > 100) {
            throw new CustomApiException("title의 길이가 100자 이하여야 합니다.");
        }

        if (boardUpdateReqDto.getContent() == null || boardUpdateReqDto.getContent().isEmpty()) {
            throw new CustomApiException("content를 작성해주세요.");
        }

        boardService.게시글수정(id, principal.getId(), boardUpdateReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "수정성공", null), HttpStatus.OK); // HttpStatus.CREATED = 201 // 인서트(업데이트)가 잘 됐을 때는 201
    }

    @DeleteMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("비정상적인 접근입니다. 로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }

        boardService.게시글삭제(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제성공", null), HttpStatus.OK);
    }

    @PostMapping("/board")
    public @ResponseBody ResponseEntity<?> save(@RequestBody BoardSaveReqDto boardSaveReqDto) {
        // ResponseBody는 뷰리졸버 대신에 메세지컨버터가 발동한다.
        // 메세지 컨버터는 버퍼에 쓸 때 자바 오브젝트를 Json으로 변환해서 넘겨준다.
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("비정상적인 접근입니다. 로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }

        if (boardSaveReqDto.getTitle() == null || boardSaveReqDto.getTitle().isEmpty()) {
            throw new CustomApiException("title을 작성해주세요.");
        }

        if (boardSaveReqDto.getTitle().length() > 100) {
            throw new CustomApiException("title의 길이가 100자 이하여야 합니다.");
        }

        if (boardSaveReqDto.getContent() == null || boardSaveReqDto.getContent().isEmpty()) {
            throw new CustomApiException("content를 작성해주세요.");
        }

        boardService.글쓰기(boardSaveReqDto, principal.getId());
        

        return new ResponseEntity<>(new ResponseDto<>(1, "정상적으로 완료되었습니다.", null), HttpStatus.CREATED);
    }

    @GetMapping({"/", "/board"})
    public String main(Model model) {
        model.addAttribute("dtos", boardRepository.findAllWithUser());
        return "board/main";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, Model model) {
        model.addAttribute("dto", boardRepository.findByIdWithUser(id));
        return "board/detail";
    }

    @GetMapping("/board/saveForm")
    public String writeForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, Model model) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("비정상적인 접근입니다. 로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }

        Board boardPS = boardRepository.findById(id); // PS = Persistence -> 영구히 저장하다 라는 뜻
        if (boardPS == null) {
            throw new CustomException("없는 게시글을 수정할 수 없습니다.");
        }

        if (principal.getId() != boardPS.getUserId()) {
            throw new CustomException("해당 게시물을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }


        model.addAttribute("board", boardRepository.findById(id));
        return "board/updateForm";
    }
    
}