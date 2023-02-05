package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.UserReq.joinReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    
    @Transactional
    public int 회원가입(joinReqDto joinReqDto) {
        User user = userRepository.findByUsername(joinReqDto.getUsername());
        
        if (user != null) {
            throw new CustomException("동일한 username이 존재합니다");
        }

        int result = userRepository.insert(joinReqDto.getUsername(), joinReqDto.getPassword(), joinReqDto.getEmail());

        return result;
    }
}