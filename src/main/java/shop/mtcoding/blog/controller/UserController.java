package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.user.UserReq.JoinReqDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;
import shop.mtcoding.blog.service.UserService;

@Controller
public class UserController {

    @Autowired
    HttpSession session;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @DeleteMapping("/user/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("비정상적인 접근입니다. 로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }

        userService.유저삭제(id, principal.getRole());
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제성공", null), HttpStatus.OK);
    }

    @PutMapping("/user/profileUpdate")
    public ResponseEntity<?> profileUpdate(MultipartFile profile){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("잘못된 접근입니다. 로그인 후 이용해주세요", HttpStatus.UNAUTHORIZED);
        }

        if (profile.isEmpty()) {
            throw new CustomApiException("사진이 전송되지 않았습니다.");
        }

        // 사진이 아니면 Exception 터트리기
        System.out.println(profile.getContentType());
        if (!profile.getContentType().startsWith("image")) {
            throw new CustomApiException("이미지 파일만 등록이 가능합니다.");
        }

        User userPS = userService.프로필사진수정(principal.getId(), profile);
        session.setAttribute("principal", userPS);
        
        return new ResponseEntity<>(new ResponseDto<>(1, "프로필 수정이 완료되었습니다.", null), HttpStatus.OK);
    }

    @GetMapping("/user/profileUpdateForm")
    public String profileUpdateForm(Model model){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/loginForm";
        }

        User userPS = userRepository.findById(principal.getId());
        model.addAttribute("user", userPS);

        return "user/profileUpdateForm";
    }

    @PostMapping("/join")
    public String join(JoinReqDto joinReqDto) {

        if (joinReqDto.getUsername() == null || joinReqDto.getUsername().isEmpty()) {
            throw new CustomException("username이 존재하지 않습니다.");
        }

        if (joinReqDto.getPassword() == null || joinReqDto.getPassword().isEmpty()) {
            throw new CustomException("password이 존재하지 않습니다.");
        }

        if (joinReqDto.getEmail() == null || joinReqDto.getEmail().isEmpty()) {
            throw new CustomException("email이 존재하지 않습니다.");
        }

        userService.회원가입(joinReqDto);


        return "redirect:/loginForm"; // 302
    }

    @PostMapping("/login")
    public String login(LoginReqDto loginReqDto) {
        if (loginReqDto.getUsername() == null || loginReqDto.getUsername().isEmpty()) {
            throw new CustomException("username을 작성해주세요.");
        }

        if (loginReqDto.getPassword() == null || loginReqDto.getPassword().isEmpty()) {
            throw new CustomException("password를 작성해주세요.");
        }

        User principal = userService.로그인(loginReqDto);
        
        session.setAttribute("principal", principal);
        
        if (principal.getRole().equals("ADMIN")) {
            return "redirect:/admin";    
        }
        return "redirect:/";
    }
    
    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }
    
    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
