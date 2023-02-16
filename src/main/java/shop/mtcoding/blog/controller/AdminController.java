package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.model.ReplyRepository;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;
import shop.mtcoding.blog.service.UserService;

@Controller
public class AdminController {
    
    @Autowired
    HttpSession session;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    UserService userService;

    @GetMapping({"/admin", "/admin/userManagement"})
    public String userManagement(Model model, @RequestParam(defaultValue = "all") String searchOpt,
                                              @RequestParam(defaultValue = "") String words) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/admin/loginForm";
        }

        // 권한 추가

        List<User> userList = userRepository.findAll(searchOpt, words);
        model.addAttribute("searchOpt", searchOpt);
        model.addAttribute("userList", userList);

        return "admin/userManagement";
    }

    @GetMapping("/admin/boardManagement")
    public String boardManagement(Model model, @RequestParam(defaultValue = "all") String searchOpt,
                                              @RequestParam(defaultValue = "") String words) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/admin/loginForm";
        }

        // 권한 추가

        model.addAttribute("searchOpt", searchOpt);
        model.addAttribute("dtos", boardRepository.findAllWithUser(searchOpt, words));
        return "admin/boardManagement";
    }

    @GetMapping("/admin/replyManagement")
    public String replyManagement(Model model, @RequestParam(defaultValue = "all") String searchOpt,
                                               @RequestParam(defaultValue = "") String words) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/admin/loginForm";
        }

        // 권한 추가

        model.addAttribute("searchOpt", searchOpt);
        model.addAttribute("dtos", replyRepository.findAllWithUser(searchOpt, words));
        return "admin/replyManagement";
    }

    // @PostMapping("/admin/login")
    // public String login(LoginReqDto loginReqDto) {
    //     if (loginReqDto.getUsername() == null || loginReqDto.getUsername().isEmpty()) {
    //         throw new CustomException("username을 작성해주세요.");
    //     }

    //     if (loginReqDto.getPassword() == null || loginReqDto.getPassword().isEmpty()) {
    //         throw new CustomException("password를 작성해주세요.");
    //     }

    //     userService.로그인(loginReqDto);

    //     return "redirect:/admin";
    // }

    @GetMapping("/admin/loginForm")
    public String loginForm() {
        
        return "admin/loginForm";
    }
}
