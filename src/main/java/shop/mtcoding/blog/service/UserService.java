package shop.mtcoding.blog.service;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import shop.mtcoding.blog.dto.user.UserReq.JoinReqDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.dto.user.UserReq.UserUpdateDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;
import shop.mtcoding.blog.util.PathUtil;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    
    @Transactional
    public void 회원가입(JoinReqDto joinReqDto) { // 메서드가 동기화가 돼있지는 않기 때문에 여러 요청이 와도 메서드는 동시에 때려진다.
        User sameuser = userRepository.findByUsername(joinReqDto.getUsername());
        
        if (sameuser != null) {
            throw new CustomException("동일한 username이 존재합니다");
        }
        // 변경코드가 발동하면 락이 걸린다
        int result = userRepository.insert(joinReqDto.getUsername(), joinReqDto.getPassword(), joinReqDto.getEmail());

        if (result != 1) {
            throw new CustomException("요청하신 서비스가 정상 처리되지 않았습니다.");
        }
    }

    @Transactional(readOnly = true) // select 하던 도중 변경이 일어나면 select중에 데이터가 변경되므로 트랜잭션을 걸어준다. readonly = true를 걸어야 오로지 읽기만을 위한 트랜잭션이다.
    public User 로그인(LoginReqDto loginReqDto) { // select 밖에 없기 때문에 transaction이 걸릴 필요가 없다.
        User principal = userRepository.findByUsernameAndPassword(loginReqDto.getUsername(), loginReqDto.getPassword());

        if (principal == null) {
            throw new CustomException("일치하는 회원정보가 없습니다.");
        }
        return principal;
    }

    @Transactional
    public User 프로필사진수정(int principalId, MultipartFile profile) {
        User userPS = userRepository.findById(principalId);
        if (userPS == null) {
            throw new CustomException("회원정보가 존재하지 않습니다.");
        }

        String uuidImageName = PathUtil.writeImageFile(profile);
        userPS.setProfile(uuidImageName);

        try {
            userRepository.updateById(userPS.getId(), userPS.getUsername(), userPS.getPassword(), userPS.getEmail(), userPS.getProfile(), userPS.getCreatedAt());
        } catch (Exception e) {
            throw new CustomException("사진을 웹서버에 저장하지 못하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR); // 500번 에러
        }

        return userPS;

        // 2번 저장된 파일의 경로를 DB에 저장
    }
}
